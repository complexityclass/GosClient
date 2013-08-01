package com.example.adapters;

public class PersonalRow {

	String name;
	String value;
	
	public PersonalRow(String name, String value) {
		this.name = name;
		this.value = value;
	}
	

	public String getValue() {
		return value;
	}

	public String getName() {
		return name;
	}
	
	
	public void setValue(String value){
		this.value = value;
	}
	
}
