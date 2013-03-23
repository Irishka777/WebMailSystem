package com.tsystems.javaschool.webmailsystem.ejb.service;

import com.tsystems.javaschool.webmailsystem.dto.MailBoxDTO;
import com.tsystems.javaschool.webmailsystem.dto.UserDTO;
import com.tsystems.javaschool.webmailsystem.ejb.dao.FolderDAO;
import com.tsystems.javaschool.webmailsystem.ejb.dao.MailBoxDAO;
import com.tsystems.javaschool.webmailsystem.entity.Folder;
import com.tsystems.javaschool.webmailsystem.entity.MailBox;
import com.tsystems.javaschool.webmailsystem.entity.User;
import com.tsystems.javaschool.webmailsystem.exception.DataProcessingException;
import com.tsystems.javaschool.webmailsystem.exception.ExceptionType;
import org.apache.log4j.Logger;

import javax.ejb.*;

@Stateless
public class MailBoxService {
	@EJB
	private MailBoxDAO mailBoxDAO;

	@EJB
	private FolderDAO folderDAO;

	private Logger logger = Logger.getLogger(MailBoxService.class);

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public MailBoxDTO login(String email, String password) throws DataProcessingException {
		MailBox mailBox = mailBoxDAO.find(email);

		if (mailBox == null) {
			logger.warn("Mailbox with email " + email + " does not exist");
			throw new DataProcessingException(ExceptionType.wrongEmailOrPassword);
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
			MailBox mailBox = new MailBox(email,password,new User(userDTO));

			mailBoxDAO.create(mailBox);

			folderDAO.create(new Folder("Draft", mailBox));
			folderDAO.create(new Folder("Outbox", mailBox));
			folderDAO.create(new Folder("Inbox", mailBox));

			logger.info("Mailbox with email " + email + " successfully created");

		} catch (EJBException e) {
			logger.warn("Mailbox with email " + email + " already exists", e);
			throw new DataProcessingException(ExceptionType.mailBoxWithSuchANameAlreadyExists);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public UserDTO getUser(MailBoxDTO mailBoxDTO) throws DataProcessingException {
		MailBox mailBox = mailBoxDAO.find(mailBoxDTO.getEmail());

		if (mailBox == null) {
			logger.warn("Mailbox with email " + mailBoxDTO.getEmail() + " does not exist");
			throw new DataProcessingException(ExceptionType.mailBoxDoesNotExist);
		}

		return mailBox.getUser().getUserDTO();
	}

	public void changeUserData(UserDTO userDTO, MailBoxDTO mailBoxDTO) throws DataProcessingException {
		MailBox mailBox = mailBoxDAO.find(mailBoxDTO.getEmail());

		if (mailBox == null) {
			logger.warn("Mailbox with email " + mailBoxDTO.getEmail() + " does not exist");
			throw new DataProcessingException(ExceptionType.mailBoxDoesNotExist);
		}

		mailBox.setUser(new User(userDTO));
	}
}
