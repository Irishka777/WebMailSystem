package com.tsystems.javaschool.webmailsystem.managedbean;

import com.tsystems.javaschool.webmailsystem.dto.FolderDTO;
import com.tsystems.javaschool.webmailsystem.dto.MailBoxDTO;
import com.tsystems.javaschool.webmailsystem.ejb.service.FolderService;
import com.tsystems.javaschool.webmailsystem.exception.DataProcessingException;
import org.primefaces.component.api.UITree;
import org.primefaces.component.tree.Tree;
import org.primefaces.context.RequestContext;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.validation.ValidationException;
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

	private UITree tree;
	private TreeNode root;
	private MailBoxDTO mailBox = (MailBoxDTO) FacesContext
			.getCurrentInstance().getExternalContext().getSessionMap().get("mailBox");

	private TreeNode selectedFolder;

	@NotNull(message = "You should specify the folder name")
	@Pattern(regexp = "[a-zA-Z][a-zA-Z0-9]*",
			message = "Folder name should start with a latter and contain just letters and digits")
	@Size(max = 30, message = "Length of the folder name should be no more then 30 characters")
	private String newFolderName;

	@NotNull(message = "You should specify the folder name")
	@Pattern(regexp = "[a-zA-Z][a-zA-Z0-9]*",
			message = "Folder name should start with a latter and contain just letters and digits")
	@Size(max = 30, message = "Length of the folder name should be no more then 30 characters")
	private String selectedFolderName;

	@PostConstruct
	public void initialiseFoldersTree() {
		try {
			root = new DefaultTreeNode(mailBox,null);
			tree = new Tree();
			tree.setValue(root);
			List<FolderDTO> folders = folderService.getFoldersForMailBox(mailBox);
			for (FolderDTO folder : folders) {
				TreeNode node = new DefaultTreeNode(folder, root);
//				if (folder.getFolderName() == "Inbox") {
//					node.setSelected(true);
//				}
			}
		} catch (DataProcessingException e) {
			e.getExceptionPage();
		}
	}

	public void onFolderSelect(NodeSelectEvent event) {
		selectedFolder = event.getTreeNode();
		FolderDTO folder = (FolderDTO) event.getTreeNode().getData();
		selectedFolderName = folder.getFolderName();
		try {
			messagesBean.setListOfMessages(folderService.getMessagesFromFolder(folder));
		} catch (DataProcessingException e) {
			e.getExceptionPage();
		}
	}

	public String createFolder() {
		try {
			if (newFolderName == null) {
				return null;
			}
			List<TreeNode> list = root.getChildren();
			for (int i = 0; i < list.size(); i++) {
				if (((FolderDTO) list.get(i).getData()).getFolderName().equals(newFolderName)) {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage("Folder with such a name already exists"));
					return null;
				}
			}
			FolderDTO folder = folderService.createFolder(new FolderDTO(newFolderName), mailBox);
			new DefaultTreeNode(folder, root);
			newFolderName = null;
			return null;
		} catch (DataProcessingException e) {
			return e.getExceptionPage();
		}
	}

	public String deleteFolder() {
		if (selectedFolder == null) {
			return null;
		}
		try {
			folderService.deleteFolder((FolderDTO) selectedFolder.getData());
			root.getChildren().remove(selectedFolder);
			return null;
		} catch (DataProcessingException e) {
			return e.getExceptionPage();
		}
	}

	public String renameFolder() {
		if (selectedFolder == null) {
			return null;
		}
		if (((FolderDTO) selectedFolder.getData()).getFolderName().equals(selectedFolderName)) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("You should specify a new name for selected folder"));
			return null;
		}
		List<TreeNode> list = root.getChildren();
		for (int i = 0; i < list.size(); i++) {
			if (((FolderDTO) list.get(i).getData()).getFolderName().equals(selectedFolderName)) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage("Folder with such a name already exists"));
				return null;
			}
		}
		try {
			FolderDTO folder = (FolderDTO) selectedFolder.getData();
			folder.setFolderName(selectedFolderName);
			folder = folderService.renameFolder(folder);
			root.getChildren().remove(selectedFolder);
			selectedFolder = new DefaultTreeNode(folder, root);
			return null;
		} catch (DataProcessingException e) {
			return e.getExceptionPage();
		}
	}


	public void deleteFolderAction() {
		if (selectedFolder == null) {
			return;
		}
		try {
			root.getChildren().remove(selectedFolder);
			folderService.deleteFolder((FolderDTO) selectedFolder.getData());
		} catch (DataProcessingException e) {
			e.getExceptionPage();
		}
	}


//	public void findFoldersFromMailBox() {
//		try {
//			root = new DefaultTreeNode(mailBox,null);
//			folders = folderService.getFoldersForMailBox(mailBox);
//			for (FolderDTO folder : folders) {
//				new DefaultTreeNode(folder, root);
//			}
//			return "foldersAndMessages";
//		} catch (DataProcessingException e) {
//			e.getExceptionPage();
//		}
//	}

	public UITree getTree() {
		return tree;
	}

	public void setTree(UITree tree) {
		this.tree = tree;
	}
	public TreeNode getRoot() {
		return root;
	}

	public TreeNode getSelectedFolder() {
		return selectedFolder;
	}

	public void setSelectedFolder(TreeNode selectedFolder) {
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
