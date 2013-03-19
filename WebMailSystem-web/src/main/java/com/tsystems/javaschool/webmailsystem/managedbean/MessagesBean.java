package com.tsystems.javaschool.webmailsystem.managedbean;

import com.tsystems.javaschool.webmailsystem.dto.FolderDTO;
import com.tsystems.javaschool.webmailsystem.dto.MessageDTO;
import com.tsystems.javaschool.webmailsystem.ejb.service.MessageService;
import com.tsystems.javaschool.webmailsystem.exception.DataProcessingException;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
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

	private DefaultTreeNode endFolder;
//	private TreeNode endFolder;

	private FolderDTO endFolder1;

	public void messageFolderChanged(ValueChangeEvent even) {
		endFolder1 = (FolderDTO)((TreeNode) even.getNewValue()).getData();
	}
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
			return e.getExceptionMessage();
		}
	}

	public String moveMessage() {
		if (selectedMessage.length == 0) {
			return null;
		}
		try {
			messageService.moveMessage(selectedMessage,(FolderDTO) endFolder.getData());
			for (int i = 0; i < selectedMessage.length; i++) {
				listOfMessages.remove(selectedMessage[i]);
			}
			selectedMessageBody = null;
			return null;
		} catch (DataProcessingException e) {
			return e.getExceptionMessage();
		}
	}

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

	public DefaultTreeNode getEndFolder() {
		return endFolder;
	}

	public void setEndFolder(DefaultTreeNode endFolder) {
		this.endFolder = endFolder;
	}
}