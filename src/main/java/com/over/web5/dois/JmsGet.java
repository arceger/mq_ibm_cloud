package com.over.web5.dois;

import com.ibm.mq.MQException;
import com.ibm.mq.constants.MQConstants;
import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;

import javax.jms.*;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JmsGet {

    private static final String DEFAULT_APP_NAME = "Dev Experience JmsGet";
    private static final Level LOGLEVEL = Level.ALL;
    private static final Logger logger = Logger.getLogger("com.ibm.mq.samples.jms");



        // Create variables for the connection to MQ
    private static String HOST;
    private static int PORT;
    private static String CHANNEL;
    private static String QMGR;
    private static String APP_USER;
    private static String APP_PASSWORD;
    private static String APP_NAME = "java jms";
    private static String QUEUE_NAME;
    private static String CIPHER_SUITE;
    private static String CCDTURL;
    private static Boolean BINDINGS = false;

    private static long TIMEOUTTIME = 5000;  // 5 Seconds

    public static void main(String[] args) {
        logger.info("Get application is starting");

        initialiseLogging();

        SampleEnvSetter env = new SampleEnvSetter();
        int limit = env.getCount();

        logger.info("There are " + limit + " endpoints");

        for (int index = 0; index < limit; index++) {
            mqConnectionVariables(env, index);

            logger.info("Retrieving message from endpoint " + HOST + "(" + PORT + ")");

            try {
                retrieveFromEndpoint();
            } catch (JMSRuntimeException ex) {
                if (! canContinue(ex)) {
                    break;
                }
            }
        }
    }

    private static boolean canContinue(JMSRuntimeException ex) {
        if (null != ex.getCause() && ex.getCause() instanceof MQException) {
            MQException innerException = (MQException) ex.getCause();

            if (MQConstants.MQRC_HOST_NOT_AVAILABLE == innerException.getReason()) {
                logger.info("Host not available, skipping message gets from this host");
                return true;
            }
        }

        logger.warning("Unexpected exception will be terminating process");
        recordFailure(ex);
        return false;
    }

    private static void retrieveFromEndpoint() {
        JMSContext context;
        Destination destination;
        JMSConsumer consumer;
        boolean continueProcessing = true;

        JmsConnectionFactory connectionFactory = createJMSConnectionFactory();
        setJMSProperties(connectionFactory);
        logger.info("created connection factory");

        context = connectionFactory.createContext();

        logger.info("context created");
        destination = context.createQueue("queue:///" + QUEUE_NAME);
        logger.info("destination created");
        consumer = context.createConsumer(destination);
        logger.info("consumer created");

        while (continueProcessing) {
            try {
                Message receivedMessage = consumer.receive(TIMEOUTTIME);

                if (receivedMessage == null) {
                    logger.info("No message received from this endpoint");
                     continueProcessing = false;
                } else {
                  getAndDisplayMessageBody(receivedMessage);
                  logger.info("Waiting 1 second before looking for next message");
                  waitAWhile(1000);
                }
            } catch (JMSRuntimeException jmsex) {
                jmsex.printStackTrace();
                waitAWhile(1000);
            }
        }
    }

    private static void getAndDisplayMessageBody(Message receivedMessage) {
        if (receivedMessage instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) receivedMessage;
            try {
                logger.info("Received message: " + textMessage.getText());
            } catch (JMSException jmsex) {
                recordFailure(jmsex);
            }
        } else if (receivedMessage instanceof Message) {
            logger.info("Message received was not of type TextMessage.\n");
        } else {
            logger.info("Received object not of JMS Message type!\n");
        }
    }

    private static void mqConnectionVariables(SampleEnvSetter env, int index) {
        CCDTURL = env.getCheckForCCDT();

        // If there is a CCDT then Host and Port will be 
        // specified there
        if (null == CCDTURL) {
            HOST = env.getEnvValue("HOST", index);
            PORT = env.getPortEnvValue("PORT", index);
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
                cf.setStringProperty(WMQConstants.WMQ_HOST_NAME, HOST);
                cf.setIntProperty(WMQConstants.WMQ_PORT, PORT);
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

    private static void waitAWhile(int duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
        }
    }
}
