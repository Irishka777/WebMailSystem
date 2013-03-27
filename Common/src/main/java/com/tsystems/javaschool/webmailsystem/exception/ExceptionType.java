package com.tsystems.javaschool.webmailsystem.exception;

/**
 *
 */
public enum ExceptionType {
	mailBoxDoesNotExist, mailBoxWithSuchANameAlreadyExists, wrongEmailOrPassword,
	folderDoesNotExist, folderWithSuchANameAlreadyExists, systemFolderDeletion, systemFolderRenaming,
	messageDoesNotExist, wrongMessageReceiverEmail,
	noSuchAlgorithmException,
	unexpectedException
}
