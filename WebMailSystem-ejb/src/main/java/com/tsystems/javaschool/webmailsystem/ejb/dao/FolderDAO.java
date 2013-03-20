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

	public void create(Folder folder) {
		entityManager.persist(folder);
	}
	
	public void delete(Folder folder) {
		entityManager.remove(folder);
	}

	public void update(long folderId) {
		Folder folder = getFolder(folderId);
		entityManager.merge(folder);
	}

	public void update(Folder folder) {
		entityManager.merge(folder);
	}
	
	public Folder getFolder(Folder folder) {
		return entityManager.find(Folder.class, folder.getId());
	}

	public Folder getFolder(long folderId) {
		return entityManager.find(Folder.class, folderId);
	}

	public List<Message> getMessagesFromFolder(long folderId) {
		Folder folder = getFolder(folderId);
		return folder.getListOfMessages();
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

	public Folder findFolderByFolderNameAndEmail(String folderName, MailBox email) {
		TypedQuery<Folder> query = entityManager
				.createNamedQuery("findFolderByFolderNameAndEmail", Folder.class);
		query.setParameter("folderName", folderName);
		query.setParameter("mailBox", email);
		return query.getSingleResult();
	}
}
