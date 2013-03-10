package com.tsystems.javaschool.webmailsystem.ejb.service;

import com.tsystems.javaschool.webmailsystem.entity.MailBoxEntity;
import com.tsystems.javaschool.webmailsystem.ejb.dao.MailBoxDAOImpl;
import org.apache.log4j.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

@Stateless
public class MailBoxService {
	@EJB
	private MailBoxDAOImpl mailBoxDAO;

	private Logger logger = Logger.getLogger(FolderService.class);

	public boolean login(Object neededMailBox) {
		try {
			MailBoxEntity mailBox = mailBoxDAO.findByEmail(((MailBoxEntity) neededMailBox).getEmail());
			byte[] password = mailBox.getPassword();
			byte[] neededPassword = ((MailBoxEntity) neededMailBox).getPassword();
			if (password.length != neededPassword.length) {
				logger.info("Entered password for mailbox with email address "
						+ ((MailBoxEntity) neededMailBox).getEmail() + " is wrong");
				return false;
			}
			for (int i = 0; i < password.length; i++) {
				if (password[i] != neededPassword[i]) {
					logger.info("Entered password for mailbox with email address "
							+ ((MailBoxEntity) neededMailBox).getEmail() + " is wrong");
					return false;
				}
			}
			logger.info("Successful login into mailbox " + mailBox.getEmail());
			return true;
		} catch (NoResultException e) {
			logger.warn("Mailbox with such email address does not exist", e);
			return false;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}
	
	public boolean registration(Object mailBox) {
		try {
			mailBoxDAO.insert((MailBoxEntity) mailBox);
			logger.info("Mailbox with email address" + ((MailBoxEntity) mailBox).getEmail() + " successfully created");
			return true;
		} catch (PersistenceException e) {
			logger.warn("Mailbox with such email address already exists", e);
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
