package com.tsystems.javaschool.webmailsystem.managedbean;

//import com.tsystems.javaschool.webmailsystem.ejb.service.MailBoxService;
import com.tsystems.javaschool.webmailsystem.ejb.service.MessageService;
import com.tsystems.javaschool.webmailsystem.entity.MailBoxEntity;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

/**
 *
 */
@ManagedBean
public class CreateMessageBean {
//	private String sender;
	private String receiver;
	private String theme;
	private String messageBody;

	@EJB
	private MessageService messageService;

	public String sendMessage() {
		MailBoxEntity senderMailBox = (MailBoxEntity) FacesContext
				.getCurrentInstance().getExternalContext().getSessionMap().get("mailBox");
		if (messageService.sendMessage(senderMailBox,receiver,theme,messageBody)) {
			return "messageSuccessfullySentPage";
		}
		return "sendMessageError";
	}

	public String saveMessage() {
		MailBoxEntity senderMailBox = (MailBoxEntity) FacesContext
				.getCurrentInstance().getExternalContext().getSessionMap().get("mailBox");
		if (messageService.saveMessage(senderMailBox,receiver,theme,messageBody)) {
			return "messageSuccessfullySavedPage";
		}
		return "sendMessageError";
	}

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
