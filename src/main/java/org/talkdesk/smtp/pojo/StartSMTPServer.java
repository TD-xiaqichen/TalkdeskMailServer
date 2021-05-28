package org.talkdesk.smtp.pojo;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.talkdesk.container.ContainerBeans;
import org.talkdesk.smtp.core.exception.BindPortException;
import org.talkdesk.smtp.core.exception.OutOfRangePortException;
import org.talkdesk.smtp.server.SMTPServerHandler;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class StartSMTPServer {

     private int port;
     private String hostStr;

    public StartSMTPServer(int port,String hostStr){
        this.port = port;
        this.hostStr = hostStr;
        try {
            InetAddress inetAddress = InetAddress.getByName(hostStr);
            SMTPServerHandler.INSTANCE.startServer(port, inetAddress);
        } catch (BindPortException e) {
            e.printStackTrace();
        } catch (OutOfRangePortException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public StartSMTPServer(){
        PropertiesConfiguration propertiesConfiguration = new PropertiesConfiguration();
        propertiesConfiguration.setDelimiterParsingDisabled(true);
        try {
         //  propertiesConfiguration.load();
            int port = 1025;
            String hostStr = propertiesConfiguration.getString("127.0.0.1");
            InetAddress inetAddress = InetAddress.getByName(hostStr);
            SMTPServerHandler.INSTANCE.startServer(port, inetAddress);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (OutOfRangePortException e) {
            e.printStackTrace();
        } catch (BindPortException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        int port =1025;
        String hostStr = "127.0.0.1";
        InetAddress host = null;
        try {
            if (hostStr != null && !hostStr.isEmpty()) {
                host = InetAddress.getByName(hostStr);
            }
            SMTPServerHandler.INSTANCE.startServer(port, host);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (OutOfRangePortException e) {
            e.printStackTrace();
        } catch (BindPortException e) {
            e.printStackTrace();
        }

    }

}
