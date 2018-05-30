package com.iglobal.bookit.client.user.widget.object;

public class ReportDetailSummaryViewObject {
	private String groupNames, createdBy, date;
	
	public ReportDetailSummaryViewObject(String createdBy, String groupNames, String date){
		this.createdBy = createdBy;
		this.groupNames = groupNames;
		this.date = date;
	}

	public String getGroupNames() {
		return groupNames;
	}

	public void setGroupNames(String groupNames) {
		this.groupNames = groupNames;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
}
