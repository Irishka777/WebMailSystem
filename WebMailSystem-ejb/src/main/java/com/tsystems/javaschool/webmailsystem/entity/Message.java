package com.tsystems.javaschool.webmailsystem.entity;

import com.tsystems.javaschool.webmailsystem.dto.MessageDTO;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;

@Entity
public class Message implements Serializable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

	@ManyToOne(optional = false)
	private Folder folder;

	@Column(columnDefinition = "tinyint")
	private boolean messageReadFlag;

	@OneToOne
	private MailBox sender;
	
	@OneToOne
	private MailBox receiver;

	@Temporal(TemporalType.TIMESTAMP)
	private Calendar date;

	private String theme;
	
	@Column(columnDefinition = "TEXT")
	private String messageBody;

	public Message() {}
	
	public Message(MailBox sender, MailBox receiver, String theme, String messageBody) {
		this.messageReadFlag = false;
		this.sender = sender;
		this.receiver = receiver;
		date = Calendar.getInstance();
		this.theme = theme;
		this.messageBody = messageBody;
	}

	public Message(boolean  messageReadFlag, MailBox sender, MailBox receiver, String theme, String messageBody) {
		this.messageReadFlag = messageReadFlag;
		this.sender = sender;
		this.receiver = receiver;
		date = Calendar.getInstance();
		this.theme = theme;
		this.messageBody = messageBody;
	}
	
	public Message(Message message, Folder folder) {
		this.folder = folder;
		this.messageReadFlag = false;
		this.sender = message.getSender();
		this.receiver = message.getReceiver();
		this.date = message.getDate();
		this.theme = message.getTheme();
		this.messageBody = message.getMessageBody();
	}

	public MessageDTO getMessageDTO() {
		return new MessageDTO(id, messageReadFlag, folder.getFolderName(),
				sender.getEmail(), receiver.getEmail(), date, theme, messageBody);
	}
	
	public void setId(long id) {
		this.id = id;
	}
	public long getId() {
		return id;
	}

	public Folder getFolder() {
		return folder;
	}

	public void setFolder(Folder folder) {
		this.folder = folder;
	}

	public boolean getMessageReadFlag() {
		return messageReadFlag;
	}

	public void setMessageReadFlag(boolean status) {
		this.messageReadFlag = status;
	}
	
	public void setSender(MailBox sender) {
		this.sender = sender;
	}
	public MailBox getSender() {
		return sender;
	}
	
	public void setReceiver(MailBox receiver) {
		this.receiver = receiver;
	}
	public MailBox getReceiver() {
		return receiver;
	}

	public void setDate(Calendar currentDate) {
		date = currentDate;
	}

	public Calendar getDate() {
		return date;
	}
	
	public void setTheme(String theme) {
		this.theme = theme;
	}
	public String getTheme() {
		return theme;
	}
	
	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}
	public String getMessageBody() {
		return messageBody;
	}	
}