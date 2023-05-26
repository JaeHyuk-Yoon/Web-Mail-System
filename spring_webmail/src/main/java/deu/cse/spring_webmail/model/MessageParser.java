/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.spring_webmail.model;

import deu.cse.spring_webmail.PropertyReader;
import deu.cse.spring_webmail.entity.Inbox;
import deu.cse.spring_webmail.repository.InboxRepository;
import jakarta.activation.DataHandler;
import jakarta.mail.Address;
import jakarta.mail.Header;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.Part;
import jakarta.mail.internet.MimeUtility;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author skylo
 */
@Slf4j
@RequiredArgsConstructor
public class MessageParser {
    @NonNull @Getter @Setter private Message message;
    @NonNull @Getter @Setter private String userid;
    @Getter @Setter private int messageNumber;
    @Getter @Setter private String toAddress;
    @Getter @Setter private String fromAddress;
    @Getter @Setter private String ccAddress;
    @Getter @Setter private String sentDate;
    @Getter @Setter private String subject;
    @Getter @Setter private String body;
    @Getter @Setter private String fileName;
    @Getter @Setter private String downloadTempDir = "C:/temp/download/";
    @Getter @Setter private String showCheck;
    @Getter @Setter private String messageId;
    private int show;
    
    @Autowired
    private InboxRepository inboxRepository;
    
    public MessageParser(Message message, String userid, HttpServletRequest request) {
        this(message, userid);
        PropertyReader props = new PropertyReader();
        String downloadPath = props.getProperty("file.download_folder");
        downloadTempDir = request.getServletContext().getRealPath(downloadPath);
        File f = new File(downloadTempDir);
        if (!f.exists()) {
            f.mkdir();
        }
    }
    
    public boolean parse(boolean parseBody) {
        boolean status = false;

        try {
            getEnvelope(message);
            //createShowCheck();
            if (parseBody) {
                getPart(message);
            }
            // 220611 LJM: 필요시 true로 하여 메시지 본문 볼 수 있도록 할 것.
            //
            //  예외가 발생하지 않았으므로 정상적으로 동작하였음.
            status = true;
        } catch (Exception ex) {
            log.error("MessageParser.parse() - Exception : {}", ex.getMessage());
            status = false;
        }
        return status;
    }

    private void getEnvelope(Message m) throws Exception {
        messageNumber = message.getMessageNumber();
        fromAddress = message.getFrom()[0].toString();  // 101122 LJM : replaces getMyFrom2()
        toAddress = getAddresses(message.getRecipients(Message.RecipientType.TO));
        Address[] addr = message.getRecipients(Message.RecipientType.CC);
        if (addr != null) {
            ccAddress = getAddresses(addr);
        } else {
            ccAddress = "";
        }
        subject = message.getSubject();
        sentDate = message.getSentDate().toString();
        sentDate = sentDate.substring(0, sentDate.length() - 8);  // 8 for "KST 20XX"
        
    }

    // ref: http://www.oracle.com/technetwork/java/faq-135477.html#readattach
    private void getPart(Part p) throws Exception {
        String disp = p.getDisposition();

        if (disp != null && (disp.equalsIgnoreCase(Part.ATTACHMENT)
                || disp.equalsIgnoreCase(Part.INLINE))) {  // 첨부 파일
//          
            fileName = MimeUtility.decodeText(p.getFileName());
//          
            if (fileName != null) {
                // 첨부 파일을 서버의 내려받기 임시 저장소에 저장
                String tempUserDir = this.downloadTempDir + File.separator + this.userid;
                File dir = new File(tempUserDir);
                if (!dir.exists()) {  // tempUserDir 생성
                    dir.mkdir();
                }

                String filename = MimeUtility.decodeText(p.getFileName());
                // 파일명에 " "가 있을 경우 서블릿에 파라미터로 전달시 문제 발생함.
                // " "를 모두 "_"로 대체함.
                DataHandler dh = p.getDataHandler();
                FileOutputStream fos = new FileOutputStream(tempUserDir + File.separator + filename);
                dh.writeTo(fos);
                fos.flush();
                fos.close();
            }
        } else {  // 메일 본문
            if (p.isMimeType("text/*")) {
                body = (String) p.getContent();
                if (p.isMimeType("text/plain")) {
                    body = body.replace("\r\n", " <br>");
                }
            } else if (p.isMimeType("multipart/alternative")) {
                // html text보다  plain text 선호
                Multipart mp = (Multipart) p.getContent();
                for (int i = 0; i < mp.getCount(); i++) {
                    Part bp = mp.getBodyPart(i);
                    if (bp.isMimeType("text/plain")) {  // "text/html"도 있을 것임.
                        getPart(bp);
                    }
                }
            } else if (p.isMimeType("multipart/*")) {
                Multipart mp = (Multipart) p.getContent();
                for (int i = 0; i < mp.getCount(); i++) {
                    getPart(mp.getBodyPart(i));
                }
            }
        }
    }

