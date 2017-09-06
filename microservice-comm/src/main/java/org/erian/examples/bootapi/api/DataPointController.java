package org.erian.examples.bootapi.api;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import org.erian.examples.bootapi.domain.*;
import org.erian.examples.bootapi.service.*;
import org.erian.modules.constants.MediaTypes;
import org.javasimon.aop.Monitored;

@RestController
public class DataPointController {

	
	public static final String PROJECT_PATH = "/api/dataPoints/";
	
	private static Logger logger = LoggerFactory.getLogger(DataPointController.class);

	@Autowired
	private DataPointService dpService;
	
	@RequestMapping(value = "/api/dataPoints",method=RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	@Monitored
	public List<DataPoint> listAllDataPoint() {
		List<DataPoint> dataPoints = dpService.findAll();
		return dataPoints;
	}

	@RequestMapping(value = "/api/dataPoints/{id}", method=RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	@Monitored
	public DataPoint listOneDataPoint(@PathVariable("id") Integer id) {
		DataPoint dataPoint = dpService.findOne(id);
		return dataPoint;
	}

	/*@RequestMapping(value = "/api/dp/read/{tagId}", method=RequestMethod.GET)
	@Monitored
	public Integer readTagById(@PathVariable("tagId") Long tagId) {
		return dpService.readTagValue(tagId);
		
	}*/
	
//	Content-Type: application/json;charset=UTF-8
	@RequestMapping(value = "/api/dataPoints", method = RequestMethod.POST, consumes = MediaTypes.JSON_UTF_8)
	@Monitored
	public DataPoint createDataPoint(@RequestBody DataPoint dataPoint, UriComponentsBuilder uriBuilder) {
		
		dataPoint = dpService.saveDataPoint(dataPoint);
			
		return dataPoint;
	}
	
//	@RequestMapping(value = "/api/dataPoints/{id}", method = RequestMethod.DELETE)
//	@Monitored
	public void deleteDataPoint(@NotNull @PathVariable("id") Integer id) {
		dpService.deleteDataPoint(id);
	}

//	@RequestMapping(value = "/api/dataPoints", method = RequestMethod.PUT, consumes = MediaTypes.JSON_UTF_8)
//	@Monitored
	public void modifyDataPoint(@RequestBody DataPoint dataPoint,
			@RequestParam(value = "token", required = false) String token) {
		
		dpService.modifyDataPoint(dataPoint);
	}
}