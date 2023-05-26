/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.spring_webmail.model;


import deu.cse.spring_webmail.entity.Inbox;
import deu.cse.spring_webmail.entity.InboxPK;
import deu.cse.spring_webmail.repository.InboxRepository;
import deu.cse.spring_webmail.repository.TestRepository;
import deu.cse.spring_webmail.repository.UsersRepository;
import jakarta.mail.FetchProfile;
import jakarta.mail.Flags;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Store;
//<<<<<<< HEAD
import java.sql.Blob;
//import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
//=======
import java.util.ArrayList;
//>>>>>>> 20ff4424b871dea203e61496e658794f0fa92470
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

/**
 *
 * @author skylo
 */
@Slf4j
@NoArgsConstructor        // 기본 생성자 생성
@AllArgsConstructor
@Service
public class Pop3Agent {
    @Getter @Setter private String host;
    @Getter @Setter private String userid;
    @Getter @Setter private String password;
    @Getter @Setter private Store store;
    @Getter @Setter private String excveptionType;
    @Getter @Setter private HttpServletRequest request;
    //private HttpSession session = request.getSession();
    
    // 220612 LJM - added to implement REPLY
    @Getter private String sender;
    @Getter private String subject;
    @Getter private String body;
    
//    @Autowired
//    InboxRepository inboxRepository;
    
//    @Autowired
//    UsersRepository usersRepository;
    
    @Autowired
    MessageFormatter formatter;
    
    
    
    public Pop3Agent(String host, String userid, String password) {
        this.host = host;
        this.userid = userid;
        this.password = password;
    }
    
    public boolean validate() {
        boolean status = false;

        try {
            status = connectToStore();
            store.close();
        } catch (Exception ex) {
            log.error("Pop3Agent.validate() error : " + ex);
            status = false;  // for clarity
        }
        return status;
    }

    public boolean deleteMessage(int msgid, boolean really_delete) {
        boolean status = false;

        if (!connectToStore()) {
            return status;
        }

        try {
            // Folder 설정
//          
            Folder folder = store.getFolder("INBOX");
            folder.open(Folder.READ_WRITE);

            // Message에 DELETED flag 설정
            Message msg = folder.getMessage(msgid);
            msg.setFlag(Flags.Flag.DELETED, really_delete);

            // 폴더에서 메시지 삭제
            //
            // <-- 현재 지원 안 되고 있음. 폴더를 close()할 때 expunge해야 함.
            folder.close(true);  // expunge == true
            store.close();
            status = true;
        } catch (Exception ex) {
            log.error("deleteMessage() error: {}", ex.getMessage());
        }
        return status;
    }

    /*
     * 페이지 단위로 메일 목록을 보여주어야 함.
     */
    public String getMessageList(int currentpage) {
        String result = "";
        Message[] messages = null;
//        List<Message> messageList = new ArrayList<Message>();
        
        if (!connectToStore()) {  // 3.1
            log.error("POP3 connection failed!");
            return "POP3 연결이 되지 않아 메일 목록을 볼 수 없습니다.";
        }

        try {
            // 메일 폴더 열기
            Folder folder = store.getFolder("INBOX");  // 3.2
            folder.open(Folder.READ_ONLY);  // 3.3
            
//            List<Inbox> inboxList = inboxRepository.findByRepositoryName(userid);

            // 현재 수신한 메시지 모두 가져오기
            messages = folder.getMessages();      // 3.4

            FetchProfile fp = new FetchProfile();
            // From, To, Cc, Bcc, ReplyTo, Subject & Date
            fp.add(FetchProfile.Item.ENVELOPE);
            folder.fetch(messages, fp);
//<<<<<<< HEAD
//            MessageFormatter formatter = new MessageFormatter(userid);  //3.5
            formatter.setUserid(userid);
//            result = formatter.getMessageTable(messages);   // 3.6

//=======

//            MessageFormatter formatter = new MessageFormatter(userid);  //3.5
            Pagination paging = new Pagination();
            paging.setTotalmail(messages.length);
            paging.setCurrentpage(currentpage);
            int startmail = (currentpage - 1) * paging.getPostmail();
            int endmail = currentpage * paging.getPostmail();
            if(messages.length < paging.getPostmail()){
                result = formatter.getMessageTable(messages, startmail, messages.length); 
            } else{
                result = formatter.getMessageTable(messages, startmail, endmail);   // 3.6
            }
            result = result + paging.paginationAll();
            
//>>>>>>> 20ff4424b871dea203e61496e658794f0fa92470
            folder.close(true);  // 3.7
            store.close();       // 3.8
        } catch (Exception ex) {
            log.error("Pop3Agent.getMessageList() : exception = {}", ex.getMessage());
            result = "Pop3Agent.getMessageList() : exception = " + ex.getMessage();
        }
        return result;
    }

