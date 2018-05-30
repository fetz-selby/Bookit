package com.iglobal.bookit.client.user.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.iglobal.bookit.client.user.widget.object.DynamicTextBoxQueryObject;
import com.iglobal.bookit.client.utils.Utils;
import com.iglobal.bookit.shared.DataTypeConstants;

public class DynamicTime extends Composite implements IsEntryWidget{

	private DataTypeConstants dataTypeId;
	private String labelName, fieldName, dataType;
	private static int counter;
	private static DynamicTimeUiBinder uiBinder = GWT
			.create(DynamicTimeUiBinder.class);

	interface DynamicTimeUiBinder extends UiBinder<Widget, DynamicTime> {
	}

	@UiField TextBox txtbox;
	@UiField LabelElement label;
	@UiField HTMLPanel mainPanel;
	private boolean isReset;
	
	public DynamicTime(String labelName, String fieldName, String dataType, DataTypeConstants dataTypeId, boolean isReset) {
		counter ++;
		this.isReset = isReset;
		this.labelName = labelName;
		this.fieldName = fieldName;
		this.dataTypeId = dataTypeId;
		this.dataType = dataType;
		initWidget(uiBinder.createAndBindUi(this));
		initComponent();
	}
	
	private void initComponent(){
		txtbox.getElement().setId("sels_time_"+counter);
		label.setAttribute("title", labelName);
		label.setInnerText(Utils.getTruncatedText(labelName, 19));
	}
	
	public void adjustToRecordSize(){
		mainPanel.setStyleName("m-l m-b col-md-2 input-append bootstrap-timepicker user-sortable-field");
	}
	
	public void load(){
		doNativeRun();
	}
	
	private void doNativeRun(){
		Scheduler.get().scheduleDeferred(new Command() {
	        @Override public void execute() {
	         doNative(txtbox.getElement().getId(), isReset);
	        }
	      });
	}
	
	private String getColonReplacement(String time){
		if(time.contains(":")){
			return time.replace(":", ".");
		}
		return time;
	}
	
	private void onTimeLoadComplete(){
		txtbox.setText("");
	}
	
	private native void doNative(String id, boolean isReset)/*-{
		var app = this;
		$wnd.$('#'+id).timepicker('showWidget');
		
		if(isReset){
				app.@com.iglobal.bookit.client.user.widget.DynamicTime::onTimeLoadComplete()();
		}
	}-*/;
	
	

	@Override
	public DynamicTextBoxQueryObject getQuery() {
		DynamicTextBoxQueryObject queryObject = new DynamicTextBoxQueryObject();
		queryObject.setFieldName(fieldName);
		queryObject.setFieldValue(getColonReplacement(txtbox.getText().trim()));
		queryObject.setDataType(dataType);
		
		return queryObject;
	}
	
	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}
}
