package com.iglobal.bookit.client.user.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.iglobal.bookit.client.user.widget.object.DynamicTextBoxQueryObject;
import com.iglobal.bookit.shared.DataTypeConstants;

public class DynamicTextBox extends Composite implements IsEntryWidget{

	private static DynamicTextBoxUiBinder uiBinder = GWT
			.create(DynamicTextBoxUiBinder.class);
	private DataTypeConstants dataTypeId;
	private String labelName, fieldName, dataType;

	interface DynamicTextBoxUiBinder extends UiBinder<Widget, DynamicTextBox> {
	}
	
	@UiField LabelElement label;
	@UiField TextBox textBox;

	public DynamicTextBox(String labelName, String fieldName, String dataType, DataTypeConstants dataTypeId) {
		this.labelName = labelName;
		this.fieldName = fieldName;
		this.dataTypeId = dataTypeId;
		this.dataType = dataType;
		
		initWidget(uiBinder.createAndBindUi(this));
		initComponent();
	}
	
	private void initComponent(){
		label.setInnerText(labelName);
		
		switch(dataTypeId){
		case INT:
			doIntConstraints();
			break;
		case STRING:
			doTextConstraints();
			break;
		default:
			break;
		
		}
	}
	
	private void doIntConstraints(){
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
	private void doTextConstraints(){}
	
	public DynamicTextBoxQueryObject getQuery(){
		DynamicTextBoxQueryObject queryObject = new DynamicTextBoxQueryObject();
		queryObject.setFieldName(fieldName);
		queryObject.setFieldValue(textBox.getText().trim());
		queryObject.setDataType(dataType);
		
		return queryObject;
	}
	
	public void reset(){
		textBox.setText("");
	}

}
