package com.tsystems.javaschool.webmailsystem.ejb.dao;

import com.tsystems.javaschool.webmailsystem.entity.Folder;
import com.tsystems.javaschool.webmailsystem.entity.MailBox;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class MailBoxDAO {

	@PersistenceContext(unitName = "webMailSystem")
	private EntityManager entityManager;

	@EJB
	private FolderDAO folderDAO;

	public boolean create(MailBox mailBox) {
		entityManager.persist(mailBox);

		folderDAO.create(new Folder("Inbox", mailBox));
		folderDAO.create(new Folder("Outbox", mailBox));
		folderDAO.create(new Folder("Draft", mailBox));
		
		return true;
	}
	
	public MailBox update(MailBox mailBox) {
		return entityManager.merge(mailBox);
	}
	
	public boolean delete(MailBox mailBox) {
		return true;
	}

	public MailBox find(String email) {
		return entityManager.find(MailBox.class,email);
	}
	
//	public MailBox findByEmail(String email) {
//		TypedQuery<MailBox> query = entityManager.createNamedQuery("findByEmail"
//				, MailBox.class);
//		query.setParameter("address", email);
//		return query.getSingleResult();
//	}
}
