package org.talkdesk.pop3.server;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class POPServer extends Thread{


     ServerSocket serverSocket;

     public static POPServer server;

     private int pop3Port;

     private static String CONFPATH = "config.properties";

    public int getPop3Port() {
        return pop3Port;
    }

    public void setPop3Port(int pop3Port) {
        this.pop3Port = pop3Port;
    }

     private POPServer(){
         try {
         //    int pop3Port = 995;
             PropertiesConfiguration propertiesConfiguration = new PropertiesConfiguration();
             propertiesConfiguration.setDelimiterParsingDisabled(true);
             propertiesConfiguration.setFileName(CONFPATH);
             propertiesConfiguration.load();
             pop3Port = Integer.parseInt(propertiesConfiguration.getString("pop3_port"));
             serverSocket = new ServerSocket(pop3Port);
         } catch (IOException e) {
             e.printStackTrace();
         } catch (ConfigurationException e) {
             e.printStackTrace();
         }
     }

     // start Server
    public static void openServer(){
         server = new POPServer();
         server.start();
    }

    //close Server
    public static void closeServer(){
         server.close();
        System.out.println("POP Server closed");
    }

    public void close(){
        try {
            serverSocket.close();
            server.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        try {
          //  serverSocket = new ServerSocket(995);
            System.out.println("pop服务启动,端口号是:"+serverSocket.getLocalPort());
            while (true){
                Socket socket = serverSocket.accept();
                POPServerThread popServerThread = new POPServerThread(socket);
                popServerThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        POPServer.openServer();
    }

}
