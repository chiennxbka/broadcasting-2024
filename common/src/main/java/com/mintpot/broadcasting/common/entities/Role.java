package com.mintpot.broadcasting.common.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@Table(name = "tbl_role", uniqueConstraints = @UniqueConstraint(columnNames = "type"))
public class Role extends BaseEntity<Long> implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "type", unique = true)
    private String type;

    @Column(name = "name", nullable = false)
    private String name;
}
