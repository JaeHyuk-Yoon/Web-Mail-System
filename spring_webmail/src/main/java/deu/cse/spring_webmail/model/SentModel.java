/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.spring_webmail.model;

import deu.cse.spring_webmail.entity.Inbox;
import deu.cse.spring_webmail.repository.InboxRepository;
import deu.cse.spring_webmail.repository.UsersRepository;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author pcb
 */
@Slf4j
@NoArgsConstructor        // 기본 생성자 생성
@AllArgsConstructor
@Service
public class SentModel {

    @Getter
    @Setter
    private String host;
    @Getter
    @Setter
    private String userid;
    @Getter
    @Setter
    private String password;
    @Getter
    @Setter
    private Store store;
    @Getter
    @Setter
    private String excveptionType;
    @Getter
    @Setter
    private HttpServletRequest request;

    // 220612 LJM - added to implement REPLY
    @Getter
    private String sender;
    @Getter
    private String subject;
    @Getter
    private String body;
    @Getter
    private String SentDate;

    @Autowired
    InboxRepository inboxRepository;
    
    @Autowired
    MessageFormatter formatter;

    @Autowired
    UsersRepository usersRepository;


   
   // 보낸 메일 리스트 가져오는 함수
    public String getSentMessageList() {

        Message[] messages = null;
        String result = "";
        String sender = String.format("%s@localhost", userid);
        try {
            List<Inbox> sentInboxList = inboxRepository.findBySender(sender);
            messages = new Message[sentInboxList.size()];

            for (int i = 0; i < sentInboxList.size(); i++) {
                Inbox testInbox = sentInboxList.get(i);
                Session session = Session.getDefaultInstance(new Properties());
                messages[i] = new MimeMessage(session, testInbox.getMessageBody().getBinaryStream());  // 배열에 추가
            }

//            MessageFormatter formatter = new MessageFormatter(sender);
            formatter.setSender(sender);
            formatter.setRequest(request);
            result = formatter.getSentMessageTable(messages);
        } catch (Exception ex) {
            log.error("SentModel.getSentMessageList() : exception = {}", ex.getMessage());
            result = "SentModel.getSentMessageList() : exception = " + ex.getMessage();
        } finally {
            return result;
        }

    }

    
    // 보낸 메일 해당 내용 가져오는 함수
    public String getSentMessage(int n) {
        String result = "";
        String sender = String.format("%s@localhost", userid);
     

        try {
            List<Inbox> sentInboxList = inboxRepository.findBySender(sender);
            if (sentInboxList == null || sentInboxList.isEmpty()) {
                return result;
            }

            Inbox sentInbox = sentInboxList.get(n - 1);
            Session session = Session.getDefaultInstance(new Properties());
            MimeMessage message = new MimeMessage(session, sentInbox.getMessageBody().getBinaryStream());

            InternetAddress[] toAddresses = (InternetAddress[]) message.getRecipients(Message.RecipientType.TO);
            InternetAddress[] ccAddresses = (InternetAddress[]) message.getRecipients(Message.RecipientType.CC);

//            MessageFormatter formatter = new MessageFormatter(sender);
            formatter.setSender(sender);
            formatter.setRequest(request);
            result = formatter.getMessageContent(message);

            MessageParser parser = new MessageParser(message, userid, request);
            parser.parse(true);
        } catch (Exception ex) {
            log.error("SentModel.getSentMessage() : exception = {}", ex.getMessage());
            result = "SentModel.getSentMessage() : exception = " + ex.getMessage();
        } finally {
            return result;
        }
    }
}
