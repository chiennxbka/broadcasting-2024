package com.mintpot.broadcasting.common.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;


/**
 * TblDevice generated by hbm2java
 */

@Entity
@Getter
@Setter
@Table(name = "tbl_device")
@SQLDelete(sql = "update tbl_device dv set dv.active = 0 where dv.id = ?")
@Where(clause = "active = 1")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Device extends BaseEntity<Long> implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    private String name;

    private String imei;

    private String description;

    @Column(columnDefinition = "geometry")
    private Point location;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "devices")
    private Set<Campaign> campaigns = new HashSet<>(0);

    @ManyToOne
    @JoinColumn(name = "org_id")
    private Organization organization;

    private boolean active;
}
