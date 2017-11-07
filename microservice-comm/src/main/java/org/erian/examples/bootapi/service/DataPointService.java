package org.erian.examples.bootapi.service;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.erian.examples.bootapi.domain.*;
import org.erian.examples.bootapi.dto.*;
import org.erian.examples.bootapi.repository.*;
import org.erian.examples.bootapi.service.exception.ErrorCode;
import org.erian.examples.bootapi.service.exception.ServiceException;
import org.erian.examples.bootapi.service.socket.SocketConnection;
import org.erian.modules.jamod.model.ReadParams;
import org.erian.modules.jamod.model.SerialParams;
import org.erian.modules.jamod.util.ModbusSerialUtil;
import org.erian.modules.jamod.util.ModbusTcpUtil;

@Service
public class DataPointService {

	private static Logger logger = LoggerFactory.getLogger(DataPointService.class);

	public static final String INT_TO_FLOAT = "intToFloat";                                                                                           
	public static final String CENTI = "0.01";

	public static final String MODBUS_TCP = "ModbusTCP";
	public static final String MODBUS_RTU = "ModbusRTU";
	public static final String ETHERNET_IP = "EthernetIP";

	@Autowired
	private DataPointDao dataPointDao;

	@Autowired
	private ModbusTcpDao tcpDao;

	@Autowired
	private ModbusRtuDao rtuDao;

	@Autowired
	private EthernetIpDao ipDao;

	@Autowired
	private CacheManager cacheManager;
	
	@Autowired
	SocketConnection socketConn;
	
	@Cacheable("SEC02")
	@Transactional(readOnly = true)
	public List<DataPoint> findAll() {
		return dataPointDao.findAll();
	}

	@Cacheable(value = "SEC05", key="#id")
	@Transactional(readOnly = true)
	public DataPoint findOne(Integer id) {
		return dataPointDao.findOne(id);
	}

	@Transactional
	public DataPoint saveDataPoint(DataPoint dataPoint) {

		return dataPointDao.save(dataPoint);
	}

	@Transactional
	public DataPoint modifyDataPoint(DataPoint dataPoint) {

		DataPoint orginalDataPoint = dataPointDao.findOne(dataPoint.id);

		if (orginalDataPoint == null) {
			logger.error(dataPoint.id + "  is not exist");
			throw new ServiceException("The DataPoint is not exist", ErrorCode.BAD_REQUEST);
		}
		return dataPointDao.save(dataPoint);
	}

	@Transactional
	public void deleteDataPoint(Integer id) {
		DataPoint dataPoint = dataPointDao.findOne(id);

		if (dataPoint == null) {
			logger.error(id + " DataPoint which is not exist");
			throw new ServiceException("The DataPoint is not exist", ErrorCode.BAD_REQUEST);
		}

		dataPointDao.delete(id);
	}

	/**
	 * data processing for reading data from a datapiont ID
	 * 
	 * @function:
	 * @param id
	 * @return
	 * @author: Rambo Zhu 4 Sep 2017 9:48:45 pm
	 */
	public String readDataPoint(Integer id) {
		Cache cache = cacheManager.getCache("SEC02");
		// find the cache first we must use get with valuewrapper
		ValueWrapper obj = cache.get(id);
		if(obj != null)
			return (String) obj.get();
		
		String value = null;

		DataPoint dp = this.findOne(id);

		if (dp != null && dp.device != null) {
			value = this.readDataPoint(dp);
			// add into cache
			if(value != null) cache.put(id, value);
		} else {
			logger.warn("The datapoint or Device is not exists with id -- " + id);
		}
		return value;
	}

	
	public void writeDataPoint(Integer id, String val) {
		// find the cache first 

		DataPoint dp = this.findOne(id);
		
		dp.setValue = val;
		
		
		val = this.readDataPoint(dp);
		
		
			// add into cache
	}
	public String readDataPoint(DataPoint dp) {
		String value = null;
		Device d = dp.device;
		switch (d.protocol) {
		
		case MODBUS_TCP:
			value = this.modbusTCP(dp);
			break;
		case MODBUS_RTU:
			value = this.modbusRTU(dp);
		 break;
		case ETHERNET_IP:
			value = this.ethernetIP(dp);
			break;
		default:
			logger.error("unknown protocol " + d.protocol);
			break;
		}
		return value;
	}

	
	String modbusTCP(DataPoint dp) {

		Device d = dp.device;

		ModbusTCP tcp = tcpDao.findByDeviceId(d.id);

		if (tcp != null) {
			ModbusTcpRequest tcpReq = new ModbusTcpRequest(d, dp, tcp);
			Integer i = ModbusTcpUtil.readData(tcpReq.ip, tcpReq.port, tcpReq.unitId, tcpReq.ref, tcpReq.length,
					tcpReq.fCode);
			if (i != null)
				return this.processValue(i, dp.outputExpression).toString();
		} else {
			logger.error("Modbus TCP protocol is not exists for device " + d.id);
		}

		return null;
	}
	
	String modbusRTU(DataPoint dp) {

		String result = null;
		
		Device d = dp.device;

		ModbusRTU rtu = rtuDao.findByDeviceId(d.id);
		
		if (rtu != null) {
			ModbusRtuRequest rtuReq = new ModbusRtuRequest(d, dp, rtu);
			SerialParams sParams = new SerialParams(rtuReq.address, rtuReq.baudrate, rtuReq.databit, rtuReq.stopbit, rtuReq.parity, rtu.encoding);

			ReadParams request = new ReadParams(rtuReq.unitId, rtuReq.ref, rtuReq.length);
			//read the data through modbus rtu
			Integer i = ModbusSerialUtil.readData(sParams, request, rtuReq.fCode);
			if (i != null)
				result = this.processValue(i, dp.outputExpression).toString();
		} else {
			logger.error("Modbus RTU protocol is not exists for device " + d.id);
		}

		return result;
	}


	String ethernetIP(DataPoint dp) {
		String result = null;
		Device d = dp.device;
//		EthernetIP ip = ipDao.findByDeviceId(d.id);
		String path = d.path;
		String[] ips = path.split(":");
		if (ips != null && ips.length >= 2) {
			
			EthernetIP ip = new EthernetIP();
			ip.port = Integer.valueOf(ips[1]);
			ip.ip = ips[0];
			
			EthernetIpRequest ipReq = new EthernetIpRequest(d, dp, ip);
			
			// set the prepared command for set value
			if(StringUtils.isNotBlank(dp.setValue))
				ipReq.datapointPath = String.format(ipReq.datapointPath, dp.setValue);
			try {
				result = socketConn.callDevice(ipReq.ip, ipReq.port, ipReq.datapointPath);
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
				logger.error(e.getMessage());
			}
		} else {
			logger.error("Ethernet IP protocol is not exists for device " + d.id);
		}

		return result;
	}
	
	Double processValue(Integer i, String outputExpression) {

		Double d = null;
//		System.err.println("-"+outputExpression +"-");
		outputExpression = (null != outputExpression)? outputExpression.trim():"unknown";
		switch (outputExpression) {
		case INT_TO_FLOAT:
//		case "intToFloat":
			Float f = Float.intBitsToFloat(i);
			d = f.doubleValue();
			break;
		case CENTI:
			d = i * 0.01;
			break;
		default:
			logger.error("unknown outputExpression " + outputExpression);
			d = i.doubleValue();
			break;
		}
		return d;
	}
}
