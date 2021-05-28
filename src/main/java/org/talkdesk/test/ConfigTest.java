package org.talkdesk.test;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class ConfigTest {

    public static void main(String[] args) throws ConfigurationException {
        testTwo();
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
