package com.taskMgnt.taskBackend.service;

import com.taskMgnt.taskBackend.payload.UserDto;

public interface UserService {
    public UserDto createUser(UserDto userDto);
}
