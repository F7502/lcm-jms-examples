package de.unistuttgart.iaas.lcm.jms.examples.a;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import de.unistuttgart.iaas.lcm.jms.examples.utils.Utils;

/**
 * Receives one message from a queue.
 * 
 * @author hauptfn
 *
 */
public class SimpleReceive {

    // name of the source queue
    private static final String queueName = "MyFirstQueue";

    /**
     * @param args
     */
    public static void main(String[] args) {
	// initialization
	Connection connection = null;
	Session session = null;
	MessageConsumer messageConsumer = null;

	try {
	    // connect to local ActiveMQ broker using the default URL
	    ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
	    connection = connectionFactory.createConnection();
	    // create session (no transactions, auto acknowledgement)
	    session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	    // get queue (will be created if not existing)
	    Queue sourceQueue = session.createQueue(queueName);
	    // create a receiver for the queue
	    messageConsumer = session.createConsumer(sourceQueue);
	    // start the connection
	    connection.start();
	    
	    // the message to receive
	    Message message;
	    // v1, waits until a message is available in the queue 
	    message = messageConsumer.receive();
	    // v2, waits until a message is available in the queue but returns null if no message gets available after specified time (in ms)
	    //message = messageConsumer.receive(2000);
	    // v3, immediately returns null if no message is available
	    //message = messageConsumer.receiveNoWait();

	    // print message
	    if (message == null) {
		System.out.println("No message available");
	    } else if (message instanceof TextMessage) {
		System.out.println("Received " + Utils.printMessage((TextMessage) message));
	    } else {
		System.out.println("Received message (not a text message)");
	    }

	} catch (JMSException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} finally {
	    // clean up
	    try {
		System.out.println();
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
