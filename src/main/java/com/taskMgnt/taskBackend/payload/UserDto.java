package com.taskMgnt.taskBackend.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UserDto {
    private long id;
    private String name;
    private String email;
    private String password;
}
