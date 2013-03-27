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

	public void deleteMessage(MessageDTO[] messageDTOList) throws DataProcessingException {
		Folder folder = folderDAO.getFolder(messageDTOList[0].getFolder());
		if (folder == null) {
			logger.warn("Folder with messages have been deleted");
			throw new DataProcessingException(ExceptionType.folderDoesNotExist);
		}

		Message message;
		for (MessageDTO messageDTO : messageDTOList) {
			message = messageDAO.findMessage(messageDTO.getId());
			if (message == null) {
				logger.warn("One of selected messages have been deleted by some one else");
				throw new DataProcessingException(ExceptionType.messageDoesNotExist);
			}
			folder.getListOfMessages().remove(message);
		}

		logger.info("Messages successfully deleted");
	}

	public void deleteMessage(List<MessageDTO> messageDTOList, FolderDTO folderDTO) throws DataProcessingException {
		Folder folder = folderDAO.getFolder(folderDTO.getId());
		if (folder == null) {
			logger.warn("Folder with name " + folderDTO.getFolderName() + " does not exist");
			throw new DataProcessingException(ExceptionType.folderDoesNotExist);
		}

		Message message;
		for (MessageDTO messageDTO : messageDTOList) {
			message = messageDAO.findMessage(messageDTO.getId());
			if (message == null) {
				logger.warn("One of selected messages have been deleted by some one else");
				throw new DataProcessingException(ExceptionType.messageDoesNotExist);
			}
			folder.getListOfMessages().remove(message);
		}

		logger.info("Messages successfully deleted");
	}

	public void moveMessage(MessageDTO[] messageDTOList, FolderDTO newFolderDTO)
			throws DataProcessingException {
		Folder oldFolder = folderDAO.getFolder(messageDTOList[0].getFolder());
		if (oldFolder == null) {
			logger.warn("Folder with messages have been deleted");
			throw new DataProcessingException(ExceptionType.folderDoesNotExist);
		}

		Folder newFolder = folderDAO.getFolder(newFolderDTO.getId());
		if (newFolder == null) {
			logger.warn("Folder with name " + newFolderDTO.getFolderName() + " does not exist");
			throw new DataProcessingException(ExceptionType.folderDoesNotExist);
		}

		Message message;

		for (MessageDTO messageDTO : messageDTOList) {
			message = messageDAO.findMessage(messageDTO.getId());
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

	public void moveMessage(List<MessageDTO> messageDTOList, FolderDTO newFolderDTO, FolderDTO oldFolderDTO)
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

		for (MessageDTO messageDTO : messageDTOList) {
			message = messageDAO.findMessage(messageDTO.getId());
			if (message == null) {
				logger.warn("One of selected messages have been deleted by some one else");
				throw new DataProcessingException(ExceptionType.messageDoesNotExist);
			}
			oldFolder.getListOfMessages().remove(message);
			message.setFolder(newFolder);
			newFolder.getListOfMessages().add(message);
			messageDTO.setFolder(newFolder.getId());
		}

		logger.info("Messages successfully moved");
	}

	public List<MessageDTO> getMessagesFromFolder(FolderDTO folderDTO) throws DataProcessingException {
		Folder folder = folderDAO.getFolder(folderDTO.getId());

		if (folder == null) {
			logger.warn("Folder with name " + folderDTO.getFolderName() + " does not exist");
			throw new DataProcessingException(ExceptionType.folderDoesNotExist);
		}

		List<MessageDTO> listOfMessagesDTO = new ArrayList<MessageDTO>();

		for (Message message : folder.getListOfMessages()) {
			if (!message.getMessageReadFlag()) {
				message.setMessageReadFlag(true);
			}
			listOfMessagesDTO.add(message.getMessageDTO());
		}

		logger.info("Messages from folder " + folderDTO.getFolderName() + " successfully got");

		return listOfMessagesDTO;
	}

	public List<MessageDTO> receiveNewMessages(FolderDTO folderDTO)
			throws DataProcessingException {

		Folder folder = folderDAO.getFolder(folderDTO.getId());
		if (folder == null) {
			logger.warn("Folder with name " + folderDTO.getFolderName() + " does not exist");
			throw new DataProcessingException(ExceptionType.folderDoesNotExist);
		}

		List<Message> newMessages = messageDAO.getNewMessages(folder);

		if (newMessages == null) {
			return null;
		}

		List<MessageDTO> listOfMessagesDTO = new ArrayList<MessageDTO>();
		for (Message message : newMessages) {
			message.setMessageReadFlag(true);
			listOfMessagesDTO.add(message.getMessageDTO());
		}
		return listOfMessagesDTO;
	}
}