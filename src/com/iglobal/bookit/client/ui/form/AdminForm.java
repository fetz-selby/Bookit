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
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.iglobal.bookit.client.GlobalResource;
import com.iglobal.bookit.client.events.RPCCompleteHandler;
import com.iglobal.bookit.client.ui.components.PermissionWidget;
import com.iglobal.bookit.client.utils.ErrorIndicator;
import com.iglobal.bookit.client.utils.Utils;
import com.iglobal.bookit.shared.AdminRowObject;

public class AdminForm extends Composite {

	private String status = "A";
	private AdminFormEventHandler handler;
	private static AdminFormUiBinder uiBinder = GWT
			.create(AdminFormUiBinder.class);

	interface AdminFormUiBinder extends UiBinder<Widget, AdminForm> {
	}

	public interface AdminFormEventHandler{
		void onFormSuccess(AdminRowObject adminObject);
		void onFormError(AdminRowObject adminObject);
	}


	@UiField TextBox nameBox, emailBox;
	@UiField PasswordTextBox password2Box, passwordBox;
	@UiField Button submitBtn;
	@UiField RadioButton activeCheckBox, inActiveCheckBox;
	@UiField SpanElement formTitle;
	@UiField LIElement nameError, emailError, passwordError, groupError;
	//@UiField CustomSuggestBox groupSuggest;
	//@UiField SortableList groupSortableList;
	@UiField SpanElement closeBtn;
	@UiField SimplePanel permContainer;
	@UiField HTMLPanel saBox;

	private AdminRowObject adminObject;
	private boolean isAnUpdate = false;
	private PopupPanel popup;
	private PermissionWidget permWidget;
	private boolean isSuperAdmin = false;


	public AdminForm(AdminFormEventHandler handler, PopupPanel popup) {
		this.popup = popup;
		this.handler = handler;
		initWidget(uiBinder.createAndBindUi(this));
		initComponents();
		initEvent();
	}

	public AdminForm(AdminFormEventHandler handler, AdminRowObject adminObject, PopupPanel popup) {
		this.popup = popup;
		this.handler = handler;
		this.adminObject = adminObject;
		initWidget(uiBinder.createAndBindUi(this));
		initComponents();
		initEvent();
	}

	private void initComponents(){

		nameBox.getElement().setAttribute("placeholder", "Enter fullname");
		emailBox.getElement().setAttribute("placeholder", "Enter email address");
		passwordBox.getElement().setAttribute("placeholder", "*********");
		password2Box.getElement().setAttribute("placeholder", "");
		activeCheckBox.setText("Activate");
		inActiveCheckBox.setText("De-Activate");

		if(adminObject == null){
			formTitle.setInnerText("New Admin");
			adminObject = new AdminRowObject();
			activeCheckBox.setValue(true);
			isAnUpdate = false;
		}else{
			isAnUpdate = true;
			formTitle.setInnerText("Edit Admin");
			nameBox.setText(adminObject.getName());
			emailBox.setText(adminObject.getEmail());
			emailBox.setEnabled(false);
			passwordBox.setText(adminObject.getPassword());
			password2Box.setText(adminObject.getPassword());

			if(adminObject.getStatus().equals("A")){
				activeCheckBox.setValue(true);
				status = "A";
			}else{
				inActiveCheckBox.setValue(true);
				status = "D";
			}

		}

		doAdminPermsInit();

		if(GlobalResource.getInstance().getUser().isSuperUser()){
			doInitSuperAdminUI();
		}
	}
	
//	<label>Confirm password</label>
//	<g:PasswordTextBox styleName="form-control parsley-validated"
//		ui:field="password2Box" />
//	<ul id="parsley-1426225679460913" class="parsley-error-list"
//		style="display: block;">
//		<li class="required" style="display: none;">This value is required.</li>
//	</ul>
	
