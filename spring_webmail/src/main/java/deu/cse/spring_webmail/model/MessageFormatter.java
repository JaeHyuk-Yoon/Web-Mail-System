/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.spring_webmail.model;

import deu.cse.spring_webmail.entity.Inbox;
import deu.cse.spring_webmail.repository.InboxRepository;
import jakarta.mail.Message;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * @author skylo
 */
@Slf4j
@NoArgsConstructor        // 기본 생성자 생성
@AllArgsConstructor
//@RequiredArgsConstructor
@Service
public class MessageFormatter {
//    @NonNull private String userid;  // 파일 임시 저장 디렉토리 생성에 필요
    @Getter @Setter private String userid;  // 파일 임시 저장 디렉토리 생성에 필요
    private HttpServletRequest request = null;
    
    // 220612 LJM - added to implement REPLY
    @Getter private String sender;
    @Getter private String subject;
    @Getter private String body;
    List<Inbox> inboxList;
    
    @Autowired
    InboxRepository inboxRepository;

    public String getMessageTable(Message[] messages) {
        StringBuilder buffer = new StringBuilder();
        
        List<Inbox> inboxList = inboxRepository.findByRepositoryName(userid);
        
        log.error("atfer getDbMessages");
        
        // 메시지 제목 보여주기
        buffer.append("<table>");  // table start
        buffer.append("<tr> "
                + " <th> No. </td> "
                + " <th> 보낸 사람 </td>"
                + " <th> 제목 </td>     "
                + " <th> 보낸 날짜 </td>   "
                + " <th> 읽음 </td>   "
                + " <th> 삭제 </td>   "
                + " </tr>");

        for (int i = messages.length - 1; i >= 0; i--) {
            MessageParser parser = new MessageParser(messages[i], userid);
            parser.parse(false);  // envelope 정보만 필요
            
            parser.createShowCheck(inboxList);   //mine
            
            // 메시지 헤더 포맷
            // 추출한 정보를 출력 포맷 사용하여 스트링으로 만들기
            buffer.append("<tr> "
                    + " <td id=no>" + (i + 1) + " </td> "
                    + " <td id=sender>" + parser.getFromAddress() + "</td>"
                    + " <td id=subject> "
                    + " <a href=show_message?msgid=" + (i + 1) + " title=\"메일 보기\"> "
                    + parser.getSubject() + "</a> </td>"
                    + " <td id=date>" + parser.getSentDate() + "</td>"
                    + " <td id=check>"+ parser.getShowCheck() + "</td>"
                    + " <td id=delete>"
                    + "<a href=delete_mail.do"
                    + "?msgid=" + (i + 1) + "> 삭제 </a>" + "</td>"
                    + " </tr>");
        }
        buffer.append("</table>");

        return buffer.toString();
//        return "MessageFormatter 테이블 결과";
    }

    public String getMessage(Message message) {
        StringBuilder buffer = new StringBuilder();

        List<Inbox> inboxList = inboxRepository.findByRepositoryName(userid);
        
        // MessageParser parser = new MessageParser(message, userid);
        MessageParser parser = new MessageParser(message, userid, request);
        parser.parse(true);
        
        sender = parser.getFromAddress();
        subject = parser.getSubject();
        body = parser.getBody();
        
        log.error("before update show check method");
        Inbox updatedInbox = parser.updateShowCheck(inboxList);
        
        if(updatedInbox != null) inboxRepository.save(updatedInbox);
        
        buffer.append("보낸 사람: " + parser.getFromAddress() + " <br>");
        buffer.append("받은 사람: " + parser.getToAddress() + " <br>");
        buffer.append("Cc &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; : " + parser.getCcAddress() + " <br>");
        buffer.append("보낸 날짜: " + parser.getSentDate() + " <br>");
        buffer.append("제 &nbsp;&nbsp;&nbsp;  목: " + parser.getSubject() + " <br> <hr>");

        buffer.append(parser.getBody());

        String attachedFile = parser.getFileName();
        if (attachedFile != null) {
            buffer.append("<br> <hr> 첨부파일: <a href=download"
                    + "?userid=" + this.userid
                    + "&filename=" + attachedFile.replaceAll(" ", "%20")
                    + " target=_top> " + attachedFile + "</a> <br>");
        }

        return buffer.toString();
    }
    
//    public Inbox updateShowCheck(List<Inbox> inboxes) {
//        return parser.updateShowCheck(inboxList);   //mine
//    }
    
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }
}
