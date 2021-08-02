package org.talkdesk;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("org.talkdesk.mapper")
@SpringBootApplication
public class MailServerStart {

    public static void main(String[] args) {
        SpringApplication.run(MailServerStart.class,args);
    }

}
