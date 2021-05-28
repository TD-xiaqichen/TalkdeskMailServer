package org.talkdesk.smtp.server;

import com.sun.mail.smtp.SMTPTransport;
import org.talkdesk.util.DNSUtils;
import org.xbill.DNS.TextParseException;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayInputStream;
import java.net.UnknownHostException;
import java.util.Properties;

public class TrytoHost {

          public TrytoHost(String recipient,String mimeStr,String from){
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
              Session session = Session.getInstance(prop);
              SMTPTransport transport = null;
              try {
                  String mailIPAddress = DNSUtils.getSMTPAddressByRecipient(recipient);
                //  URLName urlName =new URLName("smtp","220.181.14.158",25,null,null,null);
                  URLName urlName =new URLName("smtp",mailIPAddress,25,null,null,null);
                  MimeMessage mimeMessage = new MimeMessage(session,new ByteArrayInputStream(mimeStr.getBytes()));
                  transport= (SMTPTransport) session.getTransport(urlName);
                  transport.connect();
                  System.out.println("^^^^^^^^"+mimeMessage.getHeader("To",","));
                  transport.sendMessage(mimeMessage,mimeMessage.getAllRecipients());
                  System.out.println("  Send done ");
              } catch (NoSuchProviderException e) {
                  e.printStackTrace();
              } catch (SendFailedException e) {
                  e.printStackTrace();
              } catch (MessagingException e) {
                  e.printStackTrace();
              } catch (UnknownHostException e) {
                  e.printStackTrace();
              } catch (TextParseException e) {
                  e.printStackTrace();
              }
          }

}
