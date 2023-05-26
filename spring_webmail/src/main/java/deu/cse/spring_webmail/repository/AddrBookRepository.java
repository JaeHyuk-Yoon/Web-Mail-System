/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package deu.cse.spring_webmail.repository;

import deu.cse.spring_webmail.entity.AddrBook;
import deu.cse.spring_webmail.entity.Users;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author JaeHyuk
 */
public interface AddrBookRepository extends JpaRepository<AddrBook, String> {
    List<AddrBook> findByUserId(String userid);

    AddrBook findByEmailAndNameAndPhoneAndUserId(String email, String name, String phone, String userid);
}
