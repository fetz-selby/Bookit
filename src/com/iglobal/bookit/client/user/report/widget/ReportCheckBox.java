package com.iglobal.bookit.client.user.report.widget;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.iglobal.bookit.client.user.widget.ScrollableFlowPanel;
import com.iglobal.bookit.shared.QueryConstants;

public class ReportCheckBox extends Composite {

	private HashMap<String, String> checkMap;
	private HashMap<String, String> selectedCheckHash;
	private ArrayList<CheckBox> checkList;
	private String checkedString;
	private CheckBox selectAllCheckBox;
	private ReportCheckBoxEventHandler handler;

	
	public interface ReportCheckBoxEventHandler{
		void onCheck(String value, String alias);
		void onDeCheck(String value, String alias);
		void onSelectAllChecked();
		void onSelectAllDeChecked();
	}
	private static ReportCheckBoxUiBinder uiBinder = GWT
			.create(ReportCheckBoxUiBinder.class);

	interface ReportCheckBoxUiBinder extends UiBinder<Widget, ReportCheckBox> {
	}
	
	@UiField HTMLPanel mainPanel;
	
	public ReportCheckBox(HashMap<String, String> checkMap) {
		this.checkMap =  checkMap;
		initWidget(uiBinder.createAndBindUi(this));
		initComponent();
	}

	public ReportCheckBox(HashMap<String, String> checkMap, String checkedString) {
		this.checkedString = checkedString;
		this.checkMap =  checkMap;
		initWidget(uiBinder.createAndBindUi(this));
		initComponent();
	}
	
	private void initComponent(){
		removeBlacklistFields();
		ScrollableFlowPanel flowPanel = new ScrollableFlowPanel("report-checkgroup-panel-style m-b-lg");
		
		selectedCheckHash = new HashMap<String, String>();
		
		if(checkMap != null){
			ArrayList<String> checkedList = new ArrayList<String>();
			
			if(checkedString != null){
				String[] aliasList = checkedString.split("[,]");
				for(String alias : aliasList){
					checkedList.add(alias.trim());
				}
			}
			
			checkList = new ArrayList<CheckBox>();
			selectAllCheckBox = getSelectAllCheckBox();
			flowPanel.add(selectAllCheckBox);
			flowPanel.add(getClearfix());
			for(String field : checkMap.keySet()){
				CheckBox check = null;
				if(checkedString != null){
					check = getCheckBox(checkMap.get(field), field, checkedList);
				}else{
					check = getCheckBox(checkMap.get(field), field);
				}
				checkList.add(check);
				flowPanel.add(check);
			}
		}
		
		mainPanel.add(flowPanel);
	}
	
	private void removeBlacklistFields(){
		if(checkMap != null && checkMap.containsKey(QueryConstants.CREATED_TS)){
			checkMap.remove(QueryConstants.CREATED_TS);
		}
	}
	
	public void selectAllCheckBoxes(){
		if(checkList != null){
			for(CheckBox box : checkList){
				box.setValue(true, true);
				box.fireEvent(new ClickEvent(){});
			}
		}
	}
	
	public void deSelectAllCheckBoxes(){
		if(checkList != null){
			for(CheckBox box : checkList){
				box.setValue(false);
				box.fireEvent(new ClickEvent(){});
			}
		}
	}
	
	private CheckBox getSelectAllCheckBox(){
		final CheckBox checkBox = new CheckBox("All");
		checkBox.setStyleName("report-checkgroup-style");

		checkBox.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(checkBox.getValue()){
					selectAllCheckBoxes();
					if(handler != null){
						handler.onSelectAllChecked();
					}
				}else{
					deSelectAllCheckBoxes();
					if(handler != null){
						handler.onSelectAllDeChecked();
					}
				}
			}
		});
		return checkBox;
	}
	
	private CheckBox getCheckBox(String label, String value, ArrayList<String> checkedList){
		final CheckBox check = new CheckBox(label);
		check.setStyleName("report-checkgroup-style");
		check.setName(value);
		
		check.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(check.getValue()){
					if(!selectedCheckHash.containsKey(check.getName())){
						selectedCheckHash.put(check.getName(), check.getText());
					}
					
					if(handler != null){
						handler.onCheck(check.getName(), check.getText());
					}
				}else{
					if(selectedCheckHash.containsKey(check.getName())){
						selectedCheckHash.remove(check.getName());
					}
					
					if(handler != null){
						handler.onDeCheck(check.getName(), check.getText());
					}
				}
				
				//Deselect the main checkbox if empty
				if(selectedCheckHash.size() == 0 && selectAllCheckBox != null){
					selectAllCheckBox.setValue(false);
					if(handler != null){
						handler.onSelectAllDeChecked();
					}
				}else if(checkMap.size() == selectedCheckHash.size() && selectAllCheckBox != null){
					selectAllCheckBox.setValue(true);
					if(handler != null){
						handler.onSelectAllChecked();
					}
				}else if(checkMap.size() != selectedCheckHash.size()){
					selectAllCheckBox.setValue(false);
				}
			}
		});
		
		if(checkedList.contains(label)){
			check.setValue(true);
			selectedCheckHash.put(check.getName(), check.getText());
		}
		
		return check;
	}
	
	private CheckBox getCheckBox(String label, String value){
		final CheckBox check = new CheckBox(label);
		check.setStyleName("report-checkgroup-style");
		check.setName(value);
		
		check.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(check.getValue()){
					if(!selectedCheckHash.containsKey(check.getName())){
						selectedCheckHash.put(check.getName(), check.getText());
					}
					
					if(handler != null){
						handler.onCheck(check.getName(), check.getText());
					}
				}else{
					if(selectedCheckHash.containsKey(check.getName())){
						selectedCheckHash.remove(check.getName());
					}
					
					if(handler != null){
						handler.onDeCheck(check.getName(), check.getText());
					}
				}
				//Deselect the main checkbox if empty
				if(selectedCheckHash.size() == 0 && selectAllCheckBox != null){
					selectAllCheckBox.setValue(false);
					if(handler != null){
						handler.onSelectAllDeChecked();
					}
				}else if(checkMap.size() == selectedCheckHash.size() && selectAllCheckBox != null){
					selectAllCheckBox.setValue(true);
					if(handler != null){
						handler.onSelectAllChecked();
					}
				}else if(checkMap.size() != selectedCheckHash.size()){
					selectAllCheckBox.setValue(false);
				}
			}
		});
		
		return check;
	}
	
	private SimplePanel getClearfix(){
		SimplePanel panel = new SimplePanel();
		panel.setStyleName("clearfix");
		return panel;
	}
	
	public HashMap<String, String> getSelectedCheckHash(){
		return selectedCheckHash;
	}
	
	public void setReportCheckBoxEventHandler(ReportCheckBoxEventHandler handler){
		this.handler = handler;
	}
}
