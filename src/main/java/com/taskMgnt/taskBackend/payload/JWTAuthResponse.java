package com.taskMgnt.taskBackend.payload;

import lombok.Getter;

@Getter
public class JWTAuthResponse {
    private String token;
    private String tokenType = "Bearer";
    private String username;
    private String userrole;

    public JWTAuthResponse(String token, String username , String userrole){
        this.token = token;
        this.username = username;
        this.userrole = userrole;
    }

}
