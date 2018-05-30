package com.iglobal.bookit.client.login.view;


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyPressHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.iglobal.bookit.client.login.presenter.LoginPresenter;


public class LoginView extends Composite implements LoginPresenter.Display{

	private static LoginViewUiBinder uiBinder = GWT
			.create(LoginViewUiBinder.class);

	interface LoginViewUiBinder extends UiBinder<Widget, LoginView> {
	}
	
	@UiField TextBox usernameBox;
	@UiField PasswordTextBox passwordBox;
	@UiField Anchor signInBtn;
	
	public LoginView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return usernameBox.getText().trim();
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return passwordBox.getText().trim();
	}

	@Override
	public HasClickHandlers getSigninEvent() {
		// TODO Auto-generated method stub
		return signInBtn;
	}

	@Override
	public HasClickHandlers getForgotPasswordEvent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HasKeyPressHandlers getUsernameKeyPressHandlers() {
		return usernameBox;
	}

	@Override
	public HasKeyPressHandlers getPasswordKeyPressHandlers() {
		return passwordBox;
	}

	@Override
	public void showLoginError(boolean isShow) {
		if(isShow){
			usernameBox.getElement().setAttribute("style", "border-color: #EF002E");
			passwordBox.getElement().setAttribute("style", "border-color: #EF002E");
		}else{
			usernameBox.getElement().setAttribute("style", "");
			passwordBox.getElement().setAttribute("style", "");

		}
	}

	@Override
	public void disableSignInButton(boolean isDisabled) {
		if(isDisabled){
			signInBtn.setEnabled(false);
		}else{
			signInBtn.setEnabled(true);
		}
	}

}
