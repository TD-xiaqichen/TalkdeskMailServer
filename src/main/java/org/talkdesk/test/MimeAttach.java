package org.talkdesk.test;

import lombok.Data;

import javax.activation.DataSource;
import java.util.List;
import java.util.Map;

@Data
public class MimeAttach {

     private String filename;

     private byte[] attachBody;

     private String contentType;

     private List<DataSource> ordinaryAttach;

     private Map<String,DataSource> cidMap;

}
