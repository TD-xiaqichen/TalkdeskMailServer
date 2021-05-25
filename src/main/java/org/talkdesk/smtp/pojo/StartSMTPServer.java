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

     public static final String KEYOFSMTPPORT = "smtp_port";
     public static final String HOST ="host";

    public StartSMTPServer(){
        PropertiesConfiguration propertiesConfiguration = new PropertiesConfiguration();
        propertiesConfiguration.setDelimiterParsingDisabled(true);
        propertiesConfiguration.setFileName(ContainerBeans.CONFIGPATH);
        try {
            propertiesConfiguration.load();
            int port = propertiesConfiguration.getInt(KEYOFSMTPPORT);
            String hostStr = propertiesConfiguration.getString(HOST);
            InetAddress inetAddress = InetAddress.getByName(hostStr);
            SMTPServerHandler.INSTANCE.startServer(port, inetAddress);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (OutOfRangePortException e) {
            e.printStackTrace();
        } catch (BindPortException e) {
            e.printStackTrace();
        } catch (ConfigurationException e) {
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
