package com.iglobal.bookit.client.user.report.wizard;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Widget;
import com.iglobal.bookit.client.GlobalResource;
import com.iglobal.bookit.client.user.UserReportGlobalUI;
import com.iglobal.bookit.client.user.content.ReportWizard;
import com.iglobal.bookit.client.user.misc.ReportStepsEnum;
import com.iglobal.bookit.client.user.misc.ReportWizardObject;
import com.iglobal.bookit.client.utils.FormSaveEventHandler;
import com.iglobal.bookit.client.utils.Utils;

public class ReportOverviewStage extends ReportWizard {

	private Widget wizard;
	private ReportWizardObject wizardObject;
	private static ReportOverviewStageUiBinder uiBinder = GWT
			.create(ReportOverviewStageUiBinder.class);

	interface ReportOverviewStageUiBinder extends
			UiBinder<Widget, ReportOverviewStage> {
	}

	@UiField Anchor downloadBtn, prevBtn;
	@UiField SpanElement bookNameSpan, groupsSpan, createdbySpan, createdDateSpan, requestedFieldsSpan; 
	
	public ReportOverviewStage(ReportWizardObject wizardObject) {
		this.wizardObject = wizardObject;
		setWidget(uiBinder.createAndBindUi(this));
		initComponent();
		initEvent();
	}
	
	private void initComponent(){
		downloadBtn.setText("Download");
		prevBtn.setText("Back");
		
		bookNameSpan.setInnerText(wizardObject.getBookAlias());
		groupsSpan.setInnerText(wizardObject.getGroups());
		createdbySpan.setInnerText(wizardObject.getCreatedBy());
		createdDateSpan.setInnerText(wizardObject.getDateCreated());
		requestedFieldsSpan.setInnerText(wizardObject.getSelectedFields());
	}
	
	private void initEvent(){
		downloadBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				//Show loading ....
				GlobalResource.getInstance().getUserRPC().getGeneratedReportHandler(UserReportGlobalUI.getInstance().getQueryObject(), new AsyncCallback<String>() {
					
					@Override
					public void onSuccess(String result) {
						if(result != null && result.length() > 1){
							FileDownloader file = new FileDownloader(result);
							file.download();
						}
					}
					
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}
				});
			}
		});
		
		prevBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				previous();
			}
		});
	}

	@Override
	public ReportStepsEnum getStage() {
		return ReportStepsEnum.OVERVIEW_STAGE;
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
		return null;
	}

	@Override
	public ReportStepsEnum getPreviousWizard() {
		return ReportStepsEnum.FIELDS_STAGE;
	}
	
	public class FileDownloader{
		private String handler;
		public FileDownloader(String handler){
			this.handler = handler;
		}
		
		public void download(){
			doProcessing();
		}
		
		private void doProcessing(){
			
			Utils.setFeed(new Hidden("filename", handler), "downloadreport", false, new FormSaveEventHandler() {
				
				@Override
				public void onSuccessfulSave(SubmitCompleteEvent event) {
				}
				
				@Override
				public void onFailSave() {
					// TODO Auto-generated method stub
					
				}
			});
		}
	}

}
