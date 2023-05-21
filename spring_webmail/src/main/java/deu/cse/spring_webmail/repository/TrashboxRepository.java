/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package deu.cse.spring_webmail.repository;

import deu.cse.spring_webmail.entity.Trashbox;
import deu.cse.spring_webmail.entity.TrashboxPK;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author pcb
 */
public interface TrashboxRepository extends JpaRepository<Trashbox, TrashboxPK> {

    List<Trashbox> findByRecipients(String recipients);

    void deleteByMessageName(String messageName);

}