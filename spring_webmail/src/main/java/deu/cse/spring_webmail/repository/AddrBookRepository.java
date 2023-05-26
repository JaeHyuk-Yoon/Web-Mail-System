/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package deu.cse.spring_webmail.repository;

import deu.cse.spring_webmail.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author JaeHyuk
 */
public interface AddrBookRepository extends JpaRepository<Users, String> {
    
}
