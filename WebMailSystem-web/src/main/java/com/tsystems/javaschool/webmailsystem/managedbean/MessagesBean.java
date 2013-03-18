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
import org.primefaces.event.SelectEvent;
import org.primefaces.model.TreeNode;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import java.util.List;

/**
 *
 */
@ManagedBean
@SessionScoped
public class MessagesBean {
	@EJB
	private MessageService messageService;

	private List<MessageDTO> listOfMessages;
	private MessageDTO[] selectedMessage;
	private String selectedMessageBody;

//	public void messageSelectedWithCheckbox(SelectEvent event) {
//		selectedMessageBody = ((MessageDTO) event.getObject()).getMessageBody();
//	}

	public void messageSelectedWithClick(SelectEvent event) {
		selectedMessageBody = ((MessageDTO) event.getObject()).getMessageBody();
	}

	public void messageCheckBoxClicked(ValueChangeEvent event) {

	}

	public String deleteMessage() {
		if (selectedMessage.length == 0) {
			return null;
		}
		try {
			messageService.deleteMessage(selectedMessage);
			for (int i = 0; i < selectedMessage.length; i++) {
				listOfMessages.remove(selectedMessage[i]);
			}
			selectedMessageBody = null;
			return null;
		} catch (DataProcessingException e) {
			return e.getExceptionPage();
		}
	}

//	private UIData listOfMessages;
//
//	public UIData getListOfMessages() {
//		return listOfMessages;
//	}
//
//	public void setListOfMessages(UIData listOfMessages) {
//		this.listOfMessages = listOfMessages;
//	}

	public List<MessageDTO> getListOfMessages() {
		return listOfMessages;
	}

	public void setListOfMessages(List<MessageDTO> listOfMessages) {
		this.listOfMessages = listOfMessages;
	}

	public MessageDTO[] getSelectedMessage() {
		return selectedMessage;
	}

	public void setSelectedMessage(MessageDTO[] selectedMessage) {
		this.selectedMessage = selectedMessage;
	}

	public String getSelectedMessageBody() {
		return selectedMessageBody;
	}

	public void setSelectedMessageBody(String selectedMessageBody) {
		this.selectedMessageBody = selectedMessageBody;
	}
}