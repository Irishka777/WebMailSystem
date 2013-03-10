package com.tsystems.javaschool.webmailsystem.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;


@SuppressWarnings("serial")
@Entity
@Table(name = "mailbox")
@NamedQuery(name = "findByEmail", query = "SELECT box FROM MailBoxEntity box WHERE box.email = :address")
public class MailBoxEntity implements Serializable {
	@Id
	@GeneratedValue
	private long id;
	
	@Column(nullable = false, unique = true)
	private String email;
	
	@Column(columnDefinition = "binary(50)")
	private byte[] password;
	
	private Calendar creationDate;
	
	@OneToOne(cascade = CascadeType.ALL)
	private UserEntity user;
	
	public MailBoxEntity() {}
	
	public MailBoxEntity(String email, String password, UserEntity user) {
		this.email = email.toLowerCase();

		try {
			String salt = "qwertyuiopasdfghjklzxcvbnm";
			password = password+salt;
			
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			byte[] bytesOfPassword;
			bytesOfPassword = messageDigest.digest(password.getBytes(Charset.forName("UTF8")));
			
			this.password = new byte[50];
			for (int i = 0; i < bytesOfPassword.length; i++) {
				this.password[i] = bytesOfPassword[i];
			}
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		creationDate = Calendar.getInstance();
		this.user = user;
	}

	public static boolean comparePasswords(byte[] password1, byte[] password2) {
		if (password1.length != password2.length) {
			return false;
		}
		for (int i = 0; i < password1.length; i++) {
			if (password1[i] != password2[i]) {
				return false;
			}
		}
		return true;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	public long getId() {
		return id;
	}
	
	public void setEmail(String email) {
		this.email = email.toLowerCase();
	}
	public String getEmail() {
		return email;
	}
	
//	public void setPassword(String password) {
//		this.password = password;
//	}
	public byte[] getPassword() {
		return password;
	}
	
	public void setCreationDate(Calendar currentDate) {
		creationDate = currentDate;
	}
	public Calendar getCreationDate() {
		return creationDate;
	}
	
	public void setUser(UserEntity user) {
		this.user = user;
	}
	public UserEntity getUser() {
		return user;
	}
	
	public String toString() {
		return getEmail();
	}
}