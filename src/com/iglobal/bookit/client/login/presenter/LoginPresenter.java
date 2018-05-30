package com.iglobal.bookit.client.login.presenter;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyPressHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.iglobal.bookit.client.LoginServiceAsync;
import com.iglobal.bookit.client.events.PageSwitchEvent;
import com.iglobal.bookit.client.events.PageSwitchEvent.AppType;
import com.iglobal.bookit.client.parents.Presenter;
import com.iglobal.bookit.client.ui.components.CustomWarningPanel;
import com.iglobal.bookit.client.utils.CookieVerifier;
import com.iglobal.bookit.shared.Admin;
import com.iglobal.bookit.shared.IsUser;
import com.iglobal.bookit.shared.LoggedInUser;
import com.iglobal.bookit.shared.MaximumUserLimit;
import com.iglobal.bookit.shared.QueryEnum;
import com.iglobal.bookit.shared.QueryObject;
import com.iglobal.bookit.shared.QueryOperatorEnum;
import com.iglobal.bookit.shared.User;


public class LoginPresenter implements Presenter{

	private Display view;
	private LoginServiceAsync rpc;
	private HandlerManager eventBus;

	public interface Display extends IsWidget{
		Widget asWidget();
		String getUsername();
		String getPassword();
		HasClickHandlers getSigninEvent();
		HasClickHandlers getForgotPasswordEvent();
		HasKeyPressHandlers getUsernameKeyPressHandlers();
		HasKeyPressHandlers getPasswordKeyPressHandlers();
		void disableSignInButton(boolean isDisabled);
		
		void showLoginError(boolean isShow);
	}

	public LoginPresenter(LoginServiceAsync rpc, Display view, HandlerManager eventBus){
		this.view = view;
		this.rpc = rpc;
		this.eventBus = eventBus;
	}

	@Override
	public void bind() {
		view.getSigninEvent().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				//doActiveSessionCheck();
				doSignIn();

			}
		});

		view.getUsernameKeyPressHandlers().addKeyPressHandler(new KeyPressHandler() {

			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER && !(view.getPassword().trim().isEmpty())){
					//doActiveSessionCheck();
					view.disableSignInButton(true);
					doSignIn();

				}
			}
		});

		view.getPasswordKeyPressHandlers().addKeyPressHandler(new KeyPressHandler() {

			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER && !(view.getUsername().trim().isEmpty())){
					//doActiveSessionCheck();
					view.disableSignInButton(true);
					doSignIn();

				}
			}
		});
	}
	
//	private void doActiveSessionCheck(){
//		GlobalResource.getInstance().getAdminRPC().getSessionId(view.getUsername(), new AsyncCallback<Integer>() {
//			
//			@Override
//			public void onSuccess(Integer result) {
//				if(result > 0){
//					CookieVerifier.getInstance().setAppSessionId(result+"");
//					doSignIn();
//				}else{
//					Window.alert("Your session is already active, please logout and try again. If problem still persist, see Administrator for help.");
//				}
//			}
//			
//			@Override
//			public void onFailure(Throwable caught) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
//	}

	private void doSignIn(){
		ArrayList<QueryObject> queryList = new ArrayList<QueryObject>();
		queryList.add(new QueryObject(QueryEnum.PER_ID, null, true, QueryOperatorEnum.EQUALS));
		queryList.add(new QueryObject(QueryEnum.PER_TYPE, null, true, QueryOperatorEnum.EQUALS));
		queryList.add(new QueryObject(QueryEnum.PER_STATUS, "A", true, QueryOperatorEnum.EQUALS));

		rpc.getHasQueryUser(queryList, new QueryObject(QueryEnum.PER_EMAIL, view.getUsername(), true, QueryOperatorEnum.EQUALS), new QueryObject(QueryEnum.PER_PASSWORD, view.getPassword(), true, QueryOperatorEnum.EQUALS),  new AsyncCallback<IsUser>() {

			@Override
			public void onSuccess(IsUser result) {
				if(result != null){
					view.showLoginError(false);
					if(result instanceof User){
						GWT.log("User Instance");
						CookieVerifier.addCookie(result.getPersonId(), result.getEmail(), result.getType());
						eventBus.fireEvent(new PageSwitchEvent(AppType.USER));
					}else if(result instanceof Admin){
						GWT.log("Admin Instance");
						CookieVerifier.addCookie(result.getPersonId(), result.getEmail(), result.getType());
						eventBus.fireEvent(new PageSwitchEvent(AppType.ADMIN));
					}else if(result instanceof LoggedInUser){
						//Window.alert("Your session is already active, please logout and try again. If problem still persist, see Administrator for help.");
						CustomWarningPanel warning = new CustomWarningPanel("<p>Your session is already active, please logout and try again.</p><p> If problem still persist, see Administrator for help.<p>");
						warning.show();
						view.disableSignInButton(false);
					}else if(result instanceof MaximumUserLimit){
						CustomWarningPanel warning = new CustomWarningPanel("<p>The maximum User Limit is reached.</p><p>Please contact Administrator for help.<p>");
						warning.show();
						view.disableSignInButton(false);
					}
				}else{
					GWT.log("Came as NULL");
					view.showLoginError(true);
					view.disableSignInButton(false);
				}

			}

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Caught as failed");
				view.showLoginError(true);
				view.disableSignInButton(false);
			}
		});
	}

	@Override
	public Widget getWidget() {
		bind();
		return view.asWidget();
	}

	@Override
	public void showScreen(HasOneWidget container) {
		bind();
		container.setWidget(view.asWidget());
	}

	@Override
	public Widget asWidget() {
		return getWidget();
	}

}
