/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deu.cse.spring_webmail.control;

import deu.cse.spring_webmail.model.SentMailModel;
import jakarta.mail.internet.MimeUtility;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author pcb
 */

@Controller
@PropertySource("classpath:/system.properties")
@Slf4j
public class SentController {
    
    @Autowired
    private ServletContext ctx;
    @Autowired
    private HttpSession session;
    @Autowired
    private HttpServletRequest request;
    @Value("${file.download_folder}")
    private String DOWNLOAD_FOLDER;
    
    @GetMapping("/sent_mail")
    public String sentMail(Model model, HttpServletRequest request) throws SQLException {

        log.debug("sent_mail called...");

        String userid = (String) session.getAttribute("userid");
        SentMailModel sentmail = new SentMailModel();
        sentmail.setUserid(userid);

        String sentmessageList = sentmail.getSentMessageList();
        model.addAttribute("sentmessageList", sentmessageList);

        return "sent_mail/sent_mail";    
    }
    
    @GetMapping("/show_sentmessage")
    public String showSentMessage(@RequestParam Integer msgid, Model model) throws SQLException, IOException {
        log.debug("download_folder = {}", DOWNLOAD_FOLDER);
        log.debug("show_sentmessage called...");
        
        String userid = (String) session.getAttribute("userid");
        SentMailModel sentmail = new SentMailModel();
        sentmail.setUserid(userid);
        
        String msg = sentmail.getSentMessage(msgid);
        session.setAttribute("recipients", sentmail.getRecipients());
        session.setAttribute("subject", sentmail.getSubject());
        session.setAttribute("body", sentmail.getBody());                
        
        model.addAttribute("msg", msg);
        model.addAttribute("msgid", msgid);
        
        return "/sent_mail/show_sentmessage";
    }
    
    @GetMapping("/sentdownload")
    public ResponseEntity<Resource> sentdownload(@RequestParam("recipients") String recipients,
            @RequestParam("filename") String fileName) {
        log.debug("recipients = {}, filename = {}", recipients, fileName);
        try {
            log.debug("recipients = {}, filename = {}", recipients, MimeUtility.decodeText(fileName));
        } catch (UnsupportedEncodingException ex) {
            log.error("error");
        }
        
        // 1. 내려받기할 파일의 기본 경로 설정
        String basePath = ctx.getRealPath(DOWNLOAD_FOLDER) + File.separator + recipients;

        // 2. 파일의 Content-Type 찾기
        Path path = Paths.get(basePath + File.separator + fileName);
        String contentType = null;
        try {
            contentType = Files.probeContentType(path);
            log.debug("File: {}, Content-Type: {}", path.toString(), contentType);
        } catch (IOException e) {
            log.error("downloadDo: 오류 발생 - {}", e.getMessage());
        }

        // 3. Http 헤더 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(
                ContentDisposition.builder("attachment").filename(fileName, StandardCharsets.UTF_8).build());
        headers.add(HttpHeaders.CONTENT_TYPE, contentType);

        // 4. 파일을 입력 스트림으로 만들어 내려받기 준비
        Resource resource = null;
        try {
            resource = new InputStreamResource(Files.newInputStream(path));
        } catch (IOException e) {
            log.error("downloadDo: 오류 발생 - {}", e.getMessage());
        }
        if (resource == null) {
            return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
    }
    
}
