package com.iglobal.bookit.client.ui.form;

import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.iglobal.bookit.client.GlobalResource;
import com.iglobal.bookit.client.ui.components.PermissionWidget;
import com.iglobal.bookit.client.utils.Utils;
import com.iglobal.bookit.shared.GroupRowObject;
import com.iglobal.bookit.shared.VerifierObject;

public class GroupForm extends Composite {

	private String status = "A";
	private GroupFormEventHandler handler;
	private static GroupFormUiBinder uiBinder = GWT
			.create(GroupFormUiBinder.class);

	interface GroupFormUiBinder extends UiBinder<Widget, GroupForm> {
	}

	public interface GroupFormEventHandler{
		void onFormSuccess(GroupRowObject groupObject);
		void onFormError(GroupRowObject groupObject);
	}
	
	@UiField SpanElement formTitle;
	@UiField TextBox nameBox;
	//@UiField CustomSuggestBox permsSuggest;
	//@UiField SortableList permsSortableList;
	@UiField RadioButton activeCheckBox, inActiveCheckBox;
	@UiField Button submitBtn;
	@UiField LIElement nameError, groupError;
	@UiField SpanElement closeBtn;
	@UiField SimplePanel permContainer;
	@UiField CheckBox notiCheckBox;

	private GroupRowObject groupObject;
	private boolean isAnUpdate = false;
	private PopupPanel popup;
	private PermissionWidget permWidget;

	
	
	public GroupForm(GroupFormEventHandler handler, PopupPanel popup) {
		this.popup = popup;
		this.handler = handler;
		initWidget(uiBinder.createAndBindUi(this));
		initComponents();
		initEvent();
	}
	
	public GroupForm(GroupFormEventHandler handler, GroupRowObject groupObject, PopupPanel popup) {
		this.popup = popup;
		this.handler = handler;
		this.groupObject = groupObject;
		initWidget(uiBinder.createAndBindUi(this));
		initComponents();
		initEvent();
	}
	
	private void initComponents(){

		nameBox.getElement().setAttribute("placeholder", "Enter fullname");
		activeCheckBox.setText("Activate");
		inActiveCheckBox.setText("De-Activate");
		notiCheckBox.setText("Alert");

		if(groupObject == null){
			formTitle.setInnerText("New Group");
			groupObject = new GroupRowObject();
			activeCheckBox.setValue(true);
			isAnUpdate = false;
		}else{
			isAnUpdate = true;
			formTitle.setInnerText("Edit Group");
			nameBox.setText(groupObject.getName().toLowerCase());
			
			if(groupObject.isNotification()){
				notiCheckBox.setValue(true);
			}
			
			if(groupObject.getStatus().equals("A")){
				activeCheckBox.setValue(true);
				status = "A";
			}else{
				inActiveCheckBox.setValue(true);
				status = "D";
			}
			
			doGroupPermsInit();
		}

		doGroupPermsInit();

	}
	
	private void doGroupPermsInit(){
		if(groupObject.getPerms() != null && !groupObject.getPerms().trim().isEmpty()){
			HashMap<String, String> permMap = new HashMap<String, String>();
			String[] permIds = groupObject.getPerms().split("[,]");
			for(String id : permIds){
				permMap.put(id, Utils.getPermName(id));
			}

			permWidget = new PermissionWidget(permMap);

			permContainer.setWidget(permWidget);
		}else{
			permWidget = new PermissionWidget();
			permContainer.setWidget(permWidget);
		}
		
		if(groupObject.isNotification()){
			permWidget.alertChecked();
		}
	}
	
	private void doButtonDisable(boolean isDisable){
		if(isDisable){
			submitBtn.setText("Loading ...");
			submitBtn.setEnabled(false);
		}else{
			submitBtn.setEnabled(true);
			submitBtn.setText("Submit");
		}
	}

