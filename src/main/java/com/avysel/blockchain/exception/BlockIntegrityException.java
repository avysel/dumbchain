package com.avysel.blockchain.exception;

/**
 * Thrown when Block integrity is not respected.
 */
public class BlockIntegrityException extends Exception {

	private static final long serialVersionUID = -4796121755442700871L;

	public BlockIntegrityException() {
		super();
	}
	
	public BlockIntegrityException(String message) {
		super(message);
	}
}
