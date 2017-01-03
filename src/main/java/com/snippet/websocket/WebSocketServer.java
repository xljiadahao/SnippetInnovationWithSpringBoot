package com.snippet.websocket;

import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

@ServerEndpoint(value="/websocket")
@Component
public class WebSocketServer {

	private Session session;
	
	@OnOpen
	public void onOpen(Session session) {
		System.out.println("onOpen, seesion id: " + session.getId());
		this.session = session;
		ApplicationContext.webSocketSession.put(session.getId(), session);
		System.out.println("Open New Conn, Remain session size: " + ApplicationContext.webSocketSession.keySet().size());
	}
	
	@OnClose
	public void onClose() {
		System.out.println("onClose, seesion id: " + session.getId());
		ApplicationContext.webSocketSession.remove(session.getId());
		System.out.println("Close Exist Conn, Remain session size: " + ApplicationContext.webSocketSession.keySet().size());
	}
	
	@OnMessage
	public void onMessage(String message, Session session) {
		System.out.println("onMessage: " + message + ", session id: " + session.getId());
	}

	@OnError
	public void onError(Session session, Throwable error) {
		System.out.println("onError, " + session.getId() + ", " + error.getMessage());
	}
	
	public void sendMessage(String message) {
		try {
			// sync
			this.session.getBasicRemote().sendText(message);
			// async
			// this.session.getAsyncRemote().sendText(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}

