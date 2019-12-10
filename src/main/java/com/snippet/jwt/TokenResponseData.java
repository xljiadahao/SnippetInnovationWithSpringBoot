package com.snippet.jwt;

import org.springframework.hateoas.RepresentationModel;

public class TokenResponseData extends RepresentationModel<TokenResponseData> {

    private String userName;
    private String token;
    private String qrCode;
    
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
    public String getQrCode() {
        return qrCode;
    }
    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

}
