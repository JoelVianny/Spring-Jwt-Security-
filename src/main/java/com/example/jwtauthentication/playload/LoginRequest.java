package com.example.jwtauthentication.playload;


import lombok.Data;

@Data
public class LoginRequest {
    private String userName;
    private String password;
}
