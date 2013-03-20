package com.tsystems.javaschool.webmailsystem.managedbean;

import com.tsystems.javaschool.webmailsystem.dto.MailBoxDTO;
import com.tsystems.javaschool.webmailsystem.dto.MessageDTO;
import com.tsystems.javaschool.webmailsystem.ejb.service.MessageService;
import com.tsystems.javaschool.webmailsystem.exception.DataProcessingException;
import org.primefaces.context.RequestContext;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Map;

/**
 *
 */
@ManagedBean
@SessionScoped
public class CreateMessageBean {
	@EJB
	private MessageService messageService;

	private String sender;

//	@NotNull(message = "You have not specified the receiver email")
//	@Pattern(regexp = "(([\\w[.]]*)@([a-zA-Z]*)\\.([a-zA-Z]*))", message = "The email you entered is invalid")
	@Size(max = 30, message = "Length of the email more then 30 characters")
	private String receiver;

	@Size(max = 255, message = "Length of the theme more then 255 characters")
	private String theme;

	private String messageBody;

	@PostConstruct
	public void initialiseSender() {
		sender = ((MailBoxDTO) FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap().get("mailBox")).getEmail();

		Map<String,String> requestParams = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap();

		receiver = requestParams.get("showmessage:receiver");
		theme = requestParams.get("showmessage:theme");
		messageBody = requestParams.get("showmessage:messagebody");
	}

	public String sendMessage() {
		try {
			messageService.sendMessage(new MessageDTO(sender, receiver, theme, messageBody));
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Information", "Message successfully sent"));
			receiver = null;
			theme = null;
			messageBody = null;
			return "foldersAndMessagesPage?faces-redirect=true";
		} catch (DataProcessingException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getExceptionMessage()));
			return null;
		}
	}

	public String saveMessage() {
		try {
			messageService.saveMessage(new MessageDTO(sender, receiver, theme, messageBody));
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Information", "Message successfully saved"));
			receiver = null;
			theme = null;
			messageBody = null;
			return "foldersAndMessagesPage?faces-redirect=true";
		} catch (DataProcessingException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getExceptionMessage()));
			return null;
		}
	}

	public String resendMessage() {
		return "createMessagePage?faces-redirect=true";
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
