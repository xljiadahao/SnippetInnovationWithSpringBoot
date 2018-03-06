package com.snippet.websocket;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.stereotype.Component;

@Component
public class MessageReceiver implements Runnable {

	static {
		Thread thread = new Thread(new MessageReceiver());
		thread.start();
	}
	
	private static final String BROKER_URL = "tcp://localhost:61616";
    private static final String QUEUE_NAME = "p2pqueue";

    private ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,
    		ActiveMQConnection.DEFAULT_PASSWORD, BROKER_URL);
    private Connection connection;
    private Session session;
    private Destination destination;
    private MessageConsumer consumer;

    public MessageReceiver() {}

    public void receiveMessage() {
        try {
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
            destination = session.createQueue(QUEUE_NAME);
            consumer = session.createConsumer(destination);
            while (true) {
                TextMessage message = (TextMessage) consumer.receive(100000);
                if (message != null) {
                    System.out.println("ActiveMq Receiver 收到的消息: " + message.getText());
                    String[] sessionMsg = message.getText().split(",");
                    if (sessionMsg.length < 2) {
                    	ApplicationContext.sendMessage(null, sessionMsg[0]);
                    } else {
                    	System.out.println("sessionMsg: " + sessionMsg[0] + ", " + sessionMsg[1]);
                    	ApplicationContext.sendMessage(sessionMsg[0], sessionMsg[1]);
                    } 
                } else {
                    break;
                }
            }
        } catch (JMSException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (session != null) {
                    session.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (JMSException ex) {
                ex.printStackTrace();
            }
        }
    }

	@Override
	public void run() {
		System.out.println("start AMQ listener");
		MessageReceiver queueReceiver = new MessageReceiver();
        queueReceiver.receiveMessage();
	}

}
