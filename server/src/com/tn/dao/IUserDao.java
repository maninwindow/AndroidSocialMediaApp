package com.tn.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tn.model.IMToken;
import com.tn.model.User;

import io.rong.models.TokenResult;

public interface IUserDao {

	public String[] insertUser(User user) throws Exception;
	
	public TokenResult insertToken(String phone)throws Exception;
	
	public User findUserById(int id) throws Exception;
	
	public boolean findUserByUserName(String userName) throws Exception;
	
	public User findUserByEmail(String email) throws Exception;
	
	public List<User> findUserByPhone(String phone) throws Exception;
	
	public List<User> findAllUsers()throws Exception;
	
	public List<User> selectByLimit(int limit)throws Exception;

	public int selectUserSize()throws Exception;
	
	public void deleteUser(int id) throws Exception;
	
	public void bindPhone(String username,String newphone) throws Exception;

	public boolean resetPassword(User user) throws Exception;
	
	public int midifyPassword(@Param("phone") String phone, @Param("new_pass") String newpassword)
			throws Exception;
	
	public int midifyPhone(@Param("username") String username, @Param("newphone") String newphone) throws Exception;
	
	public int activateUser(String email) throws Exception;
	
	public void updateStatusCode(User user)throws Exception;
	
}