	private void doInitSuperAdminUI(){
		//Create label
		Label label = new Label("Admin Groups");
		
		final CheckBox saCheck = new CheckBox("Super Admin");
		//saCheck.setStyleName("form-control");
		saCheck.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(saCheck.getValue()){
					isSuperAdmin = true;
				}else{
					isSuperAdmin = false;
				}
			}
		});
		
		saBox.add(label);
		saBox.add(saCheck);
		
		if(isAnUpdate && adminObject.isSuperAdmin()){
			saCheck.setValue(true);
		}
		
		saBox.removeStyleName("gwt-hide");
	}

	private void doAdminPermsInit(){
		if(adminObject.getPerms() != null && !adminObject.getPerms().trim().isEmpty()){
			HashMap<String, String> permMap = new HashMap<String, String>();
			String[] permIds = adminObject.getPerms().split("[,]");
			for(String id : permIds){
				permMap.put(id, Utils.getPermName(id));
			}

			permWidget = new PermissionWidget(permMap);

			permContainer.setWidget(permWidget);
		}else{
			permWidget = new PermissionWidget();
			permContainer.setWidget(permWidget);
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
	}

	protected void doValidation() {
		//Validate name
		switch(Utils.isNameValid(nameBox.getText().trim())){
		case VALUE_SHORT:
			showError(nameBox, nameError, "Name is too short", true);
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
			doEmailValidation();
			break;
		default:
			break;
		}
	}

	private void doEmailValidation(){
		Utils.isEmailValid(emailBox.getText().trim(), new RPCCompleteHandler<ErrorIndicator>() {

			@Override
			public void onProccessComplete(ErrorIndicator t) {
				switch(t){
				case BAD_REQUEST:
					break;
				case VALUE_EXISTS:
					if(isAnUpdate){
						showError(emailBox, emailError, "", false);
						doPasswordValidation();
						GWT.log("passed update check");
						break;
					}
					showError(emailBox, emailError, "Email already exists", true);
					break;
				case VALUE_INVALID:
					showError(emailBox, emailError, "Email is invalid", true);
					break;
				case VALUE_ISEMPTY:
					showError(emailBox, emailError, "Email can not be empty", true);
					break;
				case VALUE_OK:
					showError(emailBox, emailError, "", false);
					doPasswordValidation();
					break;
				case VALUE_SHORT:
					break;
				default:
					break;

				}
			}
		});
	}

	private void doPasswordValidation(){
		switch(Utils.isPasswordValid(passwordBox.getText().trim())){
		case BAD_REQUEST:
			break;
		case VALUE_EXISTS:
			break;
		case VALUE_INVALID:
			break;
		case VALUE_ISEMPTY:
			showError(passwordBox, passwordError, "Password can not be empty", true);
			//Commit
			break;
		case VALUE_OK:
			showError(passwordBox, passwordError, "", false);
			doPasswordCompare();
			break;
		case VALUE_SHORT:
			showError(passwordBox, passwordError, "Password must be more than 4 characters", true);
			break;
		case VALUE_TOO_LONG:
			if(isAnUpdate){
				GWT.log("Too long, now comparing");
				doPasswordCompare();
				break;
			}else{
				showError(passwordBox, passwordError, "Password must be within 30 characters", true);
				break;
			}
		default:
			break;

		}
	}

	private void doPasswordCompare(){
		if(password2Box.getText().trim().equals(passwordBox.getText().trim())){
			//do group checking
			GWT.log("password check verified");
			doPermValidation();
		}else{
			showError(passwordBox, passwordError, "password do not match", true);
		}
	}

	private void doPermValidation(){
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
		if(adminObject == null){
			adminObject = new AdminRowObject();
		}
		adminObject.setCreatedBy(GlobalResource.getInstance().getUser().getId());
		adminObject.setEmail(emailBox.getText().trim());
		adminObject.setStatus(status);
		adminObject.setName(nameBox.getText().trim());
		//adminObject.setPerms(groupSortableList.getSelectedKeyString());
		adminObject.setPerms(permWidget.getCheckedString());
		adminObject.setPassword(passwordBox.getText().trim());
		
		if(isSuperAdmin){
			adminObject.setSuperAdmin(true);
		}

		if(isAnUpdate){
			GWT.log("Is an update");
			submitBtn.setText("Loading ...");
			GlobalResource.getInstance().getAdminUpdateRPC().isAdminsUpdated(adminObject, Integer.parseInt(GlobalResource.getInstance().getUser().getPersonId()), new AsyncCallback<Boolean>() {

				@Override
				public void onSuccess(Boolean result) {
					if(result){
						submitBtn.setEnabled(false);
						GWT.log("Update successfull");
						if(handler != null){
							handler.onFormSuccess(adminObject);
						}else{
							submitBtn.setEnabled(false);
							
							if(handler != null){
								handler.onFormError(adminObject);
							}
						}
					}
					
				}

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub

				}
			});
		}else{
			GWT.log("Is not an update");
			submitBtn.setText("Loading ...");
			GlobalResource.getInstance().getAdminNewRPC().isAdminsCreated(adminObject, Integer.parseInt(GlobalResource.getInstance().getUser().getPersonId()), new AsyncCallback<Boolean>() {

				@Override
				public void onSuccess(Boolean result) {
					if(result){
						submitBtn.setEnabled(false);
						if(handler != null){
							handler.onFormSuccess(adminObject);
						}
					}else{
						submitBtn.setEnabled(false);
						
						if(handler != null){
							handler.onFormError(adminObject);
						}
					}
					
				}

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub

				}
			});

		}
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
		}
	}

	public void setAdminFormEventHandler(AdminFormEventHandler handler){
		this.handler = handler;
	}

}
