package com.tsystems.javaschool.webmailsystem.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("serial")
@Entity
@Table(name = "folder")
@NamedQueries({
	@NamedQuery(name = "findFolderByFolderNameAndEmail", query = "SELECT folder FROM FolderEntity folder WHERE " +
			"folder.folderName = :folderName AND folder.mailBox = :mailBox"),
	@NamedQuery(name = "getFoldersForMailBox", query = "SELECT folder FROM FolderEntity folder WHERE " +
			"folder.mailBox = :mailBox")
})
public class FolderEntity implements Serializable {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	private String folderName;
	
	@OneToOne
	private MailBoxEntity mailBox;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name="folder_id")
	private List<MessageEntity> listOfMessages;
	
	public FolderEntity() {}
	
	public FolderEntity(String folderName, MailBoxEntity mailBox) {
		this.folderName = folderName;
		this.mailBox = mailBox;
		this.listOfMessages = new ArrayList<MessageEntity>();
	}
	
	public void setId(long id) {
		this.id = id;
	}
	public long getId() {
		return id;
	}
	
	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}
	public String getFolderName() {
		return folderName;
	}
	
	public void setEmailAddress(MailBoxEntity emailAddress) {
		this.mailBox = emailAddress;
	}
	public MailBoxEntity getEmailAddress() {
		return mailBox;
	}
	
	public void setListOfMessages(ArrayList<MessageEntity> listOfMessages) {
		this.listOfMessages = listOfMessages;
	}
	public List<MessageEntity> getListOfMessages() {
		return listOfMessages;
	}
	
	public String toString() {
		return getFolderName();
	}
}
