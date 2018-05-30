package com.iglobal.bookit.shared;

import com.google.gwt.user.client.rpc.IsSerializable;


public class QueryObject implements IsSerializable{
	
	private String value, orderField;
	private boolean isReturnableField;
	private QueryOperatorEnum operator, joinOperator;
	
	
	private QueryEnum enumField;
	
	public QueryObject(){}
	
	public QueryObject(QueryEnum enumField, String value, boolean isReturnableField, QueryOperatorEnum operator){
		this.enumField = enumField;
		this.value = value;
		this.isReturnableField = isReturnableField;
		this.operator = operator;
	}
	
	public QueryEnum getEnumField() {
		return enumField;
	}
	public void setEnumField(QueryEnum enumField) {
		this.enumField = enumField;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public boolean isReturnableField() {
		return isReturnableField;
	}
	public void setReturnableField(boolean isReturnableField) {
		this.isReturnableField = isReturnableField;
	}
	public QueryOperatorEnum getOperator() {
		return operator;
	}
	public void setOperator(QueryOperatorEnum operator) {
		this.operator = operator;
	}
	public QueryOperatorEnum getJoinOperator() {
		return joinOperator;
	}
	public void setJoinOperator(QueryOperatorEnum joinOperator) {
		this.joinOperator = joinOperator;
	}
	public String getOrderField() {
		return orderField;
	}
	public void setOrderField(String orderField) {
		this.orderField = orderField;
	}
}
