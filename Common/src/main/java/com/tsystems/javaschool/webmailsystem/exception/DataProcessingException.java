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

	public String getExceptionPage() {
		switch (exceptionType) {
			case NoSuchAlgorithmException:
				return "noSuchAlgorithmException";
			case MailBoxWithSuchANameAlreadyExistsException:
			case WrongPasswordException:
			case NoSuchMailBoxException:
				return "loginException";
			case NoSuchFolderException:
			case UnexpectedException:
				default:
				return "unexpectedException";
		}
	}



}
