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

	private Logger logger = Logger.getLogger(FolderService.class);

	public boolean login(String email, String password) {
		MailBoxEntity neededMailBox = new MailBoxEntity(email,password,null);
		try {
			MailBoxEntity mailBox = mailBoxDAO.findByEmail(((MailBoxEntity) neededMailBox).getEmail());
			if (!MailBoxEntity.comparePasswords(neededMailBox.getPassword(),mailBox.getPassword())) {
				logger.info("Entered password for mailbox with email"
						+ ((MailBoxEntity) neededMailBox).getEmail() + " is wrong");
				return false;
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
