package com.tsystems.javaschool.webmailsystem.ejb.service;

import com.tsystems.javaschool.webmailsystem.ejb.dao.MailBoxDAO;
import com.tsystems.javaschool.webmailsystem.ejb.dao.MessageDAO;
import com.tsystems.javaschool.webmailsystem.entity.MailBoxEntity;
import com.tsystems.javaschool.webmailsystem.entity.MessageEntity;
import org.apache.log4j.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;

@Stateless
public class MessageService {

	@EJB
	private MessageDAO messageDAO;

	@EJB
	private MailBoxDAO mailBoxDAO;

	private Logger logger = Logger.getLogger(MessageService.class);

	public boolean sendMessage(MailBoxEntity senderMailBox, String receiver, String theme, String messageBody) {
		try {
			MailBoxEntity receiverMailBox = mailBoxDAO.findByEmail(receiver);
			messageDAO.send(new MessageEntity(senderMailBox,receiverMailBox,theme,messageBody));
			logger.info("Message successfully sent from " + senderMailBox.getEmail() + " to " + receiver);
			return true;
		} catch (NoResultException e) {
			logger.warn("Mailbox with email " + receiver + " does not exist", e);
			return false;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}
	
	public boolean saveMessage(MailBoxEntity senderMailBox,String receiver, String theme, String messageBody) {
		try {
			MailBoxEntity receiverMailBox = null;
			try {
				receiverMailBox = mailBoxDAO.findByEmail(receiver);
			} catch (NoResultException e) {
				logger.warn("Mailbox with email " + receiver + " does not exist", e);
				return false;
			}
			messageDAO.save(new MessageEntity(senderMailBox,receiverMailBox,theme,messageBody));
			logger.info("Message successfully  saved in draft messages of" + senderMailBox.getEmail());
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}
	
	public boolean deleteMessage(Object message) {
		try {
			messageDAO.delete((MessageEntity) message);
			logger.info("Message successfully deleted");
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}	
	}
}