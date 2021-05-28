package org.talkdesk.smtp.server;

import org.apache.commons.io.IOUtils;
import org.apache.james.server.core.MailHeaders;
import org.subethamail.wiser.Wiser;
import org.talkdesk.database.JDBCUtils;
import org.talkdesk.smtp.pojo.MailMessage;

import javax.mail.Header;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.sql.*;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

/**
 * Listens to incoming emails and redirects them to the {@code MailSaver} object.
 *
 * @author Nilhcem
 * @since 1.0
 */
public final class MailListener extends Wiser {
	private final MailSaver saver;

	/**
	 * Creates the listener.
	 *
	 * @param saver a {@code MailServer} object used to save emails and notify components.
	 */
	public MailListener(MailSaver saver) {
		this.saver = saver;
	}

	/**
	 * Accepts all kind of email <i>(always return true)</i>.
	 * <p>
	 * Called once for every RCPT TO during a SMTP exchange.<br>
     * Each accepted recipient will result in a separate deliver() call later.
     * </p>
     *
	 * @param from the user who send the email.
	 * @param recipient the recipient of the email.
	 * @return always return {@code true}
	 */
	public boolean accept(String from, String recipient) {
		return true;
	}

    /**
     * Receives emails and forwards them to the {@link MailSaver} object.
     */
	@Override
	public void deliver(String from, String recipient, InputStream data) throws IOException {
		byte[] bytes = IOUtils.toByteArray(data);
		String s = new String(bytes);
		System.out.println(from);
		System.out.println("-------------begin-------------");
		System.out.println(s);
		System.out.println("-------------end---------------");
		System.out.println(recipient);
		//saver.saveEmailAndNotify(from, recipient, data);
		boolean existFrom = saver.isExistMailbox(from,"Sent");
		if(existFrom==false){
			saver.createMailbox(from,"Sent");
		}
		Map<String, Long> stringLongMap = saver.saveEmailIntoMailbox(from, "Sent", s);
		saver.updateLastUidInMailbox(stringLongMap.get("mailboxId"),stringLongMap.get("mailUid"));
		boolean existMailbox = saver.isExistMailbox(recipient, "INBOX");
		if(existMailbox==false){
			if(recipient.contains("@163.com")){
               	//todo 发163邮箱
             new TrytoHost(recipient,s,from);
			}else{
				saver.createMailbox(recipient,"INBOX");
				Map<String, Long> inbox = saver.saveEmailIntoMailbox(recipient, "INBOX", s);
				saver.updateLastUidInMailbox(inbox.get("mailboxId"),inbox.get("mailUid"));
			}
		}
	}

	private MailMessage convertString(String orignContent, Long mailUid, Long mailboxId) throws MessagingException, IOException {
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(orignContent.getBytes(StandardCharsets.UTF_8));
		MimeMessage message = new MimeMessage(Session.getDefaultInstance(new Properties()), byteArrayInputStream);
		MailMessage mailMessage = new MailMessage();
		mailMessage.setFullContentCount((long) message.getSize());
		mailMessage.setMailUid(mailUid);
		mailMessage.setMailboxId(mailboxId);
		Enumeration allHeaders = message.getAllHeaders();
		MailHeaders mailHeaders = new MailHeaders();
		while (allHeaders.hasMoreElements()){
			Header o = (Header) allHeaders.nextElement();
			mailHeaders.addHeader(o.getName(),o.getValue());
		}
		mailMessage.setBodyStartOcets((int)mailHeaders.getSize());
		mailMessage.setMailDate(message.getSentDate());
		mailMessage.setHeader(mailHeaders.toByteArray());
		mailMessage.setBody(IOUtils.toByteArray(message.getRawInputStream()));
		return mailMessage;
	}

