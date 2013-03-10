package com.tsystems.javaschool.webmailsystem.ejb.dao;

import com.tsystems.javaschool.webmailsystem.entity.FolderEntity;
import com.tsystems.javaschool.webmailsystem.entity.MessageEntity;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Local(FolderDAO.class)
public class MessageDAOBean implements MessageDAO {

	@PersistenceContext(unitName = "defaultPersistenceUnit")
	private EntityManager entityManager;

	@EJB
	private FolderDAO folderDAO;

	public boolean send(MessageEntity message) {
		FolderEntity senderFolder = folderDAO.findFolderByFolderNameAndEmail("Outbox", message.getSender());
		senderFolder.getListOfMessages().add(message);
		
		FolderEntity receiverFolder = folderDAO.findFolderByFolderNameAndEmail("Inbox", message.getReceiver());
		receiverFolder.getListOfMessages().add(new MessageEntity(message));

		entityManager.merge(senderFolder);
		entityManager.merge(receiverFolder);
		return true;
	}
	
	public boolean save(MessageEntity message) {
		FolderEntity senderFolder = folderDAO.findFolderByFolderNameAndEmail("Draft", message.getSender());
		senderFolder.getListOfMessages().add(message);

		entityManager.merge(senderFolder);
		return true;
	}
	
	public boolean delete(MessageEntity message) {
		entityManager.remove(entityManager.merge(message));
		return true;
	}
}
