package com.iglobal.bookit.client;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.iglobal.bookit.client.constants.URLConstants;
import com.iglobal.bookit.client.events.AddEvent;
import com.iglobal.bookit.client.events.AddEventHandler;
import com.iglobal.bookit.client.events.AdminEvent;
import com.iglobal.bookit.client.events.AdminEventHandler;
import com.iglobal.bookit.client.events.BookColumnUpdateEvent;
import com.iglobal.bookit.client.events.BookColumnUpdateEventHandler;
import com.iglobal.bookit.client.events.BookEvent;
import com.iglobal.bookit.client.events.BookEventHandler;
import com.iglobal.bookit.client.events.EditEvent;
import com.iglobal.bookit.client.events.EditEventHandler;
import com.iglobal.bookit.client.events.ErrorEvent;
import com.iglobal.bookit.client.events.ErrorEventHandler;
import com.iglobal.bookit.client.events.ForceSessionCloseEvent;
import com.iglobal.bookit.client.events.ForceSessionCloseEventHandler;
import com.iglobal.bookit.client.events.GroupEvent;
import com.iglobal.bookit.client.events.GroupEventHandler;
import com.iglobal.bookit.client.events.HelpEvent;
import com.iglobal.bookit.client.events.HelpEventHandler;
import com.iglobal.bookit.client.events.InitGroupsAndPermsEvent;
import com.iglobal.bookit.client.events.InitGroupsAndPermsEventHandler;
import com.iglobal.bookit.client.events.LogoutEvent;
import com.iglobal.bookit.client.events.LogoutEventHandler;
import com.iglobal.bookit.client.events.PageSwitchEvent;
import com.iglobal.bookit.client.events.PageSwitchEvent.AppType;
import com.iglobal.bookit.client.events.PageSwitchEventHandler;
import com.iglobal.bookit.client.events.SearchEvent;
import com.iglobal.bookit.client.events.SearchEventHandler;
import com.iglobal.bookit.client.events.SessionsEvent;
import com.iglobal.bookit.client.events.SessionsEventHandler;
import com.iglobal.bookit.client.events.SettingsEvent;
import com.iglobal.bookit.client.events.SettingsEventHandler;
import com.iglobal.bookit.client.events.SuccessEvent;
import com.iglobal.bookit.client.events.SuccessEventHandler;
import com.iglobal.bookit.client.events.UserEvent;
import com.iglobal.bookit.client.events.UserEventHandler;
import com.iglobal.bookit.client.parents.Filter;
import com.iglobal.bookit.client.parents.Renderer;
import com.iglobal.bookit.client.parents.RendererHeader;
import com.iglobal.bookit.client.ui.components.CustomDraggablePopupPanel;
import com.iglobal.bookit.client.ui.components.ErrorAlert;
import com.iglobal.bookit.client.ui.components.ModuleTitleWidget;
import com.iglobal.bookit.client.ui.components.NoDataFound;
import com.iglobal.bookit.client.ui.components.RowCounterWidget;
import com.iglobal.bookit.client.ui.components.SearchBoxWidget;
import com.iglobal.bookit.client.ui.components.SearchBoxWidget.SearchBoxWidgetEventHandler;
import com.iglobal.bookit.client.ui.components.SideBarMenuWidget2;
import com.iglobal.bookit.client.ui.components.SuccessAlert;
import com.iglobal.bookit.client.ui.filters.AdminFilter;
import com.iglobal.bookit.client.ui.filters.BookFilter;
import com.iglobal.bookit.client.ui.filters.GroupFilter;
import com.iglobal.bookit.client.ui.filters.SessionsFilter;
import com.iglobal.bookit.client.ui.filters.UserFilter;
import com.iglobal.bookit.client.ui.form.AdminForm;
import com.iglobal.bookit.client.ui.form.AdminForm.AdminFormEventHandler;
import com.iglobal.bookit.client.ui.form.BookForm;
import com.iglobal.bookit.client.ui.form.BookForm.BookFormEventHandler;
import com.iglobal.bookit.client.ui.form.GroupForm;
import com.iglobal.bookit.client.ui.form.GroupForm.GroupFormEventHandler;
import com.iglobal.bookit.client.ui.form.UserForm;
import com.iglobal.bookit.client.ui.form.UserForm.UserFormEventHandler;
import com.iglobal.bookit.client.ui.headers.AdminHeader;
import com.iglobal.bookit.client.ui.headers.BookHeader;
import com.iglobal.bookit.client.ui.headers.GroupHeader;
import com.iglobal.bookit.client.ui.headers.SessionsHeader;
import com.iglobal.bookit.client.ui.headers.UserHeader;
import com.iglobal.bookit.client.ui.renderer.AdminRenderer;
import com.iglobal.bookit.client.ui.renderer.BookRenderer;
import com.iglobal.bookit.client.ui.renderer.GroupRenderer;
import com.iglobal.bookit.client.ui.renderer.SessionsRenderer;
import com.iglobal.bookit.client.ui.renderer.UserRenderer;
import com.iglobal.bookit.client.utils.CookieVerifier;
import com.iglobal.bookit.client.utils.PageDirector;
import com.iglobal.bookit.client.utils.Utils;
import com.iglobal.bookit.shared.AdminRowObject;
import com.iglobal.bookit.shared.BookColumnObject;
import com.iglobal.bookit.shared.BookRowObject;
import com.iglobal.bookit.shared.GroupRowObject;
import com.iglobal.bookit.shared.PermsRowObject;
import com.iglobal.bookit.shared.QueryEnum;
import com.iglobal.bookit.shared.QueryObject;
import com.iglobal.bookit.shared.QueryOperatorEnum;
import com.iglobal.bookit.shared.SessionsRowObject;
import com.iglobal.bookit.shared.UserRowObject;

