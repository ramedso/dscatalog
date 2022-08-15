package com.atlas.dscatalog.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tb_category")
public class Category implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Instant createdAt;

    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Instant updatedAt;

    @PrePersist
    public void prePersist(){
        createdAt = Instant.now();
    }

    @PreUpdate
    public void preUp(){
        updatedAt = Instant.now();
    }
}
