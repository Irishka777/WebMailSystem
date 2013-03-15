package com.tsystems.javaschool.webmailsystem.ejb.dao;

import com.tsystems.javaschool.webmailsystem.entity.Folder;
import com.tsystems.javaschool.webmailsystem.entity.MailBox;
import com.tsystems.javaschool.webmailsystem.entity.Message;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class FolderDAO {

	@PersistenceContext(unitName = "webMailSystem")
	private EntityManager entityManager;

	public boolean create(Folder folder) {
		entityManager.persist(folder);
		return true;
	}
	
	public boolean delete(Folder folder) {
		entityManager.remove(folder);
		return true;
	}
	
	public boolean update(Folder folder) {
		entityManager.merge(folder);
		return true;
	}
	
	public Folder getFolder(Folder folder) {
		return entityManager.find(Folder.class, folder.getId());
	}

	public Folder getFolder(long folderId) {
		return entityManager.find(Folder.class, folderId);
	}
	
	public List<Folder> getFoldersForMailBox(String email) {
		MailBox mailBox = entityManager.find(MailBox.class,email);
		TypedQuery<Folder> query = entityManager.createNamedQuery("getFoldersForMailBox", Folder.class);
		query.setParameter("mailBox", mailBox);
		return query.getResultList();
	}

	public List<Message> getMessagesFromFolder(String folderName, String email) {
		TypedQuery<Folder> query = entityManager
				.createNamedQuery("findFolderByFolderNameAndEmail", Folder.class);
		query.setParameter("folderName", folderName);
		query.setParameter("mailBox", email);
		Folder folder = query.getSingleResult();
		return folder.getListOfMessages();
	}

//	public List<Message> getMessagesFromFolder(Folder folder) {
//		folder = entityManager.find(Folder.class,folder.getId());
//		return folder.getListOfMessages();
//	}

	public Folder findFolderByFolderNameAndEmail(String folderName, MailBox email) {
		TypedQuery<Folder> query = entityManager
				.createNamedQuery("findFolderByFolderNameAndEmail", Folder.class);
		query.setParameter("folderName", folderName);
		query.setParameter("mailBox", email);
		return query.getSingleResult();
	}
}
