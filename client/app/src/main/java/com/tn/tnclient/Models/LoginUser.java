package com.tn.tnclient.Models;

/**
 * Created by vidur on 8/19/2017.
 */

public class LoginUser {

    public LoginUser(String phone, String password) {
        this.phone = phone;
        this.password = password;
    }

    private String phone;
    private String password;

    public void setphone(String contact) {
        this.phone = phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getphone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }
}
