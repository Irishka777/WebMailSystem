package com.tsystems.javaschool.webmailsystem.ejb.dao;

import com.tsystems.javaschool.webmailsystem.entity.MailBox;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class MailBoxDAO {

	@PersistenceContext(unitName = "webMailSystem")
	private EntityManager entityManager;

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void create(MailBox mailBox) {
		entityManager.persist(mailBox);
	}

//	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public MailBox find(String email) {
		return entityManager.find(MailBox.class,email);
	}
}
