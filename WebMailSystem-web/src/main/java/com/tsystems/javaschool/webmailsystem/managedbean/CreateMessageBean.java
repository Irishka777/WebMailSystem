package com.tsystems.javaschool.webmailsystem.managedbean;

import com.tsystems.javaschool.webmailsystem.dto.MailBoxDTO;
import com.tsystems.javaschool.webmailsystem.dto.MessageDTO;
import com.tsystems.javaschool.webmailsystem.ejb.service.MessageService;
import com.tsystems.javaschool.webmailsystem.exception.DataProcessingException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 *
 */
@ManagedBean
public class CreateMessageBean {
	private String sender;
	@NotNull(message = "You should specify the receiver email")
	@Pattern(regexp = "(([\\w[.]]*)@([a-zA-Z]*)\\.([a-zA-Z]*))", message = "The email you entered is invalid")
	@Size(max = 30, message = "Length of the email should be no more then 30 characters")
	private String receiver;

	@Size(max = 255, message = "Length of the theme should be no more then 255 characters")
	private String theme;
	private String messageBody;

	@EJB
	private MessageService messageService;

	@PostConstruct
	public void initialiseSender() {
		sender = ((MailBoxDTO) FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap().get("mailBox")).getEmail();
	}

	public String sendMessage() {
		try {
			messageService.sendMessage(new MessageDTO(sender, receiver, theme, messageBody));
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Message successfully sent"));
			return null;
//			return "messageSuccessfullySentPage";
		} catch (DataProcessingException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Error in process of sending message"));
			return null;
//			return e.getExceptionPage();
		}
	}

	public String saveMessage() {
		try {
			messageService.saveMessage(new MessageDTO(sender, receiver, theme, messageBody));
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Message successfully sent"));
			return null;
//			return "messageSuccessfullySavedPage";
		} catch (DataProcessingException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Error in process of sending message"));
			return null;
//			return e.getExceptionPage();
		}
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
