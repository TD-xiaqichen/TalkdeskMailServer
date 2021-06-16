package org.talkdesk.test;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.mail.util.MimeMessageParser;

import javax.activation.DataSource;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class ConfigTest {

    public static void main(String[] args) throws ConfigurationException {
        testTwo();
    }


    public static void testThree(){
        InputStream in = null;
        try{
            File attach = new File("/Users/xiaqichen/Public/testTian.eml");
            in = new FileInputStream(attach);
            MimeMessage message = new MimeMessage(Session.getDefaultInstance(new Properties()),in);
            MimeMessageParser parser = new MimeMessageParser(message);
            parser.parse();
            List<DataSource> attachmentList = parser.getAttachmentList();

        }catch (Exception e){

        }finally {
            try {
              if(in !=null) in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static  void testOne() throws ConfigurationException {
        PropertiesConfiguration propertiesConfiguration = new PropertiesConfiguration();
        propertiesConfiguration.setDelimiterParsingDisabled(true);
        propertiesConfiguration.setFileName("config.properties");
        propertiesConfiguration.load();
        String db_url = propertiesConfiguration.getString("db_url");
        System.out.println(db_url);
    }

    public static void testTwo(){
        String ii = "> i think";
        boolean b = ii.indexOf(">") == 0;
        System.out.println(b);
    }

}
