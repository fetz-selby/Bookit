package com.iglobal.bookit.server.utils;

import java.util.ArrayList;

import com.iglobal.bookit.shared.QueryObject;

public class QueryMagic {
	private String queryFilterString, queryReturnFieldsString, queryTablesString, queryOrderString;
	private ArrayList<QueryObject> queryList;
	
	public QueryMagic(ArrayList<QueryObject> queryList){
		this.queryList = queryList;
		doInit(queryList);
	}
	
	private void doInit(ArrayList<QueryObject> queryList){
		queryFilterString = Utils.getQueryFilterString(queryList);
		queryReturnFieldsString = Utils.getQueryReturnFields(queryList);
		queryTablesString = Utils.getQueryTables(queryList);
		queryOrderString = Utils.getQueryOrder(queryList);
	}

	public String getQueryFilterString() {
		return queryFilterString;
	}

	public void setQueryFilterString(String queryFilterString) {
		this.queryFilterString = queryFilterString;
	}

	public String getQueryReturnFieldsString() {
		return queryReturnFieldsString;
	}

	public void setQueryReturnFieldsString(String queryReturnFieldsString) {
		this.queryReturnFieldsString = queryReturnFieldsString;
	}

	public String getQueryTablesString() {
		return queryTablesString;
	}

	public void setQueryTablesString(String queryTablesString) {
		this.queryTablesString = queryTablesString;
	}

	public ArrayList<QueryObject> getQueryList() {
		return queryList;
	}

	public void setQueryList(ArrayList<QueryObject> queryList) {
		this.queryList = queryList;
	}

	public String getQueryOrderString() {
		return queryOrderString;
	}

	public void setQueryOrderString(String queryOrderString) {
		this.queryOrderString = queryOrderString;
	}
	
}
