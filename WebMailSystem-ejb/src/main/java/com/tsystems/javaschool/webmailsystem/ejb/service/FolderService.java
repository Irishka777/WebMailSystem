package com.tsystems.javaschool.webmailsystem.ejb.service;

import com.tsystems.javaschool.webmailsystem.ejb.dao.FolderDAO;
import com.tsystems.javaschool.webmailsystem.entity.FolderEntity;
import com.tsystems.javaschool.webmailsystem.entity.MailBoxEntity;
import org.apache.log4j.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import java.util.List;

@Stateless
public class FolderService {

	@EJB
	private FolderDAO folderDAO;

	private Logger logger = Logger.getLogger(FolderService.class);
	
	public boolean createFolder(Object folder) {
		try {
			folderDAO.insert((FolderEntity) folder);
			logger.info("Folder " + ((FolderEntity) folder).getFolderName() + " successfully created");
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}
	
	public boolean deleteFolder(Object folder) {
		try {
			folderDAO.delete((FolderEntity) folder);
			logger.info("Folder " + ((FolderEntity) folder).getFolderName() + " successfully deleted");
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}
	
	public boolean renameFolder(Object folder) {
		try {
			folderDAO.update((FolderEntity) folder);
			logger.info("Folder " + ((FolderEntity) folder).getFolderName() + " successfully renamed");
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}
	
	public boolean moveMessageToAnotherFolder(Object folder) {
		try {
			folderDAO.update((FolderEntity) folder);
			logger.info("Message successfully moved into folder " + ((FolderEntity) folder).getFolderName());
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}
	
	public FolderEntity getFolder(Object folder) {
		try {
			return folderDAO.getFolder((FolderEntity)folder);
		} catch (NoResultException e) {
			logger.warn("Folder with such id does not exist", e);
			return null;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}
	
	public List<FolderEntity> findFoldersForMailBox(Object mailBox) {
		try {
			return folderDAO.findFoldersForMailBox((MailBoxEntity)mailBox);
		} catch (NoResultException e) {
			logger.warn("Mail box with such email address does not exist", e);
			return null;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}
}