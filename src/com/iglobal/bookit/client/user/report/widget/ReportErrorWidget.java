package com.iglobal.bookit.client.user.report.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class ReportErrorWidget extends Composite {

	private static ReportErrorWidgetUiBinder uiBinder = GWT
			.create(ReportErrorWidgetUiBinder.class);

	interface ReportErrorWidgetUiBinder extends
			UiBinder<Widget, ReportErrorWidget> {
	}

	@UiField SpanElement msgSpan;
	
	public ReportErrorWidget() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
