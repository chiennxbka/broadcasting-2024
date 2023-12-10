package com.mintpot.broadcasting.common.entities;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@Table(name = "tbl_organization")
@SQLDelete(sql = "update tbl_organization org set org.active = 0 where org.id = ?")
@Where(clause = "active = 1")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Organization extends BaseEntity<Long> implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    private String name;

    private String code;

    private boolean active;
}
