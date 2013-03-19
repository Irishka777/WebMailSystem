package com.tsystems.javaschool.webmailsystem.ejb.service;

import com.tsystems.javaschool.webmailsystem.dto.MailBoxDTO;
import com.tsystems.javaschool.webmailsystem.dto.UserDTO;
import com.tsystems.javaschool.webmailsystem.ejb.dao.MailBoxDAO;
import com.tsystems.javaschool.webmailsystem.entity.MailBox;
import com.tsystems.javaschool.webmailsystem.entity.User;
import com.tsystems.javaschool.webmailsystem.exception.DataProcessingException;
import com.tsystems.javaschool.webmailsystem.exception.ExceptionType;
import org.apache.log4j.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

@Stateless
public class MailBoxService {
	@EJB
	private MailBoxDAO mailBoxDAO;

	private Logger logger = Logger.getLogger(MailBoxService.class);

	public MailBoxDTO login(String email, String password) throws DataProcessingException {
		MailBox mailBox = null;
		try {
			mailBox = mailBoxDAO.find(email);
		} catch (NoResultException e) {
			logger.warn("Mailbox with email " + email + " does not exist", e);
			throw new DataProcessingException(ExceptionType.wrongEmailOrPassword);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataProcessingException(ExceptionType.unexpectedException,e.getCause());
		}
		byte[] encodedPassword = MailBox.convertPasswordStringIntoBytesArrayUsingMD5AndSalt(password);
		if (!MailBox.comparePasswords(encodedPassword,mailBox.getPassword())) {
			logger.info("Entered password for mailbox with email" + email + " is wrong");
			throw new DataProcessingException(ExceptionType.wrongEmailOrPassword);
		}
		logger.info("Successful login into mailbox " + mailBox.getEmail());
		return mailBox.getMailBoxDTO();
	}
	
	public void signUp(String email, String password, UserDTO userDTO) throws DataProcessingException {
		try {
			mailBoxDAO.create(new MailBox(email,password, new User(userDTO)));
			logger.info("Mailbox with email " + email + " successfully created");
		} catch (PersistenceException e) {
			logger.warn("Mailbox with email " + email + " already exists", e);
			throw new DataProcessingException(ExceptionType.mailBoxWithSuchANameAlreadyExists);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataProcessingException(ExceptionType.unexpectedException,e.getCause());
		}
	}
	
//	public MailBox updateMailBoxData(Object mailBox) {
//		try {
//			return mailBoxDAO.update((MailBox) mailBox);
//		} catch (Exception e) {
//			logger.error(e.getMessage(), e);
//			return null;
//		}
//	}
	
//	public MailBox getMailBoxEntityByEmailAddress(Object emailAddress) {
//		try {
//			return mailBoxDAO.findByEmail(((String) emailAddress).toLowerCase());
//		} catch (NoResultException e) {
//			logger.warn("Mail box with such email address does not exists", e);
//			return null;
//		} catch (Exception e) {
//			logger.error(e.getMessage(), e);
//			return null;
//		}
//	}
}
