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
    

    List<Inbox> findByRecipients(String recipients);
    
    List<Inbox> findByRepositoryName(String repositoryName);

    List<Inbox> findBySender(String sender);
    
    List<Inbox> findByMessageName(String messageName);

}
