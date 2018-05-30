package com.iglobal.bookit.shared;

import com.google.gwt.user.client.rpc.IsSerializable;


public class LicenseObject implements IsSerializable{
	private String numberOfUsers, numberOfTabs;
	
	public LicenseObject(){}
	
	public LicenseObject(String numberOfUsers, String numberOfTabs){
		this.numberOfTabs = numberOfTabs;
		this.numberOfUsers = numberOfUsers;
	}

	public String getNumberOfUsers() {
		return numberOfUsers;
	}

	public void setNumberOfUsers(String numberOfUsers) {
		this.numberOfUsers = numberOfUsers;
	}

	public String getNumberOfTabs() {
		return numberOfTabs;
	}

	public void setNumberOfTabs(String numberOfTabs) {
		this.numberOfTabs = numberOfTabs;
	}
	
}
