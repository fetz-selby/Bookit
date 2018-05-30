package com.iglobal.bookit.shared;

import com.google.gwt.user.client.rpc.IsSerializable;


public class Person implements IsSerializable{
	private String id, email, password, type, status, createdTs;
	
	public Person(){}
	public Person(String id, String email, String password, String type, String status, String createdTs){
		this.id = id;
		this.email = email;
		this.password = password;
		this.type = type;
		this.status = status;
		this.createdTs = createdTs;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreatedTs() {
		return createdTs;
	}
	public void setCreatedTs(String createdTs) {
		this.createdTs = createdTs;
	}
	
	
}
