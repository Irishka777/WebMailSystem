package com.tsystems.javaschool.webmailsystem.ejb.service;

import com.tsystems.javaschool.webmailsystem.dto.FolderDTO;
import com.tsystems.javaschool.webmailsystem.dto.MessageDTO;
import com.tsystems.javaschool.webmailsystem.ejb.dao.FolderDAO;
import com.tsystems.javaschool.webmailsystem.ejb.dao.MailBoxDAO;
import com.tsystems.javaschool.webmailsystem.ejb.dao.MessageDAO;
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
public class MessageService {

	@EJB
	private MessageDAO messageDAO;

	@EJB
	private FolderDAO folderDAO;

	@EJB
	private MailBoxDAO mailBoxDAO;

	private Logger logger = Logger.getLogger(MessageService.class);

	public List<MessageDTO> getMessagesFromFolder(FolderDTO folder) throws DataProcessingException {
		try {
			List<Message> listOfMessages = messageDAO.getMessagesFromFolder(folder.getId());
			List<MessageDTO> listOfMessagesDTO= new ArrayList<MessageDTO>();
			for (Message message : listOfMessages) {
				listOfMessagesDTO.add(message.getMessageDTO());
			}
			return listOfMessagesDTO;
		} catch (NoResultException e) {
			logger.warn("Folder with name " + folder.getFolderName() + " does not exist", e);
			throw new DataProcessingException(ExceptionType.folderDoesNotExist);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataProcessingException(ExceptionType.unexpectedException,e.getCause());
		}
	}

	public void sendMessage(MessageDTO messageDTO) throws DataProcessingException {
		MailBox sender = null;
		MailBox receiver = null;
		try {
			sender = mailBoxDAO.find(messageDTO.getSender());
			if (messageDTO.getReceiver() != null) {
				receiver = mailBoxDAO.find(messageDTO.getReceiver());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataProcessingException(ExceptionType.unexpectedException,e.getCause());
		}
		Message message = new Message(true, sender, receiver, messageDTO.getTheme(), messageDTO.getMessageBody());
		if (receiver == null) {
			try {
				logger.warn("Mailbox with email " + messageDTO.getReceiver() + " does not exist");
				messageDAO.save(message);
				logger.info("Message successfully saved in draft messages of" + sender.getEmail());
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new DataProcessingException(ExceptionType.unexpectedException,e.getCause());
			}
			throw new DataProcessingException(ExceptionType.wrongMessageReceiverEmail);
		}
		try {
			messageDAO.send(message);
			logger.info("Message successfully sent from " + messageDTO.getSender() + " to " + messageDTO.getReceiver());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataProcessingException(ExceptionType.unexpectedException,e.getCause());
		}
	}

	public void saveMessage(MessageDTO messageDTO) throws DataProcessingException {
		try {
			MailBox sender = mailBoxDAO.find(messageDTO.getSender());
			MailBox receiver = null;
			if (messageDTO.getReceiver() != null) {
				receiver = mailBoxDAO.find(messageDTO.getReceiver());
			}
			if (receiver == null) {
				logger.warn("Mailbox with email " + messageDTO.getReceiver() + " does not exist");
			}
			Message message = new Message(true, sender, receiver, messageDTO.getTheme(), messageDTO.getMessageBody());
			messageDAO.save(message);
			logger.info("Message successfully saved in draft messages of" + sender.getEmail());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataProcessingException(ExceptionType.unexpectedException,e.getCause());
		}
	}
	public void deleteMessage(List<MessageDTO> messageDTO, long folderId) throws DataProcessingException {
		try {
			Folder folder = folderDAO.getFolder(folderId);
			Message message;
			for (int i = 0; i < messageDTO.size(); i++) {
				message = messageDAO.findMessage(messageDTO.get(i).getId());
				folder.getListOfMessages().remove(message);
			}
			folderDAO.update(folder);
			logger.info("Messages successfully deleted");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataProcessingException(ExceptionType.unexpectedException,e.getCause());
		}
	}

	public void deleteMessage(MessageDTO[] messageDTO, long folderId) throws DataProcessingException {
		try {
			Folder folder = folderDAO.getFolder(folderId);
			Message message;
			for (int i = 0; i < messageDTO.length; i++) {
				message = messageDAO.findMessage(messageDTO[i].getId());
				folder.getListOfMessages().remove(message);
//				messageDAO.delete(messageDTO[i].getId());
			}
			folderDAO.update(folder);
			logger.info("Messages successfully deleted");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataProcessingException(ExceptionType.unexpectedException,e.getCause());
		}
	}

	public void moveMessage(List<MessageDTO> messageDTO, long newFolderId, long oldFolderId) throws DataProcessingException {
		try {
			Folder newFolder = folderDAO.getFolder(newFolderId);
			Folder oldFolder = folderDAO.getFolder(oldFolderId);
			Message message;

			for (int i = 0; i < messageDTO.size(); i++) {
				message = messageDAO.findMessage(messageDTO.get(i).getId());
				oldFolder.getListOfMessages().remove(message);
				message.setFolder(newFolder);
				newFolder.getListOfMessages().add(message);
			}
			folderDAO.update(newFolder);
			folderDAO.update(oldFolder);
			logger.info("Messages successfully moved");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataProcessingException(ExceptionType.unexpectedException,e.getCause());
		}
	}

	public void moveMessage(MessageDTO[] messageDTO, long newFolderId, long oldFolderId) throws DataProcessingException {
		try {
			Folder newFolder = folderDAO.getFolder(newFolderId);
			Folder oldFolder = folderDAO.getFolder(oldFolderId);
			Message message;
			for (int i = 0; i < messageDTO.length; i++) {
				message = messageDAO.findMessage(messageDTO[i].getId());
				oldFolder.getListOfMessages().remove(message);
				message.setFolder(newFolder);
				newFolder.getListOfMessages().add(message);
//				messageDAO.move(message);
			}
			folderDAO.update(newFolder);
			folderDAO.update(oldFolder);
			logger.info("Messages successfully moved");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataProcessingException(ExceptionType.unexpectedException,e.getCause());
		}
	}

	public List<MessageDTO> receiveNewMessages(FolderDTO folderDTO)
			throws DataProcessingException {
		try {
			List<Message> receivedMessages = messageDAO.getNewMessages(folderDTO.getId());
			if (receivedMessages == null) {
				return null;
			}
			List<MessageDTO> listOfMessagesDTO = new ArrayList<MessageDTO>();
			for (Message message : receivedMessages) {
				listOfMessagesDTO.add(message.getMessageDTO());
			}
			return listOfMessagesDTO;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataProcessingException(ExceptionType.unexpectedException,e.getCause());
		}
	}
}