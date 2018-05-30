package com.iglobal.bookit.shared;

public enum DataTypeConstants {
	INT("Integer", "INT"), STRING("Text", "TEXT"), BLOB("Binary", "MEDIUMBLOB"), DATE("Date", "DATE"), DATETIME("Time", "DATETIME"), TIME("Time", "TIME"), ALERT("Alert", "ALERT"), ID("ID", "ID"), LOGIN("Login", "LOGIN");
	
	private String name, value;
	
	private DataTypeConstants(String name, String value){
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
