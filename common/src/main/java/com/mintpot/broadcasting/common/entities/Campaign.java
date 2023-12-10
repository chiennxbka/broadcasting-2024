package com.mintpot.broadcasting.common.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mintpot.broadcasting.common.enums.Priority;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * TblCampaign generated by hbm2java
 */
@Entity
@Getter
@Setter
@Table(name = "tbl_campaign")
@SQLDelete(sql = "update tbl_campaign cp set cp.active = 0 where cp.id = ?")
@Where(clause = "active = 1")
public class Campaign extends BaseEntity<Long> implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_time")
    private Date startTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_time")
    private Date endTime;

    @Column(name = "priority")
    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Column(name = "repeated")
    private boolean repeated;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "tbl_campaign_content", joinColumns = {@JoinColumn(name = "campaign_id")},
        inverseJoinColumns = {@JoinColumn(name = "content_id")})
    private Set<Content> contents = new HashSet<>(0);

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "tbl_campaign_device", joinColumns = {@JoinColumn(name = "campaign_id")},
        inverseJoinColumns = {@JoinColumn(name = "device_id")})
    private Set<Device> devices = new HashSet<>(0);

    @ManyToOne
    @JoinColumn(name = "org_id")
    private Organization organization;

    private boolean active;
}