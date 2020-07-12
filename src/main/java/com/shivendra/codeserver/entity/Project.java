package com.shivendra.codeserver.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "project")
@EntityListeners(AuditingEntityListener.class)
@Data
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "external_id", nullable = false)
    private String externalId;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "sdlc_system_id")
    private SdlcSystem sdlcSystem;

    @Column(name = "created_date",nullable = false)
    @CreatedDate
    private Instant createdDate;

    @Column(name = "last_modified_date",nullable = false)
    @LastModifiedDate
    private Instant lastModifiedDate;
}
