package com.iglobal.bookit.client.ui.form;

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
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;
import com.iglobal.bookit.client.GlobalResource;
import com.iglobal.bookit.client.constants.EntityEnum;
import com.iglobal.bookit.client.events.RPCCompleteHandler;
import com.iglobal.bookit.client.ui.components.CustomSuggestBox;
import com.iglobal.bookit.client.ui.components.CustomSuggestBox.CustomSuggestBoxEventHandler;
import com.iglobal.bookit.client.ui.components.SortableList;
import com.iglobal.bookit.client.ui.components.SortableList.SortableListEventHandler;
import com.iglobal.bookit.client.ui.components.object.SortableObject;
import com.iglobal.bookit.client.user.widget.ScrollableFlowPanel;
import com.iglobal.bookit.client.utils.ErrorIndicator;
import com.iglobal.bookit.client.utils.Utils;
import com.iglobal.bookit.shared.UserRowObject;

public class UserForm extends Composite {
	private String status = "A";
	private UserFormEventHandler handler;
	private static UserFormUiBinder uiBinder = GWT
			.create(UserFormUiBinder.class);

	public interface UserFormEventHandler{
		void onFormSuccess(UserRowObject userObject);
		void onFormError(UserRowObject userObject);
	}

	interface UserFormUiBinder extends UiBinder<Widget, UserForm> {
	}

	@UiField TextBox nameBox, emailBox;
	@UiField PasswordTextBox password2Box, passwordBox;
	@UiField Button submitBtn;
	@UiField RadioButton activeCheckBox, inActiveCheckBox;
	@UiField SpanElement formTitle;
	@UiField LIElement nameError, emailError, passwordError, groupError;
	@UiField CustomSuggestBox groupSuggest;
	@UiField SpanElement closeBtn;
	@UiField(provided = true) ScrollableFlowPanel groupsScrollPanel;
	
	private UserRowObject userObject;
	private boolean isAnUpdate = false;
	private PopupPanel popup;
	private SortableList groupSortableList = new SortableList();
	
	public UserForm(UserFormEventHandler handler, PopupPanel popup) {
		this.popup = popup;
		this.handler = handler;
		groupsScrollPanel = new ScrollableFlowPanel("book-groups-style");

		initWidget(uiBinder.createAndBindUi(this));
		initComponents();
		initEvent();
	}

	public UserForm(UserFormEventHandler handler, UserRowObject userObject, PopupPanel popup) {
		this.popup = popup;
		this.handler = handler;
		this.userObject = userObject;
		groupsScrollPanel = new ScrollableFlowPanel("book-groups-style");

		initWidget(uiBinder.createAndBindUi(this));
		initComponents();
		initEvent();
	}

	private void initComponents(){
		groupsScrollPanel.add(groupSortableList);
		nameBox.getElement().setAttribute("placeholder", "Enter fullname");
		emailBox.getElement().setAttribute("placeholder", "Enter email address");
		passwordBox.getElement().setAttribute("placeholder", "*********");
		password2Box.getElement().setAttribute("placeholder", "");
		activeCheckBox.setText("Active");
		inActiveCheckBox.setText("De-Activate");

		if(userObject == null){
			formTitle.setInnerText("New User");
			userObject = new UserRowObject();
			activeCheckBox.setValue(true);
			isAnUpdate = false;
		}else{
			isAnUpdate = true;
			formTitle.setInnerText("Edit User");
			nameBox.setText(userObject.getName());
			emailBox.setText(userObject.getEmail());
			emailBox.setEnabled(false);
			passwordBox.setText(userObject.getPassword());
			password2Box.setText(userObject.getPassword());
			
			if(userObject.getStatus().equals("A")){
				activeCheckBox.setValue(true);
				status = "A";
			}else{
				inActiveCheckBox.setValue(true);
				status = "D";
			}
			
			doUserGroupInit();
		}

		groupSuggest.setLabel("Group(s)");
		groupSuggest.load(EntityEnum.GROUPS);

		initSuggestBoxEventHandler();
		initSortableListEvent();
	}
	
