package com.snippet.websocket;

import java.util.Date;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class MessagePublisher {

	private static final String BROKER_URL = "tcp://localhost:61616";
    private static final String QUEUE_NAME = "p2pqueue";

    private ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,
            ActiveMQConnection.DEFAULT_PASSWORD, BROKER_URL);
    private Connection connection;
    private Session session;
    private Destination destination;
    private MessageProducer producer;

    public MessagePublisher() {}

    public void sendMessage(String msg) {
        if (connectionFactory != null) {
            try {
                connection = connectionFactory.createConnection();
                connection.start();
                session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
                destination = session.createQueue(QUEUE_NAME);
                producer = session.createProducer(destination);
                producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
                TextMessage message = session.createTextMessage(msg);
                System.out.println("ActiveMq Producer 发送的消息: " + msg);
                producer.send(message);
                session.commit();
            } catch (Exception ex) {
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
    }
    
    public static void main(String[] args) {
    	MessagePublisher activeMQPublisher = new MessagePublisher();
        activeMQPublisher.sendMessage("0,XL Producer MSG");
        System.out.println("Message sent at " + new Date());
    }
	
}
