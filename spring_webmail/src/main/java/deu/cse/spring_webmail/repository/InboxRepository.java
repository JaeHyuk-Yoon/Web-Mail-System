/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package deu.cse.spring_webmail.repository;

import deu.cse.spring_webmail.entity.Inbox;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author JaeHyuk
 */
public interface InboxRepository extends JpaRepository<Inbox, Long> {
    
    @Query(value = 
                    "SELECT * " +
                    "FROM inbox" +
                    "WHERE repository_name = :repository_name" +
                    "ORDER BY last_updated DESC",
                    nativeQuery = true)
    List<Inbox> findByReciveMail(String repository_name);
    
    @Query(value = 
                    "SELECT * " +
                    "FROM inbox" +
                    "WHERE sender = :sender" +
                    "ORDER BY last_updated DESC",
                    nativeQuery = true)
    List<Inbox> findByMineSendMail(String sender);
    
}
