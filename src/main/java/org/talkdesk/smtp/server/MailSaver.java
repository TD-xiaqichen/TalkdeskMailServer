package org.talkdesk.smtp.server;

import org.apache.commons.io.IOUtils;
import org.apache.james.server.core.MailHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.sql.Date;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Saves emails and notifies components so they can refresh their views with new data.
 *
 * @author Nilhcem
 * @since 1.0
 */
public final class MailSaver extends Observable {

	private static final Logger LOGGER = LoggerFactory.getLogger(MailSaver.class);
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	// This can be a static variable since it is Thread Safe
	private static final Pattern SUBJECT_PATTERN = Pattern.compile("^Subject: (.*)$");

	private final SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyhhmmssSSS");

	/**
	 * Saves incoming email in file system and notifies observers.
	 *
	 * @param from the user who send the email.
	 * @param to the recipient of the email.
	 * @param data an InputStream object containing the email.
	 */
	public void saveEmailAndNotify(String from, String to, InputStream data) {

	}


	public boolean isExistMailbox(String userName,String mailboxName){
		Connection connection =null;
		try {
			connection = JDBCUtils.getConnection();
			String sql = "select * from james_mailbox where user_name=? and mailbox_name =?";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setObject(1,userName);
			preparedStatement.setObject(2,mailboxName);
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()){
				return true;
			}else {
				return false;
			}
		} catch (SQLException throwables) {
			LOGGER.error(throwables.getMessage(),throwables);
		}finally {
			JDBCUtils.close(null,null,connection);
		}
		return false;
	}

	public Map<String,Long> saveEmailIntoMailbox(String userName,String mailboxName,String mimeStr){
		Connection connection = null;
		Long mailUid = 1L;
		Long mailboxId =1L;
		try {
			connection = JDBCUtils.getConnection();
			connection.setAutoCommit(false);
			String sql = "select mailbox_last_uid,mailbox_id from james_mailbox jm " +
					"where mailbox_name =? and user_name =?";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setObject(1,mailboxName);
			preparedStatement.setObject(2,userName);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()){
				mailUid = resultSet.getLong("mailbox_last_uid");
				mailboxId = resultSet.getLong("mailbox_id");
			}
			if(mailUid!=null){
				mailUid +=2;
			}

			MailMessage mailMessage = convertString(mimeStr, mailUid, mailboxId);
			sql = "INSERT INTO james_mail" +
					" (mailbox_id, mail_uid, mail_is_answered, mail_body_start_octet, mail_content_octets_count, " +
					"mail_is_deleted, mail_is_draft, mail_is_flagged, mail_date, mail_mime_type, mail_modseq, " +
					"mail_is_recent, mail_is_seen, mail_mime_subtype, mail_textual_line_count," +
					" mail_bytes, header_bytes,mirror)" +
					"VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?);";
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
			preparedStatement1.setObject(18,mailMessage.getMirror());
			preparedStatement1.executeUpdate();
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
		Map<String,Long> resultMap = new HashMap<>();
		resultMap.put("mailUid",mailUid);
		resultMap.put("mailboxId",mailboxId);
       return resultMap;
	}

	public void updateLastUidInMailbox(Long mailboxId,Long currentMailUid) {
		Connection conn = null;
		try {
			conn = JDBCUtils.getConnection();
			String sql = "update james_mailbox set mailbox_last_uid  = ? where mailbox_id =?";
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setObject(1,currentMailUid);
			preparedStatement.setObject(2,mailboxId);
			preparedStatement.executeUpdate();
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}finally {
			JDBCUtils.close(null,null,conn);
		}
	}

	private MailMessage convertString(String orignContent,Long mailUid,Long mailboxId) throws MessagingException, IOException {
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(orignContent.getBytes(StandardCharsets.UTF_8));
		MimeMessage message = new MimeMessage(Session.getDefaultInstance(new Properties()), byteArrayInputStream);
		MailMessage mailMessage = new MailMessage();
		mailMessage.setFullContentCount((long) orignContent.getBytes(StandardCharsets.UTF_8).length);
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
		mailMessage.setLineCount((long)message.getLineCount());
		mailMessage.setMirror(orignContent);
		return mailMessage;
	}

	public void createMailbox(String userName,String mailboxName) {
		Connection connection =null;
		try {
			connection = JDBCUtils.getConnection();
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
			preparedStatement.setObject(4,mailboxName);
			preparedStatement.setObject(5,"#private");
			preparedStatement.setObject(6,Math.abs(RANDOM.nextLong()));
			preparedStatement.setObject(7,userName);
			preparedStatement.executeUpdate();
		} catch (SQLException throwables) {
			LOGGER.error(throwables.getMessage(),throwables);
		}finally {
			JDBCUtils.close(null,null,connection);
		}

	}

}
