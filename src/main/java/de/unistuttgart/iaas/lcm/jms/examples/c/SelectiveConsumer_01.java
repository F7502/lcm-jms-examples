package de.unistuttgart.iaas.lcm.jms.examples.c;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import de.unistuttgart.iaas.lcm.jms.examples.utils.Utils;

public class SelectiveConsumer_01 {

    private static final int waitingTime = 1000;

    private static final String queueName = "Q1";

    private static final String filterExpr = "bla=true";

    /**
     * @param args
     */
    public static void main(String[] args) {
	String url = ActiveMQConnection.DEFAULT_BROKER_URL;
	QueueConnection inQueueConnection = null;
	QueueSession queueSession = null;
	QueueReceiver queueReceiver = null;
	try {
	    QueueConnectionFactory queueConnectionFactory = new ActiveMQConnectionFactory(url);
	    inQueueConnection = queueConnectionFactory.createQueueConnection();
	    queueSession = inQueueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
	    Queue queue = queueSession.createQueue(queueName);
	    queueReceiver = queueSession.createReceiver(queue, filterExpr);
	    inQueueConnection.start();

	    queueReceiver.setMessageListener(new MessageListener() {
		@Override
		public void onMessage(Message msg) {
		    if (msg instanceof TextMessage) {
			System.out.println("Received " + Utils.printMessage((TextMessage) msg));
		    }
		    try {
			Thread.sleep(waitingTime);
		    } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
		}
	    });
	    try {
		Thread.sleep(waitingTime * 1000);
	    } catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	} catch (JMSException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} finally {
	    try {
		if (queueReceiver != null) {
		    System.out.println("Closing QueueReceiver");
		    queueReceiver.close();
		}
		if (queueSession != null) {
		    System.out.println("Closing QueueSession");
		    queueSession.close();
		}
		if (inQueueConnection != null) {
		    System.out.println("Closing QueueConnection");
		    inQueueConnection.stop();
		    inQueueConnection.close();
		}
	    } catch (JMSException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
    }

}