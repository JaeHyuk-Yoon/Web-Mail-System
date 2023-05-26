/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package deu.cse.spring_webmail.repository;

import deu.cse.spring_webmail.entity.Inbox;
import deu.cse.spring_webmail.entity.InboxPK;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author JaeHyuk
 */
public interface InboxRepository extends JpaRepository<Inbox, InboxPK> {
    
//    @Query(value = 
//                    "SELECT * " +
//                    "FROM inbox" +
//                    "WHERE repository_name = :repository_name" +
//                    "ORDER BY last_updated DESC",
//                    nativeQuery = true)
//    List<Inbox> findByReciveMail(String repository_name);

//    @Query(value = 
//                    "SELECT * " +
//                    "FROM inbox" +
//                    "WHERE repository_name = :repository_name" +
//                    "ORDER BY last_updated DESC",
//                    nativeQuery = true)
    List<Inbox> findByRecipients(String recipients);
    
    List<Inbox> findByRepositoryName(String repositoryName);
    
//    @Query(value = 
//                    "SELECT * " +
//                    "FROM inbox" +
//                    "WHERE sender = :sender" +
//                    "ORDER BY last_updated DESC",
//                    nativeQuery = true)
//    List<Inbox> findByMineSendMail(String sender);
    
//<<<<<<< HEAD
//=======
    List<Inbox> findBySender(String sender);
    List<Inbox> findByMessageName(String messageName);
//    
//>>>>>>> ab6db2ea7e3d69473fa92bf9e669f89b2aacdfd2
}
