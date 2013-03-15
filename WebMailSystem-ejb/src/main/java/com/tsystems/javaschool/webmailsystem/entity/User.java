package com.tsystems.javaschool.webmailsystem.entity;

import com.tsystems.javaschool.webmailsystem.dto.UserDTO;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;

@Entity
public class User implements Serializable {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

	@Column(columnDefinition = "varchar(30)")
	private String firstName;

	@Column(columnDefinition = "varchar(30)")
	private String lastName;

	@Temporal(TemporalType.DATE)
	private Calendar dateOfBirth;

	@Column(columnDefinition = "varchar(30)")
	private String phoneNumber;
	
	public User() {}
	
	public User(String firstName, String lastName, Calendar dateOfBirth, String phoneNumber) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.phoneNumber = phoneNumber;
	}

	public User(UserDTO user) {
		this.id = user.getId();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.dateOfBirth = user.getDateOfBirth();
		this.phoneNumber = user.getPhoneNumber();
	}

	public UserDTO getUserDTO() {
		return new UserDTO(id,firstName,lastName,dateOfBirth,phoneNumber);
	}
	
	public void setId(long id) {
		this.id = id;
	}
	public long getId() {
		return id;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getFirstName() {
		return firstName;
	}
	
	public void setLastName(String surname) {
		this.lastName = surname;
	}
	public String getLastName() {
		return lastName;
	}
	
	public void setDate(Calendar dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public Calendar getDate() {
		return dateOfBirth;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
}
