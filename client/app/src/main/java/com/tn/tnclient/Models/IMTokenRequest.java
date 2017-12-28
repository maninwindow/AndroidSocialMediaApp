package com.tn.tnclient.Models;

/**
 * Created by alimjan on 10/28/17.
 */

public class IMTokenRequest {

    private String userId;
    private String name;
    private String portraitUrl;

    public IMTokenRequest(String userId, String name, String portraitUrl){
        this.userId = userId;
        this.name = name;
        this.portraitUrl = portraitUrl;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getPortraitUrl() {
        return portraitUrl;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPortraitUrl(String portraitUrl) {
        this.portraitUrl = portraitUrl;
    }
}
