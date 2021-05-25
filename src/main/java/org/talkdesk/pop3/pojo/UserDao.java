package org.talkdesk.pop3.pojo;

import org.apache.commons.io.IOUtils;
import org.talkdesk.database.JDBCUtils;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDao {

    public boolean isUser(String email,String password){

        return true;
    }

    public void deleteMail(String email,long mailUid){
        Connection connection = null;
        try {
            long mailboxId = 0;
            connection = JDBCUtils.getConnection();
            connection.setAutoCommit(false);
            String sql = "select mailbox_id from james_mailbox where user_name=? " +
                    "and mailbox_name =?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, email);
            preparedStatement.setObject(2, "INBOX");
            ResultSet resultSet = preparedStatement.executeQuery();
            mailboxId = resultSet.next() ? resultSet.getLong("mailbox_id") : mailboxId;

            sql = "delete from james_mail where mail_uid =? and mailbox_id = ?";
            PreparedStatement preparedStatement1 = connection.prepareStatement(sql);
            preparedStatement1.setObject(1,mailUid);
            preparedStatement1.setObject(2,mailboxId);
            preparedStatement1.executeUpdate();
            connection.commit();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            JDBCUtils.close(null,null,connection);
        }

   }

   public Map<String,Long> getLengthAndTotalSize(String email){
        Connection connection = null;
        Map<String,Long> map = new HashMap<>();
        try{
            long mailboxId = 0;
            connection = JDBCUtils.getConnection();
            String sql = "select mailbox_id from james_mailbox where user_name=? " +
                    "and mailbox_name =?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, email);
            preparedStatement.setObject(2, "INBOX");
            ResultSet resultSet = preparedStatement.executeQuery();
            mailboxId = resultSet.next() ? resultSet.getLong("mailbox_id") : mailboxId;

            sql = "select count(*) as count,sum(mail_content_octets_count) as totalsize from james_mail " +
                    " where mailbox_id =?";
            PreparedStatement preparedStatement1 = connection.prepareStatement(sql);
            preparedStatement1.setObject(1,mailboxId);
            ResultSet resultSet1 = preparedStatement1.executeQuery();
            long count=0;
            long totalsize =0;
            while (resultSet1.next()){
                 count =resultSet1.getLong("count");
                 totalsize = resultSet1.getLong("totalsize");
            }

            map.put("count",count);
            map.put("totalsize",totalsize);
        }catch (Exception e){
           e.printStackTrace();
        }finally {
            JDBCUtils.close(null,null,connection);
        }
        return map;
   }

    public byte[] getFullContent(long uid,String email){
        Connection connection =null;
        byte[] sb =null;
        try {
            long mailboxId = 0;
            connection = JDBCUtils.getConnection();
            String sql = "select mailbox_id from james_mailbox where user_name=? " +
                    "and mailbox_name =?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, email);
            preparedStatement.setObject(2, "INBOX");
            ResultSet resultSet = preparedStatement.executeQuery();
            mailboxId = resultSet.next() ? resultSet.getLong("mailbox_id") : mailboxId;
            sql = "select mail_bytes,header_bytes from james_mail " +
                    "where mailbox_id =? and mail_uid =?";
            PreparedStatement preparedStatement1 = connection.prepareStatement(sql);
            preparedStatement1.setObject(1,mailboxId);
            preparedStatement1.setObject(2,uid);
            ResultSet resultSet1 = preparedStatement1.executeQuery();
            byte[] mailBytes =null;
            byte[] headerBytes =null;
            while (resultSet1.next()){
                mailBytes = resultSet1.getBytes("mail_bytes");
                headerBytes = resultSet1.getBytes("header_bytes");
            }
            InputStream inputStream = new SequenceInputStream(new ByteArrayInputStream(headerBytes),
                    new ByteArrayInputStream(mailBytes));
           sb = IOUtils.toByteArray(inputStream);
            if(inputStream !=null){
                inputStream.close();
            }
        }catch (SQLException e){
           e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(null,null,connection);
        }
        return sb;
    }

    public String getMimeStrByUid(long uid,String email){
        Connection connection =null;
        StringBuffer sb = new StringBuffer();
        try {
            long mailboxId = 0;
            connection = JDBCUtils.getConnection();
            String sql = "select mailbox_id from james_mailbox where user_name=? " +
                    "and mailbox_name =?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, email);
            preparedStatement.setObject(2, "INBOX");
            ResultSet resultSet = preparedStatement.executeQuery();
            mailboxId = resultSet.next() ? resultSet.getLong("mailbox_id") : mailboxId;
            sql = "select mail_bytes,header_bytes from james_mail " +
                    "where mailbox_id =? and mail_uid =?";
            PreparedStatement preparedStatement1 = connection.prepareStatement(sql);
            preparedStatement1.setObject(1,mailboxId);
            preparedStatement1.setObject(2,uid);
            ResultSet resultSet1 = preparedStatement1.executeQuery();
            byte[] mailBytes =null;
            byte[] headerBytes =null;
            while (resultSet1.next()){
                 mailBytes = resultSet1.getBytes("mail_bytes");
                headerBytes = resultSet1.getBytes("header_bytes");
            }
            InputStream inputStream = new SequenceInputStream(new ByteArrayInputStream(headerBytes),
                    new ByteArrayInputStream(mailBytes));
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String txt;
                while ((txt = reader.readLine()) != null) {
                    sb.append(txt);
                }
                if(reader!=null){
                    reader.close();
                }
                if(inputStream !=null){
                    inputStream.close();
                }
        }catch (SQLException e){
              e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(null,null,connection);

        }
        return sb.toString();
    }

    public List<Long> getUidAndSeq(String email){
        Connection connection =null;
        List<Long> datas = new ArrayList<>();
        try {
            long mailboxId =0;
            connection = JDBCUtils.getConnection();
            String sql = "select mailbox_id from james_mailbox where user_name=? " +
                    "and mailbox_name =?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1,email);
            preparedStatement.setObject(2,"INBOX");
            ResultSet resultSet = preparedStatement.executeQuery();
            mailboxId =resultSet.next()?resultSet.getLong("mailbox_id"):mailboxId;
            sql = "select mail_uid from james_mail " +
                    "where mailbox_id =?";
            PreparedStatement preparedStatement1 = connection.prepareStatement(sql);
            preparedStatement1.setObject(1,mailboxId);
            ResultSet resultSet1 = preparedStatement1.executeQuery();
            while (resultSet1.next()){
                long fullOctetscount = resultSet1.getLong("mail_uid");
                datas.add(fullOctetscount);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JDBCUtils.close(null,null,connection);
        }

        return datas;
    }

    public List<Pop3Entity> getSizeAndSeq(String email){
        Connection connection =null;
//        List<Long> datas = new ArrayList<>();
        List<Pop3Entity> datas2 = new ArrayList<>();
        try {
            long mailboxId =0;
            connection = JDBCUtils.getConnection();
            String sql = "select mailbox_id from james_mailbox where user_name=? " +
                    "and mailbox_name =?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1,email);
            preparedStatement.setObject(2,"INBOX");
            ResultSet resultSet = preparedStatement.executeQuery();
            mailboxId =resultSet.next()?resultSet.getLong("mailbox_id"):mailboxId;
            sql = "select mail_content_octets_count,mail_uid from james_mail " +
                    "where mailbox_id =?";
            PreparedStatement preparedStatement1 = connection.prepareStatement(sql);
            preparedStatement1.setObject(1,mailboxId);
            ResultSet resultSet1 = preparedStatement1.executeQuery();
            int serialNum = 1;
            while (resultSet1.next()){
                long fullOctetscount = resultSet1.getLong("mail_content_octets_count");
                long mailUid = resultSet1.getLong("mail_uid");
                Pop3Entity entity = new Pop3Entity();
                entity.setMailSize(fullOctetscount);
                entity.setMailUid(mailUid);
                entity.setSerialNum(serialNum++);
              //  datas.add(fullOctetscount);
                datas2.add(entity);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JDBCUtils.close(null,null,connection);
        }
        return datas2;
    }

}
