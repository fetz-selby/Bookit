package com.iglobal.bookit.client.user.misc;

import java.util.HashMap;

import com.iglobal.bookit.shared.DataTypeConstants;

public class ReportWizardObject {
	private String bookId, bookName, bookAlias, createdBy, dateCreated, groups, columnString, selectedFields;
	private HashMap<String, DataTypeConstants> dataTypeMap;

	public ReportWizardObject(String bookId, String bookName, String bookAlias, String createdBy, String dateCreated, String groups, String columnString){
		this.bookId = bookId;
		this.bookName = bookName;
		this.bookAlias = bookAlias;
		this.createdBy = createdBy;
		this.dateCreated = dateCreated;
		this.groups = groups;
		this.columnString = columnString;
	}
	
	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public String getBookAlias() {
		return bookAlias;
	}

	public void setBookAlias(String bookAlias) {
		this.bookAlias = bookAlias;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
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

	public HashMap<String, DataTypeConstants> getDataTypeMap() {
		return dataTypeMap;
	}

	public void setDataTypeMap(HashMap<String, DataTypeConstants> dataTypeMap) {
		this.dataTypeMap = dataTypeMap;
	}
	
	public String getSelectedFields() {
		return selectedFields;
	}

	public void setSelectedFields(String selectedFields) {
		this.selectedFields = selectedFields;
	}

	@Override
	public String toString() {
		return bookId + bookName + bookAlias + createdBy + dateCreated + groups + columnString;
	}
	
	
}
