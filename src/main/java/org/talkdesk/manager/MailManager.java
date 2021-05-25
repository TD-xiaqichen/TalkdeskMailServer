package org.talkdesk.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.talkdesk.manager.mapper.MailMapper;
import org.talkdesk.model.JamesUser;

import org.apache.commons.codec.digest.DigestUtils;
@Service
public class MailManager {

    @Autowired
    private MailMapper mailMapper;

       public void addJamesUser(String email,String password){
            JamesUser jamesUser = new JamesUser();
           String md5Hex = DigestUtils.md5Hex(password);
           jamesUser.setUserName(email);
           jamesUser.setPassword(md5Hex);
           mailMapper.addJamesUser(jamesUser);
       }

}
