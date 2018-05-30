package com.iglobal.bookit.client.user.widget;

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

public class LoginWidget extends Composite{

	private LoginWidgetEventHandler handler;
	private static DynamicLoginWidgetUiBinder uiBinder = GWT
			.create(DynamicLoginWidgetUiBinder.class);

	interface DynamicLoginWidgetUiBinder extends
			UiBinder<Widget, LoginWidget> {
	}
	
	public interface LoginWidgetEventHandler{
		void onSuccessfulEntry(User user);
		void onCloseInvoked();
	}

	@UiField PasswordTextBox passwordFieldBox;
	@UiField TextBox fieldNameBox;
	@UiField SpanElement formTitle;
	@UiField LIElement passwordFieldrror, fieldNameError;
	@UiField SpanElement closeBtn, message;
	@UiField Anchor cancelLink;
	@UiField Button confirmBtn;
	
	public LoginWidget() {
		initWidget(uiBinder.createAndBindUi(this));
		initComponent();
		initEvent();
	}

	private void initComponent(){
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
					handler.onCloseInvoked();
				}
			}
		});
		
		Element closeBtnElemtent = closeBtn.cast();
		DOM.sinkEvents(closeBtnElemtent, Event.ONCLICK);
		DOM.setEventListener(closeBtnElemtent, new EventListener() {
			
			@Override
			public void onBrowserEvent(Event event) {
				if(handler != null){
					handler.onCloseInvoked();
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
						handler.onSuccessfulEntry(result);
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
	
	public void setLoginWidgetEventHandler(LoginWidgetEventHandler handler){
		this.handler = handler;
	}

}
