package com.tsystems.javaschool.webmailsystem.managedbean;

import javax.faces.bean.ManagedBean;

/**
 *
 */
@ManagedBean
public class CreateMessageBean {
	private String receiver;
	private String theme;
	private String messageBody;

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getMessageBody() {
		return messageBody;
	}

	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}
}
