package com.tsystems.javaschool.webmailsystem.entity;

import com.tsystems.javaschool.webmailsystem.dto.MailBoxDTO;
import com.tsystems.javaschool.webmailsystem.exception.DataProcessingException;
import com.tsystems.javaschool.webmailsystem.exception.ExceptionType;

import javax.persistence.*;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

@Entity
@NamedQuery(name = "findByEmail", query = "SELECT box FROM MailBox box WHERE box.email = :address")
public class MailBox implements Serializable {
	@Id
	@Column(columnDefinition = "varchar(30)")
	private String email;
	
	@Column(columnDefinition = "binary(60)")
	private byte[] password;

	@Temporal(TemporalType.TIMESTAMP)
	private Calendar creationDate;
	
	@OneToOne(cascade = CascadeType.ALL)
	private User user;
	
	public MailBox() {}
	
	public MailBox(String email, String password, User user) throws DataProcessingException {
		this.email = email.toLowerCase();
		this.password = convertPasswordStringIntoBytesArrayUsingMD5AndSalt(password);
		this.creationDate = Calendar.getInstance();
		this.user = user;
	}

	public static byte[] convertPasswordStringIntoBytesArrayUsingMD5AndSalt(String password)
			throws DataProcessingException {
		try {
			String salt = "qwertyuiopasdfghjklzxcvbn";
			password = password+salt;

			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			byte[] bytesOfPassword = messageDigest.digest(password.getBytes(Charset.forName("UTF8")));
			byte[] encodedPassword = new byte[60];
			for (int i = 0; i < bytesOfPassword.length; i++) {
				encodedPassword[i] = bytesOfPassword[i];
			}
			return encodedPassword;

		} catch (NoSuchAlgorithmException e) {
			throw new DataProcessingException(ExceptionType.NoSuchAlgorithmException);
		}
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

	public MailBoxDTO getMailBoxDTO() {
		return new MailBoxDTO(email);
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
		this.creationDate = currentDate;
	}
	public Calendar getCreationDate() {
		return creationDate;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	public User getUser() {
		return user;
	}
	
	public String toString() {
		return getEmail();
	}
}