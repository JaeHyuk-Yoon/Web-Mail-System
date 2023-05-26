/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.spring_webmail.control;


import deu.cse.spring_webmail.model.Pop3Agent;
import deu.cse.spring_webmail.model.SentModel;
import java.sql.SQLException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
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
   
    @Autowired
    private Pop3Agent pop3;
    @Autowired
    private SentModel sentModel;
   
   
    @GetMapping("/sent_mail")
    public String sentMail(Model model) throws SQLException, Exception {
       
        log.debug("sent_mail called...");
               
        sentModel.setHost((String) session.getAttribute("host"));
        sentModel.setUserid((String) session.getAttribute("userid"));
        sentModel.setPassword((String) session.getAttribute("password"));    

        String sentmessageList = sentModel.getSentMessageList();
        model.addAttribute("sentmessageList", sentmessageList);
       
        return "sent_mail/sent_mail";
    }
   
    @GetMapping("/show_sentmessage")
    public String showSentMessage(@RequestParam Integer msgid, Model model) throws Exception {
        log.debug("download_folder = {}", DOWNLOAD_FOLDER);
        System.out.println("showSentMessage()");
        //Pop3Agent pop3 = new Pop3Agent();
     
        sentModel.setHost((String) session.getAttribute("host"));
        sentModel.setUserid((String) session.getAttribute("userid"));
        sentModel.setPassword((String) session.getAttribute("password"));
        sentModel.setRequest(request);
       
        String msg = sentModel.getSentMessage(msgid);
        session.setAttribute("sender", sentModel.getSender());  
        session.setAttribute("subject", sentModel.getSubject());
        session.setAttribute("body", sentModel.getBody());
        model.addAttribute("msg", msg);
     
       
        return "/sent_mail/show_sentmessage";
    }
   
}