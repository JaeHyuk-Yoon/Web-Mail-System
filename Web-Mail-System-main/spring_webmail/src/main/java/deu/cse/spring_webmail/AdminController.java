/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.spring_webmail;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author 손진제
 */
@Controller
public class AdminController {
    
    @GetMapping("/webmail/admin_menu")
    public String adminMenu() {
        return "admin_menu";
    }
}