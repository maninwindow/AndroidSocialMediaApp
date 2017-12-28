package com.tn.validator;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import com.tn.dao.IUserDao;
import com.tn.model.User;

public class UserValidationImpl implements IUserValidation {
	
	@Autowired
	private IUserDao userDao;

	@Override
	public String[] checkIfUserExists(SqlSession session, User user) {
		boolean ifUsernameExists = false;
		List<User> ifPhoneExists = null;
		try {
			ifUsernameExists = userDao.findUserByUserName(user.getUsername().toLowerCase());
			ifPhoneExists = userDao.findUserByPhone(user.getPhone());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		String[] result = new String[3];
		if(ifUsernameExists){
			result[0] = "username";
			result[1] = String.valueOf(false);
			result[2] = "用户名已存在";
		}else if(!ifPhoneExists.isEmpty()){
			result[0] = "phone";
			result[1] = String.valueOf(false);
			result[2] = "手机号已存在";
		}else{
			session.insert("com.tn.mapper.UserMapper.register", user);
			result[1] = String.valueOf(true);
			result[2] = "注册成功!";
		}
		return result;
	}

}
