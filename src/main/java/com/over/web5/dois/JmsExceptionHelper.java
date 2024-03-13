package com.over.web5.dois;

import javax.jms.JMSException;
import javax.jms.JMSRuntimeException;
import java.util.logging.Logger;

/*
 * A helper class to report JMS exceptions in a common way
 */
public class JmsExceptionHelper {

  static void recordFailure(Logger logger, Exception ex) {
    if (ex != null) {
      if (ex instanceof JMSException ||  ex instanceof JMSRuntimeException) {
        processJMSException(logger, ex);
      }
      else {
        logger.warning(ex.getMessage());
      }
    }
    System.out.println("FAILURE");
    return;
  }

  private static void processJMSException(Logger logger, Exception jmsex) {
    logger.info(jmsex.getMessage());
    Throwable innerException = null;
    
    if (jmsex instanceof JMSException) {
      innerException = ((JMSException)jmsex).getLinkedException();
    } else if (jmsex instanceof JMSRuntimeException) {
      innerException = jmsex.getCause(); 
    }
    logger.warning("Exception is: " + jmsex);
    
    String errStack = "";
    while (innerException != null) {
      errStack += "\n  Caused by: " + innerException.getMessage();
      innerException = innerException.getCause();
    }
    if (!errStack.isEmpty()) {
      logger.warning(errStack);
    }
    // For more detailed information on the failure, uncomment the following line
    // jmsex.printStackTrace();
    return;
  }

}
