package com.tsystems.javaschool.webmailsystem.exception;

/**
 *
 */
public class DataProcessingException extends Exception {

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
			case NoSuchAlgorithmException:
				return "noSuchAlgorithmException";
			case mailBoxWithSuchANameAlreadyExists:
				return "Mailbox with such a name already exists; try to use another email";
			case wrongEmailOrPassword:
				return "Wrong email or password";
			case wrongMessageReceiverEmail:
				return "Receiver with such email does not exist; message saved in draft messages";
			case NoSuchFolderException:
			case unexpectedException:
				default:
				return "System error";
		}
	}



}
