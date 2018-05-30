package com.iglobal.bookit.client.ui.components;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.iglobal.bookit.client.user.widget.ECalender;
import com.iglobal.bookit.client.utils.Utils;
import com.iglobal.bookit.shared.QueryEnum;
import com.iglobal.bookit.shared.QueryObject;
import com.iglobal.bookit.shared.QueryOperatorEnum;

public class CustomRangeDatePicker extends Composite {

	private String labelString;
	private QueryEnum queryEnumObject;
	private QueryOperatorEnum operator;
	private static CustomRangeDatePickerUiBinder uiBinder = GWT
			.create(CustomRangeDatePickerUiBinder.class);

	interface CustomRangeDatePickerUiBinder extends
			UiBinder<Widget, CustomRangeDatePicker> {
	}

	@UiField ECalender startDate, endDate;
	
	public CustomRangeDatePicker(String labelString, QueryEnum queryEnum, QueryOperatorEnum operator) {
		this.labelString = labelString;
		this.queryEnumObject = queryEnum;
		this.operator = operator;
		initWidget(uiBinder.createAndBindUi(this));
		initComponent();
		initEvent();
	}
	
	public CustomRangeDatePicker() {
		initWidget(uiBinder.createAndBindUi(this));
		initEvent();
	}
	
	private void initEvent(){
		startDate.addValueChangeHandler(new ValueChangeHandler<Date>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				endDate.clear();
				endDate.setStartDate(event.getValue());
			}
		});
		
		endDate.addValueChangeHandler(new ValueChangeHandler<Date>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
			}
		});
	}
	
	private void initComponent(){
		//label.setInnerText(labelString);
	}
	
	public void load(){
		initComponent();
	}
	
	public String getLabelString() {
		return labelString;
	}

	public void setLabelString(String labelString) {
		this.labelString = labelString;
	}

	public QueryEnum getQueryEnumObject() {
		return queryEnumObject;
	}

	public void setQueryEnumObject(QueryEnum queryEnumObject) {
		this.queryEnumObject = queryEnumObject;
	}

	public QueryOperatorEnum getOperator() {
		return operator;
	}

	public void setOperator(QueryOperatorEnum operator) {
		this.operator = operator;
	}
	
	public QueryObject getQueryObject(){
		if(startDate.getDateString() == null || startDate.getDateString().trim().isEmpty()){
			return null;
		}else if(startDate.getDateString() != null && (endDate.getDateString() == null || endDate.getDateString().trim().isEmpty())){
			return new QueryObject(queryEnumObject, startDate.getDateString()+":"+Utils.getTodayDate(), true, QueryOperatorEnum.BETWEEN);
		}else if(endDate.getDateString() != null && (startDate.getDateString() == null || startDate.getDateString().trim().isEmpty())){
			return new QueryObject(queryEnumObject, endDate.getDateString()+":"+endDate.getDateString(), true, QueryOperatorEnum.BETWEEN);
		}else if(startDate.getDateString() != null && endDate.getDateString() != null){
			return new QueryObject(queryEnumObject, startDate.getDateString()+":"+endDate.getDateString(), true, QueryOperatorEnum.BETWEEN);
		}
		return null;
	}

}
