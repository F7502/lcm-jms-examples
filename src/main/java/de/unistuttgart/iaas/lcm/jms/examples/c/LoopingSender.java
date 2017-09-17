package de.unistuttgart.iaas.lcm.jms.examples.c;

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

    // waiting time between two send operations in ms
    private static final int waitingTime = 1000;

    // the queue to send the messages to
    private static final String queueName = "Q1";

    // the names of some message properties
    private static final String filterPropBoolean = "bla";
    private static final String filterPropInt = "blub";

    /**
     * A looping JMS sender. Sends one message per second to a queue.
     * 
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

	    // create Connection [createConnection(String userName, String
	    // password)]
	    connection = connectionFactory.createConnection();

	    // create Session [createSession(boolean transacted, int
	    // acknowledgeMode)]
	    session = connection.createSession(false, TopicSession.AUTO_ACKNOWLEDGE);

	    // create or get queue
	    Queue queue = session.createQueue(queueName);

	    // create and configure message producer
	    messageProducer = session.createProducer(queue);
	    messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);

	    // create text message
	    TextMessage message = session.createTextMessage();

	    // infinite message sending loop
	    int i = 1;
	    while (true) {
		// set properties of message
		message.setBooleanProperty(filterPropBoolean, (i % 2 == 0));
		message.setIntProperty(filterPropInt, i);

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