package org.talkdesk.pop3.pojo;

import lombok.Data;

@Data
public class Pop3Entity {

    private int serialNum;

    private long mailUid;

    private long mailSize;
}
