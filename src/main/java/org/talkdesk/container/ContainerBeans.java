package org.talkdesk.container;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.talkdesk.pop3.server.POP3ServerStart;
import org.talkdesk.smtp.pojo.StartSMTPServer;

@Configuration
@ImportResource("beans.xml")
public class ContainerBeans {


//    @Bean
//    public PropertiesConfiguration mailConfig() {
//        PropertiesConfiguration propertiesConfiguration = new PropertiesConfiguration();
//        propertiesConfiguration.setDelimiterParsingDisabled(true);
//        propertiesConfiguration.setFileName(CONFIGPATH);
//        try {
//            propertiesConfiguration.load();
//        } catch (ConfigurationException e) {
//            e.printStackTrace();
//        }
//        return propertiesConfiguration;
//    }

//     @Bean
//    public StartSMTPServer smtpServer(){
//         StartSMTPServer smtpServer = new StartSMTPServer();
//         return smtpServer;
//    }
//
//    @Bean
//    public POP3ServerStart pop3Server(){
//        return new POP3ServerStart();
//    }
}
