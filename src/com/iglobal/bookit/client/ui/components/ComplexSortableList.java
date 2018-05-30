package com.iglobal.bookit.client.ui.components;

import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.iglobal.bookit.client.GlobalResource;
import com.iglobal.bookit.client.ui.components.CustomConfirmationPanel.CustomConfirmationPanelEventHandler;
import com.iglobal.bookit.client.ui.components.object.SortableObject;
import com.iglobal.bookit.client.utils.Utils;
import com.iglobal.bookit.shared.BookColumnObject;

public class ComplexSortableList extends Composite {
	private static final String SELS = "sels_value";
	private static final int ALERT_ID = 6; 
	private ComplexSortableListEventHandler handler;
	private HashMap<String, ListBox> dataMap;
	private HashMap<String, String> reportCheckMap;
	private HashMap<String, ColumnObject> oldColumnHashMap, newColumnHashMap;
	private int counter = 0;
	private String spanId;
	private static int spanIdCounter;
	private static int globalCounter;
	private static ComplexSortableListUiBinder uiBinder = GWT
			.create(ComplexSortableListUiBinder.class);

	interface ComplexSortableListUiBinder extends
	UiBinder<Widget, ComplexSortableList> {
	}

	public interface ComplexSortableListEventHandler{
		void onSortableRowCanceled(ColumnObject object);
		void onSortableRowEdit(ColumnObject object);
		void onListItemSelected(String name, String value);
	}

	public class ColumnObject{
		private String columnCounterId, columnName, columnAlias, columnDataType, columnStatus;
		private boolean isReport;
		public ColumnObject(String columnString){
			doInit(columnString);
		}
		private void doInit(String columnString){
			//address:address:1:A:F
			if(columnString.contains(":")){
				String[] columnDetailArray = columnString.split("[:]");
				if(columnDetailArray.length == 4){
					columnName = columnDetailArray[0];
					columnAlias = columnDetailArray[1];
					columnDataType = columnDetailArray[2];
					columnStatus = columnDetailArray[3];

					GWT.log("colName "+columnName+", colAlias "+columnAlias+", dt "+columnDataType+", st "+columnStatus);
				}else if(columnDetailArray.length == 5){
					columnName = columnDetailArray[0];
					columnAlias = columnDetailArray[1];
					columnDataType = columnDetailArray[2];
					columnStatus = columnDetailArray[3];

					if(columnDetailArray[4].equals("T")){
						isReport = true;
					}else{
						isReport = false;
					}

					GWT.log("colName "+columnName+", colAlias "+columnAlias+", dt "+columnDataType+", st "+columnStatus+", isreportable "+isReport);
				}
			}
		}
		public String getColumnCounterId() {
			return columnCounterId;
		}

		public String getColumnName() {
			return columnName;
		}

		public String getColumnDataType() {
			return columnDataType;
		}

		public void setColumnDataType(String dataType){
			this.columnDataType = dataType;
		}

		public String getColumnStatus() {
			return columnStatus;
		}

		public String getColumnAlias() {
			return columnAlias;
		}

		public void setColumnAlias(String columnNewAlias) {
			this.columnAlias = columnNewAlias;
		}

		public void setColumnStatus(String status){
			this.columnStatus = status;
		}

		public String getFormattedString(){
			if(isReport){
				if(columnDataType.trim().toLowerCase().equals(""+ALERT_ID)){
					return "alert"+":"+"alert"+":"+columnDataType+":"+columnStatus+":"+"T";
				}
				return Utils.getCompoundedName(columnName).trim()+":"+columnAlias.trim()+":"+columnDataType.trim()+":"+columnStatus+":"+"T";
			}

			if(columnDataType.trim().toLowerCase().equals(""+ALERT_ID)){
				return "alert"+":"+"alert"+":"+columnDataType+":"+columnStatus+":"+"F";
			}
			
			return Utils.getCompoundedName(columnName).trim()+":"+columnAlias.trim()+":"+columnDataType+":"+columnStatus+":"+"F";
		}

		public boolean isReport() {
			return isReport;
		}

		public void setReport(boolean isReport) {
			this.isReport = isReport;
		}

		@Override
		public String toString() {
			return "columnName : "+columnName+", columnAlias : "+columnAlias+", columnDataType : "+columnDataType+", columnStatus : "+columnStatus+", isReportable :"+isReport;
		}
	}

	@UiField UListElement ulElement;

	public ComplexSortableList() {
		initWidget(uiBinder.createAndBindUi(this));
		initElement();
	}

