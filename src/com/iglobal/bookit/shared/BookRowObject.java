package com.iglobal.bookit.shared;

import com.google.gwt.user.client.rpc.IsSerializable;


public class BookRowObject implements IsSerializable{
	private String id, name, createdBy, date, status, groups, columnString, displayGrid, systemColumnString, modifiedts, modifiedBy, addColumnString, updateColumnString;
	
	public BookRowObject(){}
	public BookRowObject(String id, String name, String createdBy, String groups, String date, String status, String columnString){
		this.id = id;
		this.name = name;
		this.createdBy = createdBy;
		this.groups = groups;
		this.date = date;
		this.status = status;
		this.columnString = columnString;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getGroups() {
		return groups;
	}

	public void setGroups(String groups) {
		this.groups = groups;
	}
	
	public String getColumnString() {
		return columnString;
	}
	
	public void setColumnString(String columnString) {
		this.columnString = columnString;
	}
	
	public String getDisplayGrid() {
		return displayGrid;
	}
	
	public void setDisplayGrid(String displayGrid) {
		this.displayGrid = displayGrid;
	}
	
	public String getSystemColumnString() {
		return systemColumnString;
	}
	
	public void setSystemColumnString(String systemColumnString) {
		this.systemColumnString = systemColumnString;
	}
	
	public String getModifieds() {
		return modifiedts;
	}
	
	public void setModifiedTs(String modifiedts) {
		this.modifiedts = modifiedts;
	}
	
	public String getModifiedBy() {
		return modifiedBy;
	}
	
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public String getAddColumnString() {
		return addColumnString;
	}
	public void setAddColumnString(String addColumnString) {
		this.addColumnString = addColumnString;
	}
	public String getUpdateColumnString() {
		return updateColumnString;
	}
	public void setUpdateColumnString(String updateColumnString) {
		this.updateColumnString = updateColumnString;
	}
	

	
}
