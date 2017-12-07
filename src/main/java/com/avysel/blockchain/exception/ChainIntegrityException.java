package com.avysel.blockchain.exception;

/**
 * Thrown when Chain integrity is not respected
 */
public class ChainIntegrityException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public ChainIntegrityException() {
		super();
	}
	
	public ChainIntegrityException(String message) {
		super(message);
	}
}
