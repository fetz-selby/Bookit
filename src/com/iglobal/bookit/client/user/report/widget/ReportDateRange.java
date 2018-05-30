package com.iglobal.bookit.client.user.report.widget;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.iglobal.bookit.client.user.widget.ECalender;
import com.iglobal.bookit.shared.QueryConstants;

public class ReportDateRange extends Composite {

	private String field;
	private static ReportDateRangeUiBinder uiBinder = GWT
			.create(ReportDateRangeUiBinder.class);

	interface ReportDateRangeUiBinder extends UiBinder<Widget, ReportDateRange> {
	}
	
	@UiField ECalender startDate, endDate;

	public ReportDateRange(String field) {
		this.field = field;
		initWidget(uiBinder.createAndBindUi(this));
		initComponent();
		initEvent();
	}
	
	private void initComponent(){
		startDate.setStyle("form-control");
		endDate.setStyle("form-control");
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
	
	public String getValue(){
		if(startDate.getDateString().trim().isEmpty() && endDate.getDateString().trim().isEmpty()){
			return QueryConstants.EMPTY_DATE+":";
		}else if(startDate.getDateString().trim().isEmpty() && !endDate.getDateString().trim().isEmpty()){
			return QueryConstants.END_DATE+":"+endDate.getDateString();
		}else if(!startDate.getDateString().trim().isEmpty() && endDate.getDateString().trim().isEmpty()){
			return QueryConstants.START_DATE+":"+startDate.getDateString();
		}else if(!startDate.getDateString().trim().isEmpty() && !endDate.getDateString().trim().isEmpty()){
			return QueryConstants.BOTH_DATE+":"+startDate.getDateString()+":"+endDate.getDateString();
		}
		return startDate.getDateString()+":"+endDate.getDateString();
	}
	
	public String getField() {
		return field;
	}
	
}
