package com.tn.model;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable{

	private Integer id;
	private String username;
	private String password;
	private String gender;
	private Date birthday;
	private String avatar;
	private String email;
	private String phone;
	private String occupation;
	private String nationality;
	private String oralEnglishProficiency;
	private int statuscode;
	private String tags;
	private float distance;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	
	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getOralEnglishProficiency() {
		return oralEnglishProficiency;
	}

	public void setOralEnglishProficiency(String oralEnglishProficiency) {
		this.oralEnglishProficiency = oralEnglishProficiency;
	}
	

	public void setStatuscode(int statuscode) {
		this.statuscode = statuscode;
	}
	
	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public int getStatuscode() {
		return statuscode;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username
				+ ", password=" + password + ", gender=" + gender + ", birthday=" + birthday
				+ ", avatar=" + avatar + ", email=" + email + ", phone=" + phone
				+ ", occupation=" + occupation + ",nationallity="+ nationality 
				+ ",oralEnglishProficiency="+ oralEnglishProficiency + ",state="+ statuscode 
				+ ",tags=" + tags + ",distance=" + distance+"]";
	}
	
}
