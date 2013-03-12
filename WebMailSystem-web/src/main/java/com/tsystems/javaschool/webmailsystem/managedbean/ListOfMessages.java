package com.tsystems.javaschool.webmailsystem.managedbean;

import com.tsystems.javaschool.webmailsystem.ejb.service.FolderService;
import com.tsystems.javaschool.webmailsystem.entity.MailBoxEntity;
import com.tsystems.javaschool.webmailsystem.entity.MessageEntity;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.util.List;

/**
 *
 */
@ManagedBean
public class ListOfMessages {
	private List<MessageEntity> listOfMessages;

	@EJB
	private FolderService folderService;

	public String findMessagesFromFolder() {
		MailBoxEntity mailBox = (MailBoxEntity) FacesContext
				.getCurrentInstance().getExternalContext().getSessionMap().get("mailBox");
		listOfMessages = folderService.getMessagesFromFolder("Outbox",mailBox);
		return "listOfMessagesPage";
	}

//	public String findMessagesFromFolder(FolderEntity folder) {
//		MailBoxEntity mailBox = (MailBoxEntity) FacesContext
//				.getCurrentInstance().getExternalContext().getSessionMap().get("mailBox");
//		listOfMessages = folderService.getMessagesFromFolder(folder.getFolderName(),mailBox);
//		return "listOfMessagesPage";
//	}

//	public String dateFormater(Calendar date) {
//		SimpleDateFormat sdf = new SimpleDateFormat("d.mm.yyyy hh:mm:ss");
//		return sdf.format(date);
//	}

	public List<MessageEntity> getListOfMessages() {
		return listOfMessages;
	}

	public void setListOfMessages(List<MessageEntity> listOfMessages) {
		this.listOfMessages = listOfMessages;
	}
}