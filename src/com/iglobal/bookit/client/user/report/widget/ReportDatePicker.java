package com.iglobal.bookit.client.user.report.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.iglobal.bookit.client.user.widget.ECalender;

public class ReportDatePicker extends Composite {

	private static ReportDatePickerUiBinder uiBinder = GWT
			.create(ReportDatePickerUiBinder.class);

	interface ReportDatePickerUiBinder extends
			UiBinder<Widget, ReportDatePicker> {
	}

	@UiField(provided = true) ECalender datePicker;
	@UiField SpanElement fieldNameSpan;
	private String label;
	private String field;
	
	public ReportDatePicker(String label, String field) {
		this.label = label;
		this.field = field;
		datePicker = new ECalender("form-control");
		initWidget(uiBinder.createAndBindUi(this));
		initComponent();
	}
	
	private void initComponent(){
		fieldNameSpan.setInnerText(label);
	}
	
	public String getValue(){
		return datePicker.getDateString();
	}
	
	public String getLabel(){
		return label;
	}

	public String getField() {
		return field;
	}
	
}