    private void printMessage(boolean printBody) {
        System.out.println("From: " + fromAddress);
        System.out.println("To: " + toAddress);
        System.out.println("CC: " + ccAddress);
        System.out.println("Date: " + sentDate);
        System.out.println("Subject: " + subject);

        if (printBody) {
            System.out.println("본 문");
            System.out.println("---------------------------------");
            System.out.println(body);
            System.out.println("---------------------------------");
            System.out.println("첨부파일: " + fileName);
        }
    }

    private String getAddresses(Address[] addresses) {
        StringBuilder buffer = new StringBuilder();

        for (Address address : addresses) {
            buffer.append(address.toString());
            buffer.append(", ");
        } // 마지막에 있는 ", " 삭제
        int start = buffer.length() - 2;
        int end = buffer.length() - 1;
        buffer.delete(start, end);
        return buffer.toString();
    }
    
    public List<Inbox> getMyMail() {
        return inboxRepository.findByRepositoryName(userid);
    }
    
    //일단 DB에서 내가 받은 메일 다 빼와서 코드를 통해 MessageBody sentDate, Sender 비교하고 같은거 끼리 showcheck값 부여
    public void createShowCheck(List<Inbox> dbMessages) {

        Inbox inbox = compareMessageBody(dbMessages);
        
        show = inbox.getShowCheck();
        
        if (show == 1) {
            showCheck = "읽음";
        }
        else {
            showCheck = "안읽음";
        }
    }
    
    public Inbox updateShowCheck(List<Inbox> dbMessages) {
        
        Inbox inbox = compareMessageBody(dbMessages);
        
        show = inbox.getShowCheck();
        log.error("after compare");
        
        if (show == 0) {
            //update
            inbox.setShowCheck(1);
            return inbox;
        }
        return null;
    }
    
    //"inbox" DB에서 내가받은 메일들 가져오고 message_body 
    // 현재 내가 받은 메일들 중에서 message_body가 단일 message객체.toString이랑 같은지 확인
    private Inbox compareMessageBody(List<Inbox> dbMessages) {
        
        try {
            for(Inbox dbMessage : dbMessages) {
                Blob blob = dbMessage.getMessageBody();
                byte[] bdata = blob.getBytes(1, (int)blob.length());
                String sMessageBody = new String(bdata);
                
                if( fromAddress.equals(parseSender(sMessageBody)) && message.getSentDate().toString().equals(parseSentDate(sMessageBody)) ) {
                    return dbMessage;
                }
            }
        } catch (Exception ex) {
            log.error("MessageParser.compareMessageBody() - Exception : {}", ex.getMessage());
        }
        return null;
    }
    
    private String parseSender(String body) {
        String[] arrays = body.split("\\n");
        
        for(String array : arrays) {
            String[] lineArray = array.split(":");
            if (lineArray[0].equals("From")) {
                return lineArray[1].substring(1, lineArray[1].length()-1);
            }
        }
        return "";
    }
    
    private String parseSentDate(String body) {
        String[] arrays = body.split("\\n");
        
        for(String array : arrays) {
            String[] lineArray = array.split(":");
            
            if (lineArray[0].equals("Date")) {
               DateFormat dateFormate = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
               
               SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
               
               try {
                   Date date = dateFormate.parse(array.substring(6));
                   String afterDate = sdf.format(date);
                   
                   return afterDate;
                   
               } catch (Exception ex) {
                   log.error("MessageParser.parseSentDate() - Exception : {}", ex.getMessage());
               }
            }
        }
        return "";
    }
}
