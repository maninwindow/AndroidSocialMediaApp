package com.tn.service;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;

public interface IMessageService {

	String sendValidationCode(String phone) throws ServerException, ClientException;
	
}
