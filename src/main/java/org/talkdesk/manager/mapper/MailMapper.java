package org.talkdesk.manager.mapper;

import org.springframework.stereotype.Repository;
import org.talkdesk.database.JDBCUtils;
import org.talkdesk.model.JamesUser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class MailMapper {

    public void addJamesUser(JamesUser jamesUser) {
            Connection connection = null;
            try {
            connection = JDBCUtils.getConnection();
            String sql ="INSERT INTO james_user" +
                    "(user_name, password_hash_algorithm, password, version)" +
                    "VALUES(?, ?, ?, ?);";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setObject(1,jamesUser.getUserName());
                preparedStatement.setObject(2,jamesUser.getAlg());
                preparedStatement.setObject(3,jamesUser.getPassword());
                preparedStatement.setObject(4,jamesUser.getVersion());
                preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
                JDBCUtils.close(null,null,connection);
        }

    }

}