	private void doUserGroupInit(){
		if(userObject.getGroups().trim().length() > 1){
			for(String groupId : userObject.getGroups().trim().split(",")){
				groupSortableList.add(new SortableObject(Utils.getGroupName(groupId), groupId));
				//groupSortableList.add(new SortableObject(Utils.getGroupNameWithPerm(groupId), groupId));

			}
		}else if(userObject.getGroups().trim().length() == 1){
			groupSortableList.add(new SortableObject(Utils.getGroupName(userObject.getGroups()), userObject.getGroups()));
			//groupSortableList.add(new SortableObject(Utils.getGroupNameWithPerm(userObject.getGroups()), userObject.getGroups()));

		}
	}

	private void initSortableListEvent(){
		groupSortableList.setSortableListEventHandler(new SortableListEventHandler() {

			@Override
			public void onSortableRowEdit(SortableObject object) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSortableRowCanceled(SortableObject object) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void initSuggestBoxEventHandler(){
		groupSuggest.setGroupSuggestBoxEventHandler(new CustomSuggestBoxEventHandler() {

			@Override
			public void onSuggestSelected(String suggestWord, String value, TextBoxBase base) {
				groupSortableList.add(new SortableObject(suggestWord, value));
				base.setText("");
			}
		});
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
						GWT.log("Email is Update");
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
			showError(passwordBox, passwordError, "Password must be more than 6 characters", true);
			break;
		case VALUE_TOO_LONG:
			if(isAnUpdate){
				doPasswordCompare();
			}else{
				showError(passwordBox, passwordError, "Password should not be more than 30 characters long", true);
			}
		default:
			break;

		}
	}

	private void doPasswordCompare(){
		if(password2Box.getText().trim().equals(passwordBox.getText().trim())){
			//do group checking
			doGroupValidation();
			GWT.log("Comparing done");
		}else{
			showError(passwordBox, passwordError, "password do not match", true);
		}
	}

	private void doGroupValidation(){
		int size = groupSortableList.getSelectedSortableHashMap().size();
		if(size <= 0){
			//show error
			showError(groupSuggest, groupError, "select at least one group", true);
			return;
		}else{
			//Proceed to save
			showError(groupSuggest, groupError, "", false);
			doSave();
		}
	}

	private void doSave(){
		if(userObject == null){
			userObject = new UserRowObject();
		}
		userObject.setCreatedBy(GlobalResource.getInstance().getUser().getId());
		userObject.setEmail(emailBox.getText().trim());
		userObject.setStatus(status);
		userObject.setName(nameBox.getText().trim());
		userObject.setGroups(groupSortableList.getSelectedKeyString());
		userObject.setPassword(passwordBox.getText().trim());

		GWT.log("name "+userObject.getName()+", email "+userObject.getEmail()+", status "+userObject.getStatus()+", group "+userObject.getGroups()+", created by "+userObject.getCreatedBy()+", password "+userObject.getPassword());

		if(isAnUpdate){
			GWT.log("Person id is => "+GlobalResource.getInstance().getUser().getPersonId());
			submitBtn.setText("Loading ...");
			GlobalResource.getInstance().getAdminUpdateRPC().isUsersUpdated(userObject, Integer.parseInt(GlobalResource.getInstance().getUser().getPersonId()), new AsyncCallback<Boolean>() {
				
				@Override
				public void onSuccess(Boolean result) {
					if(result){
						submitBtn.setEnabled(false);
						if(handler != null){
							handler.onFormSuccess(userObject);
						}
					}else{
						submitBtn.setEnabled(false);

						if(handler != null){
							handler.onFormError(userObject);
						}
					}
					
					submitBtn.setEnabled(false);
				}
				
				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}
			});
		}else{
			submitBtn.setText("Loading ...");
			GlobalResource.getInstance().getAdminNewRPC().isUsersCreated(userObject, Integer.parseInt(GlobalResource.getInstance().getUser().getPersonId()), new AsyncCallback<Boolean>() {

				@Override
				public void onSuccess(Boolean result) {
					if(result){
						submitBtn.setEnabled(false);
						if(handler != null){
							handler.onFormSuccess(userObject);
						}
					}else{
						submitBtn.setEnabled(false);

						if(handler != null){
							handler.onFormError(userObject);
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

	public void setUserFormEventHandler(UserFormEventHandler handler){
		this.handler = handler;
	}
}
