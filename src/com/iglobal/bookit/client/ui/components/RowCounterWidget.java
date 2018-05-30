package com.iglobal.bookit.client.ui.components;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class RowCounterWidget extends Composite {

	private static RowCounterWidgetUiBinder uiBinder = GWT
			.create(RowCounterWidgetUiBinder.class);

	interface RowCounterWidgetUiBinder extends
			UiBinder<Widget, RowCounterWidget> {
	}

	@UiField SpanElement rowCount;
	private String count;
	
	public RowCounterWidget(String count) {
		this.count = count;
		initWidget(uiBinder.createAndBindUi(this));
		initComponent();
	}
	
	public RowCounterWidget(){
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	private void initComponent(){
		rowCount.setInnerText(count);
	}
	
	public void setRowCounts(String counts){
		rowCount.setInnerText(counts);
	}

}
