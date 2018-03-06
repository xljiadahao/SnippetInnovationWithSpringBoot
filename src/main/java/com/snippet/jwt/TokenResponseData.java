package com.snippet.jwt;

import org.springframework.hateoas.ResourceSupport;

public class TokenResponseData extends ResourceSupport {

    private String userName;
    private String token;
    
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

}
