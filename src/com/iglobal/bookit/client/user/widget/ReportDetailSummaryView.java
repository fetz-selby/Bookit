package com.iglobal.bookit.client.user.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.iglobal.bookit.client.user.widget.object.ReportDetailSummaryViewObject;

public class ReportDetailSummaryView extends Composite {

	private ReportDetailSummaryViewObject obj;
	private static ReportDetailSummaryViewUiBinder uiBinder = GWT
			.create(ReportDetailSummaryViewUiBinder.class);

	interface ReportDetailSummaryViewUiBinder extends
			UiBinder<Widget, ReportDetailSummaryView> {
	}

	@UiField SpanElement createdBySpan, createdOnSpan, groupsSpan;
	
	public ReportDetailSummaryView(ReportDetailSummaryViewObject obj) {
		this.obj = obj;
		initWidget(uiBinder.createAndBindUi(this));
		initComponent();
	}

	private void initComponent(){
		if(obj != null){
			createdBySpan.setInnerText(obj.getCreatedBy());
			createdOnSpan.setInnerText(obj.getDate());
			groupsSpan.setInnerText(obj.getGroupNames());
		}
	}
	
}
