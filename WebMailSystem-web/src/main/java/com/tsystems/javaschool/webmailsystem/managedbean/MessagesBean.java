package com.tsystems.javaschool.webmailsystem.managedbean;

import com.tsystems.javaschool.webmailsystem.dto.FolderDTO;
import com.tsystems.javaschool.webmailsystem.dto.MessageDTO;
import com.tsystems.javaschool.webmailsystem.ejb.service.FolderService;
import com.tsystems.javaschool.webmailsystem.ejb.service.MessageService;
import com.tsystems.javaschool.webmailsystem.entity.MailBox;
import com.tsystems.javaschool.webmailsystem.entity.Message;
import com.tsystems.javaschool.webmailsystem.exception.DataProcessingException;
import org.primefaces.component.api.UIData;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.TreeNode;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.util.List;

/**
 *
 */
@ManagedBean
@SessionScoped
public class MessagesBean {
	@EJB
	private MessageService messageService;

//	private UIData listOfMessages;
//
//	public UIData getListOfMessages() {
//		return listOfMessages;
//	}
//
//	public void setListOfMessages(UIData listOfMessages) {
//		this.listOfMessages = listOfMessages;
//	}

	private List<MessageDTO> listOfMessages;

	public List<MessageDTO> getListOfMessages() {
		return listOfMessages;
	}

	public void setListOfMessages(List<MessageDTO> listOfMessages) {
		this.listOfMessages = listOfMessages;
	}
}