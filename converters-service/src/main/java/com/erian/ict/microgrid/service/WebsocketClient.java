package com.erian.ict.microgrid.service;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import javax.websocket.*;

import java.io.IOException;
import java.net.URI;

@ClientEndpoint
public class WebsocketClient {
    Session userSession = null;
    String message;
    String deviceId;
    String endpointURI;

    private static Logger logger = LoggerFactory.getLogger(WebsocketClient.class);
    
    public WebsocketClient(String endpointURI,String deviceId) {
	    this.deviceId = deviceId;
	    this.endpointURI = endpointURI;
	    connect();
    }

    @OnOpen
    public void onOpen(Session userSession){
        logger.info("opening websocket for " + deviceId);
        this.userSession = userSession;
    }


    @OnClose
    public void onClose(Session userSession, CloseReason reason) throws InterruptedException {
        logger.info("closing websocket for " + deviceId);
        this.message = null;
        try {
        	if(this.userSession != null)
        		this.userSession.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @OnMessage
    public void onMessage(String message) {
        this.message = message;
        
    }

    public void sendMessage(String message) {
        if (isConnected()) {
            this.userSession.getAsyncRemote().sendText(message);
        } else {
            logger.warn("No Websocket connection opened!! " + deviceId);
        }
    }
    

    @OnError
    public void onError(Session userSession,  Throwable t) {
        message = null;
        try {
        	if(this.userSession != null)
        		this.userSession.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        logger.error("Connection occured an error " + t.getLocalizedMessage());
    }
    
//    @Scheduled(initialDelay=8000, fixedRate = 5000)
    public boolean isConnected() {
        if (this.userSession != null && userSession.isOpen()) {
            return true;
        }
        this.userSession = null;
        return false;
    }
    
    public void isConnected(boolean reconnect) {
    	if(!isConnected()){
    		connect();
    		logger.warn(this.deviceId + " WS connection is closed but we tried to reconnect!! ");
    	}
    		
    }
    
    
    public synchronized void connect() {
         try {
        	 if(endpointURI != null){
                 logger.info("Create websocket connection to " + endpointURI + " for device " + deviceId);
        		 WebSocketContainer container = ContainerProvider.getWebSocketContainer();
                 container.connectToServer(this, new URI(endpointURI));
        	 }
         } catch (Exception e) {
             logger.error("Create websocket connection failed to " + endpointURI + " for device " + deviceId);
         }
    }
    
}
