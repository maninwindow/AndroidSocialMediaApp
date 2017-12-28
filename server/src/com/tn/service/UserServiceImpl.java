/**
 * 
 */
package com.tn.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.tn.dao.IUserDao;
import com.tn.model.PassResetResponse;
import com.tn.model.Response;
import com.tn.model.User;

import com.tn.security.MD5;

import io.rong.models.TokenResult;

/**
 * @author Almett
 *
 */
public class UserServiceImpl implements IUserService {

	@Autowired
	private IUserDao userDao;
	@Autowired
	private IMessageService messageService;
	private Response response;
	private PassResetResponse passResetResponse;
	
	@Override
	public Response register(User user) throws Exception {
		response = new Response();
		String[] result = userDao.insertUser(user);
		if(result[1].equals("false")){
			response.setStatus(0);
			if(result[0].equals("username")){
				response.setCode(1);
				response.setMessage(result[2]);
			}else if(result[0].equals("phone")){
				response.setCode(2);
				response.setMessage(result[2]);
			}
		}else{
			response.setStatus(1);
			response.setMessage(result[2]);
			response.setPhone(user.getPhone());
			response.setOccupation(user.getOccupation());
		}
		return response;
	}

	@Override
	public Response login(String phone, String password) throws Exception {
		response = new Response();
		List<User> user=userDao.findUserByPhone(phone);
		if(user.isEmpty() || !phone.equals(user.get(0).getPhone())){
			response.setStatus(0);
			response.setMessage("用户不存在");
			return response;		
		}else{
			response.setStatus(1);
			if(user.size() > 1){
				response.setcode(1);
				response.setMessage("用户已注册，请登录");
			}else{
				if(MD5.getMD5(password).equals(user.get(0).getPassword())){
					setStatusCode(user.get(0).getPhone(),1);
					response.setMessage("登陆成功");
					response.setPhone(user.get(0).getPhone());
					response.setUsername(user.get(0).getUsername());
					response.setOccupation(user.get(0).getOccupation());
					return response;	
				}else{
					user.get(0).setStatuscode(2);
					response.setcode(2);
					response.setMessage("密码错误");
					return response;	
				}
			}
		}
		return response;
	}

	@Override
	public PassResetResponse phoneValidation(String phone) throws Exception {
		passResetResponse = new PassResetResponse();
		List<User> user=userDao.findUserByPhone(phone);
		if(user.isEmpty() || !phone.equals(user.get(0).getPhone())){
			//User not exist
			passResetResponse.setStatus(0);
			return passResetResponse;		
		}else{
			passResetResponse.setStatus(1);
			passResetResponse.setV_code(messageService.sendValidationCode(phone));
			passResetResponse.setOld_pass(user.get(0).getPassword());
		}
		return passResetResponse;
	}

	@Override
	public PassResetResponse resetPassword(String phone, String newPass) throws Exception {
		User user = new User();
		passResetResponse = new PassResetResponse();
		user.setPassword(MD5.getMD5(newPass));
		user.setPhone(phone);
		boolean passResetResult = userDao.resetPassword(user);
		if(passResetResult)
			passResetResponse.setStatus(1);
		else
			passResetResponse.setStatus(0);
		return passResetResponse;
	}

	@Override
	public PassResetResponse registerValidation(String phone) throws Exception {
		passResetResponse = new PassResetResponse();
		String v_code = messageService.sendValidationCode(phone);
		if(!v_code.isEmpty()){
			passResetResponse.setStatus(1);
			passResetResponse.setV_code(v_code);
		}
		return passResetResponse;
	}

	@Override
	public void setStatusCode(String phone, int statuscode) throws Exception {
		User user = new User();
		user.setPhone(phone);
		user.setStatuscode(statuscode);
		userDao.updateStatusCode(user);
	}

	@Override
	public List<User> selectByLimit(int limit) throws Exception {
		return userDao.selectByLimit(limit);
	}

	@Override
	public int selectUserSize() throws Exception {
		return userDao.selectUserSize();
	}

	@Override
	public TokenResult insertToken(String phone) throws Exception {
		return userDao.insertToken(phone);
	}
	
}
