package com.tsystems.javaschool.webmailsystem.ejb.dao;

import com.tsystems.javaschool.webmailsystem.entity.Folder;
import com.tsystems.javaschool.webmailsystem.entity.MailBox;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class FolderDAO {

	@PersistenceContext(unitName = "webMailSystem")
	private EntityManager entityManager;

//	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Folder getFolder(long folderId) {
		return entityManager.find(Folder.class, folderId);
	}

	public void flush() {
		entityManager.flush();
	}

	public List<Folder> getFoldersForMailBox(MailBox mailBox) {
		TypedQuery<Folder> query = entityManager.createNamedQuery("getFoldersForMailBox", Folder.class);
		query.setParameter("mailBox", mailBox);
		return query.getResultList();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void create(Folder folder) {
		entityManager.persist(folder);
	}

	public void delete(Folder folder) {
		entityManager.remove(folder);
	}

	public Folder findFolderByFolderNameAndEmail(String folderName, MailBox email) {
		TypedQuery<Folder> query = entityManager
				.createNamedQuery("findFolderByFolderNameAndEmail", Folder.class);
		query.setParameter("folderName", folderName);
		query.setParameter("mailBox", email);
		return query.getSingleResult();
	}
}