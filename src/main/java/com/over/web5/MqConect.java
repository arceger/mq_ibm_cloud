package com.over.web5;

import java.io.IOException;
import java.io.StringWriter;

import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ibm.mq.MQException;
import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;
import com.over.web5.xml.SecretManagerUtil;
import com.over.web5.xml.User;
import com.over.web5.xml.ECIFields;
import com.over.web5.xml.ECITransformer;
import com.over.web5.xml.UpdateFIELD;

public class MqConect {
  private static final Logger LOGGER = LoggerFactory.getLogger(Web5Application.class);
    String recMsg="teste mensagem";

    public String getRecMsg() {
        return recMsg;
    }

    public void setRecMsg(String recMsg) {
        this.recMsg = recMsg;
    }

    private static int status = 1;

    // MQ cloud ibm


    public void postAndReceive() throws JAXBException {

        JMSContext context = null;
        Destination destination = null;
        JMSConsumer consumer = null;


        try {

            // Create a connection factory
            JmsFactoryFactory ff = JmsFactoryFactory.getInstance(WMQConstants.WMQ_PROVIDER);
            JmsConnectionFactory cf = ff.createConnectionFactory();

            // Set the properties
            cf.setStringProperty(WMQConstants.WMQ_HOST_NAME, "ger-1-486b.qm.us-south.mq.appdomain.cloud");
            cf.setIntProperty(WMQConstants.WMQ_PORT,31845);
            cf.setStringProperty(WMQConstants.WMQ_CHANNEL,"MQ.QS.SVRCONN");
            cf.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, WMQConstants.WMQ_CM_CLIENT);
            cf.setStringProperty(WMQConstants.WMQ_QUEUE_MANAGER,"GER.1");
            cf.setStringProperty(WMQConstants.WMQ_APPLICATIONNAME, "Java Put/Get");
            cf.setBooleanProperty(WMQConstants.USER_AUTHENTICATION_MQCSP, true);
            cf.setStringProperty(WMQConstants.USERID,"usertest");
            cf.setStringProperty(WMQConstants.PASSWORD,"");

            cf.setStringProperty(WMQConstants.WMQ_SSL_CIPHER_SUITE, "TLS_RSA_WITH_AES_256_CBC_SHA256"); //TLS_RSA_WITH_AES_256_CBC_SHA256

            // Create JMS objects
            context = cf.createContext();
            destination = context.createQueue("DEV.QUEUE.1");

            //MQ.QUICKSTART.SVRCONN
            //user1
            //DEV.QUEUE.1


            // RECEIVE MESSAGE

            consumer = context.createConsumer(destination); // autoclosable
            String recMsg = consumer.receiveBody(String.class, 15000); // in ms or 15 seconds


            String eciResponse = recMsg;

            if (eciResponse == null) {
                LOGGER.info("SEM NOVAS MENSAGENS CLOUD");
            }
            else {

                LOGGER.info("\nRECEBENDO RETORNO DA CLOUD MQ:\n"+recMsg);
                ECITransformer transform = new ECITransformer(eciResponse);
                ECIFields fields = transform.transformToField();

                LOGGER.info("MQOUT - PROCESSANDO RETORNO DO CHAMADO..: " + fields.getField("0007"));

                // CUSTOMIZANDO A STRING DO ECI E MONTANDO XML

                UpdateFIELD c = new UpdateFIELD();


                LOGGER.info("ECI STRING: " + eciResponse);

                LOGGER.info("MQOUT - PROCESSADO CALL NUMERO : " + fields.getField("0007"));

                c.setFieldTicket(fields.getField("0007"));
                c.setRcmsTicket(fields.getField("0146"));
                c.setComments(fields.getField("0012"));
                c.setSourceSystem("FIELD");

                JAXBContext jaxbContext = JAXBContext.newInstance(UpdateFIELD.class);
                Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
                jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                StringWriter sw = new StringWriter();
                jaxbMarshaller.marshal(c, sw);
                String xmlString = sw.toString();

                String xmlString2 = xmlString.replaceAll("\n","").replaceAll("\"", "\\\"").replaceAll("    ", "");

                LOGGER.info(xmlString2);

                LOGGER.info("POSTANDO NO MQ PROXXI..");

                context.close();

                recordSuccess();

            }

        } catch (JMSException jmsex) {
            recordFailure(jmsex);
        }

    } // end main()

    public String correctString(String data, int maxSize, int size){
        if (size > maxSize) {
            data = data.substring(0, maxSize + 1);
        }
        data = data.toUpperCase();

        return data;
    }
    /**
     * Record this run as successful.
     */
    private static void recordSuccess() {
        LOGGER.info("SUCCESS");
        status = 0;
        return;
    }
    /**
     * Record this run as failure.
     *
     * @param ex
     */
    private static void recordFailure(Exception ex) {
        if (ex != null) {
            if (ex instanceof JMSException) {
                processJMSException((JMSException) ex);
            } else {
                System.out.println(ex);
            }
        }
        System.out.println("FAILURE");
        status = -1;
        return;
    }
    private static void processJMSException(JMSException jmsex) {
        System.out.println("MQ Exception "+jmsex);
        Throwable innerException = jmsex.getLinkedException();
        if (innerException != null) {
            System.out.println("Inner exception(s):");
        }
        while (innerException != null) {
            innerException = innerException.getCause();
            System.out.println("ERRO MQ.." +innerException);
        }
    }
}