public class AdminAppController implements ValueChangeHandler<String>{
	private URLConstants currentUrl;
	private HandlerManager eventBus;
	private AdminServiceAsync rpc;
	private RootPanel userSpan;
	private RootPanel auxUserSpan;
	private RootPanel addButton;
	private RootPanel searchButton;
	private RootPanel filterArea;
	private RootPanel rendererArea;
	private RootPanel moduleTitleBar;
	private Filter filter = null;
	private Renderer<?> renderer = null;
	private RootPanel statusPanel;
	private RootPanel headerPanel;
	private RendererHeader rendererHeader;
	private RowCounterWidget rowCounter;
	private SearchBoxWidget searchBox;
	private RootPanel sidebarContainer;
	private ArrayList<URLConstants> urlListHolder;

	public AdminAppController(AdminServiceAsync rpc, RootPanel mainPanel, HandlerManager eventBus){
		this.eventBus = eventBus;
		this.rpc = rpc;
	}

	private void bind(){
		urlListHolder = new ArrayList<URLConstants>();
		History.addValueChangeHandler(this);

//		if(!isUrlValid()){
//			PageDirector.getInstance().directTo("Bookit.html", "logout");
//			History.fireCurrentHistoryState();
//
//			return;
//		}
		
		//Window.alert(""+Window.Location.createUrlBuilder().buildString()+"*** Hosted "+GWT.getHostPageBaseURL()+" *** Module "+GWT.getModuleBaseURL());
		if(GlobalResource.getInstance().getUser().isSuperUser()){
			History.newItem(URLConstants.ADMINS.getUrl());
		}else{
			History.newItem(URLConstants.GROUPS.getUrl());
		}

		//Do cookie check
		History.fireCurrentHistoryState();

		eventBus.addHandler(AdminEvent.TYPE, new AdminEventHandler() {

			@Override
			public void onAdminInvoked(AdminEvent event) {
				showLoadingOnSearchButton(false);
				History.newItem(URLConstants.ADMINS.getUrl());
			}
		});

		eventBus.addHandler(BookEvent.TYPE, new BookEventHandler() {

			@Override
			public void onBookInvoked(BookEvent event) {
				showLoadingOnSearchButton(false);
				History.newItem(URLConstants.BOOKS.getUrl());
			}
		});

		eventBus.addHandler(GroupEvent.TYPE, new GroupEventHandler() {

			@Override
			public void onGroupInvoked(GroupEvent event) {
				showLoadingOnSearchButton(false);
				History.newItem(URLConstants.GROUPS.getUrl());
			}
		});

		eventBus.addHandler(UserEvent.TYPE, new UserEventHandler() {

			@Override
			public void onUserInvoked(UserEvent event) {
				showLoadingOnSearchButton(false);
				History.newItem(URLConstants.USERS.getUrl());
			}
		});

		eventBus.addHandler(PageSwitchEvent.TYPE, new PageSwitchEventHandler() {

			@Override
			public void onPageSwitch(PageSwitchEvent event) {
				switch(event.getAppType()){
				case ADMIN:
					PageDirector.getInstance().directTo("admin.html", "admin");
					break;
				case USER:
					PageDirector.getInstance().directTo("user.html", "");
					break;
				case LOGIN:
					PageDirector.getInstance().directTo("Bookit.html", "");
					break;
				default:
					PageDirector.getInstance().directTo("Bookit.html", "");
				}
			}
		});

		eventBus.addHandler(SettingsEvent.TYPE, new SettingsEventHandler() {

			@Override
			public void onSettingsInvoked(SettingsEvent event) {
				History.newItem(URLConstants.SETTINGS.getUrl());
			}
		});

		eventBus.addHandler(HelpEvent.TYPE, new HelpEventHandler() {

			@Override
			public void onHelpInvoked(HelpEvent event) {
				History.newItem(URLConstants.HELP.getUrl());
			}
		});

		eventBus.addHandler(LogoutEvent.TYPE, new LogoutEventHandler() {

			@Override
			public void onLogoutInvoked(LogoutEvent event) {
				CookieVerifier.clearCookie();
				eventBus.fireEvent(new PageSwitchEvent(AppType.LOGIN));
			}
		});

		eventBus.addHandler(AddEvent.TYPE, new AddEventHandler() {

			@Override
			public void onAddInvoked(AddEvent event) {
				showModuleAdd();
			}
		});

		eventBus.addHandler(SearchEvent.TYPE, new SearchEventHandler() {

			@Override
			public void onSearchInvoked(SearchEvent event) {
				doRendererLoad();
			}
		});

		eventBus.addHandler(SuccessEvent.TYPE, new SuccessEventHandler() {

			@Override
			public void onSuccessEventInvoked(SuccessEvent event) {
				showSuccessAlert(event.getMessage());
			}
		});

		eventBus.addHandler(ErrorEvent.TYPE, new ErrorEventHandler() {

			@Override
			public void onErrorEventInvoked(ErrorEvent event) {
				showErrorAlert(event.getMessage());
			}
		});

		eventBus.addHandler(InitGroupsAndPermsEvent.TYPE, new InitGroupsAndPermsEventHandler() {

			@Override
			public void onGroupAndPermsEventInvoked(InitGroupsAndPermsEvent event) {
				initPermsList();
				initGroupsList();
			}
		});

		eventBus.addHandler(EditEvent.TYPE, new EditEventHandler() {

			@Override
			public void onEditEventInvoked(EditEvent event) {
				showModuleEdit(event.getModelType(), event.getObject());
			}
		});

		eventBus.addHandler(BookColumnUpdateEvent.TYPE, new BookColumnUpdateEventHandler() {

			@Override
			public void onBookColumnUpdate(BookColumnUpdateEvent event) {
				initBookColumnsList();
			}
		});

		eventBus.addHandler(SessionsEvent.TYPE, new SessionsEventHandler() {

			@Override
			public void onSessionsInvoked(SessionsEvent event) {
				showLoadingOnSearchButton(false);
				History.newItem(URLConstants.SESSIONS.getUrl());
			}
		});

		eventBus.addHandler(ForceSessionCloseEvent.TYPE, new ForceSessionCloseEventHandler() {

			@Override
			public void onSessionCloseInvoked(final ForceSessionCloseEvent event) {
				GlobalResource.getInstance().getLoginRPC().isLogout(event.getEmail(), new AsyncCallback<Boolean>() {

					@Override
					public void onSuccess(Boolean result) {
						if(result){
							//Display success message
							eventBus.fireEvent(new SearchEvent(currentUrl));
							showSuccessAlert(event.getEmail()+" session ended successfully");

						}
					}

					@Override
					public void onFailure(Throwable caught) {
						showErrorAlert("Sorry, there was a problem with your request. Please try again later. Thank you.");

					}
				});
			}
		});

		loadSideBar();
	}

//	private boolean isUrlValid(){
//		String loginPage = "Bookit.html";
//		if(Window.Location.createUrlBuilder().buildString().contains(loginPage)){
//			return false;
//		}
//		
//		return true;
//	}
	
