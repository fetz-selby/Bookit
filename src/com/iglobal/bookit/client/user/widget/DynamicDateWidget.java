package com.iglobal.bookit.client.user.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.iglobal.bookit.client.user.widget.object.DynamicTextBoxQueryObject;
import com.iglobal.bookit.client.utils.Utils;
import com.iglobal.bookit.shared.DataTypeConstants;

public class DynamicDateWidget extends Composite implements IsEntryWidget{

	private static DynamicDateWidgetUiBinder uiBinder = GWT
			.create(DynamicDateWidgetUiBinder.class);
	//private DataTypeConstants dataTypeId;
	private String labelName, fieldName, dataType;

	interface DynamicDateWidgetUiBinder extends
			UiBinder<Widget, DynamicDateWidget> {
	}

	@UiField LabelElement label;
	@UiField ECalender dateWidget;
	@UiField HTMLPanel mainPanel;
	
	public DynamicDateWidget(String labelName, String fieldName, String dataType, DataTypeConstants dataTypeId) {
		this.labelName = labelName;
		this.fieldName = fieldName;
		//this.dataTypeId = dataTypeId;
		this.dataType = dataType;
		initWidget(uiBinder.createAndBindUi(this));
		initComponent();
	}
	
	
	private void initComponent(){
		label.setAttribute("title", labelName);
		label.setInnerText(Utils.getTruncatedText(labelName, 19));	}

	public void adjustToRecordSize(){
		mainPanel.setStyleName("m-l m-b col-md-2 user-sortable-field");
	}
	
	@Override
	public DynamicTextBoxQueryObject getQuery() {
//		String value = "";
//		
//		if(dateWidget.getDateString().trim().isEmpty()){
//			value = Utils.getTodayDate();
//		}else{
//			value = dateWidget.getDateString();
//		}
		
		DynamicTextBoxQueryObject queryObject = new DynamicTextBoxQueryObject();
		queryObject.setFieldName(fieldName);
		queryObject.setFieldValue(dateWidget.getDateString());
		queryObject.setDataType(dataType);
		
		return queryObject;
	}


	@Override
	public void reset() {
		//dateWidget.clear();
	}

}
