package com.tsystems.javaschool.webmailsystem.ejb.dao;

import com.tsystems.javaschool.webmailsystem.entity.FolderEntity;
import com.tsystems.javaschool.webmailsystem.entity.MailBoxEntity;

import java.util.List;

public interface FolderDAO {
	public boolean insert(FolderEntity folder);
	public boolean delete(FolderEntity folder);
	public boolean update(FolderEntity folder);
	public List<FolderEntity> findFoldersForMailBox(MailBoxEntity mailBox);
	public FolderEntity getFolder(FolderEntity folder);
	public FolderEntity findFolderByFolderNameAndEmail(String folderName, MailBoxEntity emailAddress);
}