	private void loadSideBar(){
		sidebarContainer.clear();
		sidebarContainer.add(new SideBarMenuWidget2());
	}

	private void showModuleEdit(URLConstants moduleEdit, IsSerializable object){
		if(object == null || moduleEdit == null){
			return;
		}

		final CustomDraggablePopupPanel panel = new CustomDraggablePopupPanel(true);

		switch(moduleEdit){
		case ADMINS:
			AdminRowObject adminObject = (AdminRowObject)object;
			panel.add(new AdminForm(new AdminFormEventHandler() {

				@Override
				public void onFormSuccess(AdminRowObject adminObject) {
					panel.hide();
					eventBus.fireEvent(new SearchEvent(URLConstants.ADMINS));
					showSuccessAlert("Administrators updated successfully");
					//doHistoryRefresh();
				}

				@Override
				public void onFormError(AdminRowObject adminObject) {
					panel.hide();
					eventBus.fireEvent(new SearchEvent(URLConstants.ADMINS));
					showErrorAlert(adminObject.getName()+" administrator could not be updated");					
				}
			}, adminObject, panel));
			break;
		case BOOKS:
			BookRowObject bookObject = (BookRowObject)object;
			panel.add(new BookForm(new BookFormEventHandler() {

				@Override
				public void onFormSuccess(BookRowObject adminObject) {
					panel.hide();
					eventBus.fireEvent(new SearchEvent(URLConstants.BOOKS));
					showSuccessAlert(getCorrectBookName(adminObject.getName())+" book updated successfully");
					//doHistoryRefresh();
				}

				@Override
				public void onAutoClose() {
					eventBus.fireEvent(new SearchEvent(currentUrl));
				}

				@Override
				public void onFormError(BookRowObject adminObject) {
					panel.hide();
					eventBus.fireEvent(new SearchEvent(URLConstants.BOOKS));
					showErrorAlert(getCorrectBookName(adminObject.getName())+" book could not be updated");					
				}
			}, bookObject, panel));
			break;
		case GROUPS:
			GroupRowObject groupObject = (GroupRowObject)object;
			panel.add(new GroupForm(new GroupFormEventHandler() {

				@Override
				public void onFormSuccess(GroupRowObject groupObject) {
					panel.hide();
					do2ndinitGroupsListOnUpdate(groupObject);
					eventBus.fireEvent(new SearchEvent(URLConstants.GROUPS));
					showSuccessAlert(groupObject.getName()+" group updated successfully");
					//doHistoryRefresh();
				}

				@Override
				public void onFormError(GroupRowObject groupObject) {
					panel.hide();
					do2ndinitGroupsListOnUpdate(groupObject);
					eventBus.fireEvent(new SearchEvent(URLConstants.GROUPS));
					showErrorAlert(groupObject.getName()+" group could not be updated");					
				}
			}, groupObject, panel));
			break;
		case HELP:
			break;
		case LOGIN:
			break;
		case LOGOUT:
			break;
		case SETTINGS:
			break;
		case USERS:
			UserRowObject userObject = (UserRowObject)object;
			panel.add(new UserForm(new UserFormEventHandler() {

				@Override
				public void onFormSuccess(UserRowObject userObject) {
					panel.hide();
					eventBus.fireEvent(new SearchEvent(URLConstants.USERS));
					showSuccessAlert(userObject.getName()+" updated successfully");
				}

				@Override
				public void onFormError(UserRowObject userObject) {
					panel.hide();
					eventBus.fireEvent(new SearchEvent(URLConstants.USERS));
					showErrorAlert(userObject.getName()+" user could not be updated");					
				}
			}, userObject, panel));
			break;
		default:
			break;
		}

		panel.setGlassEnabled(true);
		panel.setAnimationEnabled(true);
		panel.setGlassStyleName("glassPanel");
		//panel.setAutoHideEnabled(true);
		panel.center();
		
	}

	private String getCorrectBookName(String bookName){
		if(bookName.contains(":")){
			return bookName.split("[:]")[1];
		}
		return bookName;
	}

