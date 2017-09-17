package de.unistuttgart.iaas.lcm.jms.examples.a;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * Sends one text message to a queue.
 * 
 * @author hauptfn
 *
 */
public class SimpleSend {

    // name of the target queue
    private static final String queueName = "MyFirstQueue";
    // text that will be sent
    private static final String text = "Test 0815";

    /**
     * @param args
     */
    public static void main(String[] args) {
	try {
	    // connect to local ActiveMQ broker using the default URL
	    ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
	    Connection connection = connectionFactory.createConnection();
	    // create session (no transactions, auto acknowledgement)
	    Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	    // get queue (will be created if not existing)
	    Queue targetQueue = session.createQueue(queueName);
	    // create a sender for the queue
	    MessageProducer sender = session.createProducer(targetQueue);
	    // start the connection
	    connection.start();
	    // create text message
	    TextMessage message = session.createTextMessage(text);
	    // send message to queue
	    sender.send(message);
	    // close connection
	    connection.close();
	} catch (JMSException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

}
