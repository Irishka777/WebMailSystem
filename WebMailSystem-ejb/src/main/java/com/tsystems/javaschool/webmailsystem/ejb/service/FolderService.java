package com.tsystems.javaschool.webmailsystem.ejb.service;

import com.tsystems.javaschool.webmailsystem.dto.FolderDTO;
import com.tsystems.javaschool.webmailsystem.dto.MailBoxDTO;
import com.tsystems.javaschool.webmailsystem.dto.MessageDTO;
import com.tsystems.javaschool.webmailsystem.ejb.dao.FolderDAO;
import com.tsystems.javaschool.webmailsystem.ejb.dao.MailBoxDAO;
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

	@EJB
	private MailBoxDAO mailBoxDAO;

	private Logger logger = Logger.getLogger(FolderService.class);

	public List<MessageDTO> getMessagesFromFolder(FolderDTO folder) throws DataProcessingException {
		try {
			List<Message> listOfMessages = folderDAO.getMessagesFromFolder(folder.getId());
			List<MessageDTO> listOfMessagesDTO = new ArrayList<MessageDTO>();
			for (Message message : listOfMessages) {
				listOfMessagesDTO.add(message.getMessageDTO());
			}
			return listOfMessagesDTO;
		} catch (NoResultException e) {
			logger.warn("Folder with name " + folder.getFolderName() + " does not exist", e);
			throw new DataProcessingException(ExceptionType.NoSuchFolderException);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataProcessingException(ExceptionType.unexpectedException,e.getCause());
		}
	}

	public FolderDTO createFolder(FolderDTO folderDTO, MailBoxDTO mailBoxDTO) throws DataProcessingException {
		try {
			MailBox mailBox = mailBoxDAO.find(mailBoxDTO.getEmail());
			folderDAO.create(new Folder(folderDTO.getFolderName(), mailBox));
			folderDTO = folderDAO.findFolderByFolderNameAndEmail(folderDTO.getFolderName(),mailBox).getFolderDTO();
			logger.info("Folder " + folderDTO.getFolderName() + " successfully created");
			return folderDTO;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataProcessingException(ExceptionType.unexpectedException,e.getCause());
		}
	}
	
	public void deleteFolder(FolderDTO folderDTO) throws DataProcessingException {
		try {
			Folder folder = folderDAO.getFolder(folderDTO.getId());
			folderDAO.delete(folder);
			logger.info("Folder " + folderDTO.getFolderName() + " successfully deletes");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataProcessingException(ExceptionType.unexpectedException,e.getCause());
		}
	}
	
	public FolderDTO renameFolder(FolderDTO folderDTO) throws DataProcessingException {
		try {
			Folder folder = folderDAO.getFolder(folderDTO.getId());
			folder.setFolderName(folderDTO.getFolderName());
			folderDAO.update(folder);
			folderDTO = folderDAO.getFolder(folderDTO.getId()).getFolderDTO();
			logger.info("Folder " + folderDTO.getFolderName() + " successfully renamed");
			return folderDTO;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataProcessingException(ExceptionType.unexpectedException,e.getCause());
		}
	}
	
	public boolean moveMessageToAnotherFolder(Object folder) throws DataProcessingException {
		try {
			folderDAO.update((Folder) folder);
			logger.info("Message successfully moved into folder " + ((Folder) folder).getFolderName());
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataProcessingException(ExceptionType.unexpectedException,e.getCause());
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
			throw new DataProcessingException(ExceptionType.unexpectedException,e.getCause());
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
			throw new DataProcessingException(ExceptionType.unexpectedException,e.getCause());
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
		} catch (NoResultException e) {
			logger.warn("Folder with name " + folderName + " does not exist in " + email, e);
			throw new DataProcessingException(ExceptionType.NoSuchFolderException);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataProcessingException(ExceptionType.unexpectedException,e.getCause());
		}
	}
}