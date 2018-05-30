package com.iglobal.bookit.client.ui.components;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.iglobal.bookit.shared.QueryEnum;
import com.iglobal.bookit.shared.QueryOperatorEnum;

public class CustomTextBox extends Composite {
	
	private String placeholder = "";
	private String labelString;
	private QueryEnum queryEnumObject;
	private QueryOperatorEnum operator;
	private static CustomTextBoxUiBinder uiBinder = GWT
			.create(CustomTextBoxUiBinder.class);

	interface CustomTextBoxUiBinder extends UiBinder<Widget, CustomTextBox> {
	}

	@UiField TextBox textBox;
	@UiField LabelElement label;

	public CustomTextBox(String labelString, String placeholder, QueryEnum queryEnum, QueryOperatorEnum operator) {
		initWidget(uiBinder.createAndBindUi(this));
		this.placeholder = placeholder;
		this.labelString = labelString;
		this.queryEnumObject = queryEnum;
		this.operator = operator;
		textBox.getElement().setAttribute("placeholder", placeholder);
		initComponent();
	}
	
	public CustomTextBox(){
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	private void initComponent(){
		label.setInnerText(labelString);
		textBox.getElement().setAttribute("placeholder", placeholder);
	}
	
	public void load(){
		initComponent();
	}
	
	public String getPlaceholder() {
		return placeholder;
	}

	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}

	public String getLabelString() {
		return labelString;
	}

	public void setLabelString(String labelString) {
		this.labelString = labelString;
	}

	public QueryEnum getQueryEnumObject() {
		return queryEnumObject;
	}

	public void setQueryEnumObject(QueryEnum queryEnumObject) {
		this.queryEnumObject = queryEnumObject;
	}

	public QueryOperatorEnum getOperator() {
		return operator;
	}

	public void setOperator(QueryOperatorEnum operator) {
		this.operator = operator;
	}

	public Widget getControlWidget(){
		return textBox;
	}

}
