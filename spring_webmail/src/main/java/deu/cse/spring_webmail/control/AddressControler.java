/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.spring_webmail.control;

import deu.cse.spring_webmail.entity.AddrBook;
import deu.cse.spring_webmail.model.AddressManager;
import deu.cse.spring_webmail.model.Address;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author User
 */
@Controller
@Slf4j
@PropertySource("classpath:/system.properties")
public class AddressControler {

    @Autowired
    private ServletContext ctx;
    @Autowired
    private HttpSession session;
    @Autowired
    private HttpServletRequest request;

    @Value("${mysql.server.ip}")
    private String mysqlServerIp;
    @Value("${mysql.server.port}")
    private String mysqlServerPort;

    @Autowired
    private Environment env;

    @Autowired
    AddressManager manager;
    
    @GetMapping("/address_view")
    public String insertTable(Model model) {
        manager.setUserid((String) session.getAttribute("userid"));
        List<AddrBook> dataRows = manager.getAllRows();
        model.addAttribute("dataRows", dataRows);

        return "address/address_view";
    }

    @GetMapping("/address_insert")
    public String insertAddressBook() {
        return "address/address_insert";
    }

    @GetMapping("/address_update")
    public String updateAddressBook() {
        return "address/address_update";
    }

    @PostMapping("/insert.do")
    public String insertAddressBook(@RequestParam String email, @RequestParam String name, @RequestParam String phone, Model model) {
        manager.setUserid((String) session.getAttribute("userid"));
        manager.addRow(email, name, phone);

        List<AddrBook> dataRows = manager.getAllRows();
        model.addAttribute("dataRows", dataRows);

        return "redirect:/address_view";
    }

    @PostMapping("/update.do")
    public String updateAddressBook(@RequestParam String preemail, @RequestParam String prename, @RequestParam String prephone,
            @RequestParam String nexemail, @RequestParam String nexname, @RequestParam String nexphone, Model model) {
        manager.setUserid((String) session.getAttribute("userid"));
        manager.changeRow(preemail, prename, prephone, nexemail, nexname, nexphone);

        List<AddrBook> dataRows = manager.getAllRows();
        model.addAttribute("dataRows", dataRows);

        return "redirect:/address_view";
    }

    @GetMapping("/delete.do")
    public String deleteAddressBook(@RequestParam("email") String email, @RequestParam("name") String name, @RequestParam("phone") String phone, Model model) {
        manager.setUserid((String) session.getAttribute("userid"));
        manager.removeRow(email, name, phone);

        List<AddrBook> dataRows = manager.getAllRows();
        model.addAttribute("dataRows", dataRows);

        return "redirect:/address_view";
    }
}
