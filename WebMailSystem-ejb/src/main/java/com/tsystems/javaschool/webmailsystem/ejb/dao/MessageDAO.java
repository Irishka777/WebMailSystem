package com.tsystems.javaschool.webmailsystem.ejb.dao;

import com.tsystems.javaschool.webmailsystem.entity.Folder;
import com.tsystems.javaschool.webmailsystem.entity.Message;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class MessageDAO {

	@PersistenceContext(unitName = "webMailSystem")
	private EntityManager entityManager;

	public Message findMessage(long id) {
		return entityManager.find(Message.class, id);
	}

	public List<Message> getNewMessages(Folder folder) {
		TypedQuery<Message> query = entityManager.createNamedQuery("receiveNewMessages", Message.class);
		query.setParameter("folder", folder);
		return query.getResultList();
	}
}