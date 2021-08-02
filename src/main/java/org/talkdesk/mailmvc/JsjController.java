package org.talkdesk.mailmvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.talkdesk.entity.User;
import org.talkdesk.mapper.UserMapper;

import java.util.Random;

@Controller
public class JsjController {

    @Autowired
    private UserMapper userMapper;

    @RequestMapping("/saveData")
    @ResponseBody
    public Object saveData(){

        User u = new User();
        u.setAge(40);
        u.setEmail("xqichen233@gmail.com");
        Random random = new Random();
        u.setId(random.nextInt()+"");
        u.setName("aoteman");
        userMapper.insert(u);
        return u;
    }

}
