package com.tsystems.javaschool.webmailsystem.ejb.dao;

import com.tsystems.javaschool.webmailsystem.entity.FolderEntity;
import com.tsystems.javaschool.webmailsystem.entity.MailBoxEntity;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
public class MailBoxDAO {

	@PersistenceContext(unitName = "webMailSystem")
	private EntityManager entityManager;

	@EJB
	private FolderDAO folderDAO;

	public boolean insert(MailBoxEntity mailBox) {
		entityManager.persist(mailBox);
	
		MailBoxEntity createdMailBox = findByEmail(mailBox.getEmail());
		folderDAO.insert(new FolderEntity("Inbox",createdMailBox));
		folderDAO.insert(new FolderEntity("Outbox",createdMailBox));
		folderDAO.insert(new FolderEntity("Draft",createdMailBox));
		
		return true;
	}
	
	public MailBoxEntity update(MailBoxEntity mailBox) {
		return entityManager.merge(mailBox);
	}
	
	public boolean delete(MailBoxEntity mailBox) {
		return true;
	}
	
	public MailBoxEntity findByEmail(String email) {
		TypedQuery<MailBoxEntity> query = entityManager.createNamedQuery("findByEmail"
				, MailBoxEntity.class);
		query.setParameter("address", email);
		return query.getSingleResult();
	}
}
