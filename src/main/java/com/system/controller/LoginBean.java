package com.system.controller;

import java.io.IOException;
import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Named
@SessionScoped
public class LoginBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Inject
	private FacesContext facesContext;
	
	@Inject
	private HttpServletRequest request;
	
	@Inject
	private HttpServletResponse response;
	
	private String username;
	private String password;
	
	public LoginBean() {
		
	}
	
	public void singIn() throws ServletException, IOException {
	    request.getSession().setAttribute("username", username);
	    response.sendRedirect("/EnzymeGraph/Home.xhtml");
		
		facesContext.responseComplete();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
