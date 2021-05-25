package org.talkdesk.model;

import lombok.Data;

@Data
public class Mailbox {

       private Long mailboxId;

       private Long highestModseq;

       private Long lastUid;

       private String mailboxName;

       private String mailboxNameSpace;

       private Long validity;

       private String user_name;
}
