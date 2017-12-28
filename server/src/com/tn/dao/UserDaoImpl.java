package com.tn.dao;

import java.io.Reader;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import com.tn.factory.TNSqlSessionFactory;
import com.tn.model.IMToken;
import com.tn.model.User;
import com.tn.security.MD5;
import com.tn.validator.IUserValidation;

import io.rong.RongCloud;
import io.rong.models.TokenResult;

public class UserDaoImpl implements IUserDao {
	
	@Autowired
	private IUserValidation userValidation;
	String appKey = "82hegw5u8dr3x";//替换成您的appkey
	String appSecret = "qolTvUHlj5NO";//替换成匹配上面key的secret
	RongCloud rongCloud = RongCloud.getInstance(appKey, appSecret);

	@Override
	public String[] insertUser(User user) throws Exception {
		
		//Get sql session
		SqlSession session = TNSqlSessionFactory.getSqlSession();
		
		//Encript password by MD5
		user.setPassword(MD5.getMD5(user.getPassword()));
		
		//Username lowercase
		user.setUsername(user.getUsername().toLowerCase());
		
		//Validate if the username and phone number are exist
		String[] result = userValidation.checkIfUserExists(session, user);
		
		session.commit();
		
		session.close();
		
		return result;
	}
	
	@Override
	public TokenResult insertToken(String phone)throws Exception{
		//Get sql session
		SqlSession session = TNSqlSessionFactory.getSqlSession();
		
		List<User> user = findUserByPhone(phone);
		
		TokenResult token = rongCloud.user.getToken(user.get(0).getPhone(), user.get(0).getUsername(), user.get(0).getAvatar());
		
		session.insert("com.tn.mapper.UserMapper.insertToken",token);
		
		session.commit();
		
		session.close();
		
		return token;
	}
	
	@Override
	public User findUserById(int id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean findUserByUserName(String username) throws Exception {
		
		SqlSession session = TNSqlSessionFactory.getSqlSession();
		
		User user = session.selectOne("com.tn.mapper.UserMapper.findUserByUserName", username);
		
		session.commit();
		
		session.close();
		
		return user == null?false:true;
	}

	@Override
	public User findUserByEmail(String email) throws Exception {
		
		SqlSession session = TNSqlSessionFactory.getSqlSession();
		
		User user = session.selectOne("com.tn.mapper.UserMapper.findUserByEmail", email);
		
		session.commit();
		
		session.close();
		
		return user;
	}

	@Override
	public List<User> findUserByPhone(String phone) throws Exception {

		SqlSession session = TNSqlSessionFactory.getSqlSession();
		
		List<User> user = session.selectList("com.tn.mapper.UserMapper.findUserByPhone", phone);
		
		session.commit();
		
		session.close();
		
		return user;
	}

	@Override
	public void deleteUser(int id) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void bindPhone(String username, String newphone) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean resetPassword(User user) throws Exception {
		
		SqlSession session = TNSqlSessionFactory.getSqlSession();
		
		session.update("com.tn.mapper.UserMapper.modifyPassword", user);
		
		session.commit();
		
		session.close();
		
		return true;
	}

	@Override
	public int midifyPassword(String username, String newpassword) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int midifyPhone(String username, String newphone) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int activateUser(String email) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void updateStatusCode(User user) throws Exception {
		
		SqlSession session = TNSqlSessionFactory.getSqlSession();
		
		session.update("com.tn.mapper.UserMapper.updateStatusCode", user);
		
		session.commit();
		
		session.close();
		
	}

	@Override
	public List<User> findAllUsers() throws Exception {
		
		SqlSession session = TNSqlSessionFactory.getSqlSession();
		
		List<User> user = session.selectList("com.tn.mapper.UserMapper.findAllUsers");
		
		session.commit();
		
		session.close();
		
		return user;
	}

	@Override
	public List<User> selectByLimit(int limit) throws Exception {
		
		SqlSession session = TNSqlSessionFactory.getSqlSession();
		
		List<User> user = session.selectList("com.tn.mapper.UserMapper.selectByLimit",limit);
		
		session.commit();
		
		session.close();
		
		return user;
		
	}

	@Override
	public int selectUserSize() throws Exception {
		
		SqlSession session = TNSqlSessionFactory.getSqlSession();
		
		int user = session.selectOne("com.tn.mapper.UserMapper.selectUserSize");
		
		session.commit();
		
		session.close();
		
		return user;
	}
}
