package com.tsystems.javaschool.webmailsystem.ejb.dao;

import com.tsystems.javaschool.webmailsystem.entity.MessageEntity;

public interface MessageDAO {
	public boolean send(MessageEntity message);
	public boolean save(MessageEntity message);
	public boolean delete(MessageEntity message);
}
