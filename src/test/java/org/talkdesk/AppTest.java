package org.talkdesk;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.talkdesk.entity.User;
import org.talkdesk.mapper.UserMapper;

import java.util.Random;

@SpringBootTest
public class AppTest {

     @Autowired
     private UserMapper userMapper;

     @Test
     public void addData(){
         User u = new User();
         u.setAge(40);
         u.setEmail("xqichen233@gmail.com");
         Random random = new Random();
         u.setId(random.nextInt()+"");
         u.setName("aoteman");
         userMapper.insert(u);
     }

}
