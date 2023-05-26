/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package deu.cse.spring_webmail.repository;

import deu.cse.spring_webmail.entity.Inbox;
import deu.cse.spring_webmail.entity.InboxPK;
import deu.cse.spring_webmail.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author JaeHyuk
 */
public interface UsersRepository extends JpaRepository<Users, String> {
    
}
