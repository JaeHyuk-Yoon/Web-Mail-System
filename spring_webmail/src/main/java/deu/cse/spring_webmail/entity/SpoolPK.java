/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.spring_webmail.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Data;

/**
 *
 * @author JaeHyuk
 */

@Data

@Embeddable
public class SpoolPK implements Serializable {
    @Column(name = "MESSAGE_NAME")
    private String message_name;
    
    @Column(name = "REPOSITORY_NAME")
    private String repository_name;
}
