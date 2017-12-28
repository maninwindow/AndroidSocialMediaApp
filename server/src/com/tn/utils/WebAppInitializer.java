package com.tn.utils;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

//public class WebAppInitializer implements WebApplicationInitializer{
//
//	@Override
//	public void onStartup(ServletContext servletContext) throws ServletException {
//		AnnotationConfigWebApplicationContext wac = new AnnotationConfigWebApplicationContext();
//		wac.register(AppConfig.class);
//		wac.setServletContext(servletContext);
//		wac.refresh();
//		
//		Dynamic dynamic = servletContext.addServlet("dispatcher", new DispatcherServlet(wac));
//		dynamic.addMapping("/");
//		dynamic.setLoadOnStartup(1);
//		dynamic.setMultipartConfig(wac.getBean(MultipartConfigElement.class));
//	}
//
//}
