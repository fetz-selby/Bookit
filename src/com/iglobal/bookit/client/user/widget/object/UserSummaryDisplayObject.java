package com.iglobal.bookit.client.user.widget.object;

import com.iglobal.bookit.shared.DataTypeConstants;

public class UserSummaryDisplayObject {
	private String id, tableName, columnName, alias, value;
	private DataTypeConstants dataType;
	private boolean canEdit, isSystem;
	
	public UserSummaryDisplayObject(){}
	public UserSummaryDisplayObject(String id, String tableName, String columnName, String alias, String value, DataTypeConstants dataType, boolean canEdit){
		this.id = id;
		this.tableName = tableName;
		this.columnName = columnName;
		this.alias = alias;
		this.value = value;
		this.dataType = dataType;
		this.canEdit = canEdit;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public DataTypeConstants getDataType() {
		return dataType;
	}

	public void setDataType(DataTypeConstants dataType) {
		this.dataType = dataType;
	}

	public boolean isCanEdit() {
		return canEdit;
	}

	public void setCanEdit(boolean canEdit) {
		this.canEdit = canEdit;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getTableName() {
		return tableName;
	}
	
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public boolean isSystem() {
		return isSystem;
	}
	
	public void setSystem(boolean isSystem) {
		this.isSystem = isSystem;
	}
	
}
