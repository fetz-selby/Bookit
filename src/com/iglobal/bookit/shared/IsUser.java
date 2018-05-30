package com.iglobal.bookit.shared;

public interface IsUser {
	String getId();
	String getEmail();
	String getType();
	String getPersonId();
	String getPassword();
	
	void setId(String id);
	void setEmail(String email);
	void setType(String type);
	void setPersonId(String personId);
	void setPassword(String password);
}
