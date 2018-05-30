package com.iglobal.bookit.shared;

import com.google.gwt.user.client.rpc.IsSerializable;


public enum QueryEnum implements IsSerializable{
	U_ID("users.id", QueryConstants.INT), U_NAME("users.name", QueryConstants.STRING), U_GROUPS("users.groups", QueryConstants.STRING), U_MODIFIED_TS("users.modified_ts", QueryConstants.DATE), U_PERSON_ID("users.person_id", QueryConstants.INT), U_CREATED_TS("users.created_ts", QueryConstants.DATE), U_CREATED_BY("users.created_by", QueryConstants.INT), U_STATUS("users.status", QueryConstants.CHAR),
	A_ID("admins.id", QueryConstants.INT), A_NAME("admins.name", QueryConstants.STRING), A_PERMS("admins.perms", QueryConstants.STRING), A_MODIFIED_TS("admins.modified_ts", QueryConstants.DATE), A_PERSON_ID("admins.person_id", QueryConstants.INT), A_CREATED_BY("admins.created_by", QueryConstants.STRING), A_CREATED_TS("admins.created_ts", QueryConstants.DATE),A_STATUS("admins.status", QueryConstants.CHAR),A_IS_SA("admins.is_sa", QueryConstants.CHAR),
	G_ID("groups.id", QueryConstants.INT), G_NAME("groups.name", QueryConstants.STRING), G_PERMS("groups.perms", QueryConstants.STRING), G_CREATED_TS("groups.created_ts", QueryConstants.DATE), G_CREATED_BY("groups.created_by", QueryConstants.INT), G_MODIFIED_TS("groups.modified_ts", QueryConstants.DATE), G_STATUS("groups.status", QueryConstants.CHAR), G_IS_NOTIFICATION("is_notification", QueryConstants.STRING),
	P_ID("perms.id", QueryConstants.INT), P_NAME("perms.name", QueryConstants.STRING), P_STRING("perms.string", QueryConstants.STRING), P_STATUS("perms.status", QueryConstants.CHAR),P_CATEGORY("perms.category", QueryConstants.CHAR),
	PER_ID("persons.id", QueryConstants.INT), PER_EMAIL("persons.email", QueryConstants.STRING), PER_TYPE("persons.type", QueryConstants.CHAR), PER_PASSWORD("persons.password", QueryConstants.STRING), PER_STATUS("persons.status", QueryConstants.STRING),
	S_ID("sadmins.id", QueryConstants.INT), S_NAME("sadmins.name", QueryConstants.STRING), S_CREATED_TS("sadmins.created_ts", QueryConstants.DATE), S_STATUS("sadmins.status", QueryConstants.CHAR),
	B_ID("books.id", QueryConstants.INT), B_NAME("books.table_name", QueryConstants.STRING), B_GROUPS("books.groups", QueryConstants.STRING), B_CREATED_BY("books.created_by", QueryConstants.INT), B_CREATED_TS("books.created_ts", QueryConstants.DATE), B_MODIFIED_TS("books.modified_ts", QueryConstants.DATE), B_STATUS("books.status", QueryConstants.CHAR), B_TABLE_NAME("books.table_name", QueryConstants.STRING), B_TABLE_COL("books.column_name", QueryConstants.STRING),
	DT_ID("datatypes.id", QueryConstants.INT), DT_ALIAS("datatypes.alias", QueryConstants.STRING), DT_TABLE_NAME("datatypes.table_name", QueryConstants.STRING), DT_COLUMN_NAME("datatypes.column_name", QueryConstants.STRING), DT_DATATYPE("datatypes.dataType", QueryConstants.STRING), DT_DESC("datatypes.description", QueryConstants.STRING), DT_STATUS("datatypes.status", QueryConstants.CHAR),
	LOGIN_ID("app_logins.id", QueryConstants.INT), LOGIN_EMAIL("app_logins.email", QueryConstants.STRING), LOGIN_START_TIME("app_logins.start_time", QueryConstants.STRING), LOGIN_END_TIME("app_logins.end_time", QueryConstants.STRING), LOGIN_STATUS("app_logins.status", QueryConstants.STRING);
	
	private String field;
	private int dataType;
	
	private QueryEnum(String field, int dataType){
		this.field = field;
		this.dataType = dataType;
	}

	public String getField() {
		return field;
	}
	
	public int getDataType(){
		return dataType;
	}

}
