/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.spring_webmail.control;

import deu.cse.spring_webmail.model.AddressManager;
import deu.cse.spring_webmail.model.Address;
import java.util.List;
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
@PropertySource("classpath:/config.properties")
public class AddressControler {
    @Value("${mysql.server.ip}")
    private String mysqlServerIp;
    @Value("${mysql.server.port}")
    private String mysqlServerPort;

    @Autowired
    private Environment env;
    
    @GetMapping("/address_view")
    public String insertTable(Model model) {
        String userName = env.getProperty("spring.datasource.username");
        String password = env.getProperty("spring.datasource.password");
        String jdbcDriver = env.getProperty("spring.datasource.driver-class-name");
        log.debug("ip={}, port={}", this.mysqlServerIp, this.mysqlServerPort);

        AddressManager manager = new AddressManager(mysqlServerIp, mysqlServerPort, userName, password, jdbcDriver);
        List<Address> dataRows = manager.getAllRows();
        model.addAttribute("dataRows", dataRows);

        return "/address_view";
    }

    @GetMapping("/address_insert")
    public String insertAddressBook() {
        return "/adress_insert";
    }

    @PostMapping("/insert.do")
    public String insertAddressBook(@RequestParam String email, @RequestParam String name, @RequestParam String phone, Model model) {
        String userName = env.getProperty("spring.datasource.username");
        String password = env.getProperty("spring.datasource.password");
        String jdbcDriver = env.getProperty("spring.datasource.driver-class-name");

        AddressManager manager = new AddressManager(mysqlServerIp, mysqlServerPort, userName, password, jdbcDriver);
        manager.addRow(email, name, phone);

        List<Address> dataRows = manager.getAllRows();
        model.addAttribute("dataRows", dataRows);

        return "redirect:/address_view";
    }
    
    @PostMapping("/delete.do")
    public String deleteAddressBook(@RequestParam String email, @RequestParam String name, @RequestParam String phone, Model model) {
        String userName = env.getProperty("spring.datasource.username");
        String password = env.getProperty("spring.datasource.password");
        String jdbcDriver = env.getProperty("spring.datasource.driver-class-name");

        AddressManager manager = new AddressManager(mysqlServerIp, mysqlServerPort, userName, password, jdbcDriver);
        manager.removeRow(email, name, phone);

        List<Address> dataRows = manager.getAllRows();
        model.addAttribute("dataRows", dataRows);

        return "redirect:/address_view";
    }
}
