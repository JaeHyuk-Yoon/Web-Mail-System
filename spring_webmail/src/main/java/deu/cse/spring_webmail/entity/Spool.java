/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.spring_webmail.entity;


import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;


/**
 *
 * @author JaeHyuk
 */
@Data

@Entity
@Table(name = "SPOOL")
public class Spool {
    
    @EmbeddedId
    private InboxPK inboxpk;
    
    @Column(name = "MESSAGE_STATE")
    private String message_state;
    
    @Column(name = "ERROR_MESSAGE")
    private String error_message;
    
    @Column(name = "SENDER")
    private String sender;
    
    @Column(name = "RECIPENTS")
    private String recipients;
    
    @Column(name = "REMOTE_HOST")
    private String remote_host;
    
    @Column(name = "REMOTE_ADDR")
    private String remote_addr;
    
    @Column(name = "MESSAGE_BODY")
    @Lob
    private String message_body;
    
    @Column(name = "MESSAGE_ATTRIBUTES")
    @Lob
    private String message_attributes;
    
    @Column(name = "LAST_UPDATED")
    @Temporal(TemporalType.DATE)
    private Date last_updated;
    
    @Column(name = "SHOW_CHECK")
    private int show_check;
}
