/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deu.cse.spring_webmail.model;

import com.sun.mail.smtp.SMTPMessage;
import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Part;
import jakarta.mail.internet.MimeMultipart;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author pcb
 */
@Slf4j
@NoArgsConstructor        // 기본 생성자 생성
public class SentMailModel {
    
    @Getter @Setter private String userid;
    @Getter @Setter private Integer id;
    @Getter @Setter private String sender;
    @Getter @Setter private String recipients;
    @Getter @Setter private String cc;
    @Getter @Setter private String subject;
    @Getter @Setter private String body;
    @Getter @Setter private String attachments;
    @Getter @Setter private String sentTime;
    
    public SentMailModel(String userid) {
        this.userid = userid;
    }
    
    // 디비의 sent 테이블에 이메일 정보 저장
    public void saveSentMessage(SMTPMessage msg) {
        try {
            // 보낸 메일 정보 추출
            String sender = msg.getFrom()[0].toString();
            String recipients = msg.getRecipients(Message.RecipientType.TO)[0].toString();
            String cc = msg.getRecipients(Message.RecipientType.CC) != null 
                    ? msg.getRecipients(Message.RecipientType.CC)[0].toString() : "";
            String subject = msg.getSubject();
            String body = getTextFromMessage(msg);
            
            // body 텍스트를 '\n'를 기준으로 나눠서 각 줄마다 <br> 태그를 추가 
            String[] lines = body.split("\n");
            String bodyReplaced = "";
            
            for (int i = 0; i < lines.length; i++) {  
                if (lines[i].isEmpty()) {  // 개행문자 앞에 빈 문자열이 있을 경우 <br> 추가x
                    bodyReplaced += lines[i];
                } else {
                    if (i == lines.length - 1) {  // 마지막 줄일 경우 개행문자를 추가x
                        bodyReplaced += lines[i]; 
                    } else {  // 개행문자 앞에 <br> 추가
                        bodyReplaced += lines[i] + "<br>\n"; 
                    }
                }
            }
            
            // 첨부 파일 정보 추출
            List<String> attachments = new ArrayList<>();
            Multipart multipart = (Multipart) msg.getContent();
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) {
                    // 첨부 파일이면 파일 이름을 리스트에 추가
                    String filename = bodyPart.getFileName();
                    attachments.add(filename);
                }
            }

