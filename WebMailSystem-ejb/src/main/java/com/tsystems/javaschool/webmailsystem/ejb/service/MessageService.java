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
		MailBox sender = mailBoxDAO.find(messageDTO.getSender());
		if (sender == null) {
			logger.warn("Mailbox with email " + messageDTO.getSender() + " does not exist");
			throw new DataProcessingException(ExceptionType.mailBoxDoesNotExist);
		}

		MailBox receiver = null;
		if (messageDTO.getReceiver() != null) {
			receiver = mailBoxDAO.find(messageDTO.getReceiver());
		}

		Message message = new Message(true, sender, receiver, messageDTO.getTheme(), messageDTO.getMessageBody());

		if (receiver == null) {
			logger.warn("Mailbox with email " + messageDTO.getReceiver() + " does not exist");
			save(message);
			logger.info("Message successfully saved in draft messages of" + sender.getEmail());
			throw new DataProcessingException(ExceptionType.wrongMessageReceiverEmail);
		}

		send(message);
		logger.info("Message successfully sent from " + messageDTO.getSender() + " to " + messageDTO.getReceiver());
	}

	public void saveMessage(MessageDTO messageDTO) throws DataProcessingException {
		MailBox sender = mailBoxDAO.find(messageDTO.getSender());
		if (sender == null) {
			logger.warn("Mailbox with email " + messageDTO.getSender() + " does not exist");
			throw new DataProcessingException(ExceptionType.mailBoxDoesNotExist);
		}

		MailBox receiver = null;
		if (messageDTO.getReceiver() != null) {
			receiver = mailBoxDAO.find(messageDTO.getReceiver());
		}

		if (receiver == null) {
			logger.warn("Mailbox with email " + messageDTO.getReceiver() + " does not exist");
		}
		Message message = new Message(true, sender, receiver, messageDTO.getTheme(), messageDTO.getMessageBody());
		save(message);
		logger.info("Message successfully saved in draft messages of" + sender.getEmail());
	}

	private void send(Message message) {
		Folder senderFolder = folderDAO.findFolderByFolderNameAndEmail("Outbox", message.getSender());
		if (senderFolder == null) {
			logger.warn("Sender outbox folder does not exist");
			throw new DataProcessingException(ExceptionType.folderDoesNotExist);
		}
		message.setFolder(senderFolder);
		senderFolder.getListOfMessages().add(message);

		Folder receiverFolder = folderDAO.findFolderByFolderNameAndEmail("Inbox", message.getReceiver());
		if (receiverFolder == null) {
			logger.warn("Receiver inbox folder does not exist");
			throw new DataProcessingException(ExceptionType.folderDoesNotExist);
		}
		receiverFolder.getListOfMessages().add(new Message(message,receiverFolder));
	}

	private void save(Message message) {
		Folder senderFolder = folderDAO.findFolderByFolderNameAndEmail("Draft", message.getSender());
		if (senderFolder == null) {
			logger.warn("Folder for draft messages does not exist");
			throw new DataProcessingException(ExceptionType.folderDoesNotExist);
		}
		message.setFolder(senderFolder);
		senderFolder.getListOfMessages().add(message);
	}

	public void deleteMessage(List<MessageDTO> messageDTO, FolderDTO folderDTO) throws DataProcessingException {
		Folder folder = folderDAO.getFolder(folderDTO.getId());
		if (folder == null) {
			logger.warn("Folder with name " + folderDTO.getFolderName() + " does not exist");
			throw new DataProcessingException(ExceptionType.folderDoesNotExist);
		}

		Message message;
		for (int i = 0; i < messageDTO.size(); i++) {
			message = messageDAO.findMessage(messageDTO.get(i).getId());
			if (message == null) {
				logger.warn("One of selected messages have been deleted by some one else");
				throw new DataProcessingException(ExceptionType.messageDoesNotExist);
			}
			folder.getListOfMessages().remove(message);
		}

		logger.info("Messages successfully deleted");
	}

	public void moveMessage(List<MessageDTO> messageDTO, FolderDTO newFolderDTO, FolderDTO oldFolderDTO)
			throws DataProcessingException {
		Folder newFolder = folderDAO.getFolder(newFolderDTO.getId());
		if (newFolder == null) {
			logger.warn("Folder with name " + newFolderDTO.getFolderName() + " does not exist");
			throw new DataProcessingException(ExceptionType.folderDoesNotExist);
		}

		Folder oldFolder = folderDAO.getFolder(oldFolderDTO.getId());
		if (oldFolder == null) {
			logger.warn("Folder with name " + oldFolderDTO.getFolderName() + " does not exist");
			throw new DataProcessingException(ExceptionType.folderDoesNotExist);
		}

		Message message;

		for (int i = 0; i < messageDTO.size(); i++) {
			message = messageDAO.findMessage(messageDTO.get(i).getId());
			if (message == null) {
				logger.warn("One of selected messages have been deleted by some one else");
				throw new DataProcessingException(ExceptionType.messageDoesNotExist);
			}
			oldFolder.getListOfMessages().remove(message);
			message.setFolder(newFolder);
			newFolder.getListOfMessages().add(message);
		}

		logger.info("Messages successfully moved");
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