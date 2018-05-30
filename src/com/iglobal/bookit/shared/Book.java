package com.iglobal.bookit.shared;

import com.google.gwt.user.client.rpc.IsSerializable;


public class Book implements IsSerializable{
	private String id, name, createdBy, createdTs, modifiedTs, status;
	
	public Book(){}
	public Book(String id, String name, String createdBy, String createdTs, String modifiedTs, String status){
		this.id = id;
		this.name = name;
		this.createdBy = createdBy;
		this.createdTs = createdTs;
		this.modifiedTs = modifiedTs;
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
	public String getCreatedTs() {
		return createdTs;
	}
	public void setCreatedTs(String createdTs) {
		this.createdTs = createdTs;
	}
	public String getModifiedTs() {
		return modifiedTs;
	}
	public void setModifiedTs(String modifiedTs) {
		this.modifiedTs = modifiedTs;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
