/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package deu.cse.spring_webmail.model;

import com.sun.mail.smtp.SMTPMessage;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.internet.MimeUtility;
import java.io.File;
import java.util.Properties;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author jongmin
 */
@Slf4j
public class SmtpAgent {

    @Getter @Setter protected String host = null;
    @Getter @Setter protected String userid = null;
    @Getter @Setter protected String to = null;
    @Getter @Setter protected String cc = null;
    @Getter @Setter protected String subj = null;
    @Getter @Setter protected String body = null;
    @Getter @Setter protected String file1 = null;

    public SmtpAgent(String host, String userid) {
        this.host = host;
        this.userid = userid;
    }

    // LJM 100418 -  현재 로그인한 사용자의 이메일 주소를 반영하도록 수정되어야 함. - test only
    // LJM 100419 - 일반 웹 서버와의 SMTP 동작시 setFrom() 함수 사용 필요함.
    //              없을 경우 메일 전송이 송신주소가 없어서 걸러짐.
    public boolean sendMessage() {
        boolean status = false;

        // 1. property 설정
        Properties props = System.getProperties();
        props.put("mail.debug", false);
        props.put("mail.smtp.host", this.host);
        log.debug("SMTP host : {}", props.get("mail.smtp.host"));

        // 2. session 가져오기
        Session session = Session.getDefaultInstance(props, null);
        session.setDebug(false);

        try {
            SMTPMessage msg = new SMTPMessage(session);

            // msg.setFrom(new InternetAddress(this.userid + "@" + this.host));
            msg.setFrom(new InternetAddress(this.userid));  // 200102 LJM - 테스트 목적으로 수정
            //msg.setFrom(new InternetAddress("jongmin@deu.ac.kr"));


            // setRecipient() can be called repeatedly if ';' or ',' exists
            if (this.to.indexOf(';') != -1) {
                this.to = this.to.replaceAll(";", ",");
            }
            msg.setRecipients(Message.RecipientType.TO, this.to);  // 200102 LJM - 수정

            if (this.cc.length() > 1) {
                if (this.cc.indexOf(';') != -1) {
                    this.cc = this.cc.replaceAll(";", ",");
                }
                msg.setRecipients(Message.RecipientType.CC, this.cc);
            }

            //msg.setSubject(s);
//            msg.setSubject(MimeUtility.encodeText(this.subj, "euc-kr", "B"));
            msg.setSubject(this.subj);

            //msg.setHeader("Content-Type", "text/plain; charset=utf-8");
            msg.setHeader("User-Agent", "LJM-WM/0.1");
            //msg.setHeader("Content-Transfer-Encoding", "8bit");
            //msg.setAllow8bitMIME(true);

            // body
            MimeBodyPart mbp = new MimeBodyPart();
            // Content-Type, Content-Transfer-Encoding 설정 의미 없음.
            // 자동으로 설정되는 것 같음. - LJM 041202
            // mbp.setHeader("Content-Type", "text/plain; charset=euc-kr");
            // mbp.setHeader("Content-Transfer-Encoding", "8bit");
            mbp.setText(this.body);

            Multipart mp = new MimeMultipart();
            mp.addBodyPart(mbp);

            // 첨부 파일 추가
            if (this.file1 != null) {
                MimeBodyPart a1 = new MimeBodyPart();
                DataSource src = new FileDataSource(this.file1);
                a1.setDataHandler(new DataHandler(src));
                // 22011 LJM: 윈도우즈/우분투에 따라서 달라져야 함
                int index = this.file1.lastIndexOf(File.separator);
                String fileName = this.file1.substring(index + 1);
                // "B": base64, "Q": quoted-printable
                a1.setFileName(MimeUtility.encodeText(fileName, "UTF-8", "B"));
                mp.addBodyPart(a1);
            }
            msg.setContent(mp);

            // 메일 전송
            Transport.send(msg);

            // 메일 전송 완료되었으므로 서버에 저장된
            // 첨부 파일 삭제함
            if (this.file1 != null) {
                File f = new File(this.file1);
                if (!f.delete()) {
                    log.error(this.file1 + ": 파일 삭제가 제대로 안 됨.");
                }
            }
            status = true;
        } catch (Exception ex) {
            log.error("sendMessage() error: {}", ex);
        } finally {
            return status;
        }
    }  // sendMessage()
}
