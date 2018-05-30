package com.iglobal.bookit.shared;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ReportFilterLoadObject implements IsSerializable{
	private HashMap<String, ArrayList<String>> itemsHash;
	private HashMap<String, DataTypeConstants> fieldDataTypeHash;
	
	public ReportFilterLoadObject(){}
	public ReportFilterLoadObject(HashMap<String, ArrayList<String>> itemsHash, HashMap<String, DataTypeConstants> fieldDataTypeHash){
		this.itemsHash = itemsHash;
		this.fieldDataTypeHash = fieldDataTypeHash;
	}

	public HashMap<String, ArrayList<String>> getItemsHash() {
		return itemsHash;
	}
	public void setItemsHash(HashMap<String, ArrayList<String>> itemsHash) {
		this.itemsHash = itemsHash;
	}
	public HashMap<String, DataTypeConstants> getFieldDataTypeHash() {
		return fieldDataTypeHash;
	}
	public void setFieldDataTypeHash(
			HashMap<String, DataTypeConstants> fieldDataTypeHash) {
		this.fieldDataTypeHash = fieldDataTypeHash;
	}
	
}
