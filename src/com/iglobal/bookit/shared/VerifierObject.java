package com.iglobal.bookit.shared;

import com.google.gwt.user.client.rpc.IsSerializable;


public class VerifierObject implements IsSerializable{
	private String id, name;
	private boolean isExist;
	
	public VerifierObject(){}
	
	public VerifierObject(String id, String name, boolean isExist){
		this.id = id;
		this.name = name;
		this.isExist = isExist;
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

	public boolean isExist() {
		return isExist;
	}

	public void setExist(boolean isExist) {
		this.isExist = isExist;
	}
	
	
}
