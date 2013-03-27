package com.tsystems.javaschool.webmailsystem.managedbean;

import com.tsystems.javaschool.webmailsystem.dto.FolderDTO;
import com.tsystems.javaschool.webmailsystem.dto.MessageDTO;
import com.tsystems.javaschool.webmailsystem.ejb.service.MessageService;
import com.tsystems.javaschool.webmailsystem.exception.DataProcessingException;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleSelectEvent;
import org.primefaces.event.UnselectEvent;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
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

	private List<MessageDTO> listOfMessages;
	private MessageDTO[] selectedMessages2;
	private MessageDTO selectedMessage;

	private FolderDTO endFolder;

	public void showButtonClicked() {
//		selectedMessage =
	}
	public void messageSelectedWithClick(SelectEvent event) {
		selectedMessage = (MessageDTO) event.getObject();
	}

	public void messageUnselectedWithClick(UnselectEvent event) {
		selectedMessage = null;
	}

	public void messageSelectedWithCheckBox(SelectEvent event) {
		selectedMessage = (MessageDTO) event.getObject();
	}

	public void messageUnselectedWithCheckBox(UnselectEvent event) {
		selectedMessage = null;
	}

	public void messagesToggleSelect(ToggleSelectEvent event) {
		selectedMessage = null;
	}

	private void resetSelectedMessages() {
		selectedMessage = null;
		selectedMessages2 = null;
	}

	public String deleteMessage() {
		if (selectedMessages2.length == 0) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"You have not selected messages for deletion"));
			return null;
		}

		try {
			messageService.deleteMessage(selectedMessages2);
			for (MessageDTO message : selectedMessages2) {
				listOfMessages.remove(message);
			}
			resetSelectedMessages();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Information","Messages successfully deleted"));
			return null;
		} catch (DataProcessingException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",e.getExceptionMessage()));
			return null;
		}
	}

	public String moveMessage() {
		if (selectedMessages2.length == 0) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"You have not selected messages to move"));
			return null;
		}

		if (selectedMessages2[0].getFolder() == endFolder.getId()) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"You have not changed selected messages folder"));
			return null;
		}

		try {
			messageService.moveMessage(selectedMessages2, endFolder);
			for (MessageDTO message : selectedMessages2) {
				listOfMessages.remove(message);
			}
			resetSelectedMessages();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Information","Messages successfully moved to another folder"));
			return null;
		} catch (DataProcessingException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",e.getExceptionMessage()));
			return null;
		}
	}

	public void receiveMessagesFromFolder(FolderDTO folder) {
		resetSelectedMessages();
		try {
			listOfMessages = messageService.getMessagesFromFolder(folder);
		} catch (DataProcessingException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getExceptionMessage()));
		}
	}

	public void receiveNewMessages(FolderDTO folder) {
		try {
			List<MessageDTO> newMessages = messageService.receiveNewMessages(folder);
			if (newMessages != null) {
				listOfMessages.addAll(newMessages);
			}
		} catch (DataProcessingException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getExceptionMessage()));
		}
	}

	public List<MessageDTO> getListOfMessages() {
		return listOfMessages;
	}

	public void setListOfMessages(List<MessageDTO> listOfMessages) {
		this.listOfMessages = listOfMessages;
	}

	public MessageDTO[] getSelectedMessages2() {
		return selectedMessages2;
	}

	public void setSelectedMessages2(MessageDTO[] selectedMessages2) {
		this.selectedMessages2 = selectedMessages2;
	}

	public MessageDTO getSelectedMessage() {
		return selectedMessage;
	}

	public void setSelectedMessage(MessageDTO selectedMessage) {
		this.selectedMessage = selectedMessage;
	}

	public FolderDTO getEndFolder() {
		return endFolder;
	}

	public void setEndFolder(FolderDTO endFolder) {
		this.endFolder = endFolder;
	}
}