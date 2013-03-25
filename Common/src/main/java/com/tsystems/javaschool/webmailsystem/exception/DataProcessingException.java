package com.tsystems.javaschool.webmailsystem.exception;

import javax.ejb.ApplicationException;

/**
 *
 */
@ApplicationException(rollback=true)
public class DataProcessingException extends RuntimeException {

	private ExceptionType exceptionType;

	public DataProcessingException(ExceptionType exceptionType) {
		this.exceptionType = exceptionType;
	}

	public DataProcessingException(ExceptionType exceptionType, Throwable cause) {
		super(cause);
		this.exceptionType = exceptionType;
	}

	public String getExceptionMessage() {
		switch (exceptionType) {
			case mailBoxDoesNotExist:
				return "Mailbox with such email does not exist";
			case mailBoxWithSuchANameAlreadyExists:
				return "Mailbox with such a name already exists; try to use another email";
			case wrongEmailOrPassword:
				return "Wrong email or password";

			case folderDoesNotExist:
				return "Folder with such a name does not exist";
			case folderWithSuchANameAlreadyExists:
				return "Folder with such a name already exists";

			case messageDoesNotExist:
				return "One of selected messages have been deleted by some one else";
			case wrongMessageReceiverEmail:
				return "Receiver with such email does not exist; message saved in draft messages";

			case noSuchAlgorithmException:
				return "noSuchAlgorithmException";
			case unexpectedException:
				default:
				return "System error";
		}
	}



}
