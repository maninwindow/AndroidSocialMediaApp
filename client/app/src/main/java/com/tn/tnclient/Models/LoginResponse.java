package com.tn.tnclient.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Suhail on 8/7/2017.
 */

public class LoginResponse {

    //class to handle responses from api call for login
    @SerializedName("status")
    @Expose
    private Integer status;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("code")
    @Expose
    private Integer code;

    @SerializedName("sid")
    @Expose
    private String sid;

    @SerializedName("contact")
    @Expose
    private String phone;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("occupation")
    @Expose
    private String occupation;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getUsername() {
        return username;

    }
    public String getOccupation() {
        return occupation;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}