package com.tsystems.javaschool.webmailsystem.ejb.service;

import com.tsystems.javaschool.webmailsystem.entity.MessageEntity;
import com.tsystems.javaschool.webmailsystem.ejb.dao.MessageDAOImpl;
import org.apache.log4j.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class MessageService {

	@EJB
	MessageDAOImpl messageDAO;

	private Logger logger = Logger.getLogger(FolderService.class);

	public boolean sendMessage(Object message) {
		try {
			if (((MessageEntity) message).getReceiver() == null) {
				return false;
//				return new ServerResponse(true, false, null, "Mail box with sush email address does not exist");
			}
			messageDAO.send((MessageEntity) message);
			logger.info("Message successfully sent from "
					+ ((MessageEntity) message).getSender()
					+ " to " + ((MessageEntity) message).getReceiver());
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}	
	}
	
	public boolean saveMessage(Object message) {
		try {
			messageDAO.save((MessageEntity) message);
			logger.info("Message successfully saved in draft messages of " + ((MessageEntity) message).getSender());
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