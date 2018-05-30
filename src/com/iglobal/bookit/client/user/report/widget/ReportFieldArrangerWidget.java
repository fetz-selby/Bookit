package com.iglobal.bookit.client.user.report.widget;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class ReportFieldArrangerWidget extends Composite {

	private static int idCounter;
	private HashMap<String, SimplePanel> draggableHash;
	private HashMap<String, String> unarrangedHash;
	private static ReportFieldArrangerWidgetUiBinder uiBinder = GWT
			.create(ReportFieldArrangerWidgetUiBinder.class);

	interface ReportFieldArrangerWidgetUiBinder extends
			UiBinder<Widget, ReportFieldArrangerWidget> {
	}

	@UiField HTMLPanel mainPanel;
	public ReportFieldArrangerWidget(HashMap<String, String> unarrangedHash) {
		idCounter ++;
		this.unarrangedHash = unarrangedHash;
		initWidget(uiBinder.createAndBindUi(this));
		initComponent();
		renderDragables(unarrangedHash);
	}
	
	public ReportFieldArrangerWidget(ArrayList<String> arrangedKeysList, ArrayList<String> arrangedValuesList) {
		idCounter ++;
		doUnArragedHash(arrangedKeysList, arrangedValuesList);
		initWidget(uiBinder.createAndBindUi(this));
		initComponent();
		renderOrderedDraggables(arrangedValuesList, arrangedKeysList);
	}
	
	public ReportFieldArrangerWidget() {
		idCounter ++;
		initWidget(uiBinder.createAndBindUi(this));
		initComponent();
		initDataStructures();
	}
	
	private void doUnArragedHash(ArrayList<String> keys, ArrayList<String> values){
		if(keys.size() == values.size()){
			HashMap<String, String> tmpUnarrangedHash = new HashMap<String, String>();
			
			for(int i = 0; i < keys.size(); i++){
				tmpUnarrangedHash.put(keys.get(i), values.get(i));
			}
			
			unarrangedHash = tmpUnarrangedHash;
		}
	}
	
	private void initDataStructures(){
		unarrangedHash = new HashMap<String, String>();
	}
	
	private void initComponent(){
		mainPanel.getElement().setId("sels_report_field_arranger_"+idCounter);
		draggableHash = new HashMap<String, SimplePanel>();
	}
	
	private void renderDragables(HashMap<String, String> unarrangedHash){
		if(unarrangedHash != null){
			this.unarrangedHash = new HashMap<String, String>();
			for(String str : unarrangedHash.keySet()){
				SimplePanel draglet = getPanelWidget(unarrangedHash.get(str));
				mainPanel.add(draglet);
				
				draggableHash.put(str, draglet);
				this.unarrangedHash.put(str, unarrangedHash.get(str));
			}
			
			doDragging();
		}
	}
	
	private void renderOrderedDraggables(ArrayList<String> orderedValues, ArrayList<String> orderedKeys){
//		for(String orderedValue : orderedValues){
//			SimplePanel draglet = getPanelWidget(orderedValue)
//			mainPanel.add(getPanelWidget(orderedValue));
//		}
		
		if(orderedValues.size() == orderedKeys.size()){
			for(int i = 0; i < orderedValues.size(); i++){
				SimplePanel draglet = getPanelWidget(orderedValues.get(i));
				mainPanel.add(draglet);
				
				draggableHash.put(orderedKeys.get(i), draglet);
			}
		}
		
		doDragging();

	}
	
	private SimplePanel getPanelWidget(String str){
		SimplePanel panel = new SimplePanel();
		panel.getElement().setInnerText(str);
		panel.setStyleName("report-draggable-style");
		return panel;
	}
	
	public ArrayList<String> getOrder(){
		ArrayList<String> list = new ArrayList<String>();
		NodeList<Node> nodes = mainPanel.getElement().getChildNodes();
		
		for(int i = 0; i < nodes.getLength(); i++){
			Element element = ((Element)nodes.getItem(i).cast());
			list.add(element.getInnerText().toString());

			GWT.log(element.getInnerText());
		}
		
		return list;
	}
	
	public ArrayList<String> getOrderedKey(){
		ArrayList<String> orderedList = getOrder();
		ArrayList<String> orderedKeysList = new ArrayList<String>();
		
		for(String orderedField : orderedList){
			if(unarrangedHash.containsValue(orderedField)){
				for(String key : unarrangedHash.keySet()){
					if(unarrangedHash.get(key).trim().equals(orderedField)){
						orderedKeysList.add(key);
					}
				}
			}
		}
		
		return orderedKeysList;
	}
	
	public void addDraggable(String name, String value){
		if(!draggableHash.containsKey(value)){
			GWT.log("++++ got in");
			if(!unarrangedHash.containsKey(value)){
				unarrangedHash.put(value, name);
			}
			
			SimplePanel draglet = getPanelWidget(name);
			mainPanel.add(draglet);
			draggableHash.put(value, draglet);
			
			doDragging();
		}
	}
	
	public void removeDraggable(String value){
		if(draggableHash.containsKey(value)){
			draggableHash.get(value).removeFromParent();
		}
		
		if(unarrangedHash.containsKey(value)){
			unarrangedHash.remove(value);
		}
	}
	
	public void removeAll(){
		mainPanel.clear();
		draggableHash.clear();
		unarrangedHash.clear();
	}
	
	public void addAll(HashMap<String, String> dragletHash){
		mainPanel.clear();
		draggableHash.clear();
		unarrangedHash.clear();
		
		renderDragables(dragletHash);
	}
	
	private void doDragging(){
		 Scheduler.get().scheduleDeferred(new Command() {
		        @Override public void execute() {
					doNativeSortable(mainPanel.getElement().getId());
		        }
		      });
	}
	
	private native void doNativeSortable(String id)/*-{
		var app = this;
		$wnd.$('#'+id).sortable();
	}-*/;


}
