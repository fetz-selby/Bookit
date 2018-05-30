package com.iglobal.bookit.client.ui.components;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class CustomDatePicker extends Composite {

	private static CustomDatePickerUiBinder uiBinder = GWT
			.create(CustomDatePickerUiBinder.class);

	interface CustomDatePickerUiBinder extends
			UiBinder<Widget, CustomDatePicker> {
	}

	@UiField InputElement  dateElement;
	
	public CustomDatePicker() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
