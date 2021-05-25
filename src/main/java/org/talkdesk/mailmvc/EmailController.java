package org.talkdesk.mailmvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.talkdesk.manager.MailManager;
import org.talkdesk.model.MailDTO;

import java.util.HashMap;
import java.util.Map;

@Controller
public class EmailController {

    @Autowired
    private MailManager manager;

    @RequestMapping("addEmail")
    @ResponseBody
    public Object addEmailAccount(@RequestBody MailDTO mailDTO){
        String email = mailDTO.getEmail();
        String password = mailDTO.getPassword();
        manager.addJamesUser(email,password);
        Map<String,Object> map = new HashMap<>();
        map.put("status",1);
        map.put("msg","ok");
        return map;
    }

}
