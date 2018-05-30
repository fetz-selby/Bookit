package com.iglobal.bookit.server.utils;

import java.util.HashMap;

public class BookTableQueryMagic {
	private static final String APPENDER = "_";
	private String tableName;
	private HashMap<String, String> fieldValue, fieldDataType, allFieldsHashMap;
	private String fields = "", values = "";
	
	public BookTableQueryMagic(){}
	public BookTableQueryMagic(String tableName, HashMap<String, String> fieldValue, HashMap<String, String> fieldDataType){
		this.tableName = tableName+APPENDER;
		this.fieldValue = fieldValue;
		this.fieldDataType = fieldDataType;
		doProcess();
	}

	public void load(){
		doProcess();
	}
	
	private void doProcess(){
		values = "";
		fields = "";
		
		final String COMMA = ",";
		
		for(String field: fieldValue.keySet()){
			String value = fieldValue.get(field);
			String dataType = fieldDataType.get(field);
			
			fields += field.trim()+APPENDER+COMMA;
			
			if(dataType.trim().equals("INT")){
				values += value+COMMA;
			}else if(dataType.trim().equals("TEXT")){
				values += "'"+value+"'"+COMMA;
			}else if(dataType.trim().equals("DATE")){
				values += "'"+value+"'"+COMMA;
			}else if(dataType.trim().equals("TIME")){
				values += "'"+value+"'"+COMMA;
			}else if(dataType.trim().equals("ALERT")){
				values += "'"+value+"'"+COMMA;
			}else if(dataType.trim().equals("ID")){
				values += value+COMMA;
			}else if(dataType.trim().equals("LOGIN")){
				values += "'"+value+"'"+COMMA;
			}else{
				System.out.println("[dataType] "+dataType);
			}
		}
		
		fields += "created_ts"+COMMA+"status";
		values += "'"+Utils.getTodayDateTime()+"'"+COMMA+"'A'";
		
		//Remove all post comma's
		//fields = fields.substring(0, fields.lastIndexOf(COMMA));
		//values = values.substring(0, values.lastIndexOf(COMMA));
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public HashMap<String, String> getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(HashMap<String, String> fieldValue) {
		this.fieldValue = fieldValue;
	}

	public HashMap<String, String> getFieldDataType() {
		return fieldDataType;
	}

	public void setFieldDataType(HashMap<String, String> fieldDataType) {
		this.fieldDataType = fieldDataType;
	}
	
	public String getFields(){
		return "("+fields+")";
	}
	
	public String getValues(){
		return "("+values+")";
	}
	
	public String getTableName(){
		return tableName;
	}
	
	public String getUpdateConstructedCall(){
		return "insert into "+tableName+" "+getFields()+" values "+getValues();
	}
	
	public String getAddConstructedCall(){
		final String AND = " and ";
		final String LIKE = " like ";
		final String EQUALS = " = ";
		final String COMMA = ",";
		
		String filterQuery = "";
		String fields = "";
		for(String field : fieldValue.keySet()){
			String dataType = fieldDataType.get(field);
			String value = fieldValue.get(field);
			
			if(dataType.trim().equals("INT")){
				filterQuery += field.trim()+APPENDER+EQUALS+value;
			}else if(dataType.trim().equals("TEXT")){
				filterQuery += field.trim()+APPENDER+LIKE+"'%"+value+"%'";
			}else if(dataType.trim().equals("DATE")){
				filterQuery += field.trim()+APPENDER+LIKE+"'%"+value+"%'";
			}else if(dataType.trim().equals("TIME")){
				filterQuery += field.trim()+APPENDER+LIKE+"'%"+value+"%'";
			}else if(dataType.trim().equals("ALERT")){
				filterQuery += field.trim()+APPENDER+LIKE+"'%"+value+"%'";
			}else if(dataType.trim().equals("LOGIN")){
				filterQuery += field.trim()+APPENDER+LIKE+"'%"+value+"%'";
			}else if(dataType.trim().equals("ID")){
				filterQuery += field.trim()+APPENDER+EQUALS+value;
			}
			fields += field.trim()+APPENDER+COMMA;
			filterQuery+=AND;
		}
		
		if(filterQuery.contains(AND) && filterQuery.endsWith(AND)){
			filterQuery = filterQuery.substring(0, filterQuery.lastIndexOf(AND));
			fields = fields.substring(0, fields.lastIndexOf(COMMA));
		}
		
		System.out.println("[BookTableQueryMagic] select * from "+tableName+" where "+filterQuery);
		
		if(filterQuery.trim().isEmpty()){
			return "select * from "+tableName+" where status = 'A'"; 
		}
		
		return "select * from "+tableName+" where "+filterQuery;

		
		//return "select "+fields+" from "+tableName+" where "+filterQuery;
	}
	
	public HashMap<String, String> getAllFieldsHashMap() {
		return allFieldsHashMap;
	}
	public void setAllFieldsHashMap(HashMap<String, String> allFieldsHashMap) {
		this.allFieldsHashMap = allFieldsHashMap;
	}
	
}
