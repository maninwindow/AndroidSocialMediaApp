package com.tn.tnclient.Models;

/**
 * Created by alimjan on 11/2/17.
 */

public class User {

    private String phone;
    private String username;
    private String avatar;
    private String tags;
    private String distance;

    public User(String phone, String username, String avatar, String tags, String distance){
        this.phone = phone;
        this.username = username;
        this.avatar = avatar;
        this.tags = tags;
        this.distance = distance;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public String getTags() {
        return tags;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDistance() {
        return distance;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
