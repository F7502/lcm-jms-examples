package de.unistuttgart.iaas.lcm.jms.examples.b;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.TopicSession;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import de.unistuttgart.iaas.lcm.jms.examples.utils.Utils;

/**
 * A looping JMS receiver. Retrieves one message each two seconds from a queue.
 * Uses synchronous receive methods.
 * 
 * @author hauptfn
 */
public class LoopingConsumerSynchronous {

    // waiting time between two receive operations in ms
    private static final int waitingTime = 2000;

    // the queue to send the messages to
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
	MessageConsumer messageConsumer = null;

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

	    // create message consumer
	    messageConsumer = session.createConsumer(queue);

	    // start connection
	    connection.start();

	    // infinite message receive loop
	    Message message;
	    while (true) {
		// retrieve next message
		message = messageConsumer.receive();
		// other variants of receiving messages
		// message = messageConsumer.receive(5000);
		// message = messageConsumer.receiveNoWait();

		// print message
		if (message instanceof TextMessage) {
		    System.out.println("Received " + Utils.printMessage((TextMessage) message));
		}

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
		if (messageConsumer != null) {
		    System.out.println("Closing MessageConsumer");
		    messageConsumer.close();
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
