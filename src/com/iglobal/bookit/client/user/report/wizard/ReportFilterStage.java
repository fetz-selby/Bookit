package com.iglobal.bookit.client.user.report.wizard;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;
import com.iglobal.bookit.client.GlobalResource;
import com.iglobal.bookit.client.constants.DBConstants;
import com.iglobal.bookit.client.user.UserReportGlobalUI;
import com.iglobal.bookit.client.user.content.ReportWizard;
import com.iglobal.bookit.client.user.misc.ReportStepsEnum;
import com.iglobal.bookit.client.user.misc.ReportWizardObject;
import com.iglobal.bookit.client.user.report.widget.ReportCheckBox;
import com.iglobal.bookit.client.user.report.widget.ReportCheckBox.ReportCheckBoxEventHandler;
import com.iglobal.bookit.client.user.report.widget.ReportDatePicker;
import com.iglobal.bookit.client.user.report.widget.ReportDateRange;
import com.iglobal.bookit.client.user.report.widget.ReportFieldArrangerWidget;
import com.iglobal.bookit.client.user.report.widget.ReportSuggestBox;
import com.iglobal.bookit.client.user.report.widget.ReportSuggestBox.ReportSuggestBoxEventHandler;
import com.iglobal.bookit.client.user.widget.ScrollableFlowPanel;
import com.iglobal.bookit.shared.BookColumnObject;
import com.iglobal.bookit.shared.DataTypeConstants;
import com.iglobal.bookit.shared.QueryConstants;
import com.iglobal.bookit.shared.ReportFilterLoadObject;
import com.iglobal.bookit.shared.ReportGeneratorQueryObject;

public class ReportFilterStage extends ReportWizard {

	private Widget wizard;

	private ReportWizardObject wizardObject;
	private HashMap<String, String> filterHashMap, allReturnableFieldsHashMap, dataTypeHashMap, filterWithValueHash;
	private HashMap<String, DataTypeConstants> aliasWithDataType, fieldsWithDataType;
	private ReportGeneratorQueryObject queryObject;
	private ReportCheckBox checkBoxes;
	private String selectedcheckbox;
	private ArrayList<Widget> filterWidgetList;
	private ReportFieldArrangerWidget arranger;
	private boolean isDraggableShow = false;

	private static ReportFilterStageUiBinder uiBinder = GWT
			.create(ReportFilterStageUiBinder.class);

	interface ReportFilterStageUiBinder extends
	UiBinder<Widget, ReportFilterStage> {
	}

	@UiField FlowPanel fieldPanel;
	@UiField SimplePanel filterPanel, draggableContainer;
	@UiField DivElement fieldArrangeLabelDiv;
	@UiField Anchor nextBtn, backBtn;

	public ReportFilterStage(ReportWizardObject wizardObject) {
		this.wizardObject = wizardObject;
		setWidget(uiBinder.createAndBindUi(this));

		init();
		initEvent();
		doRPC();
	}

	public ReportFilterStage(ReportWizardObject wizardObject, String selectedcheckbox) {
		this.wizardObject = wizardObject;
		this.selectedcheckbox = selectedcheckbox;
		setWidget(uiBinder.createAndBindUi(this));

		init();
		initEvent();
		doRPC();
	}

	private void init(){
		allReturnableFieldsHashMap = new HashMap<String, String>();
		filterHashMap = new HashMap<String, String>();
		dataTypeHashMap = new HashMap<String, String>();
		filterWithValueHash = new HashMap<String, String>();

		aliasWithDataType = new HashMap<String, DataTypeConstants>();
		fieldsWithDataType = new HashMap<String, DataTypeConstants>();
		filterWidgetList = new ArrayList<Widget>();
	}

	private SimplePanel getLoadingPanel(){
		SimplePanel panel = new SimplePanel();
		panel.getElement().setAttribute("style", "height: 50px;");
		Label label = new Label("Loading ...");
		panel.add(label);
		
		return panel;
	}
	
