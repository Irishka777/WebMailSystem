package com.tsystems.javaschool.webmailsystem.ejb.dao;

import com.tsystems.javaschool.webmailsystem.entity.Folder;
import com.tsystems.javaschool.webmailsystem.entity.Message;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class MessageDAO {

	@PersistenceContext(unitName = "webMailSystem")
	private EntityManager entityManager;

	@EJB
	private FolderDAO folderDAO;

	public void send(Message message) {
		Folder senderFolder = folderDAO.findFolderByFolderNameAndEmail("Outbox", message.getSender());
		message.setFolder(senderFolder);
		senderFolder.getListOfMessages().add(message);
		
		Folder receiverFolder = folderDAO.findFolderByFolderNameAndEmail("Inbox", message.getReceiver());
		receiverFolder.getListOfMessages().add(new Message(message,receiverFolder));

		entityManager.merge(senderFolder);
		entityManager.merge(receiverFolder);
	}
	
	public void save(Message message) {
		Folder senderFolder = folderDAO.findFolderByFolderNameAndEmail("Draft", message.getSender());
		message.setFolder(senderFolder);
		senderFolder.getListOfMessages().add(message);

		entityManager.merge(senderFolder);
	}
	
	public void delete(Message message) {
		entityManager.remove(entityManager.merge(message));
	}

	public void delete(long id) {
		Message message = entityManager.find(Message.class, id);
		entityManager.remove(entityManager.merge(message));
	}

	public void move(Message message) {
		entityManager.merge(message);
	}

	public Message findMessage(long id) {
		return entityManager.find(Message.class, id);
	}

	public List<Message> getMessagesFromFolder(long folderId) {
		Folder folder = folderDAO.getFolder(folderId);
		return folder.getListOfMessages();
	}
}
