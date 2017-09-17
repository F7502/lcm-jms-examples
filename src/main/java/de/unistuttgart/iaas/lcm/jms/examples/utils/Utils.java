package de.unistuttgart.iaas.lcm.jms.examples.utils;

import java.util.Enumeration;

import javax.jms.JMSException;
import javax.jms.TextMessage;

/**
 * Utilities
 * 
 * @author hauptfn
 */
public class Utils {

    /**
     * 
     * @param msg
     *            A JMS TextMessage
     * @return A string showing the content and the properties of the text
     *         message:<br/>
     *         'text content' (property1=value1, property2=value2)
     */
    public static String printMessage(TextMessage msg) {
	String result = "";
	if (msg != null) {
	    try {
		// message content
		result += "'" + msg.getText() + "'";
		// get all properties (property names)
		@SuppressWarnings("unchecked")
		Enumeration<String> e = msg.getPropertyNames();
		String s;
		// if there is at least one property...
		if (e.hasMoreElements()) {
		    result += " (";
		    // for all properties...
		    while (e.hasMoreElements()) {
			// get property name
			s = e.nextElement();
			result += s;
			result += "=";
			// get property value (can be any type, we get an
			// object)
			result += msg.getObjectProperty(s);
			if (e.hasMoreElements()) {
			    result += ", ";
			}
		    }
		    result += ")";
		}
	    } catch (JMSException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
	return result;
    }

}