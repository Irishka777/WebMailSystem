package com.tsystems.javaschool.webmailsystem.managedbean;

import com.tsystems.javaschool.webmailsystem.dto.UserDTO;
import com.tsystems.javaschool.webmailsystem.ejb.service.MailBoxService;
import com.tsystems.javaschool.webmailsystem.exception.DataProcessingException;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.validation.constraints.*;
import java.util.Date;

/**
 *
 */
@ManagedBean
public class SignUpBean {
	@EJB
	private MailBoxService mailBoxService;

	@NotNull(message = "You should specify the email")
	@Pattern(regexp = "(([\\w[.]]*)@([a-zA-Z]*)\\.([a-zA-Z]*))", message = "The email you entered is invalid")
	@Size(max = 30, message = "Length of the email should be no more then 30 characters")
	private String email;

	@NotNull(message = "You should specify the password")
	@Size(min = 5, max = 30, message = "Length of the password should be no less then 5 and no more then 30 characters")
	private String password;

	@NotNull(message = "You should specify your first name")
	@Pattern(regexp = "[a-zA-Z]*", message = "First name may contain only letters")
	@Size(max = 30, message = "Length of the first name should be no more then 30 characters")
	private String firstName;

	@NotNull(message = "You should specify your last name")
	@Pattern(regexp = "[a-zA-Z]*", message = "Last name may contain only letters")
	@Size(max = 30, message = "Length of the last name should be no more then 30 characters")
	private String lastName;

	@Pattern(regexp = "[\\d]*", message = "The phone number may contain only digits")
	@Size(max = 30, message = "Length of the phone number should be no more then 30 characters")
	private String phoneNumber;

	private Date dateOfBirth;

	public String signUp() {
		try {
			mailBoxService.signUp(email, password,new UserDTO(firstName, lastName, dateOfBirth, phoneNumber));
			return "logInPage?faces-redirect=true";
		} catch (DataProcessingException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(e.getExceptionMessage()));
			return null;
		}
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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
}
