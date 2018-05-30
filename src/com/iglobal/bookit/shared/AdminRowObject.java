package com.iglobal.bookit.shared;

import com.google.gwt.user.client.rpc.IsSerializable;


public class AdminRowObject implements IsSerializable{
	private String id, name, email, perms, createdBy, date, status, password, personId, systemPassword;
	private boolean isSuperAdmin = false;
	
	public AdminRowObject(){}
	public AdminRowObject(String id, String name, String email, String perms, String createdBy, String date, String status, String password, String personId, String systemPassword){
		this.id = id;
		this.name = name;
		this.email = email;
		this.perms = perms;
		this.createdBy = createdBy;
		this.date = date;
		this.status = status;
		this.password = password;
		this.personId = personId;
		this.systemPassword = systemPassword;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPerms() {
		return perms;
	}

	public void setPerms(String perms) {
		this.perms = perms;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPersonId() {
		return personId;
	}
	
	public void setPersonId(String personId) {
		this.personId = personId;
	}
	
	public String getSystemPassword() {
		return systemPassword;
	}
	
	public void setSystemPassword(String systemPassword) {
		this.systemPassword = systemPassword;
	}
	
	public boolean isSuperAdmin() {
		return isSuperAdmin;
	}
	
	public void setSuperAdmin(boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}
	
}
