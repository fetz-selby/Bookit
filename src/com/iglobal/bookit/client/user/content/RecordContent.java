package com.iglobal.bookit.client.user.content;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.iglobal.bookit.client.GlobalResource;
import com.iglobal.bookit.client.constants.DBConstants;
import com.iglobal.bookit.client.ui.components.CustomDraggablePopupPanel;
import com.iglobal.bookit.client.ui.components.NoDataFound;
import com.iglobal.bookit.client.user.widget.DynamicDateWidget;
import com.iglobal.bookit.client.user.widget.DynamicRecordTextBox;
import com.iglobal.bookit.client.user.widget.DynamicTime;
import com.iglobal.bookit.client.user.widget.FlowRowWidget;
import com.iglobal.bookit.client.user.widget.FlowRowWidget.FlowRowWidgetEventHandler;
import com.iglobal.bookit.client.user.widget.FlowRowWidgetHeader;
import com.iglobal.bookit.client.user.widget.IsEntryWidget;
import com.iglobal.bookit.client.user.widget.ScrollableFlowPanel;
import com.iglobal.bookit.client.user.widget.SummaryDisplayWidget;
import com.iglobal.bookit.client.user.widget.SummaryDisplayWidget.SummaryDisplayWidgetEventHandler;
import com.iglobal.bookit.client.user.widget.editpanel.AvatarEditPanel;
import com.iglobal.bookit.client.user.widget.editpanel.DateEditPanel;
import com.iglobal.bookit.client.user.widget.editpanel.EditPanelEventHandler;
import com.iglobal.bookit.client.user.widget.editpanel.IsEditableUserWidget;
import com.iglobal.bookit.client.user.widget.editpanel.LoginEditPanel;
import com.iglobal.bookit.client.user.widget.editpanel.NumberEditPanel;
import com.iglobal.bookit.client.user.widget.editpanel.TextEditPanel;
import com.iglobal.bookit.client.user.widget.object.UserSummaryDisplayObject;
import com.iglobal.bookit.client.utils.UserPermissionHandler;
import com.iglobal.bookit.shared.BookColumnObject;
import com.iglobal.bookit.shared.BookRowObject;
import com.iglobal.bookit.shared.DataTypeConstants;
import com.iglobal.bookit.shared.DynamicBookQueryObject;
import com.iglobal.bookit.shared.DynamicRecordObject;
import com.iglobal.bookit.shared.QueryConstants;
import com.iglobal.bookit.shared.QueryEnum;
import com.iglobal.bookit.shared.QueryObject;
import com.iglobal.bookit.shared.QueryOperatorEnum;

public class RecordContent extends Composite {

	private ArrayList<String> list;
	private boolean hasRead, hasUpdate;
	private UserPermissionHandler permsHandler;
	private String bookId;
	private RootPanel auxPanel;
	private RecordContentEventHandler handler;
	private static int idCounter;
	private static RecordContentUiBinder uiBinder = GWT
			.create(RecordContentUiBinder.class);

	interface RecordContentUiBinder extends UiBinder<Widget, RecordContent> {
	}

	public interface RecordContentEventHandler{
		void onFieldLoadComplete();
	}

	@UiField AnchorElement searchBtn;
	@UiField FlowPanel fieldPanel, displayPanel;
	@UiField SpanElement rowCount;

	private String bookName;
	private HashMap<String, IsEntryWidget> fieldHashMap;
	private HashMap<String, String> imageMap;
	private HashMap<String, String> allFieldsHash;
	private boolean isCanUpdate = false;

	public RecordContent(String bookId, RootPanel auxPanel, UserPermissionHandler permsHandler) {
		this.permsHandler = permsHandler;
		this.bookId = bookId;
		this.auxPanel = auxPanel;
		initWidget(uiBinder.createAndBindUi(this));
		initComponent();
		initEvent();
		doRPC();
	}

