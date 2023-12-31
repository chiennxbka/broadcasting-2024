package com.mintpot.broadcasting.common.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * TblHistory generated by hbm2java
 */

@Entity
@Getter
@Setter
@Table(name = "tbl_history")
public class History implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "uid")
    private Long uid;

    @Column(name = "ip_address")
    private String ipAddress;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "login_time")
    private Date loginTime;
}
