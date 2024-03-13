package com.over.web5.dois;

import com.ibm.mq.jms.MQDestination;
import com.over.web5.dois.SampleEnvSetter;
import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;

import javax.jms.*;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JmsPut {

    private static final String DEFAULT_APP_NAME = "Dev JmsPut";
    private static final Level LOGLEVEL = Level.ALL;
    private static final Logger logger = Logger.getLogger("com.ibm.mq.samples.jms");

    // Create variables for the connection to MQ
    private static String ConnectionString ;
    private static String CHANNEL ;
    private static String QMGR ;
    private static String APP_USER ;
    private static String APP_PASSWORD ;
    private static String APP_NAME ;
    private static String QUEUE_NAME ;

    private static String CIPHER_SUITE;
    private static String CCDTURL;
    private static Boolean BINDINGS = false;

    public static void main(String[] args) {
        initialiseLogging();
        mqConnectionVariables();
        logger.info("Put application is starting");

        JMSContext context = null;
        Destination destination = null;
        JMSProducer producer = null;

        JmsConnectionFactory connectionFactory = createJMSConnectionFactory();
        setJMSProperties(connectionFactory);
        logger.info("created connection factory");

        context = connectionFactory.createContext();
        logger.info("context created");

        // Set targetClient to be non JMS, so no JMS headers are transmitted.
        // Only one of these settings is required, but both shown here.
        // 1. Add targetClient parameter to Queue uri
        destination = context.createQueue("queue:///" + QUEUE_NAME + "?targetClient=1");
        // destination = context.createQueue("queue:///" + QUEUE_NAME);
        logger.info("destination created");

        // 2. Cast destination queue to underlying MQQueue and set target client
        setTargetClient(destination);

        producer = context.createProducer();
        logger.info("producer created");

        for (int i = 1; i <= 10; i++) {
            TextMessage message = context.createTextMessage("This is message number " + i + ".");
            producer.send(destination, message);
        }
        logger.info("Sent all messages!");
    }

    private static void mqConnectionVariables() {
        SampleEnvSetter env = new SampleEnvSetter();
        int index = 0;

        CCDTURL = env.getCheckForCCDT();

        // If the CCDT is in use then a connection string will 
        // not be needed.
        if (null == CCDTURL) {
            ConnectionString = env.getConnectionString();
        }

        CHANNEL = env.getEnvValue("CHANNEL", index);
        QMGR = env.getEnvValue("QMGR", index);
        APP_USER = env.getEnvValue("APP_USER", index);
        APP_PASSWORD = env.getEnvValue("APP_PASSWORD", index);
        APP_NAME = env.getEnvValueOrDefault("APP_NAME", DEFAULT_APP_NAME, index);
        QUEUE_NAME = env.getEnvValue("QUEUE_NAME", index);
        CIPHER_SUITE = env.getEnvValue("CIPHER_SUITE", index);
        BINDINGS = env.getEnvBooleanValue("BINDINGS", index);
    }

    private static JmsConnectionFactory createJMSConnectionFactory() {
        JmsFactoryFactory ff;
        JmsConnectionFactory cf;
        try {
            // JMS
            ff = JmsFactoryFactory.getInstance(WMQConstants.WMQ_PROVIDER);
            // Jakarta
            // ff = JmsFactoryFactory.getInstance(WMQConstants.JAKARTA_WMQ_PROVIDER);

            cf = ff.createConnectionFactory();
        } catch (JMSException jmsex) {
            recordFailure(jmsex);
            cf = null;
        }
        return cf;
    }

    private static void setJMSProperties(JmsConnectionFactory cf) {
        try {
            if (null == CCDTURL) {
                cf.setStringProperty(WMQConstants.WMQ_CONNECTION_NAME_LIST, ConnectionString);
                if (null == CHANNEL && !BINDINGS) {
                    logger.warning("When running in client mode, either channel or CCDT must be provided");
                } else if (null != CHANNEL) {
                    cf.setStringProperty(WMQConstants.WMQ_CHANNEL, CHANNEL);
                }
            } else {
                logger.info("Will be making use of CCDT File " + CCDTURL);
                cf.setStringProperty(WMQConstants.WMQ_CCDTURL, CCDTURL);
                
                // Set the WMQ_CLIENT_RECONNECT_OPTIONS property to allow 
                // the MQ JMS classes to attempt a reconnect 
                // cf.setIntProperty(WMQConstants.WMQ_CLIENT_RECONNECT_OPTIONS, WMQConstants.WMQ_CLIENT_RECONNECT);
            }

            if (BINDINGS) {
                cf.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, WMQConstants.WMQ_CM_BINDINGS);
            } else {
                cf.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, WMQConstants.WMQ_CM_CLIENT);
            }

            cf.setStringProperty(WMQConstants.WMQ_QUEUE_MANAGER, QMGR);
            cf.setStringProperty(WMQConstants.WMQ_APPLICATIONNAME, APP_NAME);
            if (null != APP_USER && !APP_USER.trim().isEmpty()) {
                cf.setBooleanProperty(WMQConstants.USER_AUTHENTICATION_MQCSP, true);
                cf.setStringProperty(WMQConstants.USERID, APP_USER);
                cf.setStringProperty(WMQConstants.PASSWORD, APP_PASSWORD);
            }
            if (CIPHER_SUITE != null && !CIPHER_SUITE.isEmpty()) {
                cf.setStringProperty(WMQConstants.WMQ_SSL_CIPHER_SUITE, CIPHER_SUITE);
            }
        } catch (JMSException jmsex) {
            recordFailure(jmsex);
        }
        return;
    }

    private static void setTargetClient(Destination destination) {
      try {
          MQDestination mqDestination = (MQDestination) destination;
          mqDestination.setTargetClient(WMQConstants.WMQ_CLIENT_NONJMS_MQ);
      } catch (JMSException jmsex) {
        logger.warning("Unable to set target destination to non JMS");
      }
    }

    private static void recordFailure(Exception ex) {
        JmsExceptionHelper.recordFailure(logger,ex);
        return;
    }

    private static void initialiseLogging() {
        Logger defaultLogger = Logger.getLogger("");
        Handler[] handlers = defaultLogger.getHandlers();
        if (handlers != null && handlers.length > 0) {
            defaultLogger.removeHandler(handlers[0]);
        }

        Handler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(LOGLEVEL);
        logger.addHandler(consoleHandler);

        logger.setLevel(LOGLEVEL);
        logger.finest("Logging initialised");
    }

}
