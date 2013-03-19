package com.tsystems.javaschool.webmailsystem.managedbean;

import com.tsystems.javaschool.webmailsystem.dto.MailBoxDTO;
import com.tsystems.javaschool.webmailsystem.ejb.service.MailBoxService;
import com.tsystems.javaschool.webmailsystem.exception.DataProcessingException;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static javax.faces.context.FacesContext.*;

/**
 *
 */
@ManagedBean
public class LoginBean {
	@EJB
	private MailBoxService mailBoxService;

	@NotNull(message = "You should specify the email")
	@Pattern(regexp = "(([\\w[.]]*)@([a-zA-Z]*)\\.([a-zA-Z]*))",
			message = "The email should look like mail@mail.ru")
	private String email;

	@NotNull(message = "You should specify the password")
	@Size(min = 5, message = "Length of the password should be no less then five characters")
	private String password;

	public String logIn() {
		try {
			MailBoxDTO mailBox = mailBoxService.login(email,password);
			getCurrentInstance().getExternalContext().getSessionMap().put("mailBox",mailBox);
			return "foldersAndMessagesPage?faces-redirect=true";
		} catch (DataProcessingException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(e.getExceptionMessage()));
			return null;
		}
	}

	public String logOut() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		session.invalidate();
		return "logInPage?faces-redirect=true";
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