	private void showModuleAdd(){
		final CustomDraggablePopupPanel panel = new CustomDraggablePopupPanel(true);

		switch(currentUrl){
		case ADMINS:
			//Show a popup for admin add
			panel.setDraggable(true);
			panel.add(new AdminForm(new AdminFormEventHandler() {

				@Override
				public void onFormSuccess(AdminRowObject adminObject) {
					panel.hide();
					showSuccessAlert(adminObject.getName()+" administrator added successfully");
				}

				@Override
				public void onFormError(AdminRowObject adminObject) {
					panel.hide();
					showErrorAlert(adminObject.getName()+" administrator could not be saved");					
				}
			}, panel));
			break;
		case BOOKS:
			panel.setDraggable(true);
			panel.add(new BookForm(new BookFormEventHandler() {

				@Override
				public void onFormSuccess(BookRowObject adminObject) {
					panel.hide();
					showSuccessAlert(getCorrectBookName(adminObject.getName())+" book added successfully");
				}

				@Override
				public void onAutoClose() {
					eventBus.fireEvent(new SearchEvent(currentUrl));
				}

				@Override
				public void onFormError(BookRowObject adminObject) {
					panel.hide();
					showErrorAlert(getCorrectBookName(adminObject.getName())+" book could not be saved");					
				}
			}, panel));
			break;
		case GROUPS:
			panel.setDraggable(true);
			panel.add(new GroupForm(new GroupFormEventHandler() {

				@Override
				public void onFormSuccess(GroupRowObject groupObject) {
					panel.hide();
					do2ndinitGroupsListOnAdd(groupObject);
					//doHistoryRefresh();

				}

				@Override
				public void onFormError(GroupRowObject groupObject) {
					panel.hide();
					showErrorAlert(getCorrectBookName(groupObject.getName())+" group could not be saved");					
				}
			}, panel));
			break;
		case HELP:
			break;
		case LOGIN:
			break;
		case LOGOUT:
			break;
		case SETTINGS:
			break;
		case USERS:
			panel.setDraggable(true);
			panel.add(new UserForm(new UserFormEventHandler() {

				@Override
				public void onFormSuccess(UserRowObject userObject) {
					panel.hide();
					showSuccessAlert(userObject.getName()+" added successfully as a User");
				}

				@Override
				public void onFormError(UserRowObject userObject) {
					panel.hide();
					showErrorAlert(getCorrectBookName(userObject.getName())+" user could not be saved");					}
			}, panel));
			break;
		default:
			break;

		}

		panel.setGlassEnabled(true);
		panel.setAnimationEnabled(true);
		panel.setGlassStyleName("glassPanel");
		//panel.setAutoHideEnabled(true);
		panel.center();
	}

	private void showSuccessAlert(String message){
		statusPanel.clear();
		statusPanel.add(new SuccessAlert(message));
		doRendererLoad();
	}

	private void showErrorAlert(String message){
		statusPanel.clear();
		statusPanel.add(new ErrorAlert(message));
		doRendererLoad();
	}

	private void doRendererLoad(){
		if(currentUrl == null && getLastNonNullUrl() == null){
			return;
		}else if(currentUrl == null && getLastNonNullUrl() != null){
			currentUrl = getLastNonNullUrl();
		}

		switch(currentUrl){
		case ADMINS:
			doAdminFilterQueryCall();
			break;
		case BOOKS:
			doBookFilterQueryCall();
			break;
		case GROUPS:
			doGroupFilterQueryCall();
			break;
		case SESSIONS:
			doSessionsFilterQueryCall();
		case HELP:
			break;
		case LOGIN:
			break;
		case LOGOUT:
			eventBus.fireEvent(new LogoutEvent());
			break;
		case SETTINGS:
			break;
		case USERS:
			doUserFilterQueryCall();
			break;
		default:
			break;

		}
	}

	private void doSearchRendererLoad(String query){
		if(currentUrl == null && getLastNonNullUrl() == null){
			return;
		}else if(currentUrl == null && getLastNonNullUrl() != null){
			currentUrl = getLastNonNullUrl();
		}

		switch(currentUrl){
		case ADMINS:
			doAdminSearchFilterQueryCall(query);
			break;
		case BOOKS:
			doBookSearchFilterQueryCall(query);
			break;
		case GROUPS:
			doGroupSearchFilterQueryCall(query);
			break;
		case SESSIONS:
			doSessionSearchFilterQueryCall(query);
			break;
		case HELP:
			break;
		case LOGIN:
			break;
		case LOGOUT:
			eventBus.fireEvent(new LogoutEvent());
			break;
		case SETTINGS:
			break;
		case USERS:
			doUserSearchFilterQueryCall(query);
			break;
		default:
			break;

		}
	}

	private void reloadRenderer(Renderer<?> renderer){
		rendererArea.clear();
		rendererArea.add(renderer);
	}
	
	private void showLoading(){
		rendererArea.clear();
		Label label = new Label("Loading ...");
		label.setStyleName("loading-style");
		rendererArea.add(label);
	}
	
	private void showLoadingOnSearchButton(boolean isShow){
		if(isShow){
			searchButton.getElement().setInnerText("Loading ...");
		}else{
			searchButton.getElement().setInnerText("Search");
		}
	}

	private void doAdminFilterQueryCall(){
		showLoading();
		rpc.getAdminList(filter.getSearchFilterQuery(searchBox.getSearchText()), new AsyncCallback<ArrayList<AdminRowObject>>() {

			@Override
			public void onSuccess(ArrayList<AdminRowObject> result) {
				if(result != null){
					renderer = new AdminRenderer(result);
					if(result.size() == 0 || result.size() < 0){
						reloadRenderer(new NoDataFound("Sorry, No Administrators Found For Your Search Criteria"));
					}else{
						reloadRenderer(renderer);
					}
					rowCounter.setRowCounts(result.size()+"");
				}
				showLoadingOnSearchButton(false);
			}

			@Override
			public void onFailure(Throwable caught) {
				reloadRenderer(new NoDataFound("Sorry, there was a problem with your request. Please try again later. Thank you."));
				showLoadingOnSearchButton(false);

			}
		});
	}

