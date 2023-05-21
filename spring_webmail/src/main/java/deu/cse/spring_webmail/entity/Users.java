/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.spring_webmail.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author JaeHyuk
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class Users {
    
    @Id
    @Column(name = "username")
    private String username;
    
    @Column(name = "pwd_hash")
    private String pwdHash;
    
    @Column(name = "pwd_algorithm")
    private String pwdAlgorithm;
    
//    @Column(name = "use_forwarding")
//    private int useForwarding;
    
    @Column(name = "forward_destination")
    private String forwardDestination;
    
//    @Column(name = "use_alias")
//    private int useAlias;
//    
//    @Column(name = "alias")
//    private String alias;
}
