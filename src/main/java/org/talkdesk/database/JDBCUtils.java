package org.talkdesk.database;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.talkdesk.container.ContainerBeans;

import java.sql.*;

public class JDBCUtils {

    private static String url = null;
    private static String user = null;
    private static String password = null;
    private static String driver = null;

    private static String CONFPATH = "config.properties";

    public JDBCUtils(){

    }

//    public JDBCUtils(String url,String driver,String user,String password){
//        this.url=url;
//        this.driver=driver;
//        this.user=user;
//        this.password=password;
//        try {
//            Class.forName(driver);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//    }

    static {
        PropertiesConfiguration propertiesConfiguration = new PropertiesConfiguration();
        propertiesConfiguration.setDelimiterParsingDisabled(true);
        propertiesConfiguration.setFileName(CONFPATH);
        try {
            propertiesConfiguration.load();
      //      url = "jdbc:postgresql://127.0.0.1:5432/talkdesk";
            url=propertiesConfiguration.getString("db_url");
        //    user = "postgres";
            user = propertiesConfiguration.getString("db_user");
        //    password = "Aa12345678";
            password = propertiesConfiguration.getString("db_password");
          //  driver = "org.postgresql.Driver";
            driver = propertiesConfiguration.getString("db_driver");
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    public  static Connection getConnection() throws SQLException {
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
