package com.iglobal.bookit.client.user.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.iglobal.bookit.client.user.widget.object.ReportDetailSummaryViewObject;

public class ReportBookDetail extends Composite {

	private ReportDetailSummaryViewObject summaryObj;
	private ScrollableFlowPanel scroller;
	private static ReportBookDetailUiBinder uiBinder = GWT
			.create(ReportBookDetailUiBinder.class);

	interface ReportBookDetailUiBinder extends
	UiBinder<Widget, ReportBookDetail> {
	}

	@UiField SpanElement bookNameSpan;
	@UiField SimplePanel detailPanel;
	//private String bookId;
	private String bookName;

	public ReportBookDetail(String bookId, String bookName, ReportDetailSummaryViewObject summaryObj) {
		this.summaryObj = summaryObj;
		//this.bookId = bookId;
		this.bookName = bookName;
		initWidget(uiBinder.createAndBindUi(this));
		initComponent();
	}

	private void initComponent(){
		scroller = new ScrollableFlowPanel("");
		detailPanel.setWidget(scroller);
		
		if(summaryObj != null){
			scroller.add(new ReportDetailSummaryView(summaryObj));
		}
		
		bookNameSpan.setInnerText(bookName);
	}

}
