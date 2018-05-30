package com.iglobal.bookit.shared;

import com.google.gwt.user.client.rpc.IsSerializable;


public class SessionsRowObject implements IsSerializable{
	private String id, email, startDateTime, endDateTime, status;
	
	public SessionsRowObject(){}
	
	public SessionsRowObject(String id, String email, String startDateTime, String endDateTime, String status){
		this.id = id;
		this.email = email;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.status = status;
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

	public String getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(String startDateTime) {
		this.startDateTime = startDateTime;
	}

	public String getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(String endDateTime) {
		this.endDateTime = endDateTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
