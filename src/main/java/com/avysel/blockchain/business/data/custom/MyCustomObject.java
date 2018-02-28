package com.avysel.blockchain.business.data.custom;

/**
 * Example of custom object to store in blockchain
 */
public class MyCustomObject {

	private String name;
	private int number;
	private boolean isOrNot;
	
	public MyCustomObject(String name, int number, boolean isOrNot) {
		super();
		this.name = name;
		this.number = number;
		this.isOrNot = isOrNot;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public boolean isOrNot() {
		return isOrNot;
	}
	public void setOrNot(boolean isOrNot) {
		this.isOrNot = isOrNot;
	}
}
