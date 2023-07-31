package com.taskMgnt.taskBackend.payload;

import lombok.Getter;

@Getter
public class JWTAuthResponse {
    private String token;
    private String tokenType = "Bearer";
    private String username;

    public JWTAuthResponse(String token, String username){
        this.token = token;
        this.username = username;
    }

}