            // sent 테이블에 보낸 메일 정보 저장
            Connection conn = null;
            PreparedStatement pstmt = null;
            try {
                conn = getConnection();  // DB 연결
                // sent테이블에 sender, recipients, cc, subject, body, attachments 값 저장
                String sql = "INSERT INTO sent (sender, recipients, cc, subject, body, attachments) VALUES (?, ?, ?, ?, ?, ?)";
                pstmt = conn.prepareStatement(sql);

                pstmt.setString(1, sender);
                pstmt.setString(2, recipients);
                pstmt.setString(3, cc);
                pstmt.setString(4, subject);
                pstmt.setString(5, bodyReplaced);
                pstmt.setString(6, String.join(",", attachments));
                
                pstmt.executeUpdate();
            } catch (SQLException ex) {
                log.error("saveSentMessage() error: {}", ex);
            } finally {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            }
        } catch (Exception ex) {
            log.error("saveSentMessage() error: {}", ex);
        }
    }
    
    // body 텍스트 추출
    private String getTextFromMessage(Message message) throws MessagingException, IOException {
        String result = "";
        if (message.isMimeType("text/plain")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        }
        return result;
    }

    // 파일 내부의 텍스트 추출
    private String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws MessagingException, IOException {
        StringBuilder result = new StringBuilder();
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result.append("\n").append(bodyPart.getContent());
                break; 
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                result.append("\n").append(org.jsoup.Jsoup.parse(html).text());
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                result.append(getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent()));
            }
        }
        return result.toString();
    }
    
    
    public static Connection getConnection() throws SQLException {
        Connection conn = null;
        try {
            String url = "jdbc:mysql://localhost:3306/webmail";
            String user = "root";
            String password = "1234";
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException ex) {
            throw new SQLException("Failed to get database connection.", ex);
        }
        return conn;
    }

    // 보낸 메일 목록 생성
    public String getSentMessageList() throws SQLException {
        String result = "";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int cnt = 0;

        try {
            // DB 연결
            conn = getConnection();

            // sent 테이블의 id 카운트 
            String cSql = "SELECT count(id) FROM sent WHERE sender = ? ORDER BY sentTime DESC";
            pstmt = conn.prepareStatement(cSql);
            pstmt.setString(1, userid);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                cnt = rs.getInt("count(id)");
            }

            // sent 테이블에서 본인이 보낸 이메일 정보 가져와서 <table> 태그 생성
            String sql = "SELECT * FROM sent WHERE sender = ? ORDER BY sentTime DESC";
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, userid);
            rs = pstmt.executeQuery();

            // 메일 목록 HTML 생성
            StringBuilder buffer = new StringBuilder();
            buffer.append("<table border='1'>");
            buffer.append("<tr>");
            buffer.append("<th>No.</th>");
            buffer.append("<th>받은 사람</th>");
            buffer.append("<th>제목</th>");
            buffer.append("<th>보낸 날짜</th>");
            buffer.append("</tr>");

            while (rs.next()) {
                id = rs.getInt("id");
                sender = rs.getString("sender");
                recipients = rs.getString("recipients");
                cc = rs.getString("cc");
                subject = rs.getString("subject");
                body = rs.getString("body");
                attachments = rs.getString("attachments");
                sentTime = rs.getString("sentTime");

                buffer.append("<tr> "
                        + " <td id=no>" + cnt + " </td> "
                        + " <td id=recipients>" + recipients + "</td>"
                        + " <td id=subject> "
                        + " <a href=show_sentmessage?msgid=" + (id) + " title=\"메일 보기\"> "
                        + subject + "</a> </td>"
                        + " <td id=date>" + sentTime + "</td>"
                        + " </tr>");
                // cnt--;
            }

            buffer.append("</table>");
            result = buffer.toString();

        } catch (SQLException ex) {
            log.error("getSentMessageList() error: {}", ex);
            result = "Database error : Error occurred while retrieving sent mail";
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
            if (conn != null) {
                conn.close();
            }
            if (rs != null) {
                rs.close();
            }
            return result;
        }
    }
    
    // 메일 정보 출력
    public String getSentMessage(int msgid) throws SQLException, IOException {
        StringBuilder buffer = new StringBuilder();

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // DB 연결
            conn = getConnection();

            // sent 테이블에서 해당하는 이메일 정보 가져와서 출력
            String sql = "SELECT * FROM sent WHERE id = ? AND sender = ? ORDER BY sentTime DESC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, msgid);
            pstmt.setString(2, userid);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                id = rs.getInt("id");
                sender = rs.getString("sender");
                recipients = rs.getString("recipients");
                cc = rs.getString("cc");
                subject = rs.getString("subject");
                body = rs.getString("body");
                attachments = rs.getString("attachments");
                sentTime = rs.getString("sentTime");
            }

            buffer.append("보낸 사람: " + sender + " <br>");
            buffer.append("받은 사람: " + recipients + " <br>");
            buffer.append("Cc &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; : " + cc + " <br>");
            buffer.append("보낸 날짜: " + sentTime + " <br>");
            buffer.append("제 &nbsp;&nbsp;&nbsp;  목: " + subject + " <br> <hr>");
            buffer.append(body);

            String attachedFile = attachments;
            if (attachedFile != null) {
                buffer.append("<br> <hr> 첨부파일: <a href=sentdownload"
                        + "?recipients=" + this.recipients
                        + "&filename=" + attachedFile.replaceAll(" ", "%20")
                        + " target=_top> " + attachedFile + "</a> <br>");
            }

        } catch (SQLException ex) {
            log.error("getSentMessage() error: {}", ex);
            return "Database error : Error occurred while retrieving sent mail";
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
            if (conn != null) {
                conn.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return buffer.toString();
    }
    
}
