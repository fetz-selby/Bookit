package com.iglobal.bookit.shared;

public enum QueryOperatorEnum {
	EQUALS(" = "), LIKE(" like "), OR(" or "), AND(" and "), BETWEEN(" >= : <= ");
	private String operator;

	private QueryOperatorEnum(String operator){
		this.operator = operator;
	}

	public String getOperator() {
		return operator;
	}

}
