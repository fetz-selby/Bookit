package com.iglobal.bookit.shared;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;

public class User implements IsSerializable, IsUser{
	private String id, name, email, type, personId, password;
	private ArrayList<String> permsList = new ArrayList<String>();
	private boolean isSuperUser = false;
	
	public User(){}
	public User(String id, String personId, String name, String email, String type){
		this.id = id;
		this.personId = personId;
		this.name = name;
		this.email = email;
		this.type = type;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<String> getPermsList() {
		return permsList;
	}
	public void setPermsList(ArrayList<String> permsList) {
		this.permsList = permsList;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean isSuperUser() {
		return isSuperUser;
	}
	public void setSuperUser(boolean isSuperUser) {
		this.isSuperUser = isSuperUser;
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
