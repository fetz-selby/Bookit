package com.iglobal.bookit.shared;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Admin implements IsSerializable, IsUser{
	private String id, personId, name, email, type, password;
	private ArrayList<String> permsList;
	
	public Admin(){}
	public Admin(String id, String personId, String name, String email, String type, ArrayList<String> permsList){
		this.id = id;
		this.personId = personId;
		this.name = name;
		this.email = email;
		this.permsList = permsList;
		this.type = type;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public ArrayList<String> getPermsList() {
		return permsList;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setPermsList(ArrayList<String> permsList) {
		this.permsList = permsList;
	}
	@Override
	public String getEmail() {
		return email;
	}
	@Override
	public String getType() {
		return type;
	}
	@Override
	public String getPersonId() {
		return personId;
	}
	@Override
	public void setPersonId(String personId) {
		this.personId = personId;
	}
	@Override
	public String getPassword() {
		return password;
	}
	@Override
	public void setPassword(String password) {
		this.password = password;
	}
	
}
