package com.tsystems.javaschool.webmailsystem.ejb.dao;

import com.tsystems.javaschool.webmailsystem.entity.FolderEntity;
import com.tsystems.javaschool.webmailsystem.entity.MailBoxEntity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class FolderDAO {

	@PersistenceContext(unitName = "webMailSystem")
	private EntityManager entityManager;

	public boolean insert(FolderEntity folder) {
		entityManager.persist(folder);
		return true;
	}
	
	public boolean delete(FolderEntity folder) {
		entityManager.remove(folder);
		return true;
	}
	
	public boolean update(FolderEntity folder) {
		entityManager.merge(folder);
		return true;
	}
	
	public FolderEntity getFolder(FolderEntity folder) {
		return entityManager.find(FolderEntity.class, folder.getId());
	}
	
	public List<FolderEntity> findFoldersForMailBox(MailBoxEntity mailBox) {
		TypedQuery<FolderEntity> query = entityManager.createNamedQuery("getFoldersForMailBox", FolderEntity.class);
		query.setParameter("mailBox", mailBox);
		return query.getResultList();
	}
	
	public FolderEntity findFolderByFolderNameAndEmail(String folderName, MailBoxEntity emailAddress) {
		TypedQuery<FolderEntity> query = entityManager
				.createNamedQuery("findFolderByFolderNameAndEmail", FolderEntity.class);
		query.setParameter("mailBox", emailAddress);
		query.setParameter("folderName", folderName);
		return query.getSingleResult();
	}
}
