package com.tsystems.javaschool.webmailsystem.ejb.service;

import com.tsystems.javaschool.webmailsystem.dto.FolderDTO;
import com.tsystems.javaschool.webmailsystem.dto.MessageDTO;
import com.tsystems.javaschool.webmailsystem.ejb.dao.MailBoxDAO;
import com.tsystems.javaschool.webmailsystem.ejb.dao.MessageDAO;
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
			throw new DataProcessingException(ExceptionType.NoSuchFolderException);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataProcessingException(ExceptionType.UnexpectedException,e.getCause());
		}
	}

	public void sendMessage(MessageDTO messageDTO) throws DataProcessingException {
		try {
			MailBox sender = mailBoxDAO.find(messageDTO.getSender());
			MailBox receiver = mailBoxDAO.find(messageDTO.getReceiver());
			Message message = new Message(true, sender, receiver, messageDTO.getTheme(), messageDTO.getMessageBody());
			messageDAO.send(message);
			logger.info("Message successfully sent from " + messageDTO.getSender() + " to " + messageDTO.getReceiver());
		} catch (NoResultException e) {
			logger.warn("Mailbox with email " + messageDTO.getReceiver() + " does not exist", e);
			throw new DataProcessingException(ExceptionType.NoSuchMailBoxException);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataProcessingException(ExceptionType.UnexpectedException,e.getCause());
		}
	}

	public void saveMessage(MessageDTO messageDTO) throws DataProcessingException {
		try {
			MailBox sender = mailBoxDAO.find(messageDTO.getSender());
			MailBox receiver = null;
			try {
				receiver = mailBoxDAO.find(messageDTO.getReceiver());
			} catch (NoResultException e) {
				logger.warn("Mailbox with email " + messageDTO.getReceiver() + " does not exist", e);
			}
			Message message = new Message(true, sender, receiver, messageDTO.getTheme(), messageDTO.getMessageBody());
			messageDAO.save(message);
			logger.info("Message successfully saved in draft messages of" + sender.getEmail());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataProcessingException(ExceptionType.UnexpectedException,e.getCause());
		}
	}

//	public boolean sendMessage(MailBox senderMailBox, String receiver, String theme, String messageBody) {
//		try {
//			MailBox receiverMailBox = mailBoxDAO.find(receiver);
//			messageDAO.send(new Message(senderMailBox,receiverMailBox,theme,messageBody));
//			logger.info("Message successfully sent from " + senderMailBox.getEmail() + " to " + receiver);
//			return true;
//		} catch (NoResultException e) {
//			logger.warn("Mailbox with email " + receiver + " does not exist", e);
//			return false;
//		} catch (Exception e) {
//			logger.error(e.getMessage(), e);
//			return false;
//		}
//	}
	
	public boolean deleteMessage(Object message) {
		try {
			messageDAO.delete((Message) message);
			logger.info("Message successfully deleted");
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}	
	}
}