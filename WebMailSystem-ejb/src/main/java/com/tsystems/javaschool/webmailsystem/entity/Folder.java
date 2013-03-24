package com.tsystems.javaschool.webmailsystem.entity;

import com.tsystems.javaschool.webmailsystem.dto.FolderDTO;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"FOLDERNAME","MAILBOX_EMAIL"}))
@NamedQueries({
	@NamedQuery(name = "findFolderByFolderNameAndEmail", query = "SELECT folder FROM Folder folder WHERE " +
			"folder.folderName = :folderName AND folder.mailBox = :mailBox"),
	@NamedQuery(name = "getFoldersForMailBox", query = "SELECT folder FROM Folder folder WHERE " +
			"folder.mailBox = :mailBox")
})
public class Folder implements Serializable{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

	@Column(name = "FOLDERNAME", columnDefinition = "varchar(30)")
	private String folderName;
	
	@OneToOne
	@JoinColumn(name = "MAILBOX_EMAIL")
	private MailBox mailBox;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "folder", orphanRemoval = true)
	private List<Message> listOfMessages;
	
	public Folder() {}
	
	public Folder(String folderName, MailBox mailBox) {
		this.folderName = folderName;
		this.mailBox = mailBox;
		this.listOfMessages = new ArrayList<Message>();
	}

	public FolderDTO getFolderDTO() {
		return new FolderDTO(id, folderName);
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
	
	public void setMailBox(MailBox mailBox) {
		this.mailBox = mailBox;
	}
	public MailBox getMailBox() {
		return mailBox;
	}
	
	public void setListOfMessages(ArrayList<Message> listOfMessages) {
		this.listOfMessages = listOfMessages;
	}
	public List<Message> getListOfMessages() {
		return listOfMessages;
	}
	
	public String toString() {
		return getFolderName();
	}
}
