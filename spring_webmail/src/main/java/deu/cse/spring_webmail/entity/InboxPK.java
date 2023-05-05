/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.spring_webmail.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author JaeHyuk
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class InboxPK implements Serializable {
    @Column(name = "message_name")
    private String messageName;
    
    @Column(name = "repository_name")
    private String repositoryName;
}
