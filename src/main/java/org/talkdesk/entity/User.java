package org.talkdesk.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

@Data
@TableName(value = "t_user")
public class User {

    private String id;
    private String name;
    private Integer age;
    private String email;
}
