package com.taskMgnt.taskBackend.payload;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateTaskDto {
    private long id;
    private String taskname;
}
