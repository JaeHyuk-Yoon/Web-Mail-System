/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.spring_webmail.control;

import deu.cse.spring_webmail.model.Pop3Agent;
import deu.cse.spring_webmail.model.TrashModel;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author pcb
 */
@Controller
@PropertySource("classpath:/system.properties")
@Slf4j
public class TrashController {
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
    private TrashModel trashModel;
   
   
    @GetMapping("/trash_mail")
    public String trashMail(Model model) throws SQLException, Exception {
       
        log.debug("trash_mail called...");
               
        trashModel.setHost((String) session.getAttribute("host"));
        trashModel.setUserid((String) session.getAttribute("userid"));
        trashModel.setPassword((String) session.getAttribute("password"));    

        String trashmessageList = trashModel.getTrashMessageList();
        model.addAttribute("trashmessageList", trashmessageList);
       
        return "trash_mail/trash_mail";
    }
   

    @GetMapping("/show_trashmessage")
    public String showTrashMessage(@RequestParam Integer msgid, Model model) throws Exception {
        log.debug("download_folder = {}", DOWNLOAD_FOLDER);
        System.out.println("showTrashMessage()");
        //Pop3Agent pop3 = new Pop3Agent();
     
        trashModel.setHost((String) session.getAttribute("host"));
        trashModel.setUserid((String) session.getAttribute("userid"));
        trashModel.setPassword((String) session.getAttribute("password"));
        trashModel.setRequest(request);
       
        String msg = trashModel.getTrashMessage(msgid);
        session.setAttribute("sender", trashModel.getSender());  
        session.setAttribute("subject", trashModel.getSubject());
        session.setAttribute("body", trashModel.getBody());
        model.addAttribute("msg", msg);
     
       
        return "/trash_mail/show_trashmessage";
    }
   
   
   
    @GetMapping("/permanentlyDelete_mail.do")
    public String permanentlyDeleteMailDo(@RequestParam("msgid") Integer msgId, RedirectAttributes attrs) {
        log.debug("dpermanentlyDelete_mail.do: msgid = {}", msgId);
       
        String host = (String) session.getAttribute("host");
        String userid = (String) session.getAttribute("userid");
        String password = (String) session.getAttribute("password");

       // Pop3Agent pop3 = new Pop3Agent(host, userid, password);
        boolean deleteSuccessful = trashModel.permanentlyDelete(msgId);
        if (deleteSuccessful) {
            attrs.addFlashAttribute("msg", "메시지 삭제를 성공하였습니다.");
        } else {
            attrs.addFlashAttribute("msg", "메시지 삭제를 실패하였습니다.");
        }
       
        return "redirect:trash_mail";
    }
   
    @GetMapping("/restore_mail.do")
    public String restoreMailDo(@RequestParam("msgid") Integer msgId, RedirectAttributes attrs) {
        log.debug("restore_mail.do: msgid = {}", msgId);
       
        String host = (String) session.getAttribute("host");
        String userid = (String) session.getAttribute("userid");
        String password = (String) session.getAttribute("password");

       // Pop3Agent pop3 = new Pop3Agent(host, userid, password);
       
       boolean deleteSuccessful = trashModel.restoreMessage(msgId);
        if (deleteSuccessful) {
            attrs.addFlashAttribute("msg", "메시지를 받은메일함으로 이동했습니다.");
        } else {
            attrs.addFlashAttribute("msg", "메시지 복구를 실패하였습니다.");
        }
       
        return "redirect:trash_mail";
    }
   
   
}