package com.iglobal.bookit.client.ui.components;

import java.util.HashMap;
import java.util.TreeSet;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.iglobal.bookit.shared.QueryEnum;
import com.iglobal.bookit.shared.QueryObject;
import com.iglobal.bookit.shared.QueryOperatorEnum;

public class ComboDataListBox extends Composite {
	final static String INITIAL_VALUE = "0";
	
	private ComboDataListBoxEventHandler handler;
	private HashMap<String, String> map;
	private String labelString, initialString = "-- Select --";
	
	private QueryEnum queryEnum;
	private QueryOperatorEnum operator;
	private static ComboDataListBoxUiBinder uiBinder = GWT
			.create(ComboDataListBoxUiBinder.class);

	interface ComboDataListBoxUiBinder extends
			UiBinder<Widget, ComboDataListBox> {
	}

	public interface ComboDataListBoxEventHandler{
		void onItemSelected(String item, String value);
	}
	
	@UiField LabelElement label;
	@UiField ListBox dataBox;
	
	public ComboDataListBox(String labelString, HashMap<String, String> map, QueryEnum queryObject, QueryOperatorEnum operator) {
		this.map = map;
		this.labelString = labelString;
		this.queryEnum = queryObject;
		this.operator = operator;
		initWidget(uiBinder.createAndBindUi(this));
		initComponent();
		initEvent();
	}
	
	public ComboDataListBox(){
		initWidget(uiBinder.createAndBindUi(this));
		initComponent();
		initEvent();
	}
	
	private void initEvent(){
		dataBox.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				if(handler != null){
					handler.onItemSelected(dataBox.getItemText(dataBox.getSelectedIndex()), dataBox.getValue(dataBox.getSelectedIndex()));
				}
			}
		});
	}

	private void initComponent(){
		if(map == null){
			return;
		}
		
		TreeSet<String> treeSorter = new TreeSet<String>();
		
		for(String value : map.keySet()){
			treeSorter.add(value);
		}
		
		dataBox.addItem(initialString, INITIAL_VALUE);
		
		for(String value : treeSorter){
			dataBox.addItem(value, map.get(value));
		}
		
		label.setInnerText(labelString);
	}
	
	public void setInitialString(String initialString){
		this.initialString = initialString;
	}
	
	public void load(){
		initComponent();
	}
	
	public HashMap<String, String> getMap() {
		return map;
	}

	public void setMap(HashMap<String, String> map) {
		this.map = map;
	}

	public String getLabelString() {
		return labelString;
	}

	public void setLabelString(String labelString) {
		this.labelString = labelString;
	}

	public void setQueryEnum(QueryEnum queryEnum) {
		this.queryEnum = queryEnum;
	}

	public void setOperator(QueryOperatorEnum operator) {
		this.operator = operator;
	}

	public QueryEnum getQueryEnum() {
		return queryEnum;
	}
	
	public QueryOperatorEnum getOperator() {
		return operator;
	}

	public void setComboDataListBoxEventHandler(ComboDataListBoxEventHandler handler){
		this.handler = handler;
	}

	public QueryObject getQueryObject() {
		String value = dataBox.getValue(dataBox.getSelectedIndex()).trim();
		if(value.equals(INITIAL_VALUE)){
			return null;
		}
		
		QueryObject queryObject = new QueryObject(queryEnum, value, true, operator);
		return queryObject;
	}
	
}
