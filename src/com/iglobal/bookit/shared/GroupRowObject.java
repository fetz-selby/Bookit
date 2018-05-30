package com.iglobal.bookit.shared;

import com.google.gwt.user.client.rpc.IsSerializable;


public class GroupRowObject implements IsSerializable {
	private String id, name, createdBy, date, perms, status, permedName;
	private boolean isNotification;
	
	public GroupRowObject(){}
	public GroupRowObject(String id, String name, String createdBy, String date, String perms, String status){
		this.id = id;
		this.name = name;
		this.createdBy = createdBy;
		this.date = date;
		this.perms = perms;
		this.status = status;
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

	public String getPerms() {
		return perms;
	}

	public void setPerms(String perms) {
		this.perms = perms;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getPermedName() {
		return permedName;
	}
	
	public void setPermedName(String permedName) {
		this.permedName = permedName;
	}
	
	public boolean isNotification() {
		return isNotification;
	}
	
	public void setNotification(boolean isNotification) {
		this.isNotification = isNotification;
	}
	
}
