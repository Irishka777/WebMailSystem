package com.tsystems.javaschool.webmailsystem.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
@Entity
@Table(name = "user")
public class UserEntity implements Serializable {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	private String firstName;
	private String lastName;

	@Temporal(TemporalType.DATE)
	private Date dateOfBirth;

	private String phoneNumber;
	
	public UserEntity() {}
	
	public UserEntity(String firstName, String lastName, Date dateOfBirth, String phoneNumber) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.phoneNumber = phoneNumber;
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
	
	public void setDate(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public Date getDate() {
		return dateOfBirth;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
}
