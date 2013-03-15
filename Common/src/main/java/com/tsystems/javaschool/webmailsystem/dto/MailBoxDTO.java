package com.tsystems.javaschool.webmailsystem.dto;

/**
 *
 */
public class MailBoxDTO {
	private String email;

	public MailBoxDTO() {}

	public MailBoxDTO(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String toString() {
		return email;
	}
}