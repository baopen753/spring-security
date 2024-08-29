package com.baopen753.securitywithauthorizationimplementing.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "contact_messages")
public class Contact {

    @Id
    @Column(name = "contact_id")
    @JsonIgnore
    private String contactId;

    @Column(name = "contact_name")
    @JsonProperty("contact_name")
    private String contactName;

    @Column(name = "contact_email")
    @JsonProperty("contact_email")
    private String contactEmail;

    @JsonProperty("subject")
    private String subject;

    @JsonProperty("message")
    private String message;

    @Column(name = "create_dt")
    @JsonIgnore
    private Date createDt;

}
