package com.taskMgnt.taskBackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "users",uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email"})
})
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "createdat", nullable = false)
    private LocalDateTime createdat;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
    @PrePersist
    public void prePersist() {
        createdat = LocalDateTime.now();
    }
}
