/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.spring_webmail.entity;

import java.io.Serializable;
import java.sql.Blob;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author pcb
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "trashbox")
@IdClass(TrashboxPK.class)
public class Trashbox implements Serializable {
    @Id
    @Column(name = "message_name")
    private String messageName;
   
    @Id
    @Column(name = "repository_name")
    private String repositoryName;
   
    @Column(name = "message_state")
    private String messageState;
   
    @Column(name = "error_message")
    private String errorMessage;
   
    @Column(name = "sender")
    private String sender;
   
    @Column(name = "recipients")
    private String recipients;
   
    @Column(name = "remote_host")
    private String remoteHost;
   
    @Column(name = "remote_addr")
    private String remoteAddr;
   
    @Column(name = "message_body")
    @Lob
    private Blob messageBody;
   
    @Column(name = "message_attributes")
    @Lob
    private Blob messageAttributes;
   
    @Column(name = "last_updated")
    @Temporal(TemporalType.DATE)
    private Date lastUpdated;
}
