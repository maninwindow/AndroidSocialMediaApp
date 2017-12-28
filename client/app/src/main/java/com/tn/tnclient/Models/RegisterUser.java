package com.tn.tnclient.Models;

/**
 * Created by vidur on 8/10/2017.
 */

public class RegisterUser {

    //New Commit

    private String username;
    private String email;
    private String phone;
    private String password;



    public RegisterUser(String username, String phone, String password) {

        this.username = username;
        this.phone = phone;
        this.password = password;

    }

    public String getusername() {
        return username;
    }

    public void setusername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
