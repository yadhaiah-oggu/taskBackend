package com.taskMgnt.taskBackend.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class RegisterUserDto {
    private long id;
    private String name;
    private String email;
    private String password;
    private String role;
}
