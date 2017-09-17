package de.unistuttgart.iaas.lcm.jms.examples;

import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.jms.TopicSession;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class QueueSpam {

    private static final int count = 50;

    private static final String queueName = "Q2";

    /**
     * @param args
     */
    public static void main(String[] args) {
	String url = ActiveMQConnection.DEFAULT_BROKER_URL;
	QueueConnection outQueueConnection = null;
	QueueSession queueSession = null;
	MessageProducer messageProducer = null;
	try {
	    QueueConnectionFactory queueConnectionFactory = new ActiveMQConnectionFactory(url);
	    outQueueConnection = queueConnectionFactory.createQueueConnection();
	    queueSession = outQueueConnection.createQueueSession(false, TopicSession.AUTO_ACKNOWLEDGE);
	    Queue queue = queueSession.createQueue(queueName);
	    messageProducer = queueSession.createProducer(queue);
	    messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);

	    TextMessage message = queueSession.createTextMessage();

	    System.out.println("Adding " + count + " messages to queue " + queueName);
	    for (int i = 0; i < count; i++) {
		message.setText("msg no. " + i);
		System.out.print(".");
		messageProducer.send(message);
	    }
	    System.out.println();
	} catch (JMSException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} finally {
	    try {
		if (messageProducer != null) {
		    System.out.println("Closing MessageProducer");
		    messageProducer.close();
		}
		if (queueSession != null) {
		    System.out.println("Closing QueueSession");
		    queueSession.close();
		}
		if (outQueueConnection != null) {
		    System.out.println("Closing QueueConnection");
		    outQueueConnection.close();
		}
	    } catch (JMSException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
    }

}