	private void initEvent(){
		submitBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				doButtonDisable(true);
				doValidation();
			}
		});
		
		inActiveCheckBox.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(inActiveCheckBox.getValue()){
					activeCheckBox.setValue(false);
					status = "D";
				}else{
					activeCheckBox.setValue(true);
					status = "A";
				}
			}
		});
		
		activeCheckBox.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(activeCheckBox.getValue()){
					inActiveCheckBox.setValue(false);
					status = "A";
				}else{
					inActiveCheckBox.setValue(true);
					status = "D";
				}
			}
		});
		
		Element closeElement = closeBtn.cast();
		DOM.sinkEvents(closeElement, Event.ONCLICK);
		DOM.setEventListener(closeElement, new EventListener() {
			
			@Override
			public void onBrowserEvent(Event event) {
				if(popup != null){
					popup.hide();
				}
			}
		});
		
		notiCheckBox.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(notiCheckBox.getValue()){
					permWidget.alertChecked();
				}else{
					permWidget.alertUnChecked();
				}
			}
		});
	}
	
	protected void doValidation() {
		//Validate name
		switch(Utils.isNameValid(nameBox.getText().trim())){
		case VALUE_SHORT:
			showError(nameBox, nameError, "Group name is too short", true);
			break;
		case BAD_REQUEST:
			break;
		case VALUE_EXISTS:
			showError(nameBox, nameError, "",  true);
			break;
		case VALUE_INVALID:
			showError(nameBox, nameError, "Invalid name format", true);
			break;
		case VALUE_ISEMPTY:
			showError(nameBox, nameError, "Name can not be empty", true);
			break;
		case VALUE_OK:
			showError(nameBox, nameError, "", false);
			doGroupValidation();
			break;
		default:
			break;
		}
	}
	
	private void doIsNameExistValidation(final boolean isUpdate){
		GlobalResource.getInstance().getAdminRPC().getIfGroupNameExist(nameBox.getText(), new AsyncCallback<VerifierObject>() {
			
			@Override
			public void onSuccess(VerifierObject result) {
				if(result != null){
					if(isUpdate && groupObject.getId().equals(result.getId())){
						showError(nameBox, nameError, "", false);
						doUpdate();
					}else if(isUpdate && !groupObject.getId().equals(result.getId())){
						showError(nameBox, nameError, "Sorry, group name already exists.", true);
					}else if(!isUpdate && result.isExist()){
						showError(nameBox, nameError, "Sorry, group name already exists.", true);
					}else if(result.isExist()){
						showError(nameBox, nameError, "Sorry, group name already exists.", true);
					}
				}else{
					if(isUpdate){
						showError(nameBox, nameError, "", false);
						doUpdate();
					}else{
						showError(nameBox, nameError, "", false);
						doNewGroupCreation();
					}
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private void doUpdate(){
		submitBtn.setText("Loading ...");
		GlobalResource.getInstance().getAdminUpdateRPC().isGroupsUpdated(groupObject, Integer.parseInt(GlobalResource.getInstance().getUser().getPersonId()), new AsyncCallback<Boolean>() {
			
			@Override
			public void onSuccess(Boolean result) {
				if(result){
					if(handler != null){
						handler.onFormSuccess(groupObject);
					}
				}else{
					if(handler != null){
						handler.onFormError(groupObject);
					}
				}
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private void doNewGroupCreation(){
		submitBtn.setText("Loading ...");
		GlobalResource.getInstance().getAdminNewRPC().isGroupsCreated(groupObject, Integer.parseInt(GlobalResource.getInstance().getUser().getPersonId()), new AsyncCallback<Boolean>() {

			@Override
			public void onSuccess(Boolean result) {
				if(result){
					submitBtn.setEnabled(false);
					if(handler != null){
						handler.onFormSuccess(groupObject);
					}
				}else{
					submitBtn.setEnabled(false);
					
					if(handler != null){
						handler.onFormError(groupObject);
					}
				}
				
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}
		});
	}
	
	private void doGroupValidation(){
		if(permWidget.getCheckedString().isEmpty()){
			//show error
			showError(permContainer, groupError, "select at least one permission", true);
			return;
		}else{
			//Proceed to save
			showError(permContainer, groupError, "", false);
			doSave();
		}
	}
	
	private void doSave(){
		if(groupObject == null){
			groupObject = new GroupRowObject();
		}
		groupObject.setCreatedBy(GlobalResource.getInstance().getUser().getId());
		groupObject.setStatus(status);
		groupObject.setName(nameBox.getText().trim().toLowerCase());
		//groupObject.setPerms(permsSortableList.getSelectedKeyString());
		groupObject.setPerms(permWidget.getCheckedString());
		
		groupObject.setNotification(notiCheckBox.getValue());


		GWT.log("name "+groupObject.getName()+", group "+groupObject.getPerms()+", created by "+groupObject.getCreatedBy());

		doIsNameExistValidation(isAnUpdate);
	}
	
	private void showError(Widget widget, LIElement label, String errorLabel, boolean isShowError){

		final String ERROR_CLASS = "parsley-error";

		label.setInnerText(errorLabel);
		if(isShowError){
			widget.getElement().addClassName(ERROR_CLASS);
			label.setAttribute("style", "display: block; color: #ff5f5f");
			doButtonDisable(false);
		}else{
			widget.getElement().removeClassName(ERROR_CLASS);
			label.setAttribute("style", "display: none;");
			//doButtonDisable(true);
		}
	}
	
}
