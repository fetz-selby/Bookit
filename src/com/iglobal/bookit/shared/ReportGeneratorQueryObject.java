package com.iglobal.bookit.shared;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ReportGeneratorQueryObject implements IsSerializable{
	private String tableName, userId;
	private HashMap<String, String> filterWithValueHash, returnFieldsHash;
	private HashMap<String, DataTypeConstants> aliasWithDataTypeConstantHash, fieldsWithDataTypeConstantHash;
	
	private ArrayList<String> orderFieldKeysList, orderFieldValueList;
	
	public ReportGeneratorQueryObject(){}
	public ReportGeneratorQueryObject(String userId, String tableName, HashMap<String, String> filterWithValueHash, HashMap<String, String> returnFieldsHash, HashMap<String, DataTypeConstants> aliasWithDataTypeConstantHash, HashMap<String, DataTypeConstants> fieldsWithDataTypeConstantHash){
		this.tableName = tableName;
		this.userId = userId;
		this.filterWithValueHash = filterWithValueHash;
		this.returnFieldsHash = returnFieldsHash;
		this.aliasWithDataTypeConstantHash = aliasWithDataTypeConstantHash;
		this.fieldsWithDataTypeConstantHash = fieldsWithDataTypeConstantHash;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public HashMap<String, String> getFilterWithValueHash() {
		return filterWithValueHash;
	}

	public void setFilterWithValueHash(HashMap<String, String> filterWithValue) {
		this.filterWithValueHash = filterWithValue;
	}

	public HashMap<String, String> getReturnFieldsHash() {
		return returnFieldsHash;
	}

	public void setReturnFieldsHash(HashMap<String, String> returnFieldsHash) {
		this.returnFieldsHash = returnFieldsHash;
	}
	
	public HashMap<String, DataTypeConstants> getAliasWithDataTypeConstantHash() {
		return aliasWithDataTypeConstantHash;
	}
	
	public void setAliasWithDataTypeConstantHash(
			HashMap<String, DataTypeConstants> aliasWithDataTypeConstantHash) {
		this.aliasWithDataTypeConstantHash = aliasWithDataTypeConstantHash;
	}
	
	public HashMap<String, DataTypeConstants> getFieldsWithDataTypeConstantHash() {
		return fieldsWithDataTypeConstantHash;
	}
	
	public void setFieldsWithDataTypeConstantHash(
			HashMap<String, DataTypeConstants> fieldsWithDataTypeConstantHash) {
		this.fieldsWithDataTypeConstantHash = fieldsWithDataTypeConstantHash;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public ArrayList<String> getOrderFieldKeysList() {
		return orderFieldKeysList;
	}
	
	public void setOrderFieldKeysList(ArrayList<String> orderFieldKeysList) {
		this.orderFieldKeysList = orderFieldKeysList;
	}
	
	public ArrayList<String> getOrderFieldValueList() {
		return orderFieldValueList;
	}
	
	public void setOrderFieldValueList(ArrayList<String> orderFieldValueList) {
		this.orderFieldValueList = orderFieldValueList;
	}
}
