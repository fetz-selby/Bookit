package com.iglobal.bookit.client.ui.components.object;

public class SortableObject {
	private String name, value, counterId, previousName, id, fieldName;
	
	public SortableObject(){}
	public SortableObject(String name, String value){
		this.name = name;
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getCounterId() {
		return counterId;
	}
	public void setCounterId(String counterId) {
		this.counterId = counterId;
	}
	public String getPreviousName() {
		return previousName;
	}
	public void setPreviousName(String previousName) {
		this.previousName = previousName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public void setFieldName(String fieldName){
		this.fieldName = fieldName;
	}
	
	public String getFieldName(){
		return fieldName;
	}
}
