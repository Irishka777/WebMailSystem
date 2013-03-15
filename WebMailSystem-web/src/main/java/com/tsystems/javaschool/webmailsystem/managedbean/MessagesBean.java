package com.tsystems.javaschool.webmailsystem.managedbean;

import com.tsystems.javaschool.webmailsystem.dto.FolderDTO;
import com.tsystems.javaschool.webmailsystem.dto.MessageDTO;
import com.tsystems.javaschool.webmailsystem.ejb.service.FolderService;
import com.tsystems.javaschool.webmailsystem.ejb.service.MessageService;
import com.tsystems.javaschool.webmailsystem.entity.MailBox;
import com.tsystems.javaschool.webmailsystem.entity.Message;
import com.tsystems.javaschool.webmailsystem.exception.DataProcessingException;
import org.primefaces.event.NodeSelectEvent;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.util.List;

/**
 *
 */
@ManagedBean
@ViewScoped
public class MessagesBean {
	private List<MessageDTO> listOfMessages;

	private Message selectedMessage;

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

	public void onFolderSelect(NodeSelectEvent event) {
		FolderDTO folder = (FolderDTO) event.getTreeNode().getData();
		try {
			listOfMessages = messageService.getMessagesFromFolder(folder);
		} catch (DataProcessingException e) {
			e.getExceptionPage();
		}
	}

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

	public List<MessageDTO> getListOfMessages() {
		return listOfMessages;
	}

	public void setListOfMessages(List<MessageDTO> listOfMessages) {
		this.listOfMessages = listOfMessages;
	}

	public Message getSelectedMessage() {
		return selectedMessage;
	}

	public void setSelectedMessage(Message selectedMessage) {
		this.selectedMessage = selectedMessage;
	}

}