package com.erian.microgrid.api.MicrogridApi.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.erian.microgrid.api.MicrogridApi.model.Device;
import com.erian.microgrid.api.MicrogridApi.service.DeviceHelper;

@Path("/devices")

public class DeviceResource {	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Device> getDeviceDetails(){
		return DeviceHelper.GetAllDevices();
	}

	@GET
	@Path("/{deviceId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Device getDevice(@PathParam("deviceId") int deviceId){
		return DeviceHelper.GetDevice(deviceId);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	 // POST action + "devices/" is a recommended URI to "create new device" resource
	// URI to test : http://localhost:8080/MicrogridApi/devices?{"ID"=2,"IPAdress":"127.34.5646.12","busID":2,"classID":2,"className":"Source","description":"test Update Load 2 top Load 2device","isConnected":1,"isProgrammable":1,"location":"somewhere 2","microgridID":1,"microgridName":"Lab Level 5 Microgrid","model":"Model S2","name":"Load 2","portNumber":"3030upd2","typeID":2,"typeName":"DC source","vendor":"Some Vendor"} 
	public Device addDevice(Device newDevice){
		return DeviceHelper.AddNewDevice(newDevice);
	}	
	
	/*
	private Device GetFakeDevice()
	{
		Device newDevice1 = new Device();
		newDevice1.setBusID(1);
		newDevice1.setMicrogridID(1);
		newDevice1.setName("API call add 3");
		newDevice1.setTypeID(1);
		newDevice1.setVendor("vendor3");
		newDevice1.setModel("Model3");
		newDevice1.setLocation("location3");
		newDevice1.setIPAdress("some IP 3");
		newDevice1.setPortNumber("2002");
		newDevice1.setIsProgrammable(1);
		newDevice1.setIsConnected(0);
		return newDevice1;
	}
	*/
}

