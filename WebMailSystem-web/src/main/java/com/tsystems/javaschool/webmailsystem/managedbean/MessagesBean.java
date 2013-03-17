package com.tsystems.javaschool.webmailsystem.managedbean;

import com.tsystems.javaschool.webmailsystem.dto.FolderDTO;
import com.tsystems.javaschool.webmailsystem.dto.MessageDTO;
import com.tsystems.javaschool.webmailsystem.ejb.service.FolderService;
import com.tsystems.javaschool.webmailsystem.ejb.service.MessageService;
import com.tsystems.javaschool.webmailsystem.entity.MailBox;
import com.tsystems.javaschool.webmailsystem.entity.Message;
import com.tsystems.javaschool.webmailsystem.exception.DataProcessingException;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.TreeNode;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import java.util.List;

/**
 *
 */
@ManagedBean
@SessionScoped
public class MessagesBean {
//	private List<MessageDTO> listOfMessages;
	private UIData listOfMessages;
	private UIData selectedMessages;

//	private Message selectedMessage;

//	private TreeNode selectedFolder;

	@EJB
	private MessageService messageService;

//	public void inicialiseMessagesTable() {
//
//	}

//	public void findMessagesFromFolder() {
//		try {
//
//			listOfMessages = messageService.getMessagesFromFolder(folder);
////			return "messagesPage";
//		} catch (DataProcessingException e) {
//			e.getExceptionPage();
//		}
//	}
//	public void findMessagesFromFolder(FolderDTO folder) {
//		try {
//			listOfMessages = messageService.getMessagesFromFolder(folder);
////			return "messagesPage";
//		} catch (DataProcessingException e) {
//			e.getExceptionPage();
//		}
//	}

//	public String showMessagesFromFolder() {
//		try {
//			listOfMessages.setValue(messageService.getMessagesFromFolder((FolderDTO) selectedFolder.getData()));
////			listOfMessages = messageService.getMessagesFromFolder(folder);
//			return null;
//		} catch (DataProcessingException e) {
//			return e.getExceptionPage();
//		}
//	}

	public void onFolderSelect(NodeSelectEvent event) {
		FolderDTO folder = (FolderDTO) event.getTreeNode().getData();
		try {
			listOfMessages.setValue(messageService.getMessagesFromFolder(folder));
			listOfMessages.processUpdates(FacesContext.getCurrentInstance());
//			FacesContext.getCurrentInstance().renderResponse();
//			listOfMessages = messageService.getMessagesFromFolder(folder);
//			return null;
		} catch (DataProcessingException e) {
			e.getExceptionPage();
		}
	}

//	public String deleteMessages() {
//		try {
//			for (int i = 0; i < selectedMessages.; i++){}
//			messageService.deleteMessage();
//		} catch (DataProcessingException e) {
//			return e.getExceptionPage();
//		}
//	}

//	public String findMessagesFromFolder(Folder folder) {
//		MailBox mailBox = (MailBox) FacesContext
//				.getCurrentInstance().getExternalContext().getSessionMap().get("mailBox");
//		listOfMessages = folderService.getMessagesFromFolder(folder.getFolderName(),mailBox);
//		return "listOfMessagesPage";
//	}

//	public String dateFormater(Calendar date) {
//		SimpleDateFormat sdf = new SimpleDateFormat("d.mm.yyyy hh:mm:ss");
//		return sdf.format(date);
//	}

//	public List<MessageDTO> getListOfMessages() {
//		return listOfMessages;
//	}
//
//	public void setListOfMessages(List<MessageDTO> listOfMessages) {
//		this.listOfMessages = listOfMessages;
//	}

	public UIData getListOfMessages() {
		return listOfMessages;
	}

	public void setListOfMessages(UIData listOfMessages) {
		this.listOfMessages = listOfMessages;
	}

//	public Message getSelectedMessage() {
//		return selectedMessage;
//	}
//
//	public void setSelectedMessage(Message selectedMessage) {
//		this.selectedMessage = selectedMessage;
//	}

//	public TreeNode getSelectedFolder() {
//		return selectedFolder;
//	}
//
//	public void setSelectedFolder(TreeNode selectedFolder) {
//		this.selectedFolder = selectedFolder;
//	}

}