	private void saveEmail(String recipient,String mimeStr){
		Connection connection = null;
		try {
			Long mailUid = 1L;
			Long mailboxId =1L;
			connection = JDBCUtils.getConnection();
			connection.setAutoCommit(false);
			String sql = "select mailbox_last_uid,mailbox_id from james_mailbox jm " +
					"where mailbox_name ='INBOX' and user_name =?";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setObject(1,recipient);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()){
				mailUid = resultSet.getLong("mailbox_last_uid");
				mailboxId = resultSet.getLong("mailbox_id");
			}
			if(mailUid!=null){
				mailUid +=1;
			}

			MailMessage mailMessage = convertString(mimeStr, mailUid, mailboxId);
			sql = "INSERT INTO james_mail" +
					" (mailbox_id, mail_uid, mail_is_answered, mail_body_start_octet, mail_content_octets_count, " +
					"mail_is_deleted, mail_is_draft, mail_is_flagged, mail_date, mail_mime_type, mail_modseq, " +
					"mail_is_recent, mail_is_seen, mail_mime_subtype, mail_textual_line_count," +
					" mail_bytes, header_bytes)" +
					"VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
			PreparedStatement preparedStatement1 = connection.prepareStatement(sql);
			preparedStatement1.setObject(1,mailMessage.getMailboxId());
			preparedStatement1.setObject(2,mailMessage.getMailUid());
			preparedStatement1.setObject(3,mailMessage.isAnswered());
			preparedStatement1.setObject(4,mailMessage.getBodyStartOcets());
			preparedStatement1.setObject(5,mailMessage.getFullContentCount());
			preparedStatement1.setObject(6,mailMessage.isDeleted());
			preparedStatement1.setObject(7,mailMessage.isDraft());
			preparedStatement1.setObject(8,mailMessage.isFlagged());
			preparedStatement1.setObject(9,new Date(mailMessage.getMailDate().getTime()));
			preparedStatement1.setObject(10,mailMessage.getMimeType());
			SecureRandom RANDOM = new SecureRandom();
			preparedStatement1.setObject(11,Math.abs(RANDOM.nextInt()));
			preparedStatement1.setObject(12,mailMessage.isRecent());
			preparedStatement1.setObject(13,mailMessage.isSeen());
			preparedStatement1.setObject(14,mailMessage.getSubType());
			preparedStatement1.setObject(15,mailMessage.getLineCount());
			preparedStatement1.setObject(16,mailMessage.getBody());
			preparedStatement1.setObject(17,mailMessage.getHeader());
			preparedStatement1.executeUpdate();

			updateLastUidInMailbox(mailMessage.getMailUid(),mailMessage.getMailUid(),connection);
			connection.commit();
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			JDBCUtils.close(null,null,connection);
		}

	}


	private void updateLastUidInMailbox(Long mailboxId,Long currentMailUid,Connection conn) throws SQLException {
		String sql = "update james_mailbox set mailbox_last_uid  = ? where mailbox_id =?";
		PreparedStatement preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setObject(1,currentMailUid);
		preparedStatement.setObject(2,mailboxId);
        preparedStatement.executeUpdate();
	}

	private void createMailbox4Recipient(String recipient,Connection connection) throws SQLException {
		SecureRandom RANDOM = new SecureRandom();
         Long mailboxId = Math.abs(RANDOM.nextLong());
         String sql = "INSERT INTO public.james_mailbox " +
				 "(mailbox_id, mailbox_highest_modseq, mailbox_last_uid, " +
				 "mailbox_name, mailbox_namespace, mailbox_uid_validity, user_name) " +
				 "VALUES(?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setObject(1,mailboxId);
		preparedStatement.setObject(2,Math.abs(RANDOM.nextLong()));
		preparedStatement.setObject(3,0);
		preparedStatement.setObject(4,"INBOX");
		preparedStatement.setObject(5,"#private");
		preparedStatement.setObject(6,Math.abs(RANDOM.nextLong()));
		preparedStatement.setObject(7,recipient);
		preparedStatement.executeUpdate();
	}

}