	private void doBookFilterQueryCall(){
		showLoading();
		rpc.getBookList(filter.getSearchFilterQuery(searchBox.getSearchText()), new AsyncCallback<ArrayList<BookRowObject>>() {

			@Override
			public void onSuccess(ArrayList<BookRowObject> result) {
				if(result != null){
					renderer = new BookRenderer(result);
					if(result.size() == 0 || result.size() < 0){
						reloadRenderer(new NoDataFound("Sorry, No Books Found For Your Search Criteria"));
					}else{
						reloadRenderer(renderer);
					}
					rowCounter.setRowCounts(result.size()+"");
				}
				showLoadingOnSearchButton(false);

			}

			@Override
			public void onFailure(Throwable caught) {
				reloadRenderer(new NoDataFound("Sorry, there was a problem with your request. Please try again later. Thank you."));
				showLoadingOnSearchButton(false);
			}
		});
	}

	private void doGroupFilterQueryCall(){
		showLoading();
		rpc.getGroupList(filter.getSearchFilterQuery(searchBox.getSearchText()), new AsyncCallback<ArrayList<GroupRowObject>>() {

			@Override
			public void onSuccess(ArrayList<GroupRowObject> result) {
				if(result != null){
					renderer = new GroupRenderer(result);
					if(result.size() == 0 || result.size() < 0){
						reloadRenderer(new NoDataFound("Sorry, No Groups Found For Your Search Criteria"));
					}else{
						reloadRenderer(renderer);
					}
					rowCounter.setRowCounts(result.size()+"");
				}
				showLoadingOnSearchButton(false);
			}

			@Override
			public void onFailure(Throwable caught) {
				reloadRenderer(new NoDataFound("Sorry, there was a problem with your request. Please try again later. Thank you."));
				showLoadingOnSearchButton(false);
			}
		});
	}

	private void doUserFilterQueryCall(){
		showLoading();
		rpc.getUserList(filter.getSearchFilterQuery(searchBox.getSearchText()), new AsyncCallback<ArrayList<UserRowObject>>() {

			@Override
			public void onSuccess(ArrayList<UserRowObject> result) {
				if(result != null){
					renderer = new UserRenderer(result);
					if(result.size() == 0 || result.size() < 0){
						reloadRenderer(new NoDataFound("Sorry, No Users Found For Your Search Criteria"));
					}else{
						reloadRenderer(renderer);
					}
					rowCounter.setRowCounts(result.size()+"");
				}
				showLoadingOnSearchButton(false);
			}

			@Override
			public void onFailure(Throwable caught) {
				reloadRenderer(new NoDataFound("Sorry, there was a problem with your request. Please try again later. Thank you."));
				showLoadingOnSearchButton(false);
			}
		});
	}

	private void doSessionsFilterQueryCall(){
		showLoading();
		rpc.getSessionsList(filter.getSearchFilterQuery(searchBox.getSearchText()), new AsyncCallback<ArrayList<SessionsRowObject>>() {

			@Override
			public void onSuccess(ArrayList<SessionsRowObject> result) {
				if(result != null){
					renderer = new SessionsRenderer(result);
					if(result.size() == 0 || result.size() < 0){
						NoDataFound msg = new NoDataFound("Sorry, No Sessions Found For Your Search Criteria");
						msg.setLowerMessage("No users are currently logged into the system.");

						reloadRenderer(msg);
					}else{
						reloadRenderer(renderer);
					}
					rowCounter.setRowCounts(result.size()+"");
				}
				showLoadingOnSearchButton(false);
			}

			@Override
			public void onFailure(Throwable caught) {
				reloadRenderer(new NoDataFound("Sorry, there was a problem with your request. Please try again later. Thank you."));
				showLoadingOnSearchButton(false);
			}
		});
	}

	private void doAdminSearchFilterQueryCall(String query){
		showLoading();
		rpc.getAdminList(filter.getSearchFilterQuery(query), new AsyncCallback<ArrayList<AdminRowObject>>() {

			@Override
			public void onSuccess(ArrayList<AdminRowObject> result) {
				if(result != null){
					renderer = new AdminRenderer(result);
					if(result.size() == 0 || result.size() < 0){
						reloadRenderer(new NoDataFound("Sorry, No Administrators Found For Your Search Criteria"));
					}else{
						reloadRenderer(renderer);
					}
					rowCounter.setRowCounts(result.size()+"");
				}
				showLoadingOnSearchButton(false);
			}

			@Override
			public void onFailure(Throwable caught) {
				reloadRenderer(new NoDataFound("Sorry, there was a problem with your request. Please try again later. Thank you."));
			}
		});
	}

	private void doBookSearchFilterQueryCall(String query){
		showLoading();
		rpc.getBookList(filter.getSearchFilterQuery(query), new AsyncCallback<ArrayList<BookRowObject>>() {

			@Override
			public void onSuccess(ArrayList<BookRowObject> result) {
				if(result != null){
					renderer = new BookRenderer(result);
					if(result.size() == 0 || result.size() < 0){
						reloadRenderer(new NoDataFound("Sorry, No Books Found For Your Search Criteria"));
					}else{
						reloadRenderer(renderer);
					}
					rowCounter.setRowCounts(result.size()+"");
				}
				showLoadingOnSearchButton(false);
			}

			@Override
			public void onFailure(Throwable caught) {
				reloadRenderer(new NoDataFound("Sorry, there was a problem with your request. Please try again later. Thank you."));
				showLoadingOnSearchButton(false);
			}
		});
	}

