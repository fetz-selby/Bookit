package com.iglobal.bookit.shared;

import com.google.gwt.user.client.rpc.IsSerializable;


public class DataTypeRowObject implements IsSerializable{
	private String id, alias, tableName, columnName, dataType, description, status;
	public DataTypeRowObject(){}
	public DataTypeRowObject(String id, String alias, String tableName, String columnName, String dataType, String description, String status){
		this.id = id;
		this.alias = alias;
		this.tableName = tableName;
		this.columnName = columnName;
		this.dataType = dataType;
		this.description = description;
		this.status = status;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
