package com.taskMgnt.taskBackend.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTaskDto {
    private long id;
    private String taskname;
    private String status;
}
