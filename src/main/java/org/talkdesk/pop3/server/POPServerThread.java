package org.talkdesk.pop3.server;


import org.talkdesk.pop3.pojo.Pop3Entity;
import org.talkdesk.pop3.pojo.UserDao;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Map;

public class POPServerThread extends Thread {
    Socket socket;
    boolean flag = true;
    BufferedInputStream inputStream;
    BufferedOutputStream outputStream;

    private String email = null, password;

    private String confirm="+OK ";
    private String refuse="-ERR ";

    List<Pop3Entity> redisInfo = null;

    private UserDao userDao = new UserDao();

    public POPServerThread(Socket socket) {
        this.socket = socket;
        try {
            inputStream = new BufferedInputStream(socket.getInputStream());
            outputStream = new BufferedOutputStream(socket.getOutputStream());
            write("+OK Welcome to gqm Mail Pop3 Server");
            System.out.println("+OK Welcome to gqm Mail Pop3 Server");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String input;
        while (flag) {
            try {
                input = read();
                if (input.toLowerCase().startsWith("capa")) {
                    System.out.println(input);
                      write("+OK ");
                    write("UIDL");
                    write("RETR");
                    write(". ");
                } else if (input.toLowerCase().startsWith("user")) {
                    System.out.println(input);
                    String[] strings = input.split(" ");
                    email = strings[1];
                    write("+OK core mail");
                } else if (input.toLowerCase().startsWith("pass")) {
                     authLogin(input);
                } else if(input.toLowerCase().startsWith("list")){
                    list(input);
                //    break;
                } else if(input.toLowerCase().startsWith("uidl")){
                    uidl(input);
                } else if(input.toLowerCase().startsWith("retr")){
                    retr(input);
                    break;
                }else if(input.toLowerCase().startsWith("noop")){
                    write("+OK ");
                }else if(input.toLowerCase().startsWith("quit")){
                   update();
                }
                else {

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void authLogin(String input) throws IOException {
        System.out.println(input);
        String[] strings = input.split(" ");
        password = strings[1];
        if (userDao.isUser(email, password)) {
            deal(input);
        }
    }

    public void deal(String input) throws IOException {
        while (true) {
            System.out.println("deal >>>>> command"+input);
            if (input.toLowerCase().startsWith("stat")) {
                stat();
            }

            else if(input.toLowerCase().startsWith("pass")){
                 pass();
                 break;
            }

        }
    }

    public void pass() throws IOException{
        write("+OK Welcome "+email);
    }

    public void update() throws IOException{
        write("+OK Apache James POP3 Server signing off");
    }

    public void top() throws IOException{
        System.out.println("进没进top------");
        write("+OK");
    }

    public void retr(String input) throws IOException{
        String[] commandAndDetails = input.split(" ");
        if(commandAndDetails.length>1){
            String seq = commandAndDetails[1];
            Integer seqNum = Integer.parseInt(seq);
            long uid = 0;
            for(Pop3Entity m:redisInfo){
                if(seqNum.equals(m.getSerialNum())){
                    uid = m.getMailUid();
                }
            }
            System.out.println("uid是："+uid);
            byte[] fullContent = userDao.getFullContent(uid, email);
            System.out.println("===========mime begin================");
            System.out.println(new String(fullContent));
           System.out.println("===========mime end================");
            write("+OK Message follows");
            write(new String(fullContent));
            write(".");
        }else{
            System.out.println("retr command param:"+input);
        }

    }

    public void list(String input) throws IOException{
        String[] commandAndDetails = input.split(" ");
        if(commandAndDetails.length>1){
            System.out.println("list command param:"+commandAndDetails.length);
        }else{
            Map<String, Long> lengthAndTotalSize = userDao.getLengthAndTotalSize(email);

            List<Pop3Entity> sizeAndSeq = userDao.getSizeAndSeq(email);
            this.redisInfo = sizeAndSeq;
            int seqIndex = 1;
            System.out.println("value of lengthAndTotalSize is:"+sizeAndSeq.size()+"<------>"+lengthAndTotalSize.get("totalsize"));
             write("+OK "+sizeAndSeq.size()+" "+lengthAndTotalSize.get("totalsize"));
            for(int i=0;i<sizeAndSeq.size();i++){
                write( sizeAndSeq.get(i).getSerialNum()+" "+ sizeAndSeq.get(i).getMailSize());
            }
        }
        write(".");
    }

    public void uidl(String input) throws IOException{
        String[] commandAndDetails = input.split(" ");
        if(commandAndDetails.length>1){
            System.out.println("uidl command param:"+commandAndDetails.length);
        }else{
            System.out.println("进入了 uidl 命令:"+input);
            write("+OK unique-id listing follows");
            for(int i=0;i<this.redisInfo.size();i++){
                write(redisInfo.get(i).getSerialNum()+" "+redisInfo.get(i).getMailUid());
            }
            write(".");
        }
    }

    public void stat() throws IOException{
        Map<String, Long> lengthAndTotalSize = userDao.getLengthAndTotalSize(email);
        write("+OK "+ lengthAndTotalSize.get("count")+" "+lengthAndTotalSize.get("totalsize"));
    }

    public String read() throws IOException {
        int ch;
        StringBuilder sb = new StringBuilder();
        while (true) {

            if(inputStream.available()==0){
                break;
            }
            ch = inputStream.read();
            if (ch == 10) {
                break;
            }
            if (ch == 13) {
                continue;
            }
            sb.append((char) ch);
        }
        String input = sb.toString();
        return input;
    }

    public void write(String string) throws IOException {
        outputStream.write((string + ((char) (13)) + ((char) (10))).getBytes());
        outputStream.flush();
    }

    public void writeMail(byte[] fullcontent) throws IOException{
        outputStream.write(fullcontent);
        outputStream.flush();
    }

}
