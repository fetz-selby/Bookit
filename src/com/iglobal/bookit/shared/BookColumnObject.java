package com.iglobal.bookit.shared;

import com.google.gwt.user.client.rpc.IsSerializable;


public class BookColumnObject implements IsSerializable{
	private String id, alias, dataType, tableName, columnName, status, description;
	private boolean isLockable;
	
	public BookColumnObject(){}
	public BookColumnObject(String id, String alias, String dataType, String status, String tableName, String columnName, String description){
		this.id = id;
		this.alias = alias;
		this.dataType = dataType;
		this.status = status;
		this.tableName = tableName;
		this.columnName = columnName;
		this.description = description;
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
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isLockable() {
		return isLockable;
	}
	public void setLockable(boolean isLockable) {
		this.isLockable = isLockable;
	}
}
