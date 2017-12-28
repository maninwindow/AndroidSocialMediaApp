package com.tn.tnclient.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alimjan on 10/18/17.
 */

public class PassResetResponse {

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("v_code")
    @Expose
    private String v_code;

    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("code")
    @Expose
    private int code;

    @SerializedName("old_pass")
    @Expose
    private String old_pass;

    public String getPhone() {
        return phone;
    }

    public String getV_code() {
        return v_code;
    }

    public int getStatus() {
        return status;
    }

    public int getCode() {
        return code;
    }

    public String getOld_pass() {
        return old_pass;
    }


    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setV_code(String v_code) {
        this.v_code = v_code;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setOld_pass(String old_pass) {
        this.old_pass = old_pass;
    }
}
