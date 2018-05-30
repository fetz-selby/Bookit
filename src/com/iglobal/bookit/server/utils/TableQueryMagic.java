package com.iglobal.bookit.server.utils;


public class TableQueryMagic {
	private String  createTableQuery, alterColumnQuery;
	private String tableName;
	private String bookColumns, alterBookColumn;
	
	public TableQueryMagic(String tableName, String bookColumns, String alterBookColumn){
		this.tableName = tableName;
		this.bookColumns = bookColumns;
		this.alterBookColumn = alterBookColumn;
		doInit();
	}
	
	private void doInit(){
		if(bookColumns == null){
			createTableQuery = null;
		}else{
			createTableQuery = Utils.getCreateTableQuery(tableName, bookColumns);
		}
		
		if(alterBookColumn == null){
			alterColumnQuery = null;
		}else{
			alterColumnQuery = Utils.getAlterTableQuery(tableName, alterBookColumn);
		}
	}

	public String getTableName() {
		return tableName;
	}

	public String getCreateTableQuery() {
		return createTableQuery;
	}

	public String getAlterColumnQuery() {
		return alterColumnQuery;
	}
	
}
