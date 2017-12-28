package com.tn.controller;

import java.util.Collection;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.google.gson.GsonBuilder;
import com.tn.dao.IUserDao;
import com.tn.model.PassResetResponse;
import com.tn.model.Response;
import com.tn.model.User;
import com.tn.service.IUserService;
import com.tn.utils.AppConfig;

import io.rong.models.TokenResult;

@Controller
@RequestMapping("/user")
@SessionAttributes("response")
public class UserController {
	
	@Autowired
	private IUserService userService;
	GsonBuilder gson = new GsonBuilder();
	
	@RequestMapping(value="/register", method = RequestMethod.POST,produces = "application/json; charset=utf-8")
	public @ResponseBody String registerController(@RequestBody User user,HttpServletRequest request)throws Exception{
		Response response = userService.register(user);
		response.setSid(request.getSession().getId());
		return gson.create().toJson(response);
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST,produces = "application/json; charset=utf-8")
	public @ResponseBody String loginSuccess(@RequestBody User user, HttpServletRequest request) throws Exception {
		Response userInfo = userService.login(user.getPhone(), user.getPassword());
		userInfo.setSid(request.getSession().getId());
		return gson.create().toJson(userInfo);
	}
	
	@RequestMapping(value = "/phoneValidation", method = RequestMethod.GET,produces = "application/json; charset=utf-8")
	public @ResponseBody String validationCode(@RequestParam(value = "phone") String phone)throws Exception{
		return gson.create().toJson(userService.phoneValidation(phone));
	}
	
	@RequestMapping(value = "/registerValidation", method = RequestMethod.GET,produces = "application/json; charset=utf-8")
	public @ResponseBody String registerValidation(@RequestParam(value = "phone") String phone)throws Exception{
		return gson.create().toJson(userService.registerValidation(phone));
	}
	
	@RequestMapping(value = "/resetPassword", method = RequestMethod.GET,produces = "application/json; charset=utf-8")
	public @ResponseBody String resetPassword(@RequestParam("phone") String phone, 
			@RequestParam(value = "new_pass") String newPass) throws Exception{
		return gson.create().toJson(userService.resetPassword(phone, newPass));
	}
	
	@RequestMapping(value = "/users", method = RequestMethod.GET,produces = "application/json; charset=utf-8")
	public @ResponseBody String retrieveAllUsers(@RequestParam("limit") int limit) throws Exception{
		return gson.create().toJson(userService.selectByLimit(limit));
	}
	 
	@RequestMapping(value = "/size", method = RequestMethod.GET,produces = "application/json; charset=utf-8")
	public @ResponseBody String userCount() throws Exception{
		return String.valueOf(userService.selectUserSize());
	}
	
	@RequestMapping(value = "/profileImage", method = RequestMethod.POST)
	public @ResponseBody String imageUploader(@RequestParam("image") MultipartFile image, @RequestParam String name) throws Exception{
		return "";
	}
	
	@RequestMapping(value = "/token", method = RequestMethod.GET,produces = "application/json; charset=utf-8")
	public @ResponseBody String retrieveToken(@RequestParam("phone") String phone) throws Exception{
		TokenResult tr = userService.insertToken(phone);
		return String.valueOf(tr);
	}
}
