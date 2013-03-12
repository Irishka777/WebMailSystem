package com.tsystems.javaschool.webmailsystem.managedbean;

import com.tsystems.javaschool.webmailsystem.ejb.service.MailBoxService;
import com.tsystems.javaschool.webmailsystem.entity.MailBoxEntity;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

/**
 *
 */
@ManagedBean
public class LoginBean {
	private String email;
	private String password;

	@EJB
	private MailBoxService mailBoxService;

	public String login() {
		MailBoxEntity mailBox = mailBoxService.login(email,password);
		if (mailBox == null) {
			return "loginError";
		}
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("mailBox",mailBox);
		return "createMessageForm";
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
