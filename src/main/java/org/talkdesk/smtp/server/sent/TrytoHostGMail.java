package org.talkdesk.smtp.server.sent;

import com.sun.mail.smtp.SMTPTransport;
import org.subethamail.smtp.client.SMTPClient;
import org.talkdesk.util.DNSUtils;
import org.xbill.DNS.TextParseException;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Properties;

public class TrytoHostGMail {

    private Properties props;

    private String from;

    private String recipient;

    private String mimeStr;

    private List<String> targetHosts;

    public TrytoHostGMail(String recipient, String mimeStr, String from, List<String> targetHosts){
        this.recipient =recipient;
        this.mimeStr = mimeStr;
        this.from = from;
        this.targetHosts = targetHosts;
        createPropties();
        connectHosts(targetHosts);
    }

    private void createPropties(){
        Properties prop = new Properties();
        prop.put("mail.smtp.ehlo", "true");
        prop.put("mail.smtp.host", "localhost");
        prop.put("mail.smtp.auth", "false");
        prop.put("mail.debug", "false");
        prop.put("mail.smtp.timeout", String.valueOf(3600000));
        prop.put("mail.smtp.starttls.enable", "false");
        prop.put("mail.smtp.from", from);
        prop.put("mail.smtp.sendpartial", "true");
        prop.put("mail.smtp.connectiontimeout", String.valueOf(3600000));
        prop.put("mail.smtp.ssl.enable", "false");
        this.props = prop;
    }

    private void connectHosts(List<String> targetHosts){
        try{
            for(String outgoinServer:targetHosts){
                deliverMail(outgoinServer);
            }
        }catch (Exception e){
           e.printStackTrace();
        }

    }

    private void deliverMail(String outgoinServer){
        Session session = Session.getInstance(this.props);
        SMTPTransport transport = null;
        try {
            Address[] paramTwo = new Address[1];
            InternetAddress internetAddress = new InternetAddress(recipient);
            paramTwo[0]=internetAddress;
            URLName urlName =new URLName("smtp",outgoinServer,25,null,null,null);
            MimeMessage mimeMessage = new MimeMessage(session,new ByteArrayInputStream(mimeStr.getBytes()));
            transport= (SMTPTransport) session.getTransport(urlName);
            transport.connect();
            System.out.println("^^^^^^^^"+mimeMessage.getHeader("To",","));
            transport.sendMessage(mimeMessage,paramTwo);
            System.out.println("  Send done ");
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (SendFailedException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }finally {
            try {
             if(transport != null) transport.close();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }

    }

}
