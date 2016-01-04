package com.telesoftas.retrofitimpl.data.entities;

/**
 * @author Vidmantas Kerbelis (vkerbelis@yahoo.com) on 2015-11-12.
 */
public class LoginResponse {

    private String token;
    private UserResponse user;

    public String getToken() {
        return token;
    }

    public UserResponse getUserResponse() {
        return user;
    }
}