    public String getMessageFromMeList(int currentpage) {
        String result = "";
        Message[] messages = null;

        if (!connectToStore()) {  // 3.1
            log.error("POP3 connection failed!");
            return "POP3 연결이 되지 않아 메일 목록을 볼 수 없습니다.";
        }

        
        try {
            // 메일 폴더 열기
            Folder folder = store.getFolder("INBOX");  // 3.2
            folder.open(Folder.READ_ONLY);  // 3.3

            // 현재 수신한 메시지 모두 가져오기
            messages = folder.getMessages();      // 3.4
            FetchProfile fp = new FetchProfile();
            // From, To, Cc, Bcc, ReplyTo, Subject & Date
            fp.add(FetchProfile.Item.ENVELOPE);
            folder.fetch(messages, fp);

//            MessageFormatter formatter = new MessageFormatter(userid);  //3.5
            
            log.error("before set");
            formatter.setUserid(userid);
            
            log.error("before for");
            
            ArrayList<Message> m = new ArrayList<Message>();
            for(int i = 0; i < messages.length; i++){
                if(messages[i].getFrom()[0].toString().equals(userid)){
                    m.add(messages[i]);
                }
            }
            
            log.error("after for");
            
            Message[] messagelist = new Message[m.size()];
            m.toArray(messagelist);
            m.toArray();
            
            Pagination paging = new Pagination();
            paging.setTotalmail(messagelist.length);
            paging.setCurrentpage(currentpage);
            int startmail = (currentpage - 1) * paging.getPostmail();
            int endmail = currentpage * paging.getPostmail();
            if(messagelist.length < paging.getPostmail()){
                log.error("enter true if");
                result = formatter.getMessageTable(messagelist, startmail, m.size()); 
            } else{
                log.error("enter else");
                result = formatter.getMessageTable(messagelist, startmail, endmail);   // 3.6
            }
            result = result + paging.paginationFromme();
            
            folder.close(true);  // 3.7
            store.close();       // 3.8
        } catch (Exception ex) {
            log.error("Pop3Agent.getMessageFromMeList() : exception = {}", ex.getMessage());
            result = "Pop3Agent.getMessageFromMeList() : exception = " + ex.getMessage();
        }
        return result;
    }
    
    public String getMessage(int n) {
        String result = "POP3  서버 연결이 되지 않아 메시지를 볼 수 없습니다.";

        if (!connectToStore()) {
            log.error("POP3 connection failed!");
            return result;
        }

        try {
            Folder folder = store.getFolder("INBOX");
            folder.open(Folder.READ_ONLY);
            
//            List<Inbox> inboxList = inboxRepository.findByRepositoryName(userid);
            
            Message message = folder.getMessage(n);

//            MessageFormatter formatter = new MessageFormatter(userid);
            formatter.setUserid(userid);
            formatter.setRequest(request);  // 210308 LJM - added
            result = formatter.getMessage(message);
            sender = formatter.getSender();  // 220612 LJM - added
            subject = formatter.getSubject();
            body = formatter.getBody();
            
//            Inbox updatedInbox = formatter.updateShowCheck(inboxList);

            folder.close(true);
            store.close();
        } catch (Exception ex) {
            log.error("Pop3Agent.getMessage() : exception = {}", ex);
            result = "Pop3Agent.getMessage() : exception = " + ex;
        }
        return result;
    }
    
    private boolean connectToStore() {
        boolean status = false;
        Properties props = System.getProperties();
        // https://jakarta.ee/specifications/mail/2.1/apidocs/jakarta.mail/jakarta/mail/package-summary.html
        props.setProperty("mail.pop3.host", host);
        props.setProperty("mail.pop3.user", userid);
        props.setProperty("mail.pop3.apop.enable", "false");
        props.setProperty("mail.pop3.disablecapa", "true");  // 200102 LJM - added cf. https://javaee.github.io/javamail/docs/api/com/sun/mail/pop3/package-summary.html
        props.setProperty("mail.debug", "false");
        props.setProperty("mail.pop3.debug", "false");

        Session session = Session.getInstance(props);
        session.setDebug(false);

        try {
            store = session.getStore("pop3");
            store.connect(host, userid, password);
            status = true;
        } catch (Exception ex) {
            log.error("connectToStore 예외: {}", ex.getMessage());
        }
        return status;
    }
}
