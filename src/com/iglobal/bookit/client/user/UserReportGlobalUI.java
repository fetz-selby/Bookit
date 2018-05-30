package com.iglobal.bookit.client.user;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;
import com.iglobal.bookit.client.user.misc.ReportWizardObject;
import com.iglobal.bookit.shared.ReportGeneratorQueryObject;

public class UserReportGlobalUI {
	
	private RootPanel panel, auxPanel;
	private String bookId, bookName;
	private ReportWizardObject wizardObject;
	private Element h3Title;
	private ReportGeneratorQueryObject queryObject;
	private static UserReportGlobalUI instance = new UserReportGlobalUI();
	
	private UserReportGlobalUI(){}
	
	public static UserReportGlobalUI getInstance(){
		return instance;
	}
	
	public void setRootPanel(RootPanel panel){
		this.panel = panel;
	}
	
	public RootPanel getMainPanel(){
		return panel;
	}
	
	public void setReportWizardObject(ReportWizardObject wizardObject){
		this.wizardObject = wizardObject;
	}
	
	public ReportWizardObject getWizardObject(){
		return wizardObject;
	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public void setAuxPanel(RootPanel auxAreaPanel) {
		this.auxPanel = auxAreaPanel;
	}
	
	public RootPanel getAuxPanel(){
		return auxPanel;
	}

	public void setH3TitleElement(Element h3Title) {
		this.h3Title = h3Title;
	}
	
	public void setH3Text(String text){
		h3Title.setInnerText(text);
	}

	public ReportGeneratorQueryObject getQueryObject() {
		return queryObject;
	}

	public void setQueryObject(ReportGeneratorQueryObject queryObject) {
		this.queryObject = queryObject;
	}
	
}
