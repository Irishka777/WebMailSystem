package com.tsystems.javaschool.webmailsystem.exception;

/**
 *
 */
public enum ExceptionType {
	mailBoxDoesNotExist, mailBoxWithSuchANameAlreadyExists, wrongEmailOrPassword,
	folderDoesNotExist, folderWithSuchANameAlreadyExists,
	messageDoesNotExist, wrongMessageReceiverEmail,
	noSuchAlgorithmException,
	unexpectedException
}
