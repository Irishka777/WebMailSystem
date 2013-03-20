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
			if (mailBoxDAO.create(new MailBox(email,password, new User(userDTO))) == true) {
				logger.info("Mailbox with email " + email + " successfully created");
				return;
			}
		} catch (PersistenceException e) {
			logger.warn("Mailbox with email " + email + " already exists", e);
			throw new DataProcessingException(ExceptionType.mailBoxWithSuchANameAlreadyExists);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataProcessingException(ExceptionType.unexpectedException,e.getCause());
		}
		logger.warn("Mailbox with email " + email + " already exists");
		throw new DataProcessingException(ExceptionType.mailBoxWithSuchANameAlreadyExists);
	}

	public UserDTO getUser(MailBoxDTO mailBoxDTO) throws DataProcessingException {
		try {
			MailBox mailBox = mailBoxDAO.find(mailBoxDTO.getEmail());
			return mailBox.getUser().getUserDTO();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataProcessingException(ExceptionType.unexpectedException,e.getCause());
		}
	}

	public void changeUserData(UserDTO userDTO, MailBoxDTO mailBoxDTO) throws DataProcessingException {
		try {
			MailBox mailBox = mailBoxDAO.find(mailBoxDTO.getEmail());
			mailBox.setUser(new User(userDTO));
			mailBoxDAO.update(mailBox);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DataProcessingException(ExceptionType.unexpectedException,e.getCause());
		}
	}
}
