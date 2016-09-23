package com.erian.microgrid.api.MicrogridApi.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.erian.microgrid.api.MicrogridApi.dataModel.BusData;
import com.erian.microgrid.api.MicrogridApi.dataModel.DeviceTypeData;
import com.erian.microgrid.api.MicrogridApi.dataModel.UnitData;
import com.erian.microgrid.api.MicrogridApi.model.Bus;
import com.erian.microgrid.api.MicrogridApi.model.DeviceType;
import com.erian.microgrid.api.MicrogridApi.model.Unit;

public class StaticDataHelper
{
	//***********         Device Type         ***********************

	public static List<DeviceType> GetDeviceTypes(){
		List<DeviceType> list = new ArrayList<DeviceType>();
		Connection c = null;
		try {
				c = DatabaseHelper.getConnection();
	            Statement s = c.createStatement();
	            String sql = "{call get_device_types ()}";
	            ResultSet rs = s.executeQuery(sql);
	            while (rs.next()) {
	                list.add(Mapper.MapDeviceType(processDataTypeRow(rs)));
	            }
			} catch (SQLException e) {
	            e.printStackTrace();
	            throw new RuntimeException(e);
			} finally {
				DatabaseHelper.close(c);
			}
		return list;
	}
	
	protected static DeviceTypeData processDataTypeRow(ResultSet rs) throws SQLException {
		DeviceTypeData deviceType = new DeviceTypeData();
		deviceType.setTypeID(rs.getInt("typeID"));
		deviceType.setClassID(rs.getInt("classID"));
		deviceType.setTypeName(rs.getString("typeName"));
		deviceType.setTypeDescription(rs.getString("typeDescription"));
		deviceType.setClassName(rs.getString("className"));
		deviceType.setClassDescription(rs.getString("classDescription"));
		return deviceType;
	}

	// ********************       Bus            ***************************
	public static List<Bus> GetBuses(){
		List<Bus> list = new ArrayList<Bus>();
		Connection c = null;
		try {
				c = DatabaseHelper.getConnection();
	            Statement s = c.createStatement();
	            String sql = "{call get_buses ()}";
	            ResultSet rs = s.executeQuery(sql);
	            while (rs.next()) {
	                list.add(Mapper.MapBus(processBusRow(rs)));
	            }
			} catch (SQLException e) {
	            e.printStackTrace();
	            throw new RuntimeException(e);
			} finally {
				DatabaseHelper.close(c);
			}
		return list;
	}
	
	protected static BusData processBusRow(ResultSet rs) throws SQLException {
		return new BusData(rs.getInt("id"), rs.getString("name"), rs.getString("description"));
	}
	
	// ***********************             Units          *******************************
	public static List<Unit> GetUnits(){
		List<Unit> list = new ArrayList<Unit>();
		Connection c = null;
		try {
				c = DatabaseHelper.getConnection();
	            Statement s = c.createStatement();
	            String sql = "{call get_units ()}";
	            ResultSet rs = s.executeQuery(sql);
	            while (rs.next()) {
	                list.add(Mapper.MapUnit(processUnitRow(rs)));
	            }
			} catch (SQLException e) {
	            e.printStackTrace();
	            throw new RuntimeException(e);
			} finally {
				DatabaseHelper.close(c);
			}
		return list;
	}
	
	protected static UnitData processUnitRow(ResultSet rs) throws SQLException {
		UnitData res = new UnitData();
		res.setUnitID(rs.getInt("id"));
		res.setUnitName(rs.getString("name"));
		res.setUnitDescription(rs.getString("description"));
		res.setUnitCode(rs.getString("code")); // add by Yuanbo 
		return res;
	}
}
