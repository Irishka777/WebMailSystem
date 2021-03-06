package com.tsystems.javaschool.webmailsystem.dto;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 */
public class MessageDTO {
	private long id;
	private boolean messageReadFlag;
	private long folder;
	private String sender;
	private String receiver;
	private String date;
	private String theme;
	private String messageBody;

	public MessageDTO(long id, boolean messageReadFlag, long folder,
					  String sender, String receiver,
					  Calendar date, String theme, String messageBody) {
		this.id = id;
		this.messageReadFlag = messageReadFlag;
		this.folder = folder;
		this.sender = sender;
		this.receiver = receiver;
		this.date = dateToString(date);
		this.theme = theme;
		this.messageBody = messageBody;
	}

	public MessageDTO(String sender, String receiver, String theme, String messageBody) {
//		this.messageReadFlag = true;
		this.sender = sender;
		this.receiver = receiver;
		this.theme = theme;
		this.messageBody = messageBody;
	}

	public String dateToString(Calendar date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat();
		return dateFormat.format(date.getTimeInMillis());
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isMessageReadFlag() {
		return messageReadFlag;
	}

	public void setMessageReadFlag(boolean messageReadFlag) {
		this.messageReadFlag = messageReadFlag;
	}

	public long getFolder() {
		return folder;
	}

	public void setFolder(long folder) {
		this.folder = folder;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getMessageBody() {
		return messageBody;
	}

	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}
}
