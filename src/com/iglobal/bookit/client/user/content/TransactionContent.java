package com.iglobal.bookit.client.user.content;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.iglobal.bookit.client.GlobalResource;
import com.iglobal.bookit.client.constants.DBConstants;
import com.iglobal.bookit.client.user.widget.DynamicAlertCheck;
import com.iglobal.bookit.client.user.widget.DynamicDateWidget;
import com.iglobal.bookit.client.user.widget.DynamicLoginWidget;
import com.iglobal.bookit.client.user.widget.DynamicPhotoWidget;
import com.iglobal.bookit.client.user.widget.DynamicTextBox;
import com.iglobal.bookit.client.user.widget.DynamicTime;
import com.iglobal.bookit.client.user.widget.IsEntryWidget;
import com.iglobal.bookit.client.user.widget.object.DynamicTextBoxQueryObject;
import com.iglobal.bookit.client.utils.Utils;
import com.iglobal.bookit.shared.BookColumnObject;
import com.iglobal.bookit.shared.BookRowObject;
import com.iglobal.bookit.shared.DataTypeConstants;
import com.iglobal.bookit.shared.DynamicBookQueryObject;
import com.iglobal.bookit.shared.QueryEnum;
import com.iglobal.bookit.shared.QueryObject;
import com.iglobal.bookit.shared.QueryOperatorEnum;

public class TransactionContent extends Composite {

	private TransactionContentEventHandler handler;
	private static int panelCounter;
	private String bookId;
	private String bookName, bookAlias;
	private boolean isPostCall;
	private FormPanel form;
	private boolean isAlreadyLoaded = false;
	private ArrayList<String> commonGroupList;
	private HashMap<String, IsEntryWidget> fieldHashMap;
	private static TransactionContentUiBinder uiBinder = GWT
			.create(TransactionContentUiBinder.class);

	interface TransactionContentUiBinder extends
			UiBinder<Widget, TransactionContent> {
	}

	public interface TransactionContentEventHandler{
		void onWidgetLoadComplete(String tableName, String columnString);
		void onSaveSuccessful(String bookName);
		void onSaveFailure();
	}
	
	@UiField HTMLPanel mainPanel;
	
	public TransactionContent(String bookId) {
		panelCounter ++;
		this.bookId = bookId;
		initWidget(uiBinder.createAndBindUi(this));
		doInit();
	}
	