	private void doGroupSearchFilterQueryCall(String query){
		showLoading();
		rpc.getGroupList(filter.getSearchFilterQuery(query), new AsyncCallback<ArrayList<GroupRowObject>>() {

			@Override
			public void onSuccess(ArrayList<GroupRowObject> result) {
				if(result != null){
					renderer = new GroupRenderer(result);
					if(result.size() == 0 || result.size() < 0){
						reloadRenderer(new NoDataFound("Sorry, No Groups Found For Your Search Criteria"));
					}else{
						reloadRenderer(renderer);
					}
					rowCounter.setRowCounts(result.size()+"");
				}
				showLoadingOnSearchButton(false);
			}

			@Override
			public void onFailure(Throwable caught) {
				reloadRenderer(new NoDataFound("Sorry, there was a problem with your request. Please try again later. Thank you."));
				showLoadingOnSearchButton(false);
			}
		});
	}

	private void doUserSearchFilterQueryCall(String query){
		showLoading();
		rpc.getUserList(filter.getSearchFilterQuery(query), new AsyncCallback<ArrayList<UserRowObject>>() {

			@Override
			public void onSuccess(ArrayList<UserRowObject> result) {
				if(result != null){
					renderer = new UserRenderer(result);
					if(result.size() == 0 || result.size() < 0){
						reloadRenderer(new NoDataFound("Sorry, No Users Found For Your Search Criteria"));
					}else{
						reloadRenderer(renderer);
					}
					rowCounter.setRowCounts(result.size()+"");
				}
				showLoadingOnSearchButton(false);
			}

			@Override
			public void onFailure(Throwable caught) {
				reloadRenderer(new NoDataFound("Sorry, there was a problem with your request. Please try again later. Thank you."));
				showLoadingOnSearchButton(false);
			}
		});
	}

	private void doSessionSearchFilterQueryCall(String query){
		showLoading();
		rpc.getSessionsList(filter.getSearchFilterQuery(query), new AsyncCallback<ArrayList<SessionsRowObject>>() {

			@Override
			public void onSuccess(ArrayList<SessionsRowObject> result) {
				if(result != null){
					renderer = new SessionsRenderer(result);
					if(result.size() == 0 || result.size() < 0){
						NoDataFound msg = new NoDataFound("Sorry, No Sessions Found For Your Search Criteria");
						msg.setLowerMessage("No users are currently logged into the system.");

						reloadRenderer(msg);		
					}else{
						reloadRenderer(renderer);
					}
					rowCounter.setRowCounts(result.size()+"");
				}
				showLoadingOnSearchButton(false);
			}

			@Override
			public void onFailure(Throwable caught) {
				reloadRenderer(new NoDataFound("Sorry, there was a problem with your request. Please try again later. Thank you."));
				showLoadingOnSearchButton(false);
			}
		});
	}

