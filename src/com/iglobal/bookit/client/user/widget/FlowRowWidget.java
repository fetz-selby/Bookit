package com.iglobal.bookit.client.user.widget;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.iglobal.bookit.client.utils.Utils;
import com.iglobal.bookit.shared.DataTypeConstants;

public class FlowRowWidget extends Composite {
	
	private int fieldCount = 0;
	private FlowRowWidgetEventHandler handler;
	private ArrayList<String> orderedHeader;
	private HashMap<String, String> detailsHash;
	private HashMap<String, String> fieldAliasMap;
	private HashMap<String, DataTypeConstants> dataTypeMap;
	private static FlowRowWidgetUiBinder uiBinder = GWT
			.create(FlowRowWidgetUiBinder.class);

	interface FlowRowWidgetUiBinder extends UiBinder<Widget, FlowRowWidget> {
	}

	public interface FlowRowWidgetEventHandler{
		void onWidgetClicked(String id, HashMap<String, String> detailsMap, HashMap<String, String> fieldAliasMap, HashMap<String, DataTypeConstants> dataTypeMap, ArrayList<String> fieldOrder);
	}
	
	@UiField HTMLPanel mainRow;
	
	public FlowRowWidget(HashMap<String, String> detailsMap, HashMap<String, String> fieldAliasMap, HashMap<String, DataTypeConstants> dataTypeMap, ArrayList<String> orderedHeader) {
		this.fieldAliasMap = fieldAliasMap;
		this.detailsHash = detailsMap;
		this.dataTypeMap = dataTypeMap;
		this.orderedHeader = orderedHeader;
		initWidget(uiBinder.createAndBindUi(this));
		
		//Count odds
		initBlacklistColumnCount();
		
		initEvent();
		begin();
	}
	
	private void begin(){
		if(detailsHash != null){
			GWT.log("field is "+fieldCount);
			doCategoryFind(orderedHeader.size() - fieldCount);
		}
	}
	
	private void initBlacklistColumnCount(){
		for(String column : dataTypeMap.keySet()){
			switch(dataTypeMap.get(column)){
			case BLOB:
				fieldCount ++;
				break;
			case DATE:
				break;
			case DATETIME:
				break;
			case INT:
				break;
			case STRING:
				break;
			case TIME:
				break;
			default:
				break;
			
			}
		}
	}
	
	private void initEvent(){
		mainRow.sinkEvents(Event.ONCLICK);
		mainRow.addHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(handler != null){
					handler.onWidgetClicked(detailsHash.get("id"), detailsHash, fieldAliasMap, dataTypeMap, orderedHeader);
				}				
			}
		}, ClickEvent.getType());
	}
	
	private Element getColumn(String fullValue, String columnValue, int styleValue){
	
		Element div = DOM.createElement("div");
		div.setClassName("col-md-"+styleValue);
		div.setInnerText(columnValue);
		div.setAttribute("title", fullValue);
		
		return div;
	}
	
	private void doCategoryFind(int length){
//		final int THRESHOLD = 12;
		
//		if(length ==  THRESHOLD/3){
//			//Triple it
//			doSizeStyling(30, 3);
//		}else if(length < THRESHOLD/2){
//			//Double it
//			doSizeStyling(28, 2);
//		}else if(length == THRESHOLD/2){
//			//Double it
//			doSizeStyling(28, 2);
//		}else if(length > THRESHOLD && length <= THRESHOLD){
//			//Single it
//			doSizeStyling(10, 1);
//		}else if(length > THRESHOLD){
//			//Do omitation
//			
//		}
		
		switch(length){
		case 2:
			doSizeStyling(38, 6);
			break;
		case 3:
			doSizeStyling(35, 4);
			break;
		case 4:
			doSizeStyling(30, 3);
			break;
		case 5:
			doSizeStyling(20, 2);
			break;
		case 6: 
			doSizeStyling(28, 2);
			break;
		default:
			doSmartStyling(length);
		}
		
	}
	
	private void doSizeStyling(int limit, int spanCount){
		ArrayList<String> columns = getOrderedFields();
		
		for(String column : columns){
			if(dataTypeMap.containsKey(column) && (dataTypeMap.get(column) == DataTypeConstants.BLOB)){
				continue;
			}
			
			Element div = getColumn(detailsHash.get(column), Utils.getTruncatedText(detailsHash.get(column), limit), spanCount);
			mainRow.getElement().appendChild(div);
		}
	}
	
	private ArrayList<String> getOrderedFields(){
		ArrayList<String> orderedFields = new ArrayList<String>();
		for(String list : orderedHeader){
			if(fieldAliasMap.containsValue(list)){
				for(String key : fieldAliasMap.keySet()){
					if(list.trim().equals(fieldAliasMap.get(key).trim())){
						orderedFields.add(key);
					}
				}
			}
		}
		return orderedFields;
	}
	
	private void doSmartStyling(int length){
		final int SUM_LENGTH = 12;
		
		ArrayList<String> orderedList = getOrderedFields();
		int expandCount = SUM_LENGTH - length;
		
		for(int i = 0; i < length; i++){
			String column = detailsHash.get(orderedList.get(i));
			if(dataTypeMap.containsKey(column) && (dataTypeMap.get(column) == DataTypeConstants.BLOB)){
				continue;
			}
			
			Element div = null;
			
			if(i < expandCount){
				div = getColumn(column, Utils.getTruncatedText(column, 20), 2);
			}else{
				div = getColumn(column, Utils.getTruncatedText(column, 9), 1);
			}
			
			mainRow.getElement().appendChild(div);
		}
		
	}
	
//	<div class="hidden-xs hidden-sm col-md-2 col-lg-2 m-r-xs m-l-sm"
//			ui:field="nameField">John Smith</div>

	public void setFlowRowWidgetEventHandler(FlowRowWidgetEventHandler handler){
		this.handler = handler;
	}
}
