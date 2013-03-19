package com.tsystems.javaschool.webmailsystem.managedbean;

import com.tsystems.javaschool.webmailsystem.dto.FolderDTO;
import com.tsystems.javaschool.webmailsystem.dto.MessageDTO;
import com.tsystems.javaschool.webmailsystem.ejb.service.MessageService;
import com.tsystems.javaschool.webmailsystem.exception.DataProcessingException;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import java.util.List;
import java.util.Map;

/**
 *
 */
@ManagedBean
@SessionScoped
public class MessagesBean {
	@EJB
	private MessageService messageService;

	private List<MessageDTO> listOfMessages;
	private MessageDTO[] selectedMessages;
	private MessageDTO selectedMessage;
	private String selectedMessageBody;

	private DefaultTreeNode endFolder;
//	private TreeNode endFolder;

	private FolderDTO endFolder1;

	public void messageFolderChanged(ValueChangeEvent even) {
		endFolder1 = (FolderDTO)((TreeNode) even.getNewValue()).getData();
	}
	public void messageSelectedWithClick(SelectEvent event) {
		selectedMessage = (MessageDTO) event.getObject();
		selectedMessageBody = ((MessageDTO) event.getObject()).getMessageBody();
	}

	public String deleteMessage() {
		if (selectedMessages.length == 0) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
							"You have not selected messages for deletion"));
			return null;
		}
		try {
			messageService.deleteMessage(selectedMessages);
			for (int i = 0; i < selectedMessages.length; i++) {
				listOfMessages.remove(selectedMessages[i]);
			}
			selectedMessage = null;
			selectedMessageBody = null;
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Messages successfully deleted"));
			return null;
		} catch (DataProcessingException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
							e.getExceptionMessage()));
			return null;
		}
	}

	public String moveMessage() {
		if (selectedMessages.length == 0) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
							"You have not selected messages to move"));
			return null;
		}
		if (selectedMessages[0].getFolder().equals(((FolderDTO) endFolder.getData()).getFolderName())) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
							"You have not changed messages folder"));
			return null;
		}
		try {
			messageService.moveMessage(selectedMessages,(FolderDTO) endFolder.getData());
			for (int i = 0; i < selectedMessages.length; i++) {
				listOfMessages.remove(selectedMessages[i]);
			}
			selectedMessage = null;
			selectedMessageBody = null;
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Messages successfully moved to another folder"));
			return null;
		} catch (DataProcessingException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
							e.getExceptionMessage()));
			return null;
		}
	}

//	public String resendMessage() {
//		FacesContext.getCurrentInstance()
//				.getExternalContext()
//		Map<String,String> requestParams = FacesContext.getCurrentInstance()
//				.getExternalContext().getRequestParameterMap();
//
//		receiver = requestParams.get("showmessage:receiver");
//		theme = requestParams.get("showmessage:theme");
//		messageBody = requestParams.get("showmessage:messagebody");
//		return "createMessagePage?faces-redirect=true";
//	}

	public List<MessageDTO> getListOfMessages() {
		return listOfMessages;
	}

	public void setListOfMessages(List<MessageDTO> listOfMessages) {
		this.listOfMessages = listOfMessages;
	}

	public MessageDTO[] getSelectedMessages() {
		return selectedMessages;
	}

	public void setSelectedMessages(MessageDTO[] selectedMessages) {
		this.selectedMessages = selectedMessages;
	}

	public MessageDTO getSelectedMessage() {
		return selectedMessage;
	}

	public void setSelectedMessage(MessageDTO selectedMessage) {
		this.selectedMessage = selectedMessage;
	}

	public String getSelectedMessageBody() {
		return selectedMessageBody;
	}

	public void setSelectedMessageBody(String selectedMessageBody) {
		this.selectedMessageBody = selectedMessageBody;
	}

	public DefaultTreeNode getEndFolder() {
		return endFolder;
	}

	public void setEndFolder(DefaultTreeNode endFolder) {
		this.endFolder = endFolder;
	}
}