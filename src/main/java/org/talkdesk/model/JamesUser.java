package org.talkdesk.model;

import lombok.Data;

@Data
public class JamesUser {

    private String userName;
    private String alg = "MD5";
    private String password;
    private int version =1;

}
