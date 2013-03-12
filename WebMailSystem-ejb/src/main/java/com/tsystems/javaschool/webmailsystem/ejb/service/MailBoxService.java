package com.tsystems.javaschool.webmailsystem.ejb.service;

import com.tsystems.javaschool.webmailsystem.ejb.dao.MailBoxDAO;
import com.tsystems.javaschool.webmailsystem.entity.MailBoxEntity;
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

	public MailBoxEntity login(String email, String password) {
		try {
			MailBoxEntity mailBox = mailBoxDAO.findByEmail(email);
			byte[] encodedPassword = MailBoxEntity.convertPasswordStringIntoBytesArrayUsingMD5AndSalt(password);
			if (!MailBoxEntity.comparePasswords(encodedPassword,mailBox.getPassword())) {
				logger.info("Entered password for mailbox with email" + email + " is wrong");
				return null;
			}
			logger.info("Successful login into mailbox " + mailBox.getEmail());
			return mailBox;
		} catch (NoResultException e) {
			logger.warn("Mailbox with email " + email + " does not exist", e);
			return null;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}
	
	public boolean registration(MailBoxEntity mailBox) {
		try {
			mailBoxDAO.insert(mailBox);
			logger.info("Mailbox with email " + mailBox.getEmail() + " successfully created");
			return true;
		} catch (PersistenceException e) {
			logger.warn("Mailbox with such email already exists", e);
			return false;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}
	
	public MailBoxEntity updateMailBoxData(Object mailBox) {
		try {
			return mailBoxDAO.update((MailBoxEntity) mailBox);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}
	
	public MailBoxEntity getMailBoxEntityByEmailAddress(Object emailAddress) {
		try {
			return mailBoxDAO.findByEmail(((String) emailAddress).toLowerCase());
		} catch (NoResultException e) {
			logger.warn("Mail box with such email address does not exists", e);
			return null;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}	
	}
}
