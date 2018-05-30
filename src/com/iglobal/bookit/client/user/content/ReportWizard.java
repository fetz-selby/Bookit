package com.iglobal.bookit.client.user.content;

import com.google.gwt.user.client.ui.Widget;
import com.iglobal.bookit.client.user.misc.ReportStepsEnum;
import com.iglobal.bookit.client.user.misc.ReportWizardApp;
import com.iglobal.bookit.client.user.misc.ReportWizardObject;

public abstract class ReportWizard extends ReportWizardApp{
	
	public abstract ReportStepsEnum getStage();
	public abstract ReportWizardObject getWizardObject();
	public abstract Widget getWidget();
	public abstract ReportStepsEnum getNextWizard();
	public abstract ReportStepsEnum getPreviousWizard();
	
	public void next(){
		runAppNext(getNextWizard());
	}
	
	public void previous(){
		runAppPrevious(getPreviousWizard());
	}
}
