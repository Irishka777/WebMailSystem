package com.tsystems.javaschool.webmailsystem.dto;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 */
public class UserDTO {
	private long id;
	private String firstName;
	private String lastName;
	private Calendar dateOfBirth;
	private String phoneNumber;

	public UserDTO(String firstName, String lastName, Date dateOfBirth, String phoneNumber) {
		this.firstName = firstName;
		this.lastName = lastName;
		if (dateOfBirth != null) {
			this.dateOfBirth.setTime(dateOfBirth);
		} else {
			this.dateOfBirth = null;
		}
//		this.dateOfBirth = stringToDate(dateOfBirth);
		this.phoneNumber = phoneNumber;
	}

	public UserDTO(long id, String firstName, String lastName, Calendar dateOfBirth, String phoneNumber) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.phoneNumber = phoneNumber;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Calendar getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Calendar dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String dateToString() {
		return dateOfBirth.get(Calendar.DAY_OF_MONTH)+
				"." + dateOfBirth.get(Calendar.MONTH) +
				"." + dateOfBirth.get(Calendar.YEAR);
	}

	public Calendar stringToDate(String dateOfBirth) {

		String[] dayMonthYear = dateOfBirth.split("\\.");
		Calendar calendarDateOfBirth = new GregorianCalendar(Integer.parseInt(dayMonthYear[2]),
				(Integer.parseInt(dayMonthYear[1])-1),Integer.parseInt(dayMonthYear[0]));
		return  calendarDateOfBirth;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}
