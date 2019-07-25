package Exceptions;

public class FileTraverserException extends Exception{

	private static final long serialVersionUID = -8404017760046339555L;

	public FileTraverserException(String message) {
		super(message);
	}

	public FileTraverserException(Throwable cause) {
		super(cause);
	}

}


// FILE (for test two files)