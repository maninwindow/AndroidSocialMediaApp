package com.tn.validator;

import org.apache.ibatis.session.SqlSession;

import com.tn.model.User;

public interface IUserValidation {

	String[] checkIfUserExists(SqlSession session, User user);
	
}
