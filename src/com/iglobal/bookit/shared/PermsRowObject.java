package com.iglobal.bookit.shared;

import com.google.gwt.user.client.rpc.IsSerializable;


public class PermsRowObject implements IsSerializable{
	private String id, name, string, status, category;
	
	public PermsRowObject(){}
	public PermsRowObject(String id, String name, String string, String status){
		this.id = id;
		this.name = name;
		this.status = status;
		this.string = string;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}
	
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
}
