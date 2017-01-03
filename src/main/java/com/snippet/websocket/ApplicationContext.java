package com.snippet.websocket;

import java.util.HashMap;
import java.util.Map;

import javax.websocket.Session;

public class ApplicationContext {

	public static Map<String, Session> webSocketSession = new HashMap<String, Session>();
	
	public static void sendMessage(String sessionId, String message) {
		if (sessionId != null) {
			System.out.println("send msg sessionId: " + sessionId);
			if (webSocketSession.get(sessionId) != null) {
				System.out.println("msg sent");
				webSocketSession.get(sessionId).getAsyncRemote().sendText(message);
			}
		} else {
			for (String key : webSocketSession.keySet()) {
				System.out.println("key: " + key);
				webSocketSession.get(key).getAsyncRemote().sendText(message);
			}
		}	
	}
	
}
