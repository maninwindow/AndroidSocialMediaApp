package com.tn.model;

public class PassResetResponse {

	private String phone;
	private String v_code;
	private int status;
	private int code;
	private String old_pass;
	
	public String getOld_pass() {
		return old_pass;
	}
	public void setOld_pass(String old_pass) {
		this.old_pass = old_pass;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getV_code() {
		return v_code;
	}
	public void setV_code(String v_code) {
		this.v_code = v_code;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	
}
