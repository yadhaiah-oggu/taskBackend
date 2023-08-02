package com.taskMgnt.taskBackend.payload;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Setter
@Getter
public class CreateTaskDto {
    private long id;
    @NotEmpty(message = "Task name is required")
    private String taskname;
}
