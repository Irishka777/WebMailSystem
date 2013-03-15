package com.tsystems.javaschool.webmailsystem.dto;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 */
public class MessageDTO {
	private long id;
	private boolean messageReadFlag;
	private String folder;
	private String sender;
	private String receiver;
	private String date;
//	private Calendar date;
	private String theme;
	private String messageBody;

	public MessageDTO(long id, boolean messageReadFlag,String folder,
					  String sender, String receiver,
					  Calendar date, String theme, String messageBody) {
		this.id = id;
		this.messageReadFlag = messageReadFlag;
		this.folder = folder;
		this.sender = sender;
		this.receiver = receiver;
		this.date = date.getTime().toString();
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



//	public Calendar stringToDate(String dateOfBirth) {
//		String[] dayMonthYear = dateOfBirth.split("\\.");
//		Calendar calendarDateOfBirth = new GregorianCalendar(Integer.parseInt(dayMonthYear[2]),
//				Integer.parseInt(dayMonthYear[1]),Integer.parseInt(dayMonthYear[0]));
//		return  calendarDateOfBirth;
//	}

	public String dateToString(Calendar date) {
		return  date.getTime().toString();
//		return date.get(Calendar.DAY_OF_MONTH)+
//				"." + date.get(Calendar.MONTH) +
//				"." + dateOfBirth.get(Calendar.YEAR);
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

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
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