	private void initMenuEvent(){

		if(GlobalResource.getInstance().getUser().isSuperUser()){
			com.google.gwt.user.client.Element adminElement = DOM.getElementById("adminMenu");
			DOM.sinkEvents(adminElement, Event.ONCLICK);
			DOM.setEventListener(adminElement, new EventListener() {

				@Override
				public void onBrowserEvent(Event event) {
					eventBus.fireEvent(new AdminEvent());
				}
			});
		}

		com.google.gwt.user.client.Element bookElement = DOM.getElementById("bookMenu");
		DOM.sinkEvents(bookElement, Event.ONCLICK);
		DOM.setEventListener(bookElement, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				eventBus.fireEvent(new BookEvent());
			}
		});

		com.google.gwt.user.client.Element groupElement = DOM.getElementById("groupMenu");
		DOM.sinkEvents(groupElement, Event.ONCLICK);
		DOM.setEventListener(groupElement, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				eventBus.fireEvent(new GroupEvent());
			}
		});

		com.google.gwt.user.client.Element userElement = DOM.getElementById("userMenu");
		DOM.sinkEvents(userElement, Event.ONCLICK);
		DOM.setEventListener(userElement, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				eventBus.fireEvent(new UserEvent());
			}
		});

		com.google.gwt.user.client.Element sessionsElement = DOM.getElementById("sessionMenu");
		DOM.sinkEvents(sessionsElement, Event.ONCLICK);
		DOM.setEventListener(sessionsElement, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				eventBus.fireEvent(new SessionsEvent());
			}
		});

		if(GlobalResource.getInstance().isCanAdminWrite()){
			addButton.getElement().setAttribute("style", "");
			com.google.gwt.user.client.Element addElement = addButton.getElement()
					.cast();
			DOM.sinkEvents(addElement, Event.ONCLICK);
			DOM.setEventListener(addElement, new EventListener() {

				@Override
				public void onBrowserEvent(Event event) {
					eventBus.fireEvent(new AddEvent(currentUrl));
				}
			});
		}


		com.google.gwt.user.client.Element searchElement = searchButton.getElement()
				.cast();
		DOM.sinkEvents(searchElement, Event.ONCLICK);
		DOM.setEventListener(searchElement, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				showLoadingOnSearchButton(true);
				eventBus.fireEvent(new SearchEvent(currentUrl));
				GWT.log("Search Clicked !");
			}
		});

	}

	private void initDOMElements(){
		String userName = GlobalResource.getInstance().getUser().getName();

		if(userName.contains(" ")){
			userName = userName.split("[\\s]")[0];
		}

		com.google.gwt.user.client.Element userSpanElement = userSpan.getElement().cast();
		userSpanElement.setInnerText(Utils.getTruncatedText(userName, 12));

		com.google.gwt.user.client.Element auxUserSpanElement = auxUserSpan.getElement().cast();
		auxUserSpanElement.setInnerText(Utils.getTruncatedText(userName, 12));
	}
	
	private URLConstants getLastNonNullUrl(){
		if(urlListHolder != null){
			for(int i = urlListHolder.size()-1; i > -1; i--){
				URLConstants url = urlListHolder.get(i);
				if(url != null){
					return url;
				}
			}
		}
		return null;
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		String token = event.getValue().trim();
		currentUrl = Utils.getURLObject(token);
		urlListHolder.add(currentUrl);
		filter = null;
		renderer = null;
		rendererHeader = null;

		if(currentUrl == null && getLastNonNullUrl() == null){
			return;
		}else if(currentUrl == null && getLastNonNullUrl() != null){
			currentUrl = getLastNonNullUrl();
		}

		switch(currentUrl){
		case ADMINS : 

			if(GlobalResource.getInstance().getUser().isSuperUser()){
				showAddButton(true);

				setModuleTitle("Administrators");

				filter = new AdminFilter(rpc);
				renderer = new AdminRenderer(null);
				rendererHeader = new AdminHeader();
			}else{
				//Show 404
			}

			break;
		case BOOKS : 

			showAddButton(true);

			setModuleTitle("Books");

			filter = new BookFilter(rpc);
			renderer = new BookRenderer(null);
			rendererHeader = new BookHeader();

			break;
		case GROUPS : 
			showAddButton(true);

			setModuleTitle("Groups");

			filter = new GroupFilter(rpc);
			renderer = new GroupRenderer(null);
			rendererHeader = new GroupHeader();

			break;
		case USERS : 
			showAddButton(true);

			setModuleTitle("Users");

			filter = new UserFilter(rpc);
			renderer = new UserRenderer(null);
			rendererHeader = new UserHeader();

			break;
		case SETTINGS:
			setModuleTitle("Settings");

			break;
		case HELP:
			break;
		case LOGOUT:
			//User back to login
			eventBus.fireEvent(new LogoutEvent());
			break;
		case SESSIONS : 
			showAddButton(false);
			setModuleTitle("Sessions");

			filter = new SessionsFilter();
			renderer = new SessionsRenderer(null);
			rendererHeader = new SessionsHeader();

			break;
		default: 
			//Window.alert("Got nothing!");
			eventBus.fireEvent(new LogoutEvent());
			break;
		}

		if(filter != null && renderer != null){
			showScreen(filter, renderer, rendererHeader);
		}

	}

	public void setAdminMenu(RootPanel adminMenu){
		//this.adminMenu = adminMenu;
	}

	public void setBookMenu(RootPanel bookMenu){
		//this.bookMenu = bookMenu;
	}

	public void setGroupMenu(RootPanel groupMenu){
		//this.groupMenu = groupMenu;
	}

	public void setUserMenu(RootPanel userMenu){
		//this.userMenu = userMenu;
	}

	public void setUserName(RootPanel userSpan){
		this.userSpan = userSpan;
	}

	public void setAuxUserName(RootPanel auxUserSpan){
		this.auxUserSpan = auxUserSpan;
	}

	public void setAddButton(RootPanel addButton){
		this.addButton = addButton;
	}

	public void setSearchButton(RootPanel searchButton){
		this.searchButton = searchButton;
	}

	public void setRenderingArea(RootPanel rendererArea){
		this.rendererArea = rendererArea;
	}

	public void setHeaderArea(RootPanel headerPanel){
		this.headerPanel = headerPanel;
	}

	public void setStatusMessage(RootPanel statusPanel){
		this.statusPanel = statusPanel;
	}

	private void showScreen(Filter filter , Renderer<?> renderer, RendererHeader header){
		filterArea.clear();
		filterArea.add(filter);

		rendererArea.clear();
		rendererArea.add(renderer);

		headerPanel.clear();
		headerPanel.add(header);
	}

	public void load(){
		bind();
		initMenuEvent();
		initDOMElements();
		initPermsList();
		initGroupsList();
		initBookColumnsList();

	}

	public void setFilterArea(RootPanel filterArea) {
		this.filterArea = filterArea;
	}

	private void initPermsList(){
		ArrayList<QueryObject> permsList = new ArrayList<QueryObject>();
		permsList.add(new QueryObject(QueryEnum.P_ID, null, true, null));
		permsList.add(new QueryObject(QueryEnum.P_NAME, null, true, null));
		permsList.add(new QueryObject(QueryEnum.P_STRING, null, true, null));
		permsList.add(new QueryObject(QueryEnum.P_STATUS, "A", true, QueryOperatorEnum.EQUALS));

		rpc.getPermsList(permsList, new AsyncCallback<ArrayList<PermsRowObject>>() {

			@Override
			public void onSuccess(ArrayList<PermsRowObject> result) {
				if(result != null){
					GlobalResource.getInstance().setPermsList(result);
					initPermsPrimaryList();
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				initPermsList();
			}
		});
	}

	private void initPermsPrimaryList(){
		ArrayList<QueryObject> permsList = new ArrayList<QueryObject>();
		permsList.add(new QueryObject(QueryEnum.P_ID, null, true, null));
		permsList.add(new QueryObject(QueryEnum.P_NAME, null, true, null));
		permsList.add(new QueryObject(QueryEnum.P_STRING, null, true, null));
		permsList.add(new QueryObject(QueryEnum.P_STATUS, "A", true, QueryOperatorEnum.EQUALS));
		permsList.add(new QueryObject(QueryEnum.P_CATEGORY, "P", true, QueryOperatorEnum.EQUALS));


		rpc.getPermsList(permsList, new AsyncCallback<ArrayList<PermsRowObject>>() {

			@Override
			public void onSuccess(ArrayList<PermsRowObject> result) {
				if(result != null){
					GlobalResource.getInstance().setPermsPrimaryList(result);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				reloadRenderer(new NoDataFound("Sorry, there was a problem with your request. Please try again later. Thank you."));
			}
		});
	}

	private void initGroupsList(){
		ArrayList<QueryObject> groupsList = new ArrayList<QueryObject>();
		groupsList.add(new QueryObject(QueryEnum.G_ID, null, true, null));
		groupsList.add(new QueryObject(QueryEnum.G_NAME, null, true, null));
		groupsList.add(new QueryObject(QueryEnum.G_STATUS, "A", true, QueryOperatorEnum.EQUALS));
		groupsList.add(new QueryObject(QueryEnum.G_PERMS, null, true, null));


		rpc.getGroupList(groupsList, new AsyncCallback<ArrayList<GroupRowObject>>() {

			@Override
			public void onSuccess(ArrayList<GroupRowObject> result) {
				if(result != null){
					GlobalResource.getInstance().setGroupsList(result);
				}
			}

			@Override
			public void onFailure(Throwable caught) {

			}
		});
	}
	
	private void do2ndinitGroupsListOnUpdate(final GroupRowObject groupObject){
		ArrayList<QueryObject> groupsList = new ArrayList<QueryObject>();
		groupsList.add(new QueryObject(QueryEnum.G_ID, null, true, null));
		groupsList.add(new QueryObject(QueryEnum.G_NAME, null, true, null));
		groupsList.add(new QueryObject(QueryEnum.G_STATUS, "A", true, QueryOperatorEnum.EQUALS));
		groupsList.add(new QueryObject(QueryEnum.G_PERMS, null, true, null));


		rpc.getGroupList(groupsList, new AsyncCallback<ArrayList<GroupRowObject>>() {

			@Override
			public void onSuccess(ArrayList<GroupRowObject> result) {
				if(result != null){
					GlobalResource.getInstance().setGroupsList(result);
					eventBus.fireEvent(new SearchEvent(URLConstants.GROUPS));
					showSuccessAlert(groupObject.getName()+" group updated successfully");
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				showErrorAlert("Sorry, there was a problem with your request. Please try again later. Thank you.");
			}
		});
	}
	
	private void do2ndinitGroupsListOnAdd(final GroupRowObject groupObject){
		ArrayList<QueryObject> groupsList = new ArrayList<QueryObject>();
		groupsList.add(new QueryObject(QueryEnum.G_ID, null, true, null));
		groupsList.add(new QueryObject(QueryEnum.G_NAME, null, true, null));
		groupsList.add(new QueryObject(QueryEnum.G_STATUS, "A", true, QueryOperatorEnum.EQUALS));
		groupsList.add(new QueryObject(QueryEnum.G_PERMS, null, true, null));


		rpc.getGroupList(groupsList, new AsyncCallback<ArrayList<GroupRowObject>>() {

			@Override
			public void onSuccess(ArrayList<GroupRowObject> result) {
				if(result != null){
					GlobalResource.getInstance().setGroupsList(result);
					showSuccessAlert(groupObject.getName()+" group added successfully");
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				showErrorAlert("Sorry, there was a problem with your request. Please try again later. Thank you.");
			}
		});
	}

	private void initBookColumnsList(){
		rpc.getAllBookDataTypesList(new AsyncCallback<ArrayList<BookColumnObject>>() {

			@Override
			public void onSuccess(ArrayList<BookColumnObject> result) {
				if(result != null){
					GlobalResource.getInstance().setBookColumnsList(result);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				initBookColumnsList();
			}
		});

		//CookieVerifier.clearCookie();
	}

	private void setModuleTitle(String title){
		moduleTitleBar.clear();
		rowCounter = new RowCounterWidget();
		searchBox = new SearchBoxWidget(new SearchBoxWidgetEventHandler() {

			@Override
			public void onSearchInvoked(String query) {
				doSearchRendererLoad(query);
			}
		});
		ModuleTitleWidget titleWidget = new ModuleTitleWidget(title);
		titleWidget.load();
		moduleTitleBar.add(titleWidget);
		moduleTitleBar.add(rowCounter);
		moduleTitleBar.add(searchBox);

		setSearchPlaceholder(searchBox);
	}

	private void setSearchPlaceholder(SearchBoxWidget widget){
		switch(currentUrl){
		case ADMINS:
			widget.setPlaceHolder("Please enter email to search");
			break;
		case BOOKS:
			widget.setPlaceHolder("Please enter book name to search");
			break;
		case GROUPS:
			widget.setPlaceHolder("Please enter group name to search");
			break;
		case HELP:
			break;
		case LOGIN:
			break;
		case LOGOUT:
			eventBus.fireEvent(new LogoutEvent());
			break;
		case SETTINGS:
			break;
		case USERS:
			widget.setPlaceHolder("Please enter email to search");
			break;
		case SESSIONS:
			widget.setPlaceHolder("Please enter email to search");
			break;
		default:
			widget.setPlaceHolder("Enter to search");
			break;

		}

	}

	private void showAddButton(boolean isShow){
		if(GlobalResource.getInstance().isCanAdminWrite()){
			if(isShow){
				addButton.getElement().setAttribute("style", "");
			}else{
				addButton.getElement().setAttribute("style", "display:none;");
			}
		}
	}

	public void setModuleTitleHeader(RootPanel moduleTitleBar) {
		this.moduleTitleBar = moduleTitleBar;
	}

	public void setSideBarContainer(RootPanel sideBarContainer){
		this.sidebarContainer = sideBarContainer;
	}
}
