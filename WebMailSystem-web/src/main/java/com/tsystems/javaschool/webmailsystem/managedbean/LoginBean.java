package com.tsystems.javaschool.webmailsystem.managedbean;

import com.tsystems.javaschool.webmailsystem.ejb.service.MailBoxService;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 */
@ManagedBean
@RequestScoped
public class LoginBean {
	private String email;
	private String password;

	@EJB
	private MailBoxService mailBoxService;

	public String login() {
		if (mailBoxService.login(email,password)) {
			return "createMessageForm";
		}
		return "loginError";
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
