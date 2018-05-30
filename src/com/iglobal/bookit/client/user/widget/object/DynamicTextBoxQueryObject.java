package com.iglobal.bookit.client.user.widget.object;

import com.google.gwt.user.client.ui.FileUpload;


public class DynamicTextBoxQueryObject {
	private String fieldName, fieldValue, dataType;
	private FileUpload fileUpload;

	public DynamicTextBoxQueryObject(){}
	public DynamicTextBoxQueryObject(String fieldName, String fieldValue){
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}
	
	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}
	
	public String getDataType() {
		return dataType;
	}
	
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
	public FileUpload getFileUpload() {
		return fileUpload;
	}
	public void setFileUpload(FileUpload fileUpload) {
		this.fileUpload = fileUpload;
	}
	
}
