package org.talkdesk.smtp.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class MailMessage {

      private Long mailboxId;

      private Long mailUid;

      private boolean isAnswered = false;

      private int bodyStartOcets;

      private Long fullContentCount;

      private boolean isDeleted = false;

      private boolean isDraft = false;

      private boolean isFlagged = false;

      private Date mailDate;

      private String mimeType = "text";

      private Long modSeq;

      private boolean isRecent = false;

      private boolean isSeen = false;

      private String subType ;

      private Long lineCount = null;

      private byte[] body;

      private byte[] header;

      private String mirror;

}
