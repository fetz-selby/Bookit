package com.iglobal.bookit.client;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.iglobal.bookit.client.constants.AppConstants;
import com.iglobal.bookit.client.user.UserAppController;
import com.iglobal.bookit.client.utils.CookieVerifier;
import com.iglobal.bookit.client.utils.PageDirector;
import com.iglobal.bookit.shared.QueryEnum;
import com.iglobal.bookit.shared.QueryObject;
import com.iglobal.bookit.shared.QueryOperatorEnum;
import com.iglobal.bookit.shared.User;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Bookit implements EntryPoint {

	@Override
	public void onModuleLoad() {
		final LoginServiceAsync loginRPC = GWT.create(LoginService.class);
		AdminServiceAsync adminRPC = GWT.create(AdminService.class);
		AdminUpdateServiceAsync adminUpdateRPC = GWT.create(AdminUpdateService.class);
		AdminNewServiceAsync adminNewRPC = GWT.create(AdminNewService.class);
		UserServiceAsync userRPC = GWT.create(UserService.class);

		
		RootPanel.get().clear();
		final HandlerManager eventBus = new HandlerManager(null);

		//Check for cookie, if not exists throw back to login
		
		GlobalResource.getInstance().setAdminRPC(adminRPC);
		GlobalResource.getInstance().setLoginRPC(loginRPC);
		GlobalResource.getInstance().setEventBus(eventBus);
		
		GlobalResource.getInstance().setAdminNewRPC(adminNewRPC);
		GlobalResource.getInstance().setAdminUpdateRPC(adminUpdateRPC);
		GlobalResource.getInstance().setUserRPC(userRPC);
		
		if(CookieVerifier.isCookieExist()){
			
			doUrlValidityCheck();
			
			GlobalResource.getInstance().setSessionId(CookieVerifier.getInstance().getAppSessionId());
			if(CookieVerifier.getInstance().getPersonType().equals(AppConstants.ADMIN)){
				doAdminRPCCall(adminRPC, eventBus);
			}else if(CookieVerifier.getInstance().getPersonType().equals(AppConstants.USER)){
				doUserRPCCall(adminRPC, userRPC, eventBus);
			}
		}else if(CookieVerifier.getInstance().getUsername() != null && !CookieVerifier.getInstance().getUsername().trim().isEmpty()){
			
			GWT.log("[Bookit] came to bookit");
			GlobalResource.getInstance().getLoginRPC().isLogout(CookieVerifier.getInstance().getUsername(), new AsyncCallback<Boolean>() {
				
				@Override
				public void onSuccess(Boolean result) {
					LoginAppController login = new LoginAppController(loginRPC, RootPanel.get(), eventBus);
					login.load();					
				}
				
				@Override
				public void onFailure(Throwable caught) {
					LoginAppController login = new LoginAppController(loginRPC, RootPanel.get(), eventBus);
					login.load();					
				}
			});
		}else{
			LoginAppController login = new LoginAppController(loginRPC, RootPanel.get(), eventBus);
			login.load();
		}
	}
	
	private void doUrlValidityCheck(){
		String loginPage = "Bookit.html";
		if(Window.Location.createUrlBuilder().buildString().contains(loginPage)){
			GlobalResource.getInstance().getLoginRPC().isLogout(CookieVerifier.getInstance().getUsername(),new AsyncCallback<Boolean>() {

				@Override
				public void onSuccess(Boolean result) {
					if(result){
						CookieVerifier.clearCookie();
						PageDirector.getInstance().directTo("Bookit.html", "logout");
						Window.Location.reload();
					}
				}

				@Override
				public void onFailure(Throwable caught) {
					GWT.log("Was a fail");
					
					CookieVerifier.clearCookie();
					PageDirector.getInstance().directTo("Bookit.html", "logout");
				}
			});
			
		}

	}
	
	private void doAdminRPCCall(final AdminServiceAsync adminRPC, final HandlerManager eventBus){
		
		//GWT.log("Cookie email is "+CookieVerifier.getInstance().getUsername()+", and id is "+CookieVerifier.getInstance().getId());
		
		ArrayList<QueryObject> queryList = new ArrayList<QueryObject>();
		QueryObject adminIdQuery = new QueryObject(QueryEnum.A_ID, null, true, QueryOperatorEnum.EQUALS);
		QueryObject statusQuery = new QueryObject(QueryEnum.PER_STATUS, "A", false, QueryOperatorEnum.EQUALS);
		QueryObject adminNameQuery = new QueryObject(QueryEnum.A_NAME, null, true, QueryOperatorEnum.EQUALS);
		QueryObject adminPersonIdQuery = new QueryObject(QueryEnum.A_PERSON_ID, null, true, QueryOperatorEnum.EQUALS);
		QueryObject pPersonIdQuery = new QueryObject(QueryEnum.PER_ID, null, true, QueryOperatorEnum.EQUALS);


		QueryObject permsQuery = new QueryObject(QueryEnum.A_PERMS, null, true, QueryOperatorEnum.EQUALS);
		QueryObject isSuperAdminQuery = new QueryObject(QueryEnum.A_IS_SA, null, true, QueryOperatorEnum.EQUALS);


		QueryObject emailQuery = new QueryObject(QueryEnum.PER_EMAIL, CookieVerifier.getInstance().getUsername(), true, QueryOperatorEnum.EQUALS);
		QueryObject joinAdminToPerson = new QueryObject(QueryEnum.A_PERSON_ID, QueryEnum.PER_ID.getField(), false, QueryOperatorEnum.EQUALS);
		QueryObject personId = new QueryObject(QueryEnum.PER_ID, CookieVerifier.getInstance().getId(), false, QueryOperatorEnum.EQUALS);
		QueryObject typeQuery = new QueryObject(QueryEnum.PER_TYPE, null, true, QueryOperatorEnum.EQUALS);

		queryList.add(adminIdQuery);
		queryList.add(statusQuery);
		queryList.add(adminNameQuery);
		queryList.add(typeQuery);
		queryList.add(emailQuery);
		queryList.add(permsQuery);
		queryList.add(personId);
		queryList.add(joinAdminToPerson);
		queryList.add(isSuperAdminQuery);
		queryList.add(adminPersonIdQuery);
		queryList.add(pPersonIdQuery);
		
		adminRPC.getUser(queryList, new AsyncCallback<User>() {
			
			@Override
			public void onSuccess(User result) {
				if(result != null){
					
					GWT.log("Admin name is "+result.getName()+" and person id is "+result.getPersonId());
					
					GlobalResource.getInstance().setUser(result);
					//CookieVerifier.clearCookie();
					initAdminApp(adminRPC, eventBus);
				}
			
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private void doUserRPCCall(final AdminServiceAsync userRPC, final UserServiceAsync rpc, final HandlerManager eventBus){
		ArrayList<QueryObject> queryList = new ArrayList<QueryObject>();
		QueryObject userIdQuery = new QueryObject(QueryEnum.U_ID, null, true, QueryOperatorEnum.EQUALS);
		QueryObject statusQuery = new QueryObject(QueryEnum.PER_STATUS, "A", false, QueryOperatorEnum.EQUALS);
		QueryObject userNameQuery = new QueryObject(QueryEnum.U_NAME, null, true, QueryOperatorEnum.EQUALS);
		QueryObject permsQuery = new QueryObject(QueryEnum.U_GROUPS, null, true, QueryOperatorEnum.EQUALS);

		QueryObject emailQuery = new QueryObject(QueryEnum.PER_EMAIL, CookieVerifier.getInstance().getUsername(), true, QueryOperatorEnum.EQUALS);
		QueryObject joinAdminToPerson = new QueryObject(QueryEnum.U_PERSON_ID, QueryEnum.PER_ID.getField(), false, QueryOperatorEnum.EQUALS);
		QueryObject personId = new QueryObject(QueryEnum.PER_ID, CookieVerifier.getInstance().getId(), false, QueryOperatorEnum.EQUALS);
		QueryObject typeQuery = new QueryObject(QueryEnum.PER_TYPE, null, true, QueryOperatorEnum.EQUALS);

		queryList.add(userIdQuery);
		queryList.add(statusQuery);
		queryList.add(userNameQuery);
		queryList.add(typeQuery);
		queryList.add(emailQuery);
		queryList.add(permsQuery);
		queryList.add(personId);
		queryList.add(joinAdminToPerson);
		
		userRPC.getUser(queryList, new AsyncCallback<User>() {
			
			@Override
			public void onSuccess(User result) {
				if(result != null){
					
					GWT.log("User name is "+result.getName());
					
					GlobalResource.getInstance().setUser(result);
					doUserGroupPermMatch(rpc, eventBus);
					//initUserApp(rpc, eventBus);
				}
			
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private void doUserGroupPermMatch(final UserServiceAsync rpc, final HandlerManager eventBus){
		//Here getPermsList() is actually groups
		
		GlobalResource.getInstance().getUserRPC().getAggregatedPermsHash(GlobalResource.getInstance().getUser().getPermsList(), new AsyncCallback<HashMap<String,ArrayList<String>>>() {
			
			@Override
			public void onSuccess(HashMap<String, ArrayList<String>> result) {
				if(result != null){
					GlobalResource.getInstance().setGroupPermsHash(result);
					//CookieVerifier.clearCookie();
					GWT.log("[doUserGroupPermMatch]");

					initNotificationGroups(rpc, eventBus);
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private void initNotificationGroups(final UserServiceAsync rpc, final HandlerManager eventBus){
		GlobalResource.getInstance().getUserRPC().getAllNotificationEnabledGroups(GlobalResource.getInstance().getUser().getPermsList(), new AsyncCallback<ArrayList<String>>() {
			
			@Override
			public void onSuccess(ArrayList<String> result) {
				if(result != null){
					GlobalResource.getInstance().setNotificationGroupList(result);
					GWT.log("Notification Checked !!!");
				}
				
				initUserApp(rpc, eventBus);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private void initAdminApp(AdminServiceAsync adminRPC, HandlerManager eventBus){
		AdminAppController admin = new AdminAppController(adminRPC, RootPanel.get(), eventBus);
		admin.setAdminMenu(RootPanel.get("adminMenu"));
		admin.setBookMenu(RootPanel.get("bookMenu"));
		admin.setGroupMenu(RootPanel.get("groupMenu"));
		admin.setUserMenu(RootPanel.get("userMenu"));
		admin.setUserName(RootPanel.get("uid"));
		admin.setAuxUserName(RootPanel.get("uid2"));
		admin.setAddButton(RootPanel.get("add"));
		admin.setSearchButton(RootPanel.get("search"));
		admin.setFilterArea(RootPanel.get("filterArea"));
		admin.setRenderingArea(RootPanel.get("rendererArea"));
		admin.setStatusMessage(RootPanel.get("statusArea"));
		admin.setHeaderArea(RootPanel.get("headerArea"));
		admin.setModuleTitleHeader(RootPanel.get("moduleTitleBar"));
		admin.setSideBarContainer(RootPanel.get("sidebarContainer"));
		admin.load();
	}
	
	private void initUserApp(UserServiceAsync userRPC, HandlerManager eventBus){
		UserAppController user = new UserAppController(userRPC, eventBus);
		user.setStatusPanel(RootPanel.get("statusArea"));
		user.setItemListDiv(RootPanel.get("itemListDiv"));
		user.setListBoxPanel(RootPanel.get("listContainer"));
		user.setTabsPanel(RootPanel.get("tabsPanel"));
		user.setModuleOptionWidget(RootPanel.get("moduleOptionPanel"));
		user.setContentArea(RootPanel.get("rendererArea"));
		user.setNotificationDisplayPanel(RootPanel.get("notificationDisplayPanel"));
		user.setSaveButtonId("saveBtn");
		user.setTabLineId("tabLine");
		user.setAuxArea(RootPanel.get("auxArea"));
		user.getTitleDisplayId("moduleTitle");
		user.setUserName(RootPanel.get("uid"));
		user.setAuxUserName(RootPanel.get("uid2"));
		user.setMiscDiv(RootPanel.get("miscDiv"));
		user.load();
	}

}
