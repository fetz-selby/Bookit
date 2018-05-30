package com.iglobal.bookit.shared;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DynamicRecordObject implements IsSerializable{
	private ArrayList<HashMap<String, String>> rows;
	private HashMap<String, String> aliasMap;
	private ArrayList<HashMap<String, DataTypeConstants>> dataTypeListRows;

	
	public DynamicRecordObject(){}
	public DynamicRecordObject(ArrayList<HashMap<String, String>> rows, HashMap<String, String> aliasMap, ArrayList<HashMap<String, DataTypeConstants>> dataTypeListRows){
		this.rows = rows;
		this.aliasMap = aliasMap;
		this.dataTypeListRows = dataTypeListRows;
	}
	public ArrayList<HashMap<String, String>> getRows() {
		return rows;
	}
	public void setRows(ArrayList<HashMap<String, String>> rows) {
		this.rows = rows;
	}
	public HashMap<String, String> getAliasMap() {
		return aliasMap;
	}
	public void setAliasMap(HashMap<String, String> aliasMap) {
		this.aliasMap = aliasMap;
	}
	public ArrayList<HashMap<String, DataTypeConstants>> getDataTypeListRows() {
		return dataTypeListRows;
	}
	public void setDataTypeListRows(
			ArrayList<HashMap<String, DataTypeConstants>> dataTypeListRows) {
		this.dataTypeListRows = dataTypeListRows;
	}
	
}