	private void doRPC(){
		if(wizardObject.getColumnString() != null){
			//Show loading
			
			filterPanel.clear();
			filterPanel.add(getLoadingPanel());
			
			fieldPanel.clear();
			fieldPanel.add(getLoadingPanel());
			//mad:mad:2:A:F,ball:ball:1:A:F
			String[] columnFields = wizardObject.getColumnString().split("[,]");

			for(String field : columnFields){
				String fieldName = field.split("[:]")[0];
				String fieldAlias = field.split("[:]")[1];
				String dataType = field.split("[:]")[2];
				boolean isActive = field.split("[:]")[3].equals(DBConstants.ACTIVE)? true : false;
				boolean isReportField = field.split("[:]")[4].equals(DBConstants.TRUE) ? true : false;

				if(isActive){
					for(BookColumnObject obj : GlobalResource.getInstance().getBookColumnsList()){
						if(dataType.trim().equals(obj.getId())){
							if(obj.getDataType().trim().equals(DataTypeConstants.INT.getValue())){
								aliasWithDataType.put(fieldAlias, DataTypeConstants.INT);
								fieldsWithDataType.put(fieldName, DataTypeConstants.INT);
								allReturnableFieldsHashMap.put(fieldName, fieldAlias);

							}else if(obj.getDataType().trim().equals(DataTypeConstants.STRING.getValue())){
								aliasWithDataType.put(fieldAlias, DataTypeConstants.STRING);
								fieldsWithDataType.put(fieldName, DataTypeConstants.STRING);
								allReturnableFieldsHashMap.put(fieldName, fieldAlias);

							}else if(obj.getDataType().trim().equals(DataTypeConstants.BLOB.getValue())){

							}else if(obj.getDataType().trim().equals(DataTypeConstants.DATE.getValue())){
								aliasWithDataType.put(fieldAlias, DataTypeConstants.DATE);
								fieldsWithDataType.put(fieldName, DataTypeConstants.DATE);
								allReturnableFieldsHashMap.put(fieldName, fieldAlias);

							}else if(obj.getDataType().trim().equals(DataTypeConstants.DATETIME.getValue())){
								aliasWithDataType.put(fieldAlias, DataTypeConstants.DATETIME);
								fieldsWithDataType.put(fieldName, DataTypeConstants.DATETIME);
								allReturnableFieldsHashMap.put(fieldName, fieldAlias);
								
							}else if(obj.getDataType().trim().equals(DataTypeConstants.TIME.getValue())){
								aliasWithDataType.put(fieldAlias, DataTypeConstants.STRING);
								fieldsWithDataType.put(fieldName, DataTypeConstants.STRING);
								allReturnableFieldsHashMap.put(fieldName, fieldAlias);
								
							}else if(obj.getDataType().trim().equals(DataTypeConstants.ALERT.getValue())){
								aliasWithDataType.put(fieldAlias, DataTypeConstants.ALERT);
								fieldsWithDataType.put(fieldName, DataTypeConstants.ALERT);
								allReturnableFieldsHashMap.put(fieldName, fieldAlias);
								
							}else if(obj.getDataType().trim().equals(DataTypeConstants.LOGIN.getValue())){
								aliasWithDataType.put(fieldAlias, DataTypeConstants.LOGIN);
								fieldsWithDataType.put(fieldName, DataTypeConstants.LOGIN);
								allReturnableFieldsHashMap.put(fieldName, fieldAlias);
								
							}else if(obj.getDataType().trim().equals(DataTypeConstants.ID.getValue())){
								aliasWithDataType.put(fieldAlias, DataTypeConstants.ID);
								fieldsWithDataType.put(fieldName, DataTypeConstants.ID);
								allReturnableFieldsHashMap.put(fieldName, fieldAlias);
								
							}
						}
					}

					//returnFieldsHashMap.put(fieldName, fieldAlias);
					dataTypeHashMap.put(fieldName, dataType);

				}

				if(isReportField){
					filterHashMap.put(fieldName, fieldAlias);
				}
			}

			prepareDateRange();
			queryObject = new ReportGeneratorQueryObject(GlobalResource.getInstance().getUser().getId(), wizardObject.getBookName(), null, allReturnableFieldsHashMap, aliasWithDataType, fieldsWithDataType);

			GlobalResource.getInstance().getUserRPC().getReportFilterItemList(queryObject, new AsyncCallback<ReportFilterLoadObject>() {

				@Override
				public void onSuccess(ReportFilterLoadObject result) {
					if(result != null){
						ScrollableFlowPanel scroller = new ScrollableFlowPanel("report-filtergroup-panel-style m-b-lg");
						//int filterCounter = 0;

						for(String alias : result.getItemsHash().keySet()){

							ArrayList<String> items = new ArrayList<String>();
							for(String item : result.getItemsHash().get(alias)){
								GWT.log("Key: "+alias+", Items: "+ item);
								if(item != null && !item.toLowerCase().equals("null")){
									items.add(item);
								}
							}

							if(filterHashMap.containsValue(alias)){
								String field = "";
								for(String key : filterHashMap.keySet()){
									if(filterHashMap.get(key).equals(alias)){
										field = key;
									}
								}
								
								GWT.log("[reports] "+result.getFieldDataTypeHash().get(alias));
								
								switch(result.getFieldDataTypeHash().get(alias)){
								case BLOB:
									break;
								case DATE:
									ReportDatePicker datePicker = new ReportDatePicker(alias, field);
									filterWidgetList.add(datePicker);
									scroller.add(datePicker);
									//filterCounter ++;
									break;
								case DATETIME:
									ReportSuggestBox suggestBox = getReportSuggestBox(alias, field, items);
									scroller.add(suggestBox);
									filterWidgetList.add(suggestBox);
									//filterCounter ++;
									break;
								case INT:
									ReportSuggestBox suggestBox1 = getReportSuggestBox(alias, field, items);
									doIntConstraints(suggestBox1.getSuggestBaseBox());
									scroller.add(suggestBox1);
									filterWidgetList.add(suggestBox1);
									//filterCounter ++;
									break;
								case STRING:
									ReportSuggestBox suggestBox2 = getReportSuggestBox(alias, field, items);
									scroller.add(suggestBox2);
									filterWidgetList.add(suggestBox2);
									//filterCounter ++;
									break;
								case TIME:
									ReportSuggestBox suggestBox3 = getReportSuggestBox(alias, field, items);
									scroller.add(suggestBox3);
									filterWidgetList.add(suggestBox3);
									//filterCounter ++;
									break;
								case ALERT:
									//ReportSuggestBox suggestBox3 = getReportSuggestBox(alias, field, items);
									//scroller.add(suggestBox3);
									//filterWidgetList.add(suggestBox3);
									//filterCounter ++;
									break;
								case ID:
									ReportSuggestBox suggestBox4 = getReportSuggestBox(alias, field, items);
									scroller.add(suggestBox4);
									filterWidgetList.add(suggestBox4);
									//filterCounter ++;
									break;
								case LOGIN:
									ReportSuggestBox suggestBox5 = getReportSuggestBox(alias, field, items);
									scroller.add(suggestBox5);
									filterWidgetList.add(suggestBox5);
									//filterCounter ++;
									break;
								default:
									break;
								
								}
							}
						}
						
						ReportDateRange dateRange = new ReportDateRange(QueryConstants.CREATED_TS);
						filterWidgetList.add(dateRange);
						scroller.add(dateRange);
						
						
						//if(filterCounter > 0){
						filterPanel.clear();
						filterPanel.add(scroller);
						//}else{
							//Filter widget empty
							//filterPanel.clear();
							//filterPanel.add(new ReportErrorWidget());
						//}

						if(UserReportGlobalUI.getInstance().getQueryObject() != null && UserReportGlobalUI.getInstance().getQueryObject().getOrderFieldValueList() != null && UserReportGlobalUI.getInstance().getQueryObject().getOrderFieldKeysList() != null){
							arranger = new ReportFieldArrangerWidget(UserReportGlobalUI.getInstance().getQueryObject().getOrderFieldKeysList(), UserReportGlobalUI.getInstance().getQueryObject().getOrderFieldValueList());
						}else{
							arranger = new ReportFieldArrangerWidget();
						}
						
						if(selectedcheckbox != null){
							checkBoxes = new ReportCheckBox(allReturnableFieldsHashMap, wizardObject.getSelectedFields());
						}else{
							checkBoxes = new ReportCheckBox(allReturnableFieldsHashMap);
						}
						
						//Add draggables on check
						checkBoxes.setReportCheckBoxEventHandler(new ReportCheckBoxEventHandler() {
							
							@Override
							public void onDeCheck(String value, String alias) {
								arranger.removeDraggable(value);
							}
							
							@Override
							public void onCheck(String value, String alias) {
								if(!isDraggableShow){
									showDraggingLabel(true);
									isDraggableShow = true;
								}
								arranger.addDraggable(alias, value);
							}

							@Override
							public void onSelectAllChecked() {
								if(!isDraggableShow){
									showDraggingLabel(true);
									isDraggableShow = true;
								}
								arranger.addAll(allReturnableFieldsHashMap);
							}

							@Override
							public void onSelectAllDeChecked() {
								isDraggableShow = false;
								showDraggingLabel(false);
								arranger.removeAll();
							}
						});
						
						fieldPanel.clear();
						fieldPanel.add(checkBoxes);
						draggableContainer.setWidget(arranger);
					}
				}

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub

				}
			});
		}
	}
	
	private void prepareDateRange(){
		if(allReturnableFieldsHashMap != null){
			allReturnableFieldsHashMap.put(QueryConstants.CREATED_TS, QueryConstants.CREATED_TS);
		}
		
		if(aliasWithDataType != null){
			aliasWithDataType.put(QueryConstants.CREATED_TS, DataTypeConstants.DATE);
		}

		if(fieldsWithDataType != null){
			fieldsWithDataType.put(QueryConstants.CREATED_TS, DataTypeConstants.DATE);
		}
	}
	
	private void showDraggingLabel(boolean isShow){
		if(isShow){
			fieldArrangeLabelDiv.removeClassName("gwt-hide");
		}else{
			fieldArrangeLabelDiv.addClassName("gwt-hide");
		}
	}
	
	private void doIntConstraints(final TextBoxBase textBox){
		textBox.addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				Widget sender = (Widget) event.getSource();
			    char charCode = event.getCharCode();
			    int keyCode = event.getNativeEvent().getKeyCode();
			    if (!Character.isDigit(charCode) && charCode != '.' && charCode != '-' && keyCode != KeyCodes.KEY_TAB && keyCode != KeyCodes.KEY_BACKSPACE && keyCode != KeyCodes.KEY_DELETE && keyCode != KeyCodes.KEY_ENTER && keyCode != KeyCodes.KEY_HOME && keyCode != KeyCodes.KEY_END && keyCode != KeyCodes.KEY_LEFT && keyCode != KeyCodes.KEY_UP && keyCode != KeyCodes.KEY_RIGHT && keyCode != KeyCodes.KEY_DOWN) {
			      ((TextBox) sender).cancelKey();
			    }

			    String text = textBox.getText();
			    if (text.contains(".") && charCode == '.') {
			      ((TextBox) sender).cancelKey();
			    }				
			}
		});
		
	}
	
	private ReportSuggestBox getReportSuggestBox(String suggestLabel, String field, ArrayList<String> items){
		ReportSuggestBox suggestBox = new ReportSuggestBox(suggestLabel, field, items);
		suggestBox.setReportSuggestBoxEventHandler(new ReportSuggestBoxEventHandler() {

			@Override
			public void onItemSuggest(String label, String item) {
				if(filterWithValueHash.containsKey(label)){
					filterWithValueHash.remove(label);
					filterWithValueHash.put(label, item);
				}else{
					filterWithValueHash.put(label, item);
				}
			}
		});

		
		return suggestBox;
	}

	private boolean isUpdateReportWizardObject(){
		if(checkBoxes != null){
			final String SEPARATOR = ", ";
			//Add selected checks to queryObject, and wizardObject
			HashMap<String, String> selectableReturnableFieldsHashMap = checkBoxes.getSelectedCheckHash();
			if(selectableReturnableFieldsHashMap.size() == 0){
				//Show popup info
				Window.alert("Please select some return fields. Thank you!");
				return false;
			}else{
				String selectedFields = "";
				for(String field : selectableReturnableFieldsHashMap.keySet()){
					GWT.log("Selected Field is "+field+", alias is "+selectableReturnableFieldsHashMap.get(field));
					selectedFields += selectableReturnableFieldsHashMap.get(field)+SEPARATOR;
				}

				if(selectedFields.contains(SEPARATOR)){
					selectedFields = selectedFields.substring(0, selectedFields.lastIndexOf(SEPARATOR));
				}
				
				if(filterWidgetList != null){
					HashMap<String, String> filterWithValue = new HashMap<String, String>();
					for(Widget widget : filterWidgetList){
						
						String field = "", value = "";
						
						if(widget instanceof ReportDatePicker){
							field = ((ReportDatePicker) widget).getField();
							value = ((ReportDatePicker) widget).getValue();
						}else if(widget instanceof ReportSuggestBox){
							field = ((ReportSuggestBox) widget).getField();
							value = ((ReportSuggestBox) widget).getValue();
						}else if(widget instanceof ReportDateRange){
							field = ((ReportDateRange) widget).getField();
							value = ((ReportDateRange) widget).getValue();
						}
						
						if(value == null || value.isEmpty()){
							continue;
						}
						filterWithValue.put(field, value);
					}
					
					queryObject.setFilterWithValueHash(filterWithValue);
				}
				
				wizardObject.setSelectedFields(selectedFields);
				queryObject.setReturnFieldsHash(selectableReturnableFieldsHashMap);

				UserReportGlobalUI.getInstance().setReportWizardObject(wizardObject);
				UserReportGlobalUI.getInstance().setQueryObject(queryObject);
				
				return true;
			}
		}else{
			return false;
		}

	}
	
	private void saveDraggableState(){
		if(queryObject != null){
			queryObject.setOrderFieldValueList(arranger.getOrder());
			queryObject.setOrderFieldKeysList(arranger.getOrderedKey());
			
			GWT.log("-- Ordered List "+arranger.getOrder().toString()+" --");
			GWT.log("-- Ordered List Keys "+arranger.getOrderedKey().toString()+" --");
		}
	}
	
	private void revertDraggableState(){
		if(queryObject != null){
			queryObject.setOrderFieldValueList(null);
			queryObject.setOrderFieldKeysList(null);
			
			GWT.log("-- Ordered List "+arranger.getOrder().toString()+" --");
			GWT.log("-- Ordered List Keys "+arranger.getOrderedKey().toString()+" --");
		}
	}

	private void initEvent(){
		nextBtn.setText("Next");
		nextBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(isUpdateReportWizardObject()){
					saveDraggableState();
					next();
				}

			}
		});

		backBtn.setText("Back");
		backBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				revertDraggableState();
				previous();
			}
		});
	}

	@Override
	public ReportStepsEnum getStage() {
		return ReportStepsEnum.FIELDS_STAGE;
	}

	@Override
	public ReportWizardObject getWizardObject() {
		return wizardObject;
	}

	@Override
	public Widget getWidget() {
		return wizard;
	}

	@Override
	public ReportStepsEnum getNextWizard() {
		return ReportStepsEnum.OVERVIEW_STAGE;
	}

	@Override
	public ReportStepsEnum getPreviousWizard() {
		return ReportStepsEnum.BOOK_STAGE;
	}

}