	private void doInit(){
		mainPanel.clear();
		
		mainPanel.getElement().setId("sels_sortable_panel_"+panelCounter);
		form = new FormPanel();
		form.setStyleName("hide");

		mainPanel.add(form);
		
		ArrayList<QueryObject> queryList = new ArrayList<QueryObject>();
		
		queryList.add(new QueryObject(QueryEnum.B_NAME, null, true, null));
		queryList.add(new QueryObject(QueryEnum.B_ID, bookId, true, QueryOperatorEnum.EQUALS));
		queryList.add(new QueryObject(QueryEnum.B_TABLE_NAME, null, true, null));
		queryList.add(new QueryObject(QueryEnum.B_TABLE_COL, null, true, null));
		queryList.add(new QueryObject(QueryEnum.B_GROUPS, null, true, null));
		queryList.add(new QueryObject(QueryEnum.B_CREATED_TS, null, true, null));
		queryList.add(new QueryObject(QueryEnum.B_MODIFIED_TS, null, true, null));
		queryList.add(new QueryObject(QueryEnum.B_STATUS, null, true, null));
		
		queryList.add(new QueryObject(QueryEnum.A_NAME, null, true, null));
		queryList.add(new QueryObject(QueryEnum.A_ID, QueryEnum.B_CREATED_BY.getField(), true, QueryOperatorEnum.EQUALS));
		
		GlobalResource.getInstance().getAdminRPC().getBookList(queryList, new AsyncCallback<ArrayList<BookRowObject>>() {
			
			@Override
			public void onSuccess(ArrayList<BookRowObject> result) {
				if(result != null){
					for(BookRowObject row : result){
						fieldHashMap = new HashMap<String, IsEntryWidget>();
						doColumnStringProcessing(row.getName(), row.getColumnString());
						//doGroupMatching(row.getName(), row.getColumnString(), row.getGroups());
					}
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	private void showMainPanelLoading(){
		//Window.alert("Show");
		SimplePanel panel = new SimplePanel();
		panel.getElement().setAttribute("style", "height: 50px;");
		
		Label loading = new Label("Loading ...");
		loading.addStyleName("loading-style");
		
		panel.add(loading);
		mainPanel.clear();
		mainPanel.add(panel);
	}
	
	
	private void doColumnStringProcessing(String tableName, String columnString){
		
		if(tableName.contains(":")){
			bookName = tableName.split(":")[0];
			bookAlias = tableName.split(":")[1];
		}else{
			return;
		}
		
		if(columnString.contains(",")){
			//showMainPanelLoading();
			
			String[] columnsTokens = columnString.split("[,]");
			
			//index 0 => fieldName index 1 => columnName, index 2 => dataType, index 3 => status
			for(String column : columnsTokens){
				String fieldName = column.split("[:]")[0];
				String columnName = column.split("[:]")[1];
				String dataType = column.split("[:]")[2];
				String status = column.split("[:]")[3];
				String dtCons = null;
				
				if(status.trim().equals(DBConstants.ACTIVE)){
					IsEntryWidget entryWidget = null;
					DataTypeConstants dt = null;
					for(BookColumnObject obj : GlobalResource.getInstance().getBookColumnsList()){
						if(dataType.trim().equals(obj.getId())){
							if(obj.getDataType().trim().equals("INT")){
								dt = DataTypeConstants.INT;
								dtCons = "INT";
								entryWidget = new DynamicTextBox(columnName, fieldName, dtCons, dt);
							}else if(obj.getDataType().trim().equals("TEXT")){
								dt = DataTypeConstants.STRING;
								dtCons = "TEXT";
								entryWidget = new DynamicTextBox(columnName, fieldName, dtCons, dt);
							}else if(obj.getDataType().trim().equals("MEDIUMBLOB")){
								dt = DataTypeConstants.BLOB;
								dtCons = "MEDIUMBLOB";
								entryWidget = new DynamicPhotoWidget(columnName, fieldName, dtCons, dt, null);
								isPostCall = true;
							}else if(obj.getDataType().trim().equals("TIME")){
								dt = DataTypeConstants.TIME;
								dtCons = "TIME";
								entryWidget = new DynamicTime(columnName, fieldName, dtCons, dt, false);
								((DynamicTime)entryWidget).load();
							}else if(obj.getDataType().trim().equals("DATE")){
								dt = DataTypeConstants.DATE;
								dtCons = "DATE";
								entryWidget = new DynamicDateWidget(columnName, fieldName, dtCons, dt);
							}else if(obj.getDataType().trim().equals("ALERT")){
								dt = DataTypeConstants.ALERT;
								dtCons = "ALERT";
								entryWidget = new DynamicAlertCheck(columnName, fieldName, dtCons, dt);
							}else if(obj.getDataType().trim().equals("LOGIN")){
								dt = DataTypeConstants.LOGIN;
								dtCons = "LOGIN";
								entryWidget = new DynamicLoginWidget(columnName, fieldName, dtCons, dt);
							}else if(obj.getDataType().trim().equals("ID")){
								dt = DataTypeConstants.ID;
								dtCons = "ID";
								//entryWidget = new DynamicLoginWidget(columnName, fieldName, dtCons, dt);
							}
						}
					}
					//DynamicTextBox textBox = new DynamicTextBox(columnName, fieldName, dtCons, dt);
					if(entryWidget != null){
						mainPanel.add(entryWidget);
						
						fieldHashMap.put(columnName, entryWidget);
					}
					
				}
			}
			
			if(handler != null){
				if(tableName.contains(":") && !isAlreadyLoaded){
					handler.onWidgetLoadComplete(tableName.split("[:]")[1], columnString);
					isAlreadyLoaded = true;
				}
			}
		}
	}
	
	private void addSortlet(Widget widget){
		mainPanel.add(widget);
	}
	
	public void load(){
		 Scheduler.get().scheduleDeferred(new Command() {
		        @Override public void execute() {
					doNativeSortable(mainPanel.getElement().getId());
		        }
		      });
	}
	
	public void saveFeed(){
		if(isPostCall){
			doPostSaveCall();
		}else{
			doRPCSaveCall();
		}
		
	}
	
	private void doPostSaveCall(){
		final String COMMA = ",";
		final String DELIMITER = ":";
		
		Hidden dataValues, tableValue;
		FlowPanel panel = new FlowPanel();
		
		String value = "";
		for(String key : fieldHashMap.keySet()){
			DynamicTextBoxQueryObject obj = fieldHashMap.get(key).getQuery();
			
			if(obj.getDataType().trim().equals("MEDIUMBLOB")){
				value += obj.getFieldName() + DELIMITER + obj.getFileUpload().getName() + DELIMITER + obj.getDataType();
				panel.add(obj.getFileUpload());
			}else if(obj.getDataType().trim().equals("INT")){
				value += obj.getFieldName() + DELIMITER + obj.getFieldValue() + DELIMITER + obj.getDataType();
			}else if(obj.getDataType().trim().equals("TEXT")){
				value += obj.getFieldName() + DELIMITER + obj.getFieldValue() + DELIMITER + obj.getDataType();
			}else if(obj.getDataType().trim().equals("TIME")){
				value += obj.getFieldName() + DELIMITER + obj.getFieldValue() + DELIMITER + obj.getDataType();
			}else if(obj.getDataType().trim().equals("DATE")){
				value += obj.getFieldName() + DELIMITER + obj.getFieldValue() + DELIMITER + obj.getDataType();
			}else if(obj.getDataType().trim().equals("ALERT")){
				value += obj.getFieldName() + DELIMITER + obj.getFieldValue() + DELIMITER + obj.getDataType();
			}else if(obj.getDataType().trim().equals("LOGIN")){
				value += obj.getFieldName() + DELIMITER + obj.getFieldValue() + DELIMITER + obj.getDataType();
			}else if(obj.getDataType().trim().equals("ID")){
				value += obj.getFieldName() + DELIMITER + obj.getFieldValue() + DELIMITER + obj.getDataType();
			}

			value += COMMA;
		}
		
		value = value.substring(0, value.lastIndexOf(COMMA));
		
		dataValues = new Hidden("data", value);
		tableValue = new Hidden("table", bookName);
		
		panel.add(dataValues);
		panel.add(tableValue);
		
		form.clear();
		form.setAction(GWT.getModuleBaseURL()+"postfile");
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);
		form.addSubmitCompleteHandler(new SubmitCompleteHandler() {
			
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				if(isMessageSuccess(event.getResults())){
					//resetAllFields();
					if(handler != null){
						handler.onSaveSuccessful(bookAlias);
					}
					
					doInit();
				}
			}
		});
		
		form.setWidget(panel);
		form.submit();
	}
	
	private boolean isMessageSuccess(String unProcessedMessage){
		HTML html = new HTML(unProcessedMessage);
		if(html.getText().trim().equals("S")){
			return true;
		}
			
		return false;
	}
	
	private void doRPCSaveCall(){
		HashMap<String, String> fieldValueMap = new HashMap<String, String>();
		HashMap<String, String> fieldDatatypeMap = new HashMap<String, String>();
		
		for(String key : fieldHashMap.keySet()){
			GWT.log("Key is => "+key);
			DynamicTextBoxQueryObject obj = fieldHashMap.get(key).getQuery();
			
			if(obj.getFieldValue().trim().isEmpty()){
				if(obj.getDataType().trim().equals("INT")){
					fieldValueMap.put(obj.getFieldName(), "0");
				}else if(obj.getDataType().trim().equals("TEXT")){
					fieldValueMap.put(obj.getFieldName(), "");
				}else if(obj.getDataType().trim().equals("MEDIUMBLOB")){
					fieldValueMap.put(obj.getFieldName(), "");
				}else if(obj.getDataType().trim().equals("TIME")){
					fieldValueMap.put(obj.getFieldName(), "");
				}else if(obj.getDataType().trim().equals("DATE")){
					fieldValueMap.put(obj.getFieldName(), Utils.getTodayDate());
				}else if(obj.getDataType().trim().equals("ALERT")){
					fieldValueMap.put(obj.getFieldName(), "N");
				}else if(obj.getDataType().trim().equals("LOGIN")){
					fieldValueMap.put(obj.getFieldName(), "");
				}else if(obj.getDataType().trim().equals("ID")){
					fieldValueMap.put(obj.getFieldName(), "0");
				}else{
					fieldValueMap.put(obj.getFieldName(), "");
				}
			}else{
				fieldValueMap.put(obj.getFieldName(), obj.getFieldValue());
			}
			
			fieldDatatypeMap.put(obj.getFieldName(), obj.getDataType());
		}
		
		DynamicBookQueryObject bookQuery = new DynamicBookQueryObject(bookId, bookName, fieldValueMap, fieldDatatypeMap);
		
		//Make call
		GlobalResource.getInstance().getUserRPC().addToBook(bookQuery, new AsyncCallback<Boolean>() {
			
			@Override
			public void onSuccess(Boolean result) {
				if(result){
					//resetAllFields();
					if(handler != null){
						handler.onSaveSuccessful(bookAlias);
					}
					
					doInit();
				}else{
					if(handler != null){
						handler.onSaveFailure();
					}
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				//Window.alert("Error");
			}
		});
	}
	
	private void resetAllFields(){
		for(String key : fieldHashMap.keySet()){
			fieldHashMap.get(key).reset();
		}
	}
	
	private native void doNativeSortable(String id)/*-{
		$wnd.$("#"+id).sortable();
	}-*/;

	public String getBookId() {
		return bookId;
	}
	
	public String getBookName(){
		return bookName;
	}

	public void setTransactionContentEventHandler(TransactionContentEventHandler handler){
		this.handler = handler;
	}
	
}
