package com.tsystems.javaschool.webmailsystem.ejb.dao;

import com.tsystems.javaschool.webmailsystem.entity.MailBoxEntity;

public interface MailBoxDAO {
	public boolean insert(MailBoxEntity mailBox);
	public MailBoxEntity update(MailBoxEntity mailBox);
	public boolean delete(MailBoxEntity mailBox);
	public MailBoxEntity findByEmail(String emailAddress);
}
