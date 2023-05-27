/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.spring_webmail.model;

import deu.cse.spring_webmail.entity.Inbox;
import deu.cse.spring_webmail.entity.Trashbox;
import deu.cse.spring_webmail.repository.InboxRepository;
import deu.cse.spring_webmail.repository.TrashboxRepository;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
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
public class TrashModel {

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
    TrashboxRepository trashboxRepository;
    
    @Autowired
    MessageFormatter formatter;
    
    

   
    // 삭제 메일 inbox 메시지 trashbox로 이동
    public boolean moveToTrashbox(int n) {

        boolean status = false;

        List<Inbox> inboxList = inboxRepository.findByRepositoryName(userid);


        try {
            for (Inbox inbox : inboxList) {
                if (inbox.getMessageName().equals(inboxList.get(n - 1).getMessageName())) {
                    // log.error("inboxRepository = {}", inbox.toString());

                    // inbox메일 정보 trashbox에 저장
                    Trashbox trashBox = new Trashbox();
                    trashBox.setMessageName(inbox.getMessageName());
                    trashBox.setRepositoryName(inbox.getRepositoryName());
                    trashBox.setMessageState(inbox.getMessageState());
                    trashBox.setErrorMessage(inbox.getErrorMessage());
                    trashBox.setSender(inbox.getSender());
                    trashBox.setRecipients(inbox.getRecipients());
                    trashBox.setRemoteHost(inbox.getRemoteHost());
                    trashBox.setRemoteAddr(inbox.getRemoteAddr());
                    trashBox.setMessageBody(inbox.getMessageBody());
                    trashBox.setMessageAttributes(inbox.getMessageAttributes());
                    trashBox.setLastUpdated(inbox.getLastUpdated());

                    // TrashBox에 저장
                    trashboxRepository.save(trashBox);
                   
                    status = true;
                }
            }

        } catch (Exception ex) {
            log.error("TrashModel.moveToTrashbox() : exception = {}", ex.getMessage());
        }
       
        return status;
    }

   
    // 휴지통 메일 리스트 가져오는 함수
    public String getTrashMessageList() {

        Message[] messages = null;
       
        String result = "";
        String recipients = String.format("%s@localhost", userid);
       
        try {
            List<Trashbox> trashInboxList = trashboxRepository.findByRecipients(recipients);
            messages = new Message[trashInboxList.size()];

            for (int i = 0; i < trashInboxList.size(); i++) {
                Trashbox trashbox = trashInboxList.get(i);
                Session session = Session.getDefaultInstance(new Properties());
                messages[i] = new MimeMessage(session, trashbox.getMessageBody().getBinaryStream());  
            }

//            MessageFormatter formatter = new MessageFormatter(userid);
            formatter.setUserid(userid);
            formatter.setRequest(request);
            result = formatter.getTrashMessageTable(messages);
           
        } catch (Exception ex) {
            log.error("TrashModel.getTrashMessageList() : exception = {}", ex.getMessage());
            result = "TrashModel.getTrashMessageList() : exception = " + ex.getMessage();
        } finally {
            return result;
        }
    }

   
    // 휴지통에 해당 내용 가져오는 함수
    public String getTrashMessage(int n) {
        String result = "";
        String recipients = String.format("%s@localhost", userid);

        try {
            List<Trashbox> trashInboxList = trashboxRepository.findByRecipients(recipients);
            if (trashInboxList == null || trashInboxList.isEmpty()) {
                return result;
            }

            Trashbox trashbox = trashInboxList.get(n - 1);
            Session session = Session.getDefaultInstance(new Properties());
            MimeMessage message = new MimeMessage(session, trashbox.getMessageBody().getBinaryStream());
 
//            InternetAddress[] toAddresses = (InternetAddress[]) message.getRecipients(Message.RecipientType.TO);
//            InternetAddress[] ccAddresses = (InternetAddress[]) message.getRecipients(Message.RecipientType.CC);

//            MessageFormatter formatter = new MessageFormatter(userid);
            formatter.setUserid(userid);
            formatter.setRequest(request);
            result = formatter.getMessageContent(message);

            MessageParser parser = new MessageParser(message, userid, request);
            parser.parse(true);
        } catch (Exception ex) {
            log.error("SentModel.getTrashMessage() : exception = {}", ex.getMessage());
            result = "SentModel.getTrashMessage() : exception = " + ex.getMessage();
        } finally {
            return result;
        }
    }

   
    // 메일 복구
    @Transactional
    public boolean restoreMessage(int n) {
        boolean status = false;

        String recipients = String.format("%s@localhost", userid);
        String restoreMail = "";
        try {
            List<Trashbox> trashboxList = trashboxRepository.findByRecipients(recipients);

            for (int i = 0; i < trashboxList.size(); i++) {
                Trashbox trashbox = trashboxList.get(n - 1);
                restoreMail = trashbox.getMessageName();
            }

            // inbox로 메일 복구
            for (Trashbox trashbox : trashboxList) {
                if (trashbox.getMessageName().equals(trashboxList.get(n - 1).getMessageName())) {
                   // log.error("trashboxRepository = {}", trashbox.toString());

                    // trashbox 메일 정보 inbox에 저장
                    Inbox inbox = new Inbox();
                    inbox.setMessageName(trashbox.getMessageName());
                    inbox.setRepositoryName(trashbox.getRepositoryName());
                    inbox.setMessageState(trashbox.getMessageState());
                    inbox.setErrorMessage(trashbox.getErrorMessage());
                    inbox.setSender(trashbox.getSender());
                    inbox.setRecipients(trashbox.getRecipients());
                    inbox.setRemoteHost(trashbox.getRemoteHost());
                    inbox.setRemoteAddr(trashbox.getRemoteAddr());
                    inbox.setMessageBody(trashbox.getMessageBody());
                    inbox.setMessageAttributes(trashbox.getMessageAttributes());
                    inbox.setLastUpdated(trashbox.getLastUpdated());

                    // Inbox 저장
                    inboxRepository.save(inbox);
                }
            }

            // 휴지통(trashbox)에서 메일 삭제
            trashboxRepository.deleteByMessageName(restoreMail);

            status = true;
        } catch (Exception ex) {
            log.error("restoreMessage() error: {}", ex.getMessage());
        } finally {
            return status;
        }
    }

   
    // 메일 영구 삭제
    @Transactional
    public boolean permanentlyDelete(int n) {
        boolean status = false;

        String recipients = String.format("%s@localhost", userid);
        String deleteMail = "";
        try {
            List<Trashbox> trashInboxList = trashboxRepository.findByRecipients(recipients);

            for (int i = 0; i < trashInboxList.size(); i++) {
                Trashbox trashbox = trashInboxList.get(n - 1);
                deleteMail = trashbox.getMessageName();
            }

            // trashbox에서 해당 메일 삭제
            trashboxRepository.deleteByMessageName(deleteMail);

            status = true;
        } catch (Exception ex) {
            log.error("permanentlyDelete() error: {}", ex.getMessage());
        } finally {
            return status;
        }
    }

}