	//	public RecordContent(String bookId, RootPanel auxPanel, UserPermissionHandler permsHandler) {
	//		this.permsHandler = permsHandler;
	//		this.bookId = bookId;
	//		this.auxPanel = auxPanel;
	//		initWidget(uiBinder.createAndBindUi(this));
	//		initComponent();
	//		doGetBookGroups();
	//	}

	private void initComponent(){
		idCounter ++;
		fieldPanel.getElement().setId(idCounter+"");
	}

	private void initEvent(){
		Element searchEvent = searchBtn.cast();
		DOM.sinkEvents(searchEvent, Event.ONCLICK);
		DOM.setEventListener(searchEvent, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				doSearch();
			}
		});
	}

	private void doGetBookGroups(){
		GlobalResource.getInstance().getUserRPC().getBookGroups(bookId, new AsyncCallback<ArrayList<String>>() {

			@Override
			public void onSuccess(ArrayList<String> result) {
				if(result != null){
					//Check for Rights
					for(String groupId : result){
						if(permsHandler.hasUpdatePermission(groupId)){
							hasUpdate = true;
						}

						if(permsHandler.hasReadPermission(groupId)){
							hasRead = true;
						}
					}

					initEvent();
					doRPC();
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void doRPC(){
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
					boolean flag = false;
					for(BookRowObject row : result){
						//Do group check
						if(!flag){
							doCanUpdateCheck(row.getGroups());
							flag = true;
						}

						fieldHashMap = new HashMap<String, IsEntryWidget>();
						imageMap = new HashMap<String, String>();
						doColumnStringProcessing(row.getName(), row.getColumnString());

					}
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void doCanUpdateCheck(String groupIds){
		ArrayList<String> groupList = new ArrayList<String>();

		if(groupIds.contains(",")){
			String[] groups = groupIds.split("[,]");
			for(String group : groups){
				groupList.add(group);
			}
		}else{
			groupList.add(groupIds);
		}

		for(String group : groupList){
			if(GlobalResource.getInstance().isCanUserUpdate(group)){
				isCanUpdate = true;
			}
		}
	}
	
	private void showFilterPanelLoading(){
		//Window.alert("Show");
		SimplePanel panel = new SimplePanel();
		panel.getElement().setAttribute("style", "height: 50px;");
		
		Label loading = new Label("Loading ...");
		loading.addStyleName("loading-style");
		
		panel.add(loading);
		fieldPanel.clear();
		fieldPanel.add(panel);
	}
	
	private void showDisplayPanelLoading(){
		//Window.alert("Show");
		SimplePanel panel = new SimplePanel();
		panel.getElement().setAttribute("style", "height: 50px;");
		
		Label loading = new Label("Loading ...");
		loading.addStyleName("loading-style");
		
		panel.add(loading);
		displayPanel.clear();
		displayPanel.add(panel);
	}
	
	private SimplePanel getFailureMessage(){
		SimplePanel panel = new SimplePanel();
		panel.getElement().setAttribute("style", "height: 50px;");
		
		Label loading = new Label("There was a problem with your request. Please try again later.");
		loading.addStyleName("loading-style");
		
		panel.add(loading);
		return panel;
	}

	private void doColumnStringProcessing(String tableName, String columnString){

		if(tableName.contains(":")){
			bookName = tableName.split(":")[0];
		}else{
			return;
		}

		if(columnString.contains(",")){
			showFilterPanelLoading();
			String[] columnsTokens = columnString.split("[,]");

			//index 0 => fieldName index 1 => columnName, index 2 => dataType, index 3 => status
			ScrollableFlowPanel fieldScrollable = new ScrollableFlowPanel("record-field-panel-scrollable");
			list = new ArrayList<String>();
			
			for(String column : columnsTokens){
				String fieldName = column.split("[:]")[0];
				String columnName = column.split("[:]")[1];
				String dataType = column.split("[:]")[2];
				String status = column.split("[:]")[3];
				String dtCons = null;

				if(status.trim().equals(DBConstants.ACTIVE)){
					DataTypeConstants dt = null;
					IsEntryWidget widget = null;
					for(BookColumnObject obj : GlobalResource.getInstance().getBookColumnsList()){
						if(dataType.trim().equals(obj.getId())){
							if(obj.getDataType().trim().equals(DataTypeConstants.INT.getValue())){
								dt = DataTypeConstants.INT;
								dtCons = DataTypeConstants.INT.getValue();
								widget = new DynamicRecordTextBox(columnName, fieldName, dtCons, dt);
							}else if(obj.getDataType().trim().equals(DataTypeConstants.STRING.getValue())){
								dt = DataTypeConstants.STRING;
								dtCons = DataTypeConstants.STRING.getValue();
								widget = new DynamicRecordTextBox(columnName, fieldName, dtCons, dt);
							}else if(obj.getDataType().trim().equals(DataTypeConstants.BLOB.getValue())){
								dt = DataTypeConstants.BLOB;
								dtCons = DataTypeConstants.BLOB.getValue();
								imageMap.put(columnName, fieldName);
							}else if(obj.getDataType().trim().equals(DataTypeConstants.DATE.getValue())){
								dt = DataTypeConstants.DATE;
								dtCons = DataTypeConstants.DATE.getValue();
								widget = new DynamicDateWidget(columnName, fieldName, dtCons, dt);
								((DynamicDateWidget)widget).adjustToRecordSize();
							}else if(obj.getDataType().trim().equals(DataTypeConstants.TIME.getValue())){
								dt = DataTypeConstants.TIME;
								dtCons = "TIME";
								widget = new DynamicTime(columnName, fieldName, dtCons, dt, true);
								((DynamicTime)widget).adjustToRecordSize();
								((DynamicTime)widget).load();
							}else if(obj.getDataType().trim().equals(DataTypeConstants.ALERT.getValue())){
								
								//Change widget
								dt = DataTypeConstants.ALERT;
								dtCons = DataTypeConstants.ALERT.getValue();
								widget = new DynamicRecordTextBox(columnName, fieldName, dtCons, dt);
							}else if(obj.getDataType().trim().equals(DataTypeConstants.ID.getValue())){
								dt = DataTypeConstants.ID;
								dtCons = DataTypeConstants.ID.getValue();
								widget = new DynamicRecordTextBox(columnName, fieldName, dtCons, dt);
							}else if(obj.getDataType().trim().equals(DataTypeConstants.LOGIN.getValue())){
								dt = DataTypeConstants.LOGIN;
								dtCons = DataTypeConstants.LOGIN.getValue();
								widget = new DynamicRecordTextBox(columnName, fieldName, dtCons, dt);
							}
						}
					}

					//Can't query with blobs
					if(dtCons != null && dtCons.equals("MEDIUMBLOB")){
						continue;
					}

					//DynamicRecordTextBox textBox = new DynamicRecordTextBox(columnName, fieldName, dtCons, dt);
					
					if(widget != null){
						fieldScrollable.add(widget);
						fieldHashMap.put(columnName, widget);
					}
					//fieldPanel.add(widget);

				}
				
				list.add(columnName);
			}

			fieldPanel.clear();
			fieldPanel.add(fieldScrollable);
			if(handler != null){
				handler.onFieldLoadComplete();
			}
		}
	}

	public void doSearch(){
		showDisplayPanelLoading();
		HashMap<String, String> localFieldMap = new HashMap<String, String>();
		HashMap<String, String> localFieldDataType = new HashMap<String, String>();

		allFieldsHash = new HashMap<String, String>();
		for(String columnName : fieldHashMap.keySet()){
			IsEntryWidget box = fieldHashMap.get(columnName);

			allFieldsHash.put(box.getQuery().getFieldName(), box.getQuery().getDataType());

			GWT.log("**** field, "+box.getQuery().getFieldName()+", datatype "+box.getQuery().getDataType());

			if(box.getQuery().getFieldValue().trim().isEmpty()){
				continue;
			}

			localFieldMap.put(box.getQuery().getFieldName(), box.getQuery().getFieldValue());
			localFieldDataType.put(box.getQuery().getFieldName(), box.getQuery().getDataType());
		}


		//Settings for blob image
		for(String columnName : imageMap.keySet()){
			allFieldsHash.put(columnName, "MEDIUMBLOB");
			localFieldDataType.put(columnName, "MEDIUMBLOB");
		}

		DynamicBookQueryObject queryObject = new DynamicBookQueryObject(bookId, bookName, localFieldMap, localFieldDataType);
		GWT.log("[RecordContent] bookId is "+bookId);
		queryObject.setAllFieldDatatypeHash(allFieldsHash);

		//Show Loading
		
		GlobalResource.getInstance().getUserRPC().searchFromBook(queryObject, new AsyncCallback<DynamicRecordObject>() {

			@Override
			public void onSuccess(DynamicRecordObject result) {
				if(result != null){
					displayPanel.clear();
					boolean isFlag = false;
					int i = 0;

					ScrollableFlowPanel scrollable = new ScrollableFlowPanel("record-display-panel");
					for(HashMap<String, String> rowDetails : result.getRows()){
						ArrayList<String> orderedHeader = getOrderedHeader(result.getAliasMap());
						HashMap<String, DataTypeConstants> dataTypesMap = result.getDataTypeListRows().get(i);

						doInsertMissingColumnName(orderedHeader);
						
						if(!isFlag){
							FlowRowWidgetHeader header = new FlowRowWidgetHeader(list, dataTypesMap);
							displayPanel.add(header);
							isFlag = !isFlag;
						}

						FlowRowWidget row = new FlowRowWidget(rowDetails, result.getAliasMap(), dataTypesMap, list);
						row.setFlowRowWidgetEventHandler(new FlowRowWidgetEventHandler() {

							@Override
							public void onWidgetClicked(String id, HashMap<String, String> detailsMap, HashMap<String, String> fieldAlias, HashMap<String, DataTypeConstants> dataTypeMap, ArrayList<String> fieldOrder) {
								//Clear panel
								doCreateSummary(id, bookName, detailsMap, fieldAlias, dataTypeMap, fieldOrder);
							}
						});
						scrollable.add(row);
						i++;
					}
					if(i == 0){
						NoDataFound noDataFound = new NoDataFound();
						noDataFound.setLowerMessage("");
						displayPanel.add(noDataFound);
					}else{
						displayPanel.add(scrollable);
					}
					rowCount.setInnerText(""+i);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				displayPanel.clear();
				displayPanel.add(getFailureMessage());

			}
		});
	}
	
	private void doInsertMissingColumnName(ArrayList<String> compareList){
		for(String columnName : compareList){
			if(!list.contains(columnName)){
				list.add(columnName);
			}
		}
	}

	private void doCreateSummary(String id, String bookName, HashMap<String, String> detailsMap, HashMap<String, String> fieldAlias, HashMap<String, DataTypeConstants> dataTypeMap, ArrayList<String> fieldOrder){
		auxPanel.getElement().setInnerText("");
		auxPanel.getElement().appendChild(getSummaryHeader("Summary"));
		
		//Check for authentication types
		if(isAuthenticationDatatypeAvailable(dataTypeMap)){
			isCanUpdate = false;
		}

		for(String key : detailsMap.keySet()){
			String alias = fieldAlias.get(key);
			String value = detailsMap.get(key);
			String fieldName = key;
			DataTypeConstants dataType = dataTypeMap.get(key);

			if(alias == null || alias.isEmpty()){
				continue;
			}

			//UserSummaryDisplayObject summaryObject = new UserSummaryDisplayObject(fieldName, alias, value, dataType, hasUpdate);
			UserSummaryDisplayObject summaryObject = new UserSummaryDisplayObject(id, bookName, fieldName, alias, value, dataType, isCanUpdate);

			final SummaryDisplayWidget widget = new SummaryDisplayWidget(summaryObject);
			widget.setSummaryDisplayWidgetEventHandler(new SummaryDisplayWidgetEventHandler() {

				@Override
				public void onEditClicked(UserSummaryDisplayObject obj) {
					doFetchForEditType(obj, widget);					
				}
			});

			auxPanel.add(widget);
		}

		doSummarySortable();
	}
	
	private boolean isAuthenticationDatatypeAvailable(HashMap<String, DataTypeConstants> dataTypeHash){
		
		for(String field : dataTypeHash.keySet()){
			String dataType = dataTypeHash.get(field).getValue();
			for(BookColumnObject obj : GlobalResource.getInstance().getBookColumnsList()){
				if(obj.isLockable() && obj.getDataType().trim().equals(dataType)){
					return true;
				}
			}
		}
		
		return false;
	}

	private void doFetchForEditType(final UserSummaryDisplayObject obj, final SummaryDisplayWidget widget){
		IsEditableUserWidget editableWidget = null;
		final CustomDraggablePopupPanel panel = new CustomDraggablePopupPanel(true);
		panel.setGlassStyleName("glassPanel");
		panel.setAutoHideEnabled(true);
		panel.setGlassEnabled(true);
		
		switch(obj.getDataType()){
		case BLOB:
			editableWidget = new AvatarEditPanel(obj.getAlias(), obj.getValue(), obj);
			break;
		case DATE:
			editableWidget = new DateEditPanel(obj.getAlias(), obj.getValue());
			break;
		case DATETIME:
			editableWidget = new DateEditPanel(obj.getAlias(), obj.getValue());
			break;
		case INT:
			editableWidget = new NumberEditPanel(obj.getAlias(), obj.getValue());
			break;
		case STRING:
			editableWidget = new TextEditPanel(obj.getAlias(), obj.getValue());
			break;
		case TIME:
			editableWidget = new TextEditPanel(obj.getAlias(), obj.getValue());
			break;
		case ALERT:
			//editableWidget = new TextEditPanel(obj.getAlias(), obj.getValue());
			break;
		case LOGIN:
			editableWidget = new LoginEditPanel(obj.getAlias(), obj.getValue());
			break;
		default:
			break;

		}

		
		if(editableWidget != null){
			editableWidget.setEditPanelEventHandler(new EditPanelEventHandler() {
				
				@Override
				public void onSaveClicked(String value) {
					saveEditChanges(widget, obj, value);
					panel.hide();
				}
				
				@Override
				public void onImageSaveClicked(String imgUrl) {
					saveEditChanges(widget, obj, imgUrl);
					panel.hide();
				}
				
				@Override
				public void onCancelClicked() {
					panel.hide();
				}
			});
			
			panel.add(editableWidget);
			panel.center();
		}
	}
	
	private void saveEditChanges(final SummaryDisplayWidget widget, UserSummaryDisplayObject obj, final String value){
		
		switch(obj.getDataType()){
		case BLOB:
			widget.setImage(value);
			doSearch();
			break;
		case DATE:
			GlobalResource.getInstance().getUserRPC().isUpdateSuccessful(QueryConstants.STRING, obj.getTableName(), obj.getColumnName(), value, obj.getId(), new AsyncCallback<Boolean>() {
				
				@Override
				public void onSuccess(Boolean result) {
					widget.setValue(value);
					doSearch();
				}
				
				@Override
				public void onFailure(Throwable caught) {
					
				}
			});
			break;
		case DATETIME:
			GlobalResource.getInstance().getUserRPC().isUpdateSuccessful(QueryConstants.STRING, obj.getTableName(), obj.getColumnName(), value, obj.getId(), new AsyncCallback<Boolean>() {
				
				@Override
				public void onSuccess(Boolean result) {
					widget.setValue(value);
					doSearch();
				}
				
				@Override
				public void onFailure(Throwable caught) {
					
				}
			});
			break;
		case INT:
			GlobalResource.getInstance().getUserRPC().isUpdateSuccessful(QueryConstants.INT, obj.getTableName(), obj.getColumnName(), value, obj.getId(), new AsyncCallback<Boolean>() {
				
				@Override
				public void onSuccess(Boolean result) {
					widget.setValue(value);
					doSearch();
				}
				
				@Override
				public void onFailure(Throwable caught) {
					
				}
			});
			break;
		case STRING:
			GlobalResource.getInstance().getUserRPC().isUpdateSuccessful(QueryConstants.STRING, obj.getTableName(), obj.getColumnName(), value, obj.getId(), new AsyncCallback<Boolean>() {
				
				@Override
				public void onSuccess(Boolean result) {
					widget.setValue(value);
					doSearch();
				}
				
				@Override
				public void onFailure(Throwable caught) {
					
				}
			});
			break;
		case TIME:
			GlobalResource.getInstance().getUserRPC().isUpdateSuccessful(QueryConstants.STRING, obj.getTableName(), obj.getColumnName(), value, obj.getId(), new AsyncCallback<Boolean>() {
				
				@Override
				public void onSuccess(Boolean result) {
					//Refresh list
					widget.setValue(value);
					doSearch();
				}
				
				@Override
				public void onFailure(Throwable caught) {
					
				}
			});
			break;
		case ALERT:
			GlobalResource.getInstance().getUserRPC().isUpdateSuccessful(QueryConstants.STRING, obj.getTableName(), obj.getColumnName(), value, obj.getId(), new AsyncCallback<Boolean>() {
				
				@Override
				public void onSuccess(Boolean result) {
					//Refresh list
					widget.setValue(value);
					doSearch();
				}
				
				@Override
				public void onFailure(Throwable caught) {
					
				}
			});
			break;
		case ID:
			GlobalResource.getInstance().getUserRPC().isUpdateSuccessful(QueryConstants.INT, obj.getTableName(), obj.getColumnName(), value, obj.getId(), new AsyncCallback<Boolean>() {
				
				@Override
				public void onSuccess(Boolean result) {
					//Refresh list
					widget.setValue(value);
					doSearch();
				}
				
				@Override
				public void onFailure(Throwable caught) {
					
				}
			});
			break;
		case LOGIN:
			GlobalResource.getInstance().getUserRPC().isUpdateSuccessful(QueryConstants.STRING, obj.getTableName(), obj.getColumnName(), value, obj.getId(), new AsyncCallback<Boolean>() {
				
				@Override
				public void onSuccess(Boolean result) {
					//Refresh list
					widget.setValue(value);
					doSearch();
				}
				
				@Override
				public void onFailure(Throwable caught) {
					
				}
			});
			break;
		default:
			break;
		
		}
		
	}
	
	private void saveBlobEditChanges(UserSummaryDisplayObject obj, String imgUrl){}

	private void doSummarySortable(){
		 Scheduler.get().scheduleDeferred(new Command() {
		        @Override public void execute() {
					doNativeSortable(auxPanel.getElement().getId());
		        }
		      });
	}

	private Element getSummaryHeader(String headerTitle){
		Element header = DOM.createElement("h3");
		header.addClassName("m-b-md");
		header.setInnerText(headerTitle);

		return header;
	}

	private ArrayList<String> getOrderedHeader(HashMap<String, String> hash){
		ArrayList<String> list = new ArrayList<String>();
		for(String key : hash.keySet()){
			list.add(hash.get(key));
		}
		return list;
	}

	public void load(){
		 Scheduler.get().scheduleDeferred(new Command() {
		        @Override public void execute() {
					doNativeSortable(fieldPanel.getElement().getId());
		        }
		      });
	}

	private native void doNativeSortable(String id)/*-{
		$wnd.$("#"+id).sortable();
	}-*/;

	public void setRecordContentEventHandler(RecordContentEventHandler handler){
		this.handler = handler;
	}
}
