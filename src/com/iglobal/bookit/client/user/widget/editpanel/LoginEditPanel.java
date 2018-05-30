package com.iglobal.bookit.client.user.widget.editpanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.iglobal.bookit.client.GlobalResource;
import com.iglobal.bookit.shared.User;

public class LoginEditPanel extends Composite implements IsEditableUserWidget {

	private EditPanelEventHandler handler;
	private String title, value;
	private static LoginEditPanelUiBinder uiBinder = GWT
			.create(LoginEditPanelUiBinder.class);

	interface LoginEditPanelUiBinder extends UiBinder<Widget, LoginEditPanel> {
	}
	
	@UiField PasswordTextBox passwordFieldBox;
	@UiField TextBox fieldNameBox;
	@UiField SpanElement formTitle;
	@UiField LIElement passwordFieldrror, fieldNameError;
	@UiField SpanElement closeBtn, message;
	@UiField Anchor cancelLink;
	@UiField Button confirmBtn;
	
	public LoginEditPanel(String title, String value) {
		this.title = title;
		this.value = value;
		
		initWidget(uiBinder.createAndBindUi(this));
		initComponent();
		initEvent();
	}

	private void initComponent(){
		
		formTitle.setInnerText(title+" ("+value+")");
		fieldNameBox.getElement().setAttribute("placeholder", "Please enter username");
		passwordFieldBox.getElement().setAttribute("placeholder", "Enter password");
		cancelLink.setText("Cancel");
	}
	
	private void initEvent(){
		confirmBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(isEmailValid()){
					doMakeVerification();
				}else{
					showUsernameError(true);
				}
			}
		});
		
		cancelLink.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(handler != null){
					handler.onCancelClicked();
				}
			}
		});
		
		Element closeBtnElemtent = closeBtn.cast();
		DOM.sinkEvents(closeBtnElemtent, Event.ONCLICK);
		DOM.setEventListener(closeBtnElemtent, new EventListener() {
			
			@Override
			public void onBrowserEvent(Event event) {
				if(handler != null){
					handler.onCancelClicked();
				}
			}
		});
		
		fieldNameBox.addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				if(!isEmailValid()){
					showUsernameError(true);
				}else{
					showUsernameError(false);
				}
			}
		});
	}
	
	
	private boolean isEmailValid(){
		if(fieldNameBox.getText().trim().toLowerCase().matches("^([a-z0-9_\\.-]+)@([\\d\\p{L}\\a-z\\.-]+)\\.([a-z\\.]{2,6})$")){
			return true;
		}
		return false;
	}
	
	private void doMakeVerification(){
		GlobalResource.getInstance().getUserRPC().getUser(fieldNameBox.getText().trim().toLowerCase(), passwordFieldBox.getText().trim(), new AsyncCallback<User>() {
			
			@Override
			public void onSuccess(User result) {
				if(result != null){
					if(handler != null){
						handler.onSaveClicked(result.getName());
					}
				}else{
					//Show wrong detail input
					showRequestError();
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private void showUsernameError(boolean isShow){
		if(isShow){
			fieldNameError.removeClassName("gwt-hide");
		}else{
			fieldNameError.addClassName("gwt-hide");
		}
	}
	
	private void showRequestError(){
		message.removeClassName("gwt-hide");
	}

	@Override
	public void setEditPanelEventHandler(EditPanelEventHandler handler) {
		this.handler = handler;
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	
}
