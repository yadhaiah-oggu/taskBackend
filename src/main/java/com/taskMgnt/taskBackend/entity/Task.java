package com.taskMgnt.taskBackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "taskname",nullable = false)
    private String taskname;

    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "users_id",referencedColumnName = "id")
    @JoinColumn(name = "users_id")
    private Users users;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
