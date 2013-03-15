package com.tsystems.javaschool.webmailsystem.managedbean;

import com.tsystems.javaschool.webmailsystem.dto.FolderDTO;
import com.tsystems.javaschool.webmailsystem.dto.MailBoxDTO;
import com.tsystems.javaschool.webmailsystem.ejb.service.FolderService;
import com.tsystems.javaschool.webmailsystem.exception.DataProcessingException;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.util.List;

/**
 *
 */
@ManagedBean
@SessionScoped
public class FoldersBean {
	private TreeNode root;
	private MailBoxDTO mailBox = (MailBoxDTO) FacesContext
			.getCurrentInstance().getExternalContext().getSessionMap().get("mailBox");
	private List<FolderDTO> folders;

	private TreeNode selectedFolder;

	@EJB
	private FolderService folderService;

	@PostConstruct
	public void initialiseFoldersTree() {
		try {
			root = new DefaultTreeNode(mailBox,null);
			folders = folderService.getFoldersForMailBox(mailBox);
			for (FolderDTO folder : folders) {
				new DefaultTreeNode(folder, root);
			}
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

	public TreeNode getRoot() {
		return root;
	}

	public TreeNode getSelectedFolder() {
		return selectedFolder;
	}

	public void setSelectedFolder(TreeNode selectedFolder) {
		this.selectedFolder = selectedFolder;
	}
}
