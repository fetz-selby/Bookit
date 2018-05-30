package com.iglobal.bookit.client.ui.components;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.iglobal.bookit.client.GlobalResource;
import com.iglobal.bookit.client.constants.AppConstants;
import com.iglobal.bookit.shared.PermsRowObject;

public class PermissionWidget extends Composite {

	private ArrayList<CheckBox> checkBoxList;
	private HashMap<String, CheckBox> checkBoxHash;
	private static PermissionWidgetUiBinder uiBinder = GWT
			.create(PermissionWidgetUiBinder.class);

	interface PermissionWidgetUiBinder extends
	UiBinder<Widget, PermissionWidget> {
	}

	@UiField HTMLPanel mainPanel;

	public PermissionWidget() {
		initWidget(uiBinder.createAndBindUi(this));
		initComponent(null);
	}

	public PermissionWidget(HashMap<String, String> permissionMap){
		initWidget(uiBinder.createAndBindUi(this));
		initComponent(permissionMap);
	}

	private void initComponent(HashMap<String, String> permissionMap){

		checkBoxList = new ArrayList<CheckBox>();
		checkBoxHash = new HashMap<String, CheckBox>();
		for(PermsRowObject obj : GlobalResource.getInstance().getPermsPrimaryList()){
			CheckBox check = new CheckBox(obj.getName());
			check.setFormValue(obj.getId());

			if(permissionMap != null && permissionMap.containsKey(obj.getId())){
				check.setValue(true);
			}
			
			if(obj.getName().toLowerCase().equals(AppConstants.READ)){
				check.setValue(true);
				check.setEnabled(false);
				checkBoxHash.put(AppConstants.READ, check);
			}else if(obj.getName().toLowerCase().equals(AppConstants.WRITE)){
				checkBoxHash.put(AppConstants.WRITE, check);
			}else if(obj.getName().toLowerCase().equals(AppConstants.UPDATE)){
				checkBoxHash.put(AppConstants.UPDATE, check);
			}

			checkBoxList.add(check);
			mainPanel.add(check);
		}

	}
	
	public String getCheckedString(){
		String permIds = "";
		final String COMMA = ",";
		for(CheckBox checkBox : checkBoxList){
			if(checkBox.getValue()){
				permIds += checkBox.getFormValue()+COMMA;
			}
		}
		
		if(!permIds.trim().isEmpty()){
			return permIds.substring(0, permIds.lastIndexOf(COMMA));
		}
		
		return permIds;
	}
	
	public void alertChecked(){
		//Disable and uncheck write and update
		if(checkBoxHash != null){
			checkBoxHash.get(AppConstants.WRITE).setValue(false);
			checkBoxHash.get(AppConstants.UPDATE).setValue(false);
			
			checkBoxHash.get(AppConstants.WRITE).setEnabled(false);
			checkBoxHash.get(AppConstants.UPDATE).setEnabled(false);
		}
	}
	
	public void alertUnChecked(){
		if(checkBoxHash != null){
			checkBoxHash.get(AppConstants.WRITE).setEnabled(true);
			checkBoxHash.get(AppConstants.UPDATE).setEnabled(true);
		}
	}

}
