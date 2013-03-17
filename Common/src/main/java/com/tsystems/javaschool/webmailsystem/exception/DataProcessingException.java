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
			case mailBoxWithSuchANameAlreadyExists:
				return "mailBoxWithSuchANameAlreadyExists";
			case wrongEmailOrPassword:
				return "wrongEmailOrPassword";
			case wrongMessageReceiverEmail:
				return "wrongMessageReceiverEmail";
			case NoSuchFolderException:
			case unexpectedException:
				default:
				return "unexpectedException";
		}
	}



}
