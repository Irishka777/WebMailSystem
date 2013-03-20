package com.tsystems.javaschool.webmailsystem.managedbean;

import com.tsystems.javaschool.webmailsystem.dto.FolderDTO;
import com.tsystems.javaschool.webmailsystem.dto.MessageDTO;
import com.tsystems.javaschool.webmailsystem.ejb.service.MessageService;
import com.tsystems.javaschool.webmailsystem.exception.DataProcessingException;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleSelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.TreeNode;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import java.util.ArrayList;
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
	private List<MessageDTO> selectedMessages1 = new ArrayList<MessageDTO>();
	private MessageDTO selectedMessage;

	private FolderDTO selectedFolder;
	private FolderDTO endFolder;

	private FolderDTO endFolder1;

	public void messageFolderChanged(ValueChangeEvent even) {
		endFolder1 = (FolderDTO)((TreeNode) even.getNewValue()).getData();
	}
	public void messageSelectedWithClick(SelectEvent event) {
		selectedMessage = (MessageDTO) event.getObject();
		selectedMessages1.clear();
		selectedMessages1.add(selectedMessage);
	}

	public void messageUnselectedWithClick(UnselectEvent event) {
		selectedMessages1.remove((MessageDTO) event.getObject());
		selectedMessage = null;
	}

	public void messageSelectedWithCheckBox(SelectEvent event) {
		selectedMessage = (MessageDTO) event.getObject();
		selectedMessages1.add(selectedMessage);
	}

	public void messageUnselectedWithCheckBox(UnselectEvent event) {
		selectedMessages1.remove((MessageDTO) event.getObject());
		selectedMessage = null;
	}

	public void messagesToggleSelect(ToggleSelectEvent event) {
		selectedMessage = null;
		selectedMessages1.clear();
		if (event.isSelected()) {
			for (int i = 0; i < listOfMessages.size(); i++) {
				selectedMessages1.add(listOfMessages.get(i));
			}
		}
	}

	public void resetSelectedMessages() {
		selectedMessage = null;
		selectedMessages1.clear();
		selectedMessages2 = null;
	}

	public String deleteMessage() {
		if (selectedMessages1.size() == 0) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
							"You have not selected messages for deletion"));
			return null;
		}
		try {
			messageService.deleteMessage(selectedMessages1,selectedFolder.getId());
			while (selectedMessages1.size() != 0) {
				listOfMessages.remove(selectedMessages1.get(0));
				selectedMessages1.remove(0);
			}
			resetSelectedMessages();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Information","Messages successfully deleted"));
			return null;
		} catch (DataProcessingException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
							e.getExceptionMessage()));
			return null;
		}
	}

	public String moveMessage() {
		if (selectedMessages1.size() == 0) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
							"You have not selected messages to move"));
			return null;
		}
		if (selectedMessages1.get(0).getFolder().equals(endFolder.getFolderName())) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
							"You have not changed messages folder"));
			return null;
		}
		try {
			messageService.moveMessage(selectedMessages1,endFolder.getId(),selectedFolder.getId());
			while (selectedMessages1.size() != 0) {
				listOfMessages.remove(selectedMessages1.get(0));
				selectedMessages1.remove(0);
			}
			resetSelectedMessages();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							"Information","Messages successfully moved to another folder"));
			return null;
		} catch (DataProcessingException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
							e.getExceptionMessage()));
			return null;
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

	public List<MessageDTO> getSelectedMessages1() {
		return selectedMessages1;
	}

	public void setSelectedMessages1(List<MessageDTO> selectedMessages1) {
		this.selectedMessages1 = selectedMessages1;
	}

	public MessageDTO getSelectedMessage() {
		return selectedMessage;
	}

	public void setSelectedMessage(MessageDTO selectedMessage) {
		this.selectedMessage = selectedMessage;
	}

	public FolderDTO getSelectedFolder() {
		return selectedFolder;
	}

	public void setSelectedFolder(FolderDTO selectedFolder) {
		this.selectedFolder = selectedFolder;
	}

	public FolderDTO getEndFolder() {
		return endFolder;
	}

	public void setEndFolder(FolderDTO endFolder) {
		this.endFolder = endFolder;
	}
}