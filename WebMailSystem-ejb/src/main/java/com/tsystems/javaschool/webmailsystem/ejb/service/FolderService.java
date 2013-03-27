package com.tsystems.javaschool.webmailsystem.ejb.service;

import com.tsystems.javaschool.webmailsystem.dto.FolderDTO;
import com.tsystems.javaschool.webmailsystem.dto.MailBoxDTO;
import com.tsystems.javaschool.webmailsystem.ejb.dao.FolderDAO;
import com.tsystems.javaschool.webmailsystem.ejb.dao.MailBoxDAO;
import com.tsystems.javaschool.webmailsystem.entity.Folder;
import com.tsystems.javaschool.webmailsystem.entity.MailBox;
import com.tsystems.javaschool.webmailsystem.exception.DataProcessingException;
import com.tsystems.javaschool.webmailsystem.exception.ExceptionType;
import org.apache.log4j.Logger;

import javax.ejb.*;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class FolderService {

	@EJB
	private FolderDAO folderDAO;

	@EJB
	private MailBoxDAO mailBoxDAO;

	private Logger logger = Logger.getLogger(FolderService.class);

	//	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FolderDTO> getFoldersForMailBox(MailBoxDTO mailBoxDTO) throws DataProcessingException {
		MailBox mailBox = mailBoxDAO.find(mailBoxDTO.getEmail());

		if (mailBox == null) {
			logger.warn("Mailbox with email " + mailBoxDTO.getEmail() + " does not exist");
			throw new DataProcessingException(ExceptionType.mailBoxDoesNotExist);
		}

		List<Folder> listOfFolders = folderDAO.getFoldersForMailBox(mailBox);
		List<FolderDTO> listOfFolderDTO= new ArrayList<FolderDTO>();

		for (Folder folder : listOfFolders) {
			listOfFolderDTO.add(folder.getFolderDTO());
		}

		return listOfFolderDTO;
	}

//	public List<MessageDTO> getMessagesFromFolder(FolderDTO folderDTO) throws DataProcessingException {
//		Folder folder = folderDAO.getFolder(folderDTO.getId());
//
//		if (folder == null) {
//			logger.warn("Folder with name " + folderDTO.getFolderName() + " does not exist");
//			throw new DataProcessingException(ExceptionType.folderDoesNotExist);
//		}
//
//		List<MessageDTO> listOfMessagesDTO = new ArrayList<MessageDTO>();
//
//		for (Message message : folder.getListOfMessages()) {
//			if (message.getMessageReadFlag() == false) {
//				message.setMessageReadFlag(true);
//			}
//			listOfMessagesDTO.add(message.getMessageDTO());
//		}
//
//		logger.info("Messages from folder " + folderDTO.getFolderName() + " successfully got");
//
//		return listOfMessagesDTO;
//	}

	public FolderDTO createFolder(FolderDTO folderDTO, MailBoxDTO mailBoxDTO) throws DataProcessingException {
		MailBox mailBox = mailBoxDAO.find(mailBoxDTO.getEmail());

		if (mailBox == null) {
			logger.warn("Mailbox with email " + mailBoxDTO.getEmail() + " does not exist");
			throw new DataProcessingException(ExceptionType.mailBoxDoesNotExist);
		}

		Folder folder = new Folder(folderDTO.getFolderName(), mailBox);

		try {
			folderDAO.create(folder);
		} catch (EJBException e) {
			logger.warn("Folder with name " + folderDTO.getFolderName()
					+ " already exists in "  + mailBoxDTO.getEmail());
			throw new DataProcessingException(ExceptionType.folderWithSuchANameAlreadyExists);
		}

		logger.info("Folder " + folderDTO.getFolderName() + " successfully created");
		return folder.getFolderDTO();
	}

//	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void deleteFolder(FolderDTO folderDTO) throws DataProcessingException {
		if (folderDTO.getFolderName().equals("Inbox")
				|| folderDTO.getFolderName().equals("Outbox")
				|| folderDTO.getFolderName().equals("Draft")) {
			throw new DataProcessingException(ExceptionType.systemFolderDeletion);
		}

		Folder folder = folderDAO.getFolder(folderDTO.getId());

		if (folder == null) {
			logger.warn("Folder with name " + folderDTO.getFolderName() + " does not exist");
			throw new DataProcessingException(ExceptionType.folderDoesNotExist);
		}

		folderDAO.delete(folder);

		logger.info("Folder " + folderDTO.getFolderName() + " successfully deletes");
	}

	public void renameFolder(FolderDTO folderDTO, String folderNewName) throws DataProcessingException {
		if (folderDTO.getFolderName().equals("Inbox")
				|| folderDTO.getFolderName().equals("Outbox")
				|| folderDTO.getFolderName().equals("Draft")) {
			throw new DataProcessingException(ExceptionType.systemFolderRenaming);
		}

		Folder folder = folderDAO.getFolder(folderDTO.getId());

		if (folder == null) {
			logger.warn("Folder does not exist");
			throw new DataProcessingException(ExceptionType.folderDoesNotExist);
		}

		folder.setFolderName(folderNewName);

		try {
			folderDAO.flush();
		} catch (EJBException e) {
			logger.warn("Folder with name " + folderDTO.getFolderName() + " already exists");
			throw new DataProcessingException(ExceptionType.folderWithSuchANameAlreadyExists);
		}

		logger.info("Folder successfully renamed");
	}
}

