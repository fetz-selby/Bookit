package com.iglobal.bookit.client;


import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.iglobal.bookit.client.constants.URLConstants;
import com.iglobal.bookit.client.events.PageSwitchEvent;
import com.iglobal.bookit.client.events.PageSwitchEvent.AppType;
import com.iglobal.bookit.client.events.PageSwitchEventHandler;
import com.iglobal.bookit.client.login.presenter.LoginPresenter;
import com.iglobal.bookit.client.login.view.LoginView;
import com.iglobal.bookit.client.parents.Presenter;
import com.iglobal.bookit.client.utils.CookieVerifier;
import com.iglobal.bookit.client.utils.PageDirector;
import com.iglobal.bookit.client.utils.Utils;

public class LoginAppController implements ValueChangeHandler<String>{
	private HandlerManager eventBus;
	//private RootPanel mainPanel;
	private LoginServiceAsync rpc;
	
	public LoginAppController(LoginServiceAsync rpc, RootPanel mainPanel, HandlerManager eventBus){
		this.eventBus = eventBus;
		//this.mainPanel = mainPanel;
		this.rpc = rpc;
	}
	
	private void bind(){
		History.addValueChangeHandler(this);
		if(History.getToken() ==  null || History.getToken().isEmpty()){
			History.newItem(URLConstants.LOGIN.getUrl(), true);
			History.fireCurrentHistoryState();
		}
		
		//Do cookie check
		History.fireCurrentHistoryState();
		
		eventBus.addHandler(PageSwitchEvent.TYPE, new PageSwitchEventHandler() {
			
			@Override
			public void onPageSwitch(PageSwitchEvent event) {
				// TODO Auto-generated method stub
				switch(event.getAppType()){
				case ADMIN:
					PageDirector.getInstance().directTo("admin.html", "admin");
					break;
				case USER:
					PageDirector.getInstance().directTo("user.html", "user");
					break;
				case LOGIN:
					//Window.alert("Here");
					PageDirector.getInstance().directTo("Bookit.html", "logout");
					break;
				default:
					PageDirector.getInstance().directTo("Bookit.html", "logout");

				}
			}
		});
		
		eventBus.fireEvent(new PageSwitchEvent(AppType.LOGIN));
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		String token = event.getValue().trim();
		URLConstants url = Utils.getURLObject(token);
		Presenter presenter = null;

		if(url == null){
			CookieVerifier.clearCookie();
			eventBus.fireEvent(new PageSwitchEvent(AppType.LOGIN));
			return;
		}

		switch(url){
		case LOGIN : 
			//Do login stuffs
			presenter = new LoginPresenter(rpc, new LoginView(), eventBus);
			break;
		default: 
			CookieVerifier.clearCookie();
			presenter = new LoginPresenter(rpc, new LoginView(), eventBus);
			//eventBus.fireEvent(new PageSwitchEvent(AppType.LOGIN));
			break;
		
		}
		
		if(presenter != null){
			showScreen(presenter.getWidget());
		}
		
	}
	
	private void showScreen(Widget widget){
		RootPanel.get().clear();
		RootPanel.get().add(widget);
	}
	
	public void load(){
		bind();
	}
}
