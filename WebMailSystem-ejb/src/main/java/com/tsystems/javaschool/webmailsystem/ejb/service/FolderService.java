package com.tsystems.javaschool.webmailsystem.ejb.service;

import com.tsystems.javaschool.webmailsystem.dto.FolderDTO;
import com.tsystems.javaschool.webmailsystem.dto.MailBoxDTO;
import com.tsystems.javaschool.webmailsystem.dto.MessageDTO;
import com.tsystems.javaschool.webmailsystem.ejb.dao.FolderDAO;
import com.tsystems.javaschool.webmailsystem.entity.Folder;
import com.tsystems.javaschool.webmailsystem.entity.MailBox;
import com.tsystems.javaschool.webmailsystem.entity.Message;
import com.tsystems.javaschool.webmailsystem.exception.DataProcessingException;
import com.tsystems.javaschool.webmailsystem.exception.ExceptionType;
import org.apache.log4j.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class FolderService {

	@EJB
	private FolderDAO folderDAO;

	private Logger logger = Logger.getLogger(FolderService.class);
	
	public boolean createFolder(Object folder) throws DataProcessingException {
		try {
			folderDAO.create((Folder) folder);
			logger.info("Folder " + ((Folder) folder).getFolderName() + " successfully created");
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataProcessingException(ExceptionType.UnexpectedException,e.getCause());
		}
	}
	
	public boolean deleteFolder(Object folder) throws DataProcessingException {
		try {
			folderDAO.delete((Folder) folder);
			logger.info("Folder " + ((Folder) folder).getFolderName() + " successfully deleted");
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataProcessingException(ExceptionType.UnexpectedException,e.getCause());
		}
	}
	
	public boolean renameFolder(Object folder) throws DataProcessingException {
		try {
			folderDAO.update((Folder) folder);
			logger.info("Folder " + ((Folder) folder).getFolderName() + " successfully renamed");
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataProcessingException(ExceptionType.UnexpectedException,e.getCause());
		}
	}
	
	public boolean moveMessageToAnotherFolder(Object folder) throws DataProcessingException {
		try {
			folderDAO.update((Folder) folder);
			logger.info("Message successfully moved into folder " + ((Folder) folder).getFolderName());
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataProcessingException(ExceptionType.UnexpectedException,e.getCause());
		}
	}
	
	public Folder getFolder(Object folder) throws DataProcessingException {
		try {
			return folderDAO.getFolder((Folder)folder);
		} catch (NoResultException e) {
			logger.warn("Folder with such id does not exist", e);
			throw new DataProcessingException(ExceptionType.NoSuchFolderException);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataProcessingException(ExceptionType.UnexpectedException,e.getCause());
		}
	}
	
	public List<FolderDTO> getFoldersForMailBox(MailBoxDTO mailBoxDTO) throws DataProcessingException {
		try {
			List<Folder> listOfFolders = folderDAO.getFoldersForMailBox(mailBoxDTO.getEmail());
			List<FolderDTO> listOfFolderDTO= new ArrayList<FolderDTO>();
			for (Folder folder : listOfFolders) {
				listOfFolderDTO.add(folder.getFolderDTO());
			}
			return listOfFolderDTO;
		} catch (NoResultException e) {
			logger.warn("Mail box with email " + mailBoxDTO.getEmail() + " does not exist", e);
			throw new DataProcessingException(ExceptionType.NoSuchFolderException);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataProcessingException(ExceptionType.UnexpectedException,e.getCause());
		}
	}

	public List<MessageDTO> getMessagesFromFolder(String folderName, String email) throws DataProcessingException {
		try {
			List<Message> listOfMessages = folderDAO.getMessagesFromFolder(folderName, email);
			List<MessageDTO> listOfMessagesDTO= new ArrayList<MessageDTO>();
			for (Message message : listOfMessages) {
				listOfMessagesDTO.add(message.getMessageDTO());
			}
			return listOfMessagesDTO;
//			Folder folder = folderDAO.findFolderByFolderNameAndEmail(folderName,mailBox);
//			return folder.getListOfMessages();
		} catch (NoResultException e) {
			logger.warn("Folder with name " + folderName + " does not exist in " + email, e);
			throw new DataProcessingException(ExceptionType.NoSuchFolderException);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataProcessingException(ExceptionType.UnexpectedException,e.getCause());
		}
	}
}