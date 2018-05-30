package com.iglobal.bookit.client.user.misc;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.SimplePanel;
import com.iglobal.bookit.client.user.UserReportGlobalUI;
import com.iglobal.bookit.client.user.content.ReportContent;
import com.iglobal.bookit.client.user.content.ReportContent.ReportContentEventHandler;
import com.iglobal.bookit.client.user.content.ReportWizard;
import com.iglobal.bookit.client.user.report.wizard.ReportFilterStage;
import com.iglobal.bookit.client.user.report.wizard.ReportOverviewStage;

public class ReportWizardApp extends SimplePanel{

	public ReportWizardApp(){
	}

	public void runAppNext(ReportStepsEnum stage){

		ReportWizard wizardStage = null;

		switch(stage){
		case BOOK_STAGE:
			UserReportGlobalUI.getInstance().setH3Text("");
			wizardStage = new ReportContent(UserReportGlobalUI.getInstance().getBookId(), UserReportGlobalUI.getInstance().getBookName(), null, null);
			((ReportContent)wizardStage).setReportContentEventHandler(new ReportContentEventHandler() {

				@Override
				public void onBookLoadComplete(ReportWizardObject localWizardObject) {
					UserReportGlobalUI.getInstance().setReportWizardObject(localWizardObject);
				}
			});
			break;
		case FIELDS_STAGE:
			UserReportGlobalUI.getInstance().setH3Text("");
			if(UserReportGlobalUI.getInstance().getWizardObject() == null){
				Window.alert("NULL wizard object");
			}
			wizardStage = new ReportFilterStage(UserReportGlobalUI.getInstance().getWizardObject());

			break;
		case OVERVIEW_STAGE:
			UserReportGlobalUI.getInstance().setH3Text("");
			wizardStage = new ReportOverviewStage(UserReportGlobalUI.getInstance().getWizardObject());

			break;
		default:
			Window.alert("Default loaded!");
			wizardStage = new ReportContent(UserReportGlobalUI.getInstance().getBookId(), UserReportGlobalUI.getInstance().getBookName(), null, null);
			((ReportContent)wizardStage).setReportContentEventHandler(new ReportContentEventHandler() {

				@Override
				public void onBookLoadComplete(ReportWizardObject localWizardObject) {
					UserReportGlobalUI.getInstance().setReportWizardObject(localWizardObject);
				}
			});
			break;
		}

		if(wizardStage != null){
			setWidget(wizardStage);
		}
	}
	
	public void runAppPrevious(ReportStepsEnum stage){
		ReportWizard wizardStage = null;

		switch(stage){
		case BOOK_STAGE:
			UserReportGlobalUI.getInstance().setH3Text("");
			wizardStage = new ReportContent(UserReportGlobalUI.getInstance().getBookId(), UserReportGlobalUI.getInstance().getBookName(), null, null);
			((ReportContent)wizardStage).setReportContentEventHandler(new ReportContentEventHandler() {

				@Override
				public void onBookLoadComplete(ReportWizardObject localWizardObject) {
					UserReportGlobalUI.getInstance().setReportWizardObject(localWizardObject);
				}
			});
			break;
		case FIELDS_STAGE:
			UserReportGlobalUI.getInstance().setH3Text("");
			if(UserReportGlobalUI.getInstance().getWizardObject() == null){
				Window.alert("NULL wizard object");
			}
			wizardStage = new ReportFilterStage(UserReportGlobalUI.getInstance().getWizardObject(), UserReportGlobalUI.getInstance().getWizardObject().getSelectedFields());

			break;
		case OVERVIEW_STAGE:
			UserReportGlobalUI.getInstance().setH3Text("");
			wizardStage = new ReportOverviewStage(UserReportGlobalUI.getInstance().getWizardObject());

			break;
		default:
			Window.alert("Default loaded!");
			wizardStage = new ReportContent(UserReportGlobalUI.getInstance().getBookId(), UserReportGlobalUI.getInstance().getBookName(), null, null);
			((ReportContent)wizardStage).setReportContentEventHandler(new ReportContentEventHandler() {

				@Override
				public void onBookLoadComplete(ReportWizardObject localWizardObject) {
					UserReportGlobalUI.getInstance().setReportWizardObject(localWizardObject);
				}
			});
			break;
		}

		if(wizardStage != null){
			setWidget(wizardStage);
		}
	}
}
