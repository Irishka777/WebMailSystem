package com.tsystems.javaschool.webmailsystem.managedbean;

import com.tsystems.javaschool.webmailsystem.ejb.service.MailBoxService;
import com.tsystems.javaschool.webmailsystem.entity.MailBoxEntity;
import com.tsystems.javaschool.webmailsystem.entity.UserEntity;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import java.util.Calendar;
import java.util.Date;

/**
 *
 */
@ManagedBean
public class SignUpBean {
	private String email;
	private String password;
	private String passwordReenter;
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private Date dateOfBirth;

	@EJB
	private MailBoxService mailBoxService;

	public String registration() {
		dateOfBirth = Calendar.getInstance().getTime(); // need convert
		if (mailBoxService.registration(
				new MailBoxEntity(email, password,
						new UserEntity(firstName, lastName, dateOfBirth, phoneNumber)))) {
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

	public String getPasswordReenter() {
		return passwordReenter;
	}

	public void setPasswordReenter(String passwordReenter) {
		this.passwordReenter = passwordReenter;
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