	private void initElement(){
		ulElement.setId(""+globalCounter++);
		dataMap = new HashMap<String, ListBox>();
		reportCheckMap = new HashMap<String, String>();
		oldColumnHashMap = new HashMap<String, ColumnObject>();
		newColumnHashMap = new HashMap<String, ColumnObject>();
	}

	private CheckBox getCheckBox(boolean isChecked, final String spanId){

		final CheckBox checkBox = new CheckBox();
		checkBox.setStyleName("pull-left");
		checkBox.setTitle("report");

		DOM.sinkEvents(checkBox.getElement(), Event.ONCHANGE);
		DOM.setEventListener(checkBox.getElement(), new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				if(reportCheckMap.get(spanId).equals("T")){
					doMapValueSwap(spanId, "F");
				}else{
					doMapValueSwap(spanId, "T");
				}
			}
		});

		if(isChecked){
			checkBox.setValue(true);
			checkBox.getElement().setAttribute(SELS, "T");
			reportCheckMap.put(spanId, "T");
		}else{
			reportCheckMap.put(spanId, "F");
		}

		return checkBox;
	}

	private void doMapValueSwap(String key, String value){
		if(reportCheckMap.containsKey(key)){
			reportCheckMap.remove(key);
			reportCheckMap.put(key, value);
		}
	}

	public void add(SortableObject sortableObject){
		if(dataMap.containsKey(sortableObject.getName())){
			return;
		}

		generateSpanId();
		
		Element labelElement = getLabelElement();
		CheckBox checkBox = getCheckBox(false, spanId);
		ListBox listBox = getDataTypeBox(true, null, labelElement, spanId);
		Element listboxElement = getLeftPullElements(sortableObject.getName(), listBox, labelElement);

		Element li = getLIElement();
		li.appendChild(checkBox.getElement());
		li.appendChild(listboxElement);
		li.appendChild(getActionOptionElement(li, null, spanId, sortableObject.getName(), listBox));

		ulElement.appendChild(li);
		initNative();

		ColumnObject obj = null;
		if(sortableObject.getValue().trim().isEmpty()){
			String str = sortableObject.getName();

			//			if(checkBox.getElement().getAttribute(SELS).equals("T")){
			//				str = str+":"+str+":"+(listBox.getValue(listBox.getSelectedIndex()))+":A"+":T";
			//			}else{
			//				str = str+":"+str+":"+(listBox.getValue(listBox.getSelectedIndex()))+":A"+":F";
			//			}
			str = str.trim()+":"+str.trim()+":"+(listBox.getValue(listBox.getSelectedIndex()))+":A";

			obj = new ColumnObject(str);
			obj.setReport(true);
		}

		dataMap.put(spanId, listBox);
		newColumnHashMap.put(spanId, obj);
	}

	public void add(String tableName, SortableObject sortableObject, boolean isActive){
		ColumnObject columnObject = new ColumnObject(sortableObject.getValue());

		//Do not show deleted fields
		if(columnObject.getColumnStatus().trim().equals("D")){
			return;
		}

		if(dataMap.containsKey(sortableObject.getName())){
			return;
		}

		generateSpanId();

		CheckBox checkBox = null;
		Element labelElement = getLabelElement();

		if(columnObject.isReport()){
			checkBox = getCheckBox(true, spanId);
		}else{
			checkBox = getCheckBox(false, spanId);
		}

		ListBox listBox = getDataTypeBox(isActive, columnObject.getColumnDataType(), labelElement, spanId);
		String key = sortableObject.getName();
		GWT.log("key is "+key+", value is "+columnObject.getColumnDataType());

		Element listBoxElement = getLeftPullElements(key, listBox, labelElement);

		Element li = getLIElement();
		li.appendChild(checkBox.getElement());

		li.appendChild(listBoxElement);
		li.appendChild(getActionOptionElement(li, columnObject, spanId, key, tableName, listBox));
		//li.appendChild(getTextableTagElement(sortableObject));

		ulElement.appendChild(li);
		initNative();

		dataMap.put(spanId, listBox);
		oldColumnHashMap.put(spanId, columnObject);
	}

	public void setCounter(int startCounterFrom){
		this.counter = startCounterFrom;
	}

	private Element getLIElement(){
		Element li = DOM.createElement("li");
		li.setClassName("list-group-item field-style");
		//li.setAttribute("draggable", "true");
		li.setAttribute("style", "position: relative");     

		return li;
	}

	private Element getActionOptionElement(final Element parentElement, final ColumnObject obj, final String spanId, final String text, final ListBox listBox){

		Element spanElement = DOM.createElement("div");
		spanElement.setClassName("pull-left");

		Element editAnchor = DOM.createElement("a");
		editAnchor.setAttribute("href", "#");

		Element edit = DOM.createElement("i");
		edit.setClassName("fa fa-pencil icon-muted fa-fw m-r-xs");

		Element cAnchor = DOM.createElement("a");
		cAnchor.setAttribute("href", "#");

		Element close = DOM.createElement("i");
		close.setClassName("fa fa-times icon-muted fa-fw");

		DOM.sinkEvents(edit, Event.ONCLICK);
		DOM.setEventListener(edit, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				if(!listBox.getValue(listBox.getSelectedIndex()).equals(""+ALERT_ID)){
					Element element = DOM.getElementById(spanId);
					element.setInnerText("");
					element.appendChild(getTextBox(spanId, text));
					if(handler != null){
						handler.onSortableRowEdit(obj);
					}
				}
				
			}
		});

		DOM.sinkEvents(close, Event.ONCLICK);
		DOM.setEventListener(close, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				CustomConfirmationPanel panel = new CustomConfirmationPanel();
				panel.setCustomConfirmationPanelEventHandler(new CustomConfirmationPanelEventHandler() {

					@Override
					public void onContinueClicked() {
						if(dataMap.containsKey(spanId)){

							doColumnRemoval(spanId);
							//oldColumnHashMap.remove(spanId);
							//newColumnHashMap.remove(spanId);
						}

						parentElement.removeFromParent();
						if(handler != null){
							handler.onSortableRowCanceled(obj);
						}						
					}

					@Override
					public void onCancelClicked() {
						// TODO Auto-generated method stub

					}
				});
				panel.show();
			}
		});

		editAnchor.appendChild(edit);
		cAnchor.appendChild(close);

		spanElement.appendChild(editAnchor);
		spanElement.appendChild(cAnchor);

		return spanElement;
	}

	private Element getActionOptionElement(final Element parentElement, final ColumnObject obj, final String spanId, final String text, final String tableName, final ListBox listBox){

		Element spanElement = DOM.createElement("div");
		spanElement.setClassName("pull-left");

		Element editAnchor = DOM.createElement("a");
		editAnchor.setAttribute("href", "#");

		Element edit = DOM.createElement("i");
		edit.setClassName("fa fa-pencil icon-muted fa-fw m-r-xs");

		Element cAnchor = DOM.createElement("a");
		cAnchor.setAttribute("href", "#");

		Element close = DOM.createElement("i");
		close.setClassName("fa fa-times icon-muted fa-fw");

		DOM.sinkEvents(edit, Event.ONCLICK);
		DOM.setEventListener(edit, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				if(!listBox.getValue(listBox.getSelectedIndex()).equals(""+ALERT_ID)){
					Element element = DOM.getElementById(spanId);
					element.setInnerText("");
					element.appendChild(getTextBox(spanId, text));
					if(handler != null){
						handler.onSortableRowEdit(obj);
					}
				}
			}
		});

		DOM.sinkEvents(close, Event.ONCLICK);
		DOM.setEventListener(close, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				if(isLastField()){
					CustomWarningPanel warningPanel = new CustomWarningPanel("Sorry, you can not remove the last field.");
					warningPanel.show();
					return;
				} 

				CustomConfirmationPanel panel = new CustomConfirmationPanel();
				panel.setCustomConfirmationPanelEventHandler(new CustomConfirmationPanelEventHandler() {

					@Override
					public void onContinueClicked() {

						//Make RPC to remove the column
						GlobalResource.getInstance().getAdminUpdateRPC().isColumnRemoved(tableName, obj.getColumnName(), new AsyncCallback<Boolean>() {

							@Override
							public void onSuccess(Boolean result) {
								if(dataMap.containsKey(spanId)){
									doColumnRemoval(spanId);
								}

								parentElement.removeFromParent();
								if(handler != null){
									handler.onSortableRowCanceled(obj);
								}		
							}

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub

							}
						});
					}

					@Override
					public void onCancelClicked() {
						// TODO Auto-generated method stub

					}
				});
				panel.show();
			}
		});

		editAnchor.appendChild(edit);
		cAnchor.appendChild(close);

		spanElement.appendChild(editAnchor);
		spanElement.appendChild(cAnchor);

		return spanElement;
	}

	private void doColumnRemoval(String spanId){
		if(oldColumnHashMap.containsKey(spanId)){
			ColumnObject obj = oldColumnHashMap.get(spanId);
			obj.setColumnStatus("D");

			oldColumnHashMap.remove(spanId);
			if(dataMap.containsKey(spanId)){
				dataMap.remove(spanId);
			}
		}

		if(newColumnHashMap.containsKey(spanId)){
			newColumnHashMap.remove(spanId);
			dataMap.remove(spanId);
			reportCheckMap.remove(spanId);
		}
	}

	private Element getTextBox(final String spanId, final String text){
		//<input type="text" class="form-control no-border" placeholder="">
		final TextBox editBox = new TextBox();
		editBox.setText(text);
		editBox.setStyleName("form-control sortable-txtbox");

		DOM.sinkEvents(editBox.getElement(), Event.ONKEYPRESS);
		DOM.setEventListener(editBox.getElement(), new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				if(event.getCharCode() == KeyCodes.KEY_ENTER || event.getCharCode() == KeyCodes.KEY_TAB || event.getCharCode() == KeyCodes.KEY_DOWN || event.getCharCode() == KeyCodes.KEY_UP){
					Element element = DOM.getElementById(spanId);
					element.setInnerText(editBox.getText());

					doColumnEdit(spanId, editBox.getText());
				}
			}
		});

		DOM.sinkEvents(editBox.getElement(), Event.ONBLUR);
		DOM.setEventListener(editBox.getElement(), new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				Element element = DOM.getElementById(spanId);
				element.setInnerText(editBox.getText());

				doColumnEdit(spanId, editBox.getText());
			}
		});

		
		return editBox.getElement();
	}

	private boolean isLastField(){
		if(dataMap.size() == 1){
			return true;
		}
		return false;
	}

	private void doColumnEdit(String spanId, String newColumnName){
		if(oldColumnHashMap.containsKey(spanId)){
			ColumnObject obj = oldColumnHashMap.get(spanId);

			obj.setColumnAlias(newColumnName);
		}else if(newColumnHashMap.containsKey(spanId)){
			ColumnObject obj = newColumnHashMap.get(spanId);

			if(obj == null){
				ListBox listBox = dataMap.get(spanId);
				String columnString = "";

				if(listBox.getValue(listBox.getSelectedIndex()).equals(""+ALERT_ID)){
					columnString = "alert"+":"+"alert"+":"+listBox.getValue(listBox.getSelectedIndex())+":A"+":F";
				}else{
					columnString = newColumnName.trim()+":"+newColumnName.trim()+":"+listBox.getValue(listBox.getSelectedIndex()).trim()+":A"+":F";
				}
				obj = new ColumnObject(columnString);

				newColumnHashMap.remove(spanId);
				newColumnHashMap.put(spanId, obj);
			}else{
				if(obj.getColumnName().toLowerCase().equals("alert") || obj.getColumnDataType().equals(""+ALERT_ID)){
					obj.setColumnAlias("alert");
				}else{
					obj.setColumnAlias(newColumnName);
				}
			}
		}
	}

	public HashMap<String, ListBox> getSelectedSortableHashMap(){
		return dataMap;
	}

	public String getSelectedKeyString(){
		final String DELIMITER = ",";
		boolean isChecked = false; 

		String str = "";

		for(String spanId : dataMap.keySet()){
			ListBox listBox = dataMap.get(spanId);
			String value = listBox.getValue(listBox.getSelectedIndex());

			String reportValue = "";

			if(reportCheckMap.containsKey(spanId)){
				reportValue = reportCheckMap.get(spanId);
				if(reportValue.equals("T")){
					isChecked = true;
				}else{
					isChecked = false;
				}
			}

			if(newColumnHashMap.containsKey(spanId)){
				ColumnObject obj = newColumnHashMap.get(spanId);
				obj.setColumnDataType(value);
				obj.setReport(isChecked);

				GWT.log("*** From New *** "+newColumnHashMap.get(spanId).getFormattedString());
				str += obj.getFormattedString()+DELIMITER;
				GWT.log("str is "+str);
			}
		}

		if(str.isEmpty()){
			return "";
		}else{
			return str.substring(0, str.lastIndexOf(DELIMITER));
		}
	}

	public String getUpdateKeyString(){
		final String DELIMITER = ",";
		boolean isChecked = false; 

		String str = "";

		for(String spanId : dataMap.keySet()){
			ListBox listBox = dataMap.get(spanId);
			String value = listBox.getValue(listBox.getSelectedIndex());

			String reportValue = "";

			if(reportCheckMap.containsKey(spanId)){
				reportValue = reportCheckMap.get(spanId);
				if(reportValue.equals("T")){
					isChecked = true;
				}else{
					isChecked = false;
				}
			}
			if(oldColumnHashMap.containsKey(spanId)){
				ColumnObject obj = oldColumnHashMap.get(spanId);
				obj.setColumnDataType(value);
				obj.setReport(isChecked);
				
				GWT.log("*** From Old ***");
				str += obj.getFormattedString()+DELIMITER;
			}
		}

		if(str.isEmpty()){
			return "";
		}else{
			return str.substring(0, str.lastIndexOf(DELIMITER));
		}
	}

	private String getDataListBoxValue(String key){
		ListBox dataBox = dataMap.get(key);

		return dataBox.getValue(dataBox.getSelectedIndex());
	}

	private Element getLeftPullElements(String fieldName, ListBox dataList, Element labelDiv){
		Element span = DOM.createElement("div");
		span.setClassName("pull-left m-r-lg");

		Label labelSpan = new Label();
		//labelSpan.setStyleName("m-r-sm media-sm");
		labelSpan.setText(Utils.getTruncatedText(fieldName, 24));
		labelSpan.setTitle(fieldName);
		labelDiv.appendChild(labelSpan.getElement());


		Element listBoxDiv = DOM.createElement("div");
		listBoxDiv.addClassName("pull-left book-datatype-field");

		listBoxDiv.appendChild(dataList.getElement());

		span.appendChild(labelDiv);
		span.appendChild(listBoxDiv);

		return span;
	}
	
	private Element getLabelElement(){
		Element labelDiv = DOM.createElement("div");
		labelDiv.addClassName("pull-left book-field-name");
		labelDiv.setId(spanId);
		
		return labelDiv;
	}

	private void generateSpanId(){
		spanIdCounter ++;
		spanId = "span_"+spanIdCounter;
	}

	private ListBox getDataTypeBox(boolean isActive, String itemId, final Element labelElement, final String spanId){
		final String TEXT_ID = "2";
		final ListBox dataType = new ListBox();
		//dataType.addStyleName("m-r-sm col-sm-5 media-lg");
		dataType.addStyleName("m-r-sm media-sm");


		int index = 0, counter = 0, textIndex = 0;
		for(BookColumnObject object : GlobalResource.getInstance().getBookColumnsList()){
			dataType.addItem(object.getAlias(), object.getId());

			GWT.log("id is "+itemId+", and other is "+object.getId());

			if(object.getId().trim().equals(itemId)){
				index = counter;
			}
			
			if(object.getId().trim().equals(TEXT_ID)){
				textIndex = counter;
			}

			counter ++;
		}

		if(itemId != null){
			dataType.setItemSelected(index, true);
			GWT.log("get selected index is "+dataType.getValue(dataType.getSelectedIndex()));
			dataType.fireEvent(new ClickEvent(){});
		}

		if(!isActive){
			dataType.setEnabled(false);
		}else{
			dataType.setSelectedIndex(textIndex);
		}

		Element listElement = dataType.getElement().cast();
		DOM.sinkEvents(listElement, Event.ONCLICK);
		DOM.setEventListener(listElement, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				if(handler != null){
					handler.onListItemSelected(dataType.getItemText(dataType.getSelectedIndex()), dataType.getValue(dataType.getSelectedIndex()));
				}
			}
		});
		
		DOM.sinkEvents(listElement, Event.ONCHANGE);
		DOM.setEventListener(listElement, new EventListener() {
			
			@Override
			public void onBrowserEvent(Event event) {
				if(dataType.getValue(dataType.getSelectedIndex()).equals(""+ALERT_ID)){
					labelElement.setInnerText("alert");
					doColumnEdit(spanId, "alert");
				}
			}
		});

		
		return dataType;
	}

	private Element getTextableTagElement(SortableObject obj){

		Element div = DOM.createElement("div");
		div.setClassName("clear");
		div.setInnerText(obj.getName());

		return div;
	}

	public void clear(){
		ulElement.setInnerText("");
	}

	public void setComplexSortableListEventHandler(ComplexSortableListEventHandler handler){
		this.handler = handler;
	}

	private void initNative(){

		Scheduler.get().scheduleDeferred(new Command() {
			@Override public void execute() {
				doNativeSorting(ulElement.getId());
			}
		});

	}

	private native void doNativeSorting(String id)/*-{
		$wnd.$( "#"+id).sortable();
	}-*/;

}
