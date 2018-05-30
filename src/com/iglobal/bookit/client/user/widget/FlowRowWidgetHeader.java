package com.iglobal.bookit.client.user.widget;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.iglobal.bookit.client.utils.Utils;
import com.iglobal.bookit.shared.DataTypeConstants;

public class FlowRowWidgetHeader extends Composite {

	private int fieldCount = 0;
	private HashMap<String, DataTypeConstants> dataTypeMap;
	private ArrayList<String> orderedHeader;
	private static FlowRowWidgetHeaderUiBinder uiBinder = GWT
			.create(FlowRowWidgetHeaderUiBinder.class);

	interface FlowRowWidgetHeaderUiBinder extends
			UiBinder<Widget, FlowRowWidgetHeader> {
	}

	@UiField HTMLPanel mainRow;
	
	public FlowRowWidgetHeader(ArrayList<String> orderedHeader, HashMap<String, DataTypeConstants> dataTypeMap) {
		this.orderedHeader = orderedHeader;
		this.dataTypeMap = dataTypeMap;
		initWidget(uiBinder.createAndBindUi(this));
		initBlacklistColumnCount();
		begin();
	}

	private void begin(){
		if(orderedHeader != null){
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
	
	private Element getColumn(String fullValue, String columnValue, int styleValue){
		Element div = DOM.createElement("div");
		div.setClassName("header-cell col-md-"+styleValue);
		div.setInnerText(columnValue);
		div.setAttribute("title", fullValue);
		
		return div;
	}
	
	private void doCategoryFind(int length){
		//final int THRESHOLD = 12;
		
		GWT.log("Header LENGTH is "+ length);
		
//		if(length < THRESHOLD/2){
//			//Double it
//			doSizeStyling(28,2);
//		}else if(length >= THRESHOLD/2 && length <= THRESHOLD){
//			//Single it
//			doSizeStyling(9,1);
//		}
		GWT.log("*****[FlowRowWidgetHeader] Length is "+length);

		
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
		
		for(String column : orderedHeader){
			if(dataTypeMap.containsKey(column) && (dataTypeMap.get(column) == DataTypeConstants.BLOB)){
				continue;
			}
			Element div = getColumn(column, Utils.getTruncatedText(column, limit), spanCount);
			mainRow.getElement().appendChild(div);
		}
	}
	
	private void doSmartStyling(int length){
		final int SUM_LENGTH = 12;
		
		int expandCount = SUM_LENGTH - length;
		
		for(int i = 0; i < length; i++){
			String column = orderedHeader.get(i);
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
}
