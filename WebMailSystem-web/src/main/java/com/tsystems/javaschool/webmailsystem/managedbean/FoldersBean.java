package com.tsystems.javaschool.webmailsystem.managedbean;

import com.tsystems.javaschool.webmailsystem.dto.FolderDTO;
import com.tsystems.javaschool.webmailsystem.dto.MailBoxDTO;
import com.tsystems.javaschool.webmailsystem.ejb.service.FolderService;
import com.tsystems.javaschool.webmailsystem.exception.DataProcessingException;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

/**
 *
 */
@ManagedBean
@SessionScoped
public class FoldersBean {
	@EJB
	private FolderService folderService;

	@ManagedProperty(value = "#{messagesBean}")
	private MessagesBean messagesBean;

	public void setMessagesBean(MessagesBean messagesBean) {
		this.messagesBean = messagesBean;
	}

	private DefaultTreeNode root;
	private MailBoxDTO mailBox = (MailBoxDTO) FacesContext
			.getCurrentInstance().getExternalContext().getSessionMap().get("mailBox");

	private DefaultTreeNode selectedFolder;

	@NotNull(message = "You have not specified the folder name")
	@Pattern(regexp = "[a-zA-Z][a-zA-Z0-9]*",
			message = "Folder name should start with a latter and contain just letters and digits")
	@Size(max = 30, message = "Length of the folder name more then 30 characters")
	private String newFolderName;

	@NotNull(message = "You have not specified the folder name")
	@Pattern(regexp = "[a-zA-Z][a-zA-Z0-9]*",
			message = "Folder name should start with a latter and contain just letters and digits")
	@Size(max = 30, message = "Length of the folder name more then 30 characters")
	private String selectedFolderName;

	@PostConstruct
	public void initialiseFoldersTree() {
		try {
			root = new DefaultTreeNode(mailBox,null);
			List<FolderDTO> folders = folderService.getFoldersForMailBox(mailBox);
			for (FolderDTO folder : folders) {
				DefaultTreeNode node = new DefaultTreeNode(folder, root);
				if (folder.getFolderName().equals("Inbox")) {
					selectedFolder = node;
					selectedFolderName = ((FolderDTO) selectedFolder.getData()).getFolderName();
					node.setSelected(true);
					try {
						messagesBean.setListOfMessages(folderService.getMessagesFromFolder(folder));
						messagesBean.setSelectedFolder(folder);
//						RequestContext.getCurrentInstance().update(":messagesForm:messages");
					} catch (DataProcessingException e) {
						FacesContext.getCurrentInstance().addMessage(null,
								new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getExceptionMessage()));
					}
				}
			}
		} catch (DataProcessingException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getExceptionMessage()));
		}
	}

	public void onFolderSelect(NodeSelectEvent event) {
		selectedFolder.setSelected(false);
		selectedFolder = (DefaultTreeNode) event.getTreeNode();
		selectedFolder.setSelected(true);
		selectedFolderName = ((FolderDTO) selectedFolder.getData()).getFolderName();
		FolderDTO folder = (FolderDTO) selectedFolder.getData();

		try {
			messagesBean.resetSelectedMessages();
			messagesBean.setListOfMessages(folderService.getMessagesFromFolder(folder));
			messagesBean.setSelectedFolder(folder);
		} catch (DataProcessingException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getExceptionMessage()));
		}
	}

	public String createFolder() {
		try {
			FolderDTO folder = folderService.createFolder(new FolderDTO(newFolderName), mailBox);

			new DefaultTreeNode(folder, root);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Information", "Folder successfully created"));
		} catch (DataProcessingException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getExceptionMessage()));
		}
		newFolderName = null;
		return null;
	}

	public String deleteFolder() {
		if (selectedFolder == null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
							"You have not selected a folder for deletion"));
			return null;
		}

		String folderName = ((FolderDTO) selectedFolder.getData()).getFolderName();

		if (folderName.equals("Inbox")
				|| folderName.equals("Outbox")
				|| folderName.equals("Draft")) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "You can not delete system folders"));
			return null;
		}
		try {
			folderService.deleteFolder((FolderDTO) selectedFolder.getData());
			root.getChildren().remove(selectedFolder);

			for (int i = 0; i < root.getChildCount(); i++) {
				if (((FolderDTO)root.getChildren().get(i).getData()).getFolderName().equals("Inbox")) {
					selectedFolder = (DefaultTreeNode) root.getChildren().get(i);
					selectedFolder.setSelected(true);
					selectedFolderName = ((FolderDTO) selectedFolder.getData()).getFolderName();
					try {
						messagesBean.resetSelectedMessages();
						messagesBean.setListOfMessages(folderService.getMessagesFromFolder
								((FolderDTO)root.getChildren().get(i).getData()));
						messagesBean.setSelectedFolder((FolderDTO) selectedFolder.getData());
					} catch (DataProcessingException e) {
						FacesContext.getCurrentInstance().addMessage(null,
								new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getExceptionMessage()));
					}
					break;
				}
			}

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Information","Folder successfully deleted"));

			return null;
		} catch (DataProcessingException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getExceptionMessage()));
			return null;
		}
	}

	public String renameFolder() {
		if (selectedFolder == null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
							"You have not selected a folder for renaming"));
			return null;
		}

		FolderDTO folder = (FolderDTO) selectedFolder.getData();

		if (folder.getFolderName().equals(selectedFolderName)) {
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
							"You have not specified a new name for a folder"));
			return null;
		}
		if (folder.getFolderName().equals("Inbox")
				|| folder.getFolderName().equals("Outbox")
				|| folder.getFolderName().equals("Draft")) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
					"You can not rename system folders"));
			return null;
		}

		try {
			folderService.renameFolder(folder, selectedFolderName);
			folder.setFolderName(selectedFolderName);
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,"Information",
							"Folder successfully renamed"));
			return null;
		} catch (DataProcessingException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getExceptionMessage()));
			return null;
		}
	}

	public DefaultTreeNode getRoot() {
		return root;
	}

	public TreeNode getSelectedFolder() {
		return selectedFolder;
	}

	public void setSelectedFolder(DefaultTreeNode selectedFolder) {
		this.selectedFolder = selectedFolder;
	}

	public String getNewFolderName() {
		return newFolderName;
	}

	public void setNewFolderName(String newFolderName) {
		this.newFolderName = newFolderName;
	}

	public String getSelectedFolderName() {
		return selectedFolderName;
	}

	public void setSelectedFolderName(String selectedFolderName) {
		this.selectedFolderName = selectedFolderName;
	}
}