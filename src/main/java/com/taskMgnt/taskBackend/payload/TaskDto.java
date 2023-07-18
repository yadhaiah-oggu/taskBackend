package com.taskMgnt.taskBackend.payload;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TaskDto {
    private long id;
    private String taskname;
}
