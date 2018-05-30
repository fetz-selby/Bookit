package com.iglobal.bookit.client.user.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.iglobal.bookit.client.user.widget.object.DynamicTextBoxQueryObject;
import com.iglobal.bookit.shared.DataTypeConstants;

public class DynamicAlertCheck extends Composite  implements IsEntryWidget{
	private String labelName, fieldName, dataType;
	//private DataTypeConstants dataTypeId;
	private static DynamicAlertCheckUiBinder uiBinder = GWT
			.create(DynamicAlertCheckUiBinder.class);

	interface DynamicAlertCheckUiBinder extends
			UiBinder<Widget, DynamicAlertCheck> {
	}

	@UiField CheckBox checkBox;
	
	public DynamicAlertCheck(String labelName, String fieldName, String dataType, DataTypeConstants dataTypeId) {
		this.labelName = labelName;
		this.fieldName = fieldName;
		//this.dataTypeId = dataTypeId;
		this.dataType = dataType;
		
		initWidget(uiBinder.createAndBindUi(this));
		initComponent();
	}
	
	private void initComponent(){
		checkBox.setText(labelName);
	}
	
	public DynamicTextBoxQueryObject getQuery(){
		DynamicTextBoxQueryObject queryObject = new DynamicTextBoxQueryObject();
		queryObject.setFieldName(fieldName);
		
		if(checkBox.getValue()){
			queryObject.setFieldValue("On");
		}else{
			queryObject.setFieldValue("Off");
		}
		
		queryObject.setDataType(dataType);
		
		return queryObject;
	}
	
	public void reset(){
		checkBox.setValue(false);
	}

}
