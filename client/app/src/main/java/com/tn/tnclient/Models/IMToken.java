package com.tn.tnclient.Models;

/**
 * Created by alimjan on 10/28/17.
 */

public class IMToken {

    private int code;
    private String userId;
    private String token;
    private String errormessage;

    public int getCode() {
        return code;
    }

    public String getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }

    public String getErrormessage() {
        return errormessage;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setErrormessage(String errormessage) {
        this.errormessage = errormessage;
    }
}
