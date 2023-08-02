package com.taskMgnt.taskBackend.payload;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class TaskDto {
    private long id;

    private String taskname;

    private LocalDateTime createdat;

    private LocalDateTime updatedat;

    private String status;
    private String username;


}
