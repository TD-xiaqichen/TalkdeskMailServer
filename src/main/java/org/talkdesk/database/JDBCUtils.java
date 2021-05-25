package org.talkdesk.database;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.talkdesk.container.ContainerBeans;

import java.sql.*;

public class JDBCUtils {

    private static String KEYOFURL = "db_url";
    private static String KEYOFUSER = "db_user";
    private static String KEYOFPASSWORD = "db_password";
    private static String KEYOFDRIVER = "db_driver";

    private static String url = null;
    private static String user = null;
    private static String password = null;
    private static String driver = null;

    static {
        PropertiesConfiguration propertiesConfiguration = new PropertiesConfiguration();
        propertiesConfiguration.setDelimiterParsingDisabled(true);
        propertiesConfiguration.setFileName(ContainerBeans.CONFIGPATH);
        try {
            propertiesConfiguration.load();
            url = propertiesConfiguration.getString(KEYOFURL);
            user = propertiesConfiguration.getString(KEYOFUSER);
            password = propertiesConfiguration.getString(KEYOFPASSWORD);
            driver = propertiesConfiguration.getString(KEYOFDRIVER);
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }


    }

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(url, user, password);
        return conn;
    }

    public static void close(ResultSet rs, Statement st, Connection conn) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
