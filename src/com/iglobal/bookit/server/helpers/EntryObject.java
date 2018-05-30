package com.iglobal.bookit.server.helpers;

import com.iglobal.bookit.shared.DataTypeConstants;

public class EntryObject {
	private String field, value;
	private DataTypeConstants dataType;
	
	public EntryObject(String field, String value, DataTypeConstants dataType){
		this.field = field;
		this.value = value;
		this.dataType = dataType;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
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
	
}
