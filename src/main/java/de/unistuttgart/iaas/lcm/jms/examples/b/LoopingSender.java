package de.unistuttgart.iaas.lcm.jms.examples.b;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.TopicSession;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import de.unistuttgart.iaas.lcm.jms.examples.utils.Utils;

/**
 * A looping JMS sender. Sends one message per second to a queue.
 * 
 * @author hauptfn
 */
public class LoopingSender {

    // time to wait between two send operations (in ms)
    private static final int waitingTime = 1000;
    // name of the target queue
    private static final String queueName = "Q1";

    /**
     * @param args
     */
    public static void main(String[] args) {
	// default URL of the local ActiveMQ broker
	String url = ActiveMQConnection.DEFAULT_BROKER_URL;

	// initialization
	Connection connection = null;
	Session session = null;
	MessageProducer messageProducer = null;

	try {
	    // create ConnectionFactory to connect to ActiveMQ
	    ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
	    // create Connection
	    connection = connectionFactory.createConnection();
	    // create Session
	    session = connection.createSession(false, TopicSession.AUTO_ACKNOWLEDGE);
	    // create or get queue
	    Queue queue = session.createQueue(queueName);

	    // create and configure message producer
	    messageProducer = session.createProducer(queue);
	    // persistent = messages will not be lost, even if broker shuts down
	    messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);

	    // create text message
	    TextMessage message = session.createTextMessage();

	    // infinite message sending loop
	    int i = 1;
	    while (true) {
		// set text of message
		message.setText("msg no. " + i);
		System.out.println("sending " + Utils.printMessage(message));

		// send message
		messageProducer.send(message);
		i++;

		// wait
		try {
		    Thread.sleep(waitingTime);
		} catch (InterruptedException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    }

	} catch (JMSException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} finally {
	    // clean up
	    try {
		if (messageProducer != null) {
		    System.out.println("Closing MessageProducer");
		    messageProducer.close();
		}
		if (session != null) {
		    System.out.println("Closing Session");
		    session.close();
		}
		if (connection != null) {
		    System.out.println("Closing Connection");
		    connection.close();
		}
	    } catch (JMSException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
    }

}
