package com.timeissecuritytime.domain;
 
import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
}