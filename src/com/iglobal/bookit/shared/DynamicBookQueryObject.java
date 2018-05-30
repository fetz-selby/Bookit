package com.iglobal.bookit.shared;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DynamicBookQueryObject implements IsSerializable{
	private String tableName, bookId;
	private HashMap<String, String> fieldValueHash;
	private HashMap<String, String> fieldDatatypeHash, allFieldDatatypeHash;
	
	public DynamicBookQueryObject(){}
	public DynamicBookQueryObject(String bookId, String tableName, HashMap<String, String> fieldValueHash, HashMap<String, String> fieldDatatypeHash){
		this.bookId = bookId;
		this.tableName = tableName;
		this.fieldDatatypeHash = fieldDatatypeHash;
		this.fieldValueHash = fieldValueHash;
	}
	public String getBookId() {
		return bookId;
	}
	public void setBookId(String bookId) {
		this.bookId = bookId;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public HashMap<String, String> getFieldValueHash() {
		return fieldValueHash;
	}
	public void setFieldValueHash(HashMap<String, String> fieldValueHash) {
		this.fieldValueHash = fieldValueHash;
	}
	public HashMap<String, String> getFieldDatatypeHash() {
		return fieldDatatypeHash;
	}
	public void setFieldDatatypeHash(HashMap<String, String> fieldDatatypeHash) {
		this.fieldDatatypeHash = fieldDatatypeHash;
	}
	public HashMap<String, String> getAllFieldDatatypeHash() {
		return allFieldDatatypeHash;
	}
	public void setAllFieldDatatypeHash(HashMap<String, String> allFieldDatatypeHash) {
		this.allFieldDatatypeHash = allFieldDatatypeHash;
	}
}
