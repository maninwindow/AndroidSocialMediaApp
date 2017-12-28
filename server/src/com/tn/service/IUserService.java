package com.tn.service;

import java.util.List;

import com.tn.model.PassResetResponse;
import com.tn.model.Response;
import com.tn.model.User;

import io.rong.models.TokenResult;

public interface IUserService {

	Response register(User user) throws Exception;
	
	Response login(String email,String password) throws Exception;

	PassResetResponse phoneValidation(String phone)throws Exception;
	
	PassResetResponse registerValidation(String phone)throws Exception;
	
	PassResetResponse resetPassword(String phone, String newPass)throws Exception;
	
	void setStatusCode(String phone, int statuscode)throws Exception;
	
	List<User> selectByLimit(int limit) throws Exception;
	
	int selectUserSize()throws Exception;
	
	TokenResult insertToken(String phone)throws Exception;
}
