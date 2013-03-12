package com.tsystems.javaschool.webmailsystem.managedbean;

import com.tsystems.javaschool.webmailsystem.ejb.service.FolderService;
import com.tsystems.javaschool.webmailsystem.entity.FolderEntity;
import com.tsystems.javaschool.webmailsystem.entity.MailBoxEntity;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.util.List;

/**
 *
 */
@ManagedBean
public class ListOfFolders {
	private List<FolderEntity> folders;

	@EJB
	private FolderService folderService;

	public String findFoldersFromMailBox() {
		MailBoxEntity mailBox = (MailBoxEntity) FacesContext
				.getCurrentInstance().getExternalContext().getSessionMap().get("mailBox");
		folders = folderService.getFoldersForMailBox(mailBox);
		return "listOfFoldersPage";
	}

	public List<FolderEntity> getFolders() {
		return folders;
	}

	public void setFolders(List<FolderEntity> folders) {
		this.folders = folders;
	}
}
