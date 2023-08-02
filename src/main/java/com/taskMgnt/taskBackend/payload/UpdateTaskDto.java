package com.taskMgnt.taskBackend.payload;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class UpdateTaskDto {
    private long id;
    @NotEmpty(message = "Task name is required")
    private String taskname;
    @NotEmpty(message = "Status is required")
    private String status;
}
