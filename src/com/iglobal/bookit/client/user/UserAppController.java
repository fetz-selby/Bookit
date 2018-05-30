package com.iglobal.bookit.client.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.iglobal.bookit.client.GlobalResource;
import com.iglobal.bookit.client.UserServiceAsync;
import com.iglobal.bookit.client.constants.ClientURLConstants;
import com.iglobal.bookit.client.constants.DBConstants;
import com.iglobal.bookit.client.constants.EntityEnum;
import com.iglobal.bookit.client.constants.TabWidgetTypesEnum;
import com.iglobal.bookit.client.events.ErrorEvent;
import com.iglobal.bookit.client.events.ErrorEventHandler;
import com.iglobal.bookit.client.events.LogoutEvent;
import com.iglobal.bookit.client.events.LogoutEventHandler;
import com.iglobal.bookit.client.events.PageSwitchEvent;
import com.iglobal.bookit.client.events.PageSwitchEvent.AppType;
import com.iglobal.bookit.client.events.PageSwitchEventHandler;
import com.iglobal.bookit.client.events.SuccessEvent;
import com.iglobal.bookit.client.events.SuccessEventHandler;
import com.iglobal.bookit.client.ui.components.CustomRadio;
import com.iglobal.bookit.client.ui.components.CustomRadio.CustomRadioEventHandler;
import com.iglobal.bookit.client.ui.components.CustomWarningPanel;
import com.iglobal.bookit.client.ui.components.ErrorAlert;
import com.iglobal.bookit.client.ui.components.NotificationSideWidget;
import com.iglobal.bookit.client.ui.components.SuccessAlert;
import com.iglobal.bookit.client.user.content.RecordContent;
import com.iglobal.bookit.client.user.content.RecordContent.RecordContentEventHandler;
import com.iglobal.bookit.client.user.content.TransactionContent;
import com.iglobal.bookit.client.user.content.TransactionContent.TransactionContentEventHandler;
import com.iglobal.bookit.client.user.events.AddTabEvent;
import com.iglobal.bookit.client.user.events.AddTabEventHandler;
import com.iglobal.bookit.client.user.events.BookLoadEventHandler;
import com.iglobal.bookit.client.user.events.RecordEvent;
import com.iglobal.bookit.client.user.events.RecordEventHandler;
import com.iglobal.bookit.client.user.events.ReportEvent;
import com.iglobal.bookit.client.user.events.ReportEventHandler;
import com.iglobal.bookit.client.user.events.SideBarULEventHandler;
import com.iglobal.bookit.client.user.events.TransactionEvent;
import com.iglobal.bookit.client.user.events.TransactionEventHandler;
import com.iglobal.bookit.client.user.misc.ReportStepsEnum;
import com.iglobal.bookit.client.user.misc.ReportWizardApp;
import com.iglobal.bookit.client.user.widget.CustomDataListBox;
import com.iglobal.bookit.client.user.widget.CustomDataListBox.CustomDataListBoxEventHandler;
import com.iglobal.bookit.client.user.widget.DOMElementCreator;
import com.iglobal.bookit.client.user.widget.TabWidget;
import com.iglobal.bookit.client.user.widget.TabWidget.TabWidgetEventHandler;
import com.iglobal.bookit.client.utils.CookieVerifier;
import com.iglobal.bookit.client.utils.PageDirector;
import com.iglobal.bookit.client.utils.UserPermissionHandler;
import com.iglobal.bookit.client.utils.Utils;
import com.iglobal.bookit.shared.BookColumnObject;
import com.iglobal.bookit.shared.BookRowObject;
import com.iglobal.bookit.shared.GroupRowObject;
import com.iglobal.bookit.shared.LicenseObject;
import com.iglobal.bookit.shared.QueryEnum;
import com.iglobal.bookit.shared.QueryObject;
import com.iglobal.bookit.shared.QueryOperatorEnum;

public class UserAppController implements ValueChangeHandler<String>{
	//private UserServiceAsync rpc;
	private HandlerManager eventBus;
	//private RootPanel addElement;
	private RootPanel statusPanel;
	//private RootPanel itemsPanel;
	private ClientURLConstants currentUrl;
	private RootPanel contentArea;
	private RootPanel optionPanel;
	private RootPanel listBoxPanel;
	private RootPanel userSpan;
	private RootPanel auxUserSpan;
	private RootPanel tabsPanel;
	private HashMap<String, IsWidget> transactionMap, recordsMap, reportsMap;
	private RootPanel notificationDisplayPanel;
	private RootPanel itemListDiv;
	private HashMap<String, Widget> recordTabContentHash;
	private HashMap<String, Widget> reportTabContentHash;
	private HashMap<String, Widget> transactionTabContentHash;

	private Element saveBtn;
	private Element tabLineElement;
	private int tabCounter;
	private boolean isTabLineShow;
	private RootPanel auxAreaPanel;
	private TransactionContent transactionContent;
	private RecordContent recordContent;
	//private ReportContent reportContent;
	private ReportWizardApp reportWizardApp;
	private Element h3Title;
	private static int counter;
	private UserPermissionHandler permsHandler;
	private RootPanel miscDiv;
	private ArrayList<TabWidget> tabList;
	private LicenseObject licenseObject;
	private HashMap<String, BookRowObject> recordFetcherMap;
	private boolean isLogoShown = true, isReportOpened = false;

	Timer scheduleTimer;
	protected ArrayList<BookRowObject> notificationBooksList;


	public UserAppController(UserServiceAsync rpc, HandlerManager eventBus){
		//this.rpc = rpc;
		this.eventBus = eventBus;
		counter ++;
	}

	private void bind(){
		History.addValueChangeHandler(this);
		History.newItem(ClientURLConstants.TRANSACTION.getUrl());

		transactionTabContentHash = new HashMap<String, Widget>();
		recordTabContentHash = new HashMap<String, Widget>();
		reportTabContentHash = new HashMap<String, Widget>();


		transactionMap = new HashMap<String, IsWidget>();
		recordsMap = new HashMap<String, IsWidget>();
		reportsMap = new HashMap<String, IsWidget>();

		tabList = new ArrayList<TabWidget>();

		eventBus.addHandler(TransactionEvent.TYPE, new TransactionEventHandler() {

			@Override
			public void onTransactionEventInvoke(TransactionEvent event) {
				History.newItem(ClientURLConstants.TRANSACTION.getUrl());
			}
		});

		eventBus.addHandler(RecordEvent.TYPE, new RecordEventHandler() {

			@Override
			public void onRecordEventInvoked(RecordEvent event) {
				History.newItem(ClientURLConstants.RECORDS.getUrl());
			}
		});

		eventBus.addHandler(ReportEvent.TYPE, new ReportEventHandler() {

			@Override
			public void onReportClicked(ReportEvent event) {
				History.newItem(ClientURLConstants.REPORTS.getUrl());
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
				// TODO Auto-generated method stub

			}
		});

		eventBus.addHandler(AddTabEvent.TYPE, new AddTabEventHandler() {

			@Override
			public void onTabAddInvoked(AddTabEvent event) {
				addTab(event.getTabName(), event.getId());
			}
		});

		eventBus.addHandler(LogoutEvent.TYPE, new LogoutEventHandler() {

			@Override
			public void onLogoutInvoked(LogoutEvent event) {
				GlobalResource.getInstance().getLoginRPC().isLogout(GlobalResource.getInstance().getUser().getEmail(),new AsyncCallback<Boolean>() {

					@Override
					public void onSuccess(Boolean result) {
						if(result){
							CookieVerifier.clearCookie();
							eventBus.fireEvent(new PageSwitchEvent(AppType.LOGIN));
						}
					}

					@Override
					public void onFailure(Throwable caught) {
						CookieVerifier.clearCookie();
						eventBus.fireEvent(new PageSwitchEvent(AppType.LOGIN));						
					}
				});

			}
		});

		eventBus.addHandler(PageSwitchEvent.TYPE, new PageSwitchEventHandler() {

			@Override
			public void onPageSwitch(PageSwitchEvent event) {
				// TODO Auto-generated method stub
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

		eventBus.addHandler(ReportEvent.TYPE, new ReportEventHandler() {

			@Override
			public void onReportClicked(ReportEvent event) {

			}
		});

		doModuleCreation();
		doOnPageRefresh();
		initDOMElements();
		createForm();
		doSessionCheck();
		doNotificationInit();
	}
	
	private void doNotificationInit(){
		if(GlobalResource.getInstance().getNotificationGroupList() != null){
		Utils.getAssociatedBooks(GlobalResource.getInstance().getNotificationGroupList(), new BookLoadEventHandler() {
			
			@Override
			public void onLoadComplete(ArrayList<BookRowObject> bookList) {
				if(bookList != null){
					notificationBooksList = new ArrayList<BookRowObject>();
					for(BookRowObject book : bookList){
						if(isNotificationFieldInBook(book)){
							notificationBooksList.add(book);
						}
					}
					
					doNotificationFetchCall();
				}
			}
		});
		}
		
	}
	
	private void doNotificationFetchCall(){
		GlobalResource.getInstance().getUserRPC().getNotificationInfoHash(notificationBooksList, new AsyncCallback<HashMap<String,String>>() {
			
			@Override
			public void onSuccess(HashMap<String, String> result) {
				if(result != null){
					FlowPanel notiContainer = new FlowPanel();
					
					int counter = 0;
					for(String bookName : result.keySet()){
						GWT.log("BookName => "+bookName+", value => "+result.get(bookName));
						if(Integer.parseInt(result.get(bookName)) > 0){
							NotificationSideWidget nsw = new NotificationSideWidget(bookName, Integer.parseInt(result.get(bookName)));
							notiContainer.add(nsw);
							counter ++;
						}
						
					}
					
					showNotificationMessage(notiContainer);
					if(counter > 0){
						showNotiMessage();
					}
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private void showNotificationMessage(FlowPanel notiPanel){
		notificationDisplayPanel.clear();
		notificationDisplayPanel.add(notiPanel);
	}
	
	private void showNotiMessage(){
		Element element = DOM.getElementById("notiLabel");
		element.removeClassName("gwt-hide");
	}
	
	private boolean isNotificationFieldInBook(BookRowObject book){
		//Grep for columnString and split into various columns and datatype
		
		//date:date:5:A:F,is_received:is_received:6:A:F,name:name:2:A:F
		String columnString = book.getColumnString();
		String[] columns = columnString.split("[,]");
		
		//Check for alert datatype
		for(String column : columns){
			String dataTypeId = column.split("[:]")[2];
			String status = column.split("[:]")[3];
			
			if(dataTypeId.equals(DBConstants.ALERT_ID) && status.equals(DBConstants.ACTIVE)){
				return true;
			}
		}
		
		return false;
	}

	private void doSessionCheck(){
		scheduleTimer = new Timer() {

			@Override
			public void run() {
				GlobalResource.getInstance().getUserRPC().isUserSessionActive(GlobalResource.getInstance().getUser().getEmail(), new AsyncCallback<Boolean>() {

					@Override
					public void onSuccess(Boolean result) {

						//Logs systemout if session is terminated.
						if(!result){
							eventBus.fireEvent(new LogoutEvent());
							scheduleTimer.cancel();
						}
					}

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}
				});
			}
		};

		scheduleTimer.scheduleRepeating(30*1000);
		scheduleTimer.run();
	}

	private void initDOMElements(){
		com.google.gwt.user.client.Element userSpanElement = userSpan.getElement().cast();
		userSpanElement.setInnerText(GlobalResource.getInstance().getUser().getName());

		com.google.gwt.user.client.Element auxUserSpanElement = auxUserSpan.getElement().cast();
		auxUserSpanElement.setInnerText(GlobalResource.getInstance().getUser().getName());

		auxAreaPanel.getElement().setId("sels_auxPanel_"+counter);
	}

	private void clearAuxPanel(){
		auxAreaPanel.getElement().setInnerText("");
	}

	private void doOnPageRefresh(){
		if(History.getToken().trim().isEmpty()){
			eventBus.fireEvent(new TransactionEvent());
			History.fireCurrentHistoryState();
		}else{
			History.fireCurrentHistoryState();
		}
	}

	private void doDynamicCheckOfModule(CustomRadio transactionRadio, CustomRadio recordsRadio){
		if(History.getToken().trim().equals("records")){
			recordsRadio.setActive(true);
		}else if(History.getToken().trim().equals("transactions")){
			transactionRadio.setActive(true);
		}else{
			transactionRadio.setActive(true);
		}
	}

	private void addTab(String tabName, String tabId){
		// TODO add tab implementation
	}

	private IsWidget getTransactionContent(String tabId){
		if(transactionMap.containsKey(tabId)){
			transactionContent = (TransactionContent) transactionMap.get(tabId);
			return transactionMap.get(tabId);
		}

		return null;
	}

	private IsWidget getRecordContent(String tabId){
		if(recordsMap.containsKey(tabId)){
			GWT.log("Got record!");
			return recordsMap.get(tabId);
		}

		GWT.log("Was null record!");
		return null;
	}

	private IsWidget getReportContent(String tabId){
		if(reportsMap.containsKey(tabId)){
			reportWizardApp = (ReportWizardApp) reportsMap.get(tabId);
			return reportsMap.get(tabId);
		}

		return null;
	}

	private void doModuleCreation(){
		CustomRadio transactionRadio = new CustomRadio("transaction", "trnsct", "module");
		CustomRadio recordRadio = new CustomRadio("record", "rcrd", "module");
		CustomRadio reportRadio = new CustomRadio("report", "rpt", "module");

		optionPanel.clear();

		optionPanel.add(reportRadio);
		optionPanel.add(transactionRadio);
		optionPanel.add(recordRadio);

		UserReportGlobalUI.getInstance().setAuxPanel(auxAreaPanel);
		UserReportGlobalUI.getInstance().setH3TitleElement(h3Title);

		transactionRadio.setCustomRadioEventHandler(new CustomRadioEventHandler() {

			@Override
			public void onRadioClicked(String radioValue) {
				eventBus.fireEvent(new TransactionEvent());
			}
		});

		recordRadio.setCustomRadioEventHandler(new CustomRadioEventHandler() {

			@Override
			public void onRadioClicked(String radioValue) {
				eventBus.fireEvent(new RecordEvent());
			}
		});

		reportRadio.setCustomRadioEventHandler(new CustomRadioEventHandler() {

			@Override
			public void onRadioClicked(String radioValue) {
				eventBus.fireEvent(new ReportEvent());
			}
		});

		doDynamicCheckOfModule(transactionRadio, recordRadio);
	}

	private void removeTransactionTabContent(String tabId){
		if(transactionTabContentHash.containsKey(tabId)){
			transactionTabContentHash.remove(tabId);
		}
	}

	private void removeRecordTabContent(String tabId){
		if(recordTabContentHash.containsKey(tabId)){
			recordTabContentHash.remove(tabId);
		}
	}

	private void removeReportTabContent(String tabId){
		if(reportTabContentHash.containsKey(tabId)){
			reportTabContentHash.remove(tabId);
		}
	}

	private void setContent(IsWidget widget){
		contentArea.clear();
		if(widget != null){
			contentArea.add(widget);
		}
	}

	private void showSuccessAlert(String message){
		statusPanel.clear();
		statusPanel.add(new SuccessAlert(message));
	}

	private void showContentPanelLoading(){
		//Window.alert("Show");
		SimplePanel panel = new SimplePanel();
		panel.getElement().setAttribute("style", "height: 50px;");
		
		Label loading = new Label("Loading ...");
		loading.addStyleName("loading-style");
		
		panel.add(loading);
		contentArea.clear();
		contentArea.add(panel);
	}

	private void initBooksListBox(){
		CustomDataListBox listBox = new CustomDataListBox(GlobalResource.getInstance().getUser().getId(), EntityEnum.BOOKS, GlobalResource.getInstance().getUser().getPermsList());
		listBox.load();

		listBox.setCustomDataListBoxEventHandler(new CustomDataListBoxEventHandler() {

			@Override
			public void onItemSelected(String value, String itemName) {
				if(tabCounter + 1  > Integer.parseInt(licenseObject.getNumberOfTabs())){
					CustomWarningPanel warning = new CustomWarningPanel("Your have reached your maximum tab limit. Please close unused tabs to continue.");
					warning.show();
					return;
				}
				
				if(isLogoShown){
					hideLogo();
					isLogoShown = false;
				}
				
				GWT.log("[came] value, "+value+", name "+itemName);
				if(transactionMap.containsKey(value)){
					return;
				}

				clearAuxPanel();
				showContentPanelLoading();
				setTransactionContent(value);
				showTabTitle(itemName);
				showSaveBtn(true);
				showThumbPrint(true);
			}
		});

		listBoxPanel.clear();
		listBoxPanel.add(listBox);
	}

	private void removeAllReportTab(){
		for(String key : reportTabContentHash.keySet()){
			reportTabContentHash.get(key).removeFromParent();
		}
		reportsMap.clear();
	}

	private void showRecordsBooksItemList(boolean isShow){
		if(isShow){
			DOMElementCreator elementCreator = new DOMElementCreator();
			elementCreator.initSideBarUL(GlobalResource.getInstance().getUser().getPermsList(), new SideBarULEventHandler() {

				@Override
				public void onItemRenderComplete(Element ulElement, ArrayList<BookRowObject> bookList) {
					initRecordFetcherHashMap(bookList);
					
					final ListBox recordListBox = getBookListBox();
					
					DOM.sinkEvents(recordListBox.getElement(), Event.ONCHANGE);
					DOM.setEventListener(recordListBox.getElement(), new EventListener() {
						
						@Override
						public void onBrowserEvent(Event event) {
							doRecordOnClick(recordFetcherMap.get(recordListBox.getValue(recordListBox.getSelectedIndex())));
						}
					});
					
					//listBoxPanel.getElement().setInnerText("");
					//listBoxPanel.clear();
					itemListDiv.clear();
					itemListDiv.getElement().setInnerText("");
					itemListDiv.getElement().appendChild(recordListBox.getElement());
					//listBoxPanel.getElement().appendChild(recordListBox.getElement());
				}

				@Override
				public void onItemClicked(String itemId, String itemName, String columnString) {
					
					if(tabCounter + 1  > Integer.parseInt(licenseObject.getNumberOfTabs())){
						CustomWarningPanel warning = new CustomWarningPanel("Your have reached your maximum tab limit. Please close unused tabs to continue.");
						warning.show();
						return;
					}
					
					if(recordsMap.containsKey(itemId)){
						return;
					}
					
					clearAuxPanel();
					showNoPreview(true);
					showContentPanelLoading();
					setRecordContent(itemId, itemName, columnString);
					showSaveBtn(false);
				}
			});
		}else{
			itemListDiv.getElement().setInnerText("");
		}

	}
	
	private void initRecordFetcherHashMap(ArrayList<BookRowObject> bookList){
		recordFetcherMap = new HashMap<String, BookRowObject>();
		for(BookRowObject obj : bookList){
			recordFetcherMap.put(obj.getId(), obj);
			GWT.log("populating id [] "+obj.getId());
		}
	}
	
	private ListBox getBookListBox(){
		TreeSet<String> set = new TreeSet<String>();
		HashMap<String, String> matcherMap = new HashMap<String, String>();

		if(recordFetcherMap != null){
			for(String key : recordFetcherMap.keySet()){
				BookRowObject obj = recordFetcherMap.get(key);
				String bookName = obj.getName().split(":")[1].trim();
				
				
				if(set.add(bookName)){
					matcherMap.put(bookName, obj.getId());
				}
				
			}
			
			ListBox listBox = new ListBox();
			listBox.addItem("");
			for(String itemName : set){
				String id = matcherMap.get(itemName);
				listBox.addItem(itemName, id);
			}
			
			listBox.addStyleName("form-control");
			
			if(listBox.getItemCount() == 1){
				listBox.clear();
				listBox.addItem("-- empty --");
			}
			
			return listBox;
		}
		
		GWT.log("--- List was null ---");
		return null;
	}

	private void showReportsBooksItemList(boolean isShow){
		if(isShow){
			DOMElementCreator elementCreator = new DOMElementCreator();
			elementCreator.initSideBarUL(GlobalResource.getInstance().getUser().getPermsList(), new SideBarULEventHandler() {

				@Override
				public void onItemRenderComplete(Element ulElement, ArrayList<BookRowObject> bookList) {
					initRecordFetcherHashMap(bookList);
					
					final ListBox reportsListBox = getBookListBox();
					DOM.sinkEvents(reportsListBox.getElement(), Event.ONCHANGE);
					DOM.setEventListener(reportsListBox.getElement(), new EventListener() {
						
						@Override
						public void onBrowserEvent(Event event) {
							doReportsOnClick(recordFetcherMap.get(reportsListBox.getValue(reportsListBox.getSelectedIndex())));
						}
					});
					
					//listBoxPanel.getElement().setInnerText("");
					//listBoxPanel.clear();
					itemListDiv.clear();
					itemListDiv.getElement().setInnerText("");
					itemListDiv.getElement().appendChild(reportsListBox.getElement());
					//listBoxPanel.getElement().appendChild(reportsListBox.getElement());
					
				}

				@Override
				public void onItemClicked(String itemId, String itemName, String columnString) {
					if(tabCounter + 1  > Integer.parseInt(licenseObject.getNumberOfTabs())){
						CustomWarningPanel warning = new CustomWarningPanel("Your have reached your maximum tab limit. Please close unused tabs to continue.");
						warning.show();
						return;
					}
					
					clearAuxPanel();
					showNoPreview(true);
					showContentPanelLoading();
					removeAllReportTab();
					
					setReportContent(itemId, itemName);
					showSaveBtn(false);
				}
			});
		}else{
			itemListDiv.getElement().setInnerText("");
		}

	}
	
	private void doRecordOnClick(BookRowObject bookObject){
		
		if(tabCounter + 1  > Integer.parseInt(licenseObject.getNumberOfTabs())){
			CustomWarningPanel warning = new CustomWarningPanel("Your have reached your maximum tab limit. Please close unused tabs to continue.");
			warning.show();
			return;
		}
		
		GWT.log("record id is "+bookObject.getId());
		
		if(recordsMap.containsKey(bookObject.getId())){
			return;
		}
		
		if(isLogoShown){
			hideLogo();
			isLogoShown = false;
		}
		
		clearAuxPanel();
		showNoPreview(true);
		showContentPanelLoading();
		setRecordContent(bookObject.getId(), bookObject.getName().split("[:]")[1], bookObject.getColumnString());
		showSaveBtn(false);
	}

	private void doReportsOnClick(BookRowObject bookObject){
		if(tabCounter + 1  > Integer.parseInt(licenseObject.getNumberOfTabs())){
			CustomWarningPanel warning = new CustomWarningPanel("Your have reached your maximum tab limit. Please close unused tabs to continue.");
			warning.show();
			return;
		}
		
		if(isLogoShown){
			hideLogo();
			isLogoShown = false;
		}
		
		clearAuxPanel();
		showNoPreview(true);
		showContentPanelLoading();
		removeAllReportTab();
		
		setReportContent(bookObject.getId(), bookObject.getName().split("[:]")[1]);
		showSaveBtn(false);
	}
	
	private void setTransactionContent(final String bookId){

		if(transactionMap.containsKey(bookId)){
			transactionContent = (TransactionContent)transactionMap.get(bookId);
			contentArea.clear();
			contentArea.add(transactionContent);

		}else{
			transactionContent = new TransactionContent(bookId);
			transactionContent.setTransactionContentEventHandler(new TransactionContentEventHandler() {

				@Override
				public void onWidgetLoadComplete(String tableName, String columnString) {
					addTransactionTab(bookId, tableName, columnString);
				}

				@Override
				public void onSaveSuccessful(String bookName) {
					showSuccessMessage(bookName);
				}

				@Override
				public void onSaveFailure() {
					showErrorMessage();
				}
			});
			transactionContent.load();
			contentArea.clear();
			contentArea.add(transactionContent);
			//Add to transactionMap
			transactionMap.put(transactionContent.getBookId(), transactionContent);
		}

	}

	private void setRecordContent(final String bookId, final String itemName, final String columnString){
		if(recordsMap.containsKey(bookId)){
			recordContent = (RecordContent)recordsMap.get(bookId);
			contentArea.clear();
			contentArea.add(recordContent);
		}else{
			recordContent = new RecordContent(bookId, auxAreaPanel, permsHandler);
			recordContent.setRecordContentEventHandler(new RecordContentEventHandler() {

				@Override
				public void onFieldLoadComplete() {
					addRecordTab(bookId, itemName, columnString);
				}
			});
			recordContent.load();
			contentArea.clear();
			contentArea.add(recordContent);
			recordsMap.put(bookId, recordContent);
		}

		showTabTitle(itemName);
	}

	private void setReportContent(final String bookId, final String itemName){
		if(reportsMap.containsKey(bookId)){
			reportWizardApp = (ReportWizardApp)reportsMap.get(bookId);
			contentArea.clear();
			contentArea.add(reportWizardApp);
		}else{
			//reportContent = new ReportContent(bookId, itemName, auxAreaPanel, permsHandler);
			reportWizardApp = new ReportWizardApp();
			//reportWizardApp.setBookId(bookId);
			//reportWizardApp.setBookName(itemName);

			UserReportGlobalUI.getInstance().setBookId(bookId);
			UserReportGlobalUI.getInstance().setBookName(itemName);

			reportWizardApp.runAppNext(ReportStepsEnum.BOOK_STAGE);

			addReportTab(bookId, itemName);
			
			if(tabCounter + 1  > Integer.parseInt(licenseObject.getNumberOfTabs())){
				return;
			}

			//reportContent.load();
			contentArea.clear();
			contentArea.add(reportWizardApp);
			reportsMap.put(bookId, reportWizardApp);
		}

		showTabTitle("");
	}
	
	private void showErrorMessage(){
		statusPanel.clear();
		statusPanel.add(new ErrorAlert(null));
	}

	private void showSuccessMessage(String bookName){
		statusPanel.clear();
		statusPanel.add(new SuccessAlert(bookName+", successfully updated."));
	}

	private void removeTransactionContent(String bookId){
		if(transactionMap.containsKey(bookId)){
			transactionMap.remove(bookId);
		}
	}

	private void removeRecordContent(String bookId){
		if(recordsMap.containsKey(bookId)){
			recordsMap.remove(bookId);
		}
	}

	private void removeReportContent(String bookId){
		if(reportsMap.containsKey(bookId)){
			reportsMap.remove(bookId);
		}
	}

	private void addTransactionTab(String bookId, final String title, String columnString){
		if(tabCounter + 1 > Integer.parseInt(licenseObject.getNumberOfTabs())){
			CustomWarningPanel warning = new CustomWarningPanel("Your have reached your maximum tab limit. Please close unused tabs to continue.");
			warning.show();
			return;
		}

		final TabWidget tab = new TabWidget(bookId, title, columnString, TabWidgetTypesEnum.TRANSACTIONS);
		tab.setTabWidgetEventHandler(new TabWidgetEventHandler() {

			@Override
			public void onTabWigetCloseClicked(String bookId) {

				-- tabCounter;
				removeTransactionTabContent(bookId);

				tab.removeFromParent();
				removeTransactionContent(bookId);

				contentArea.clear();
				doTabLineShow();
				showTabTitle("");
				fireNextAvailableTab(tab);

				if(tabCounter == 0 && !isReportOpened){
					isLogoShown = true;
					showLogo();
				}
				
			}

			@Override
			public void onTabWigetClicked(String bookId, String columnString, TabWidget widget) {
				setContent(getTransactionContent(bookId));
				deSelectAllTabs(tab);
				showTabTitle(tab.getTabTitle());
				showSaveBtn(true);
				clearAuxPanel();
				showThumbPrint(true);
			}
		});

		transactionTabContentHash.put(bookId, tab);
		++ tabCounter;

		tabList.add(tab);
		tabsPanel.add(tab);

		//Setting tab active
		tab.setActive(true);
		deSelectAllTabs(tab);

		doTabLineShow();
	}

	private void addRecordTab(String bookId, final String title, String columnString){

		//Check tab limit
		if(tabCounter + 1 > Integer.parseInt(licenseObject.getNumberOfTabs())){
			CustomWarningPanel warning = new CustomWarningPanel("Your have reached your maximum tab limit. Please close unused tabs to continue.");
			warning.show();
			return;
		}

		final TabWidget tab = new TabWidget(bookId, title, columnString, TabWidgetTypesEnum.RECORDS);
		tab.setTabWidgetEventHandler(new TabWidgetEventHandler() {

			@Override
			public void onTabWigetCloseClicked(String bookId) {
				-- tabCounter;
				removeRecordTabContent(bookId);
				tab.removeFromParent();
				removeRecordContent(bookId);
				contentArea.clear();	

				doTabLineShow();
				showTabTitle("");
				fireNextAvailableTab(tab);
				
				if(tabCounter == 0 && !isReportOpened){
					isLogoShown = true;
					showLogo();
				}
			}

			@Override
			public void onTabWigetClicked(String bookId, String columnString, TabWidget widget) {
				setContent(getRecordContent(bookId));
				deSelectAllTabs(tab);
				showTabTitle(title);
				showSaveBtn(false);
				clearAuxPanel();
				showNoPreview(true);
			}
		});

		recordTabContentHash.put(bookId, tab);
		++ tabCounter;

		tabList.add(tab);
		tabsPanel.add(tab);

		//Setting tab active
		tab.setActive(true);
		deSelectAllTabs(tab);

		doTabLineShow();
	}

	private void addReportTab(String bookId, final String title){
		if(tabCounter + 1  > Integer.parseInt(licenseObject.getNumberOfTabs())){
			CustomWarningPanel warning = new CustomWarningPanel("Your have reached your maximum tab limit. Please close unused tabs to continue.");
			warning.show();
			return;
		}

		final TabWidget tab = new TabWidget(bookId, "Report", null, TabWidgetTypesEnum.REPORTS);
		tab.setTabWidgetEventHandler(new TabWidgetEventHandler() {

			@Override
			public void onTabWigetCloseClicked(String bookId) {
				//-- tabCounter;
				isReportOpened = false;
				removeReportTabContent(bookId);
				tab.removeFromParent();
				removeReportContent(bookId);
				contentArea.clear();	

				doTabLineShow();
				showTabTitle("");
				fireNextAvailableTab(tab);
				
				if(tabCounter == 0 && !isReportOpened){
					isLogoShown = true;
					showLogo();
				}
			}

			@Override
			public void onTabWigetClicked(String bookId, String columnString, TabWidget widget) {
				setContent(getReportContent(bookId));
				deSelectAllTabs(tab);
				showTabTitle("");
				showSaveBtn(false);
				clearAuxPanel();
				showNoPreview(true);
			}
		});

		reportTabContentHash.put(bookId, tab);
		//++ tabCounter;

		tabList.add(tab);
		tabsPanel.add(tab);
		isReportOpened = true;

		//Setting tab active
		tab.setActive(true);
		deSelectAllTabs(tab);

		doTabLineShow();
	}

	private void fireNextAvailableTab(TabWidget tab){
		if(tabList.contains(tab)){
			int position = tabList.indexOf(tab);
			if(position > 0){
				tabList.get(position - 1).fireOnTabWidgetClick();
			}else if(position == 0){
				//Check if other tab exist
				if(tabList.size() == 1){
					tabList.get(0).fireOnTabWidgetClick();
				}else if(tabList.size() > 1){
					tabList.get(1).fireOnTabWidgetClick();
				}
			}
			tabList.remove(tab);
		}
	}

	private void showTabTitle(String title){
		h3Title.setInnerText(title);
	}

	private void showNoPreview(boolean isShow){
		if(isShow){
			Element noPreview = new DOMElementCreator().getNoPreview();
			auxAreaPanel.getElement().setInnerText("");
			auxAreaPanel.getElement().appendChild(noPreview);
		}else{
			auxAreaPanel.getElement().setInnerText("");
		}
	}

	private void showThumbPrint(boolean isShow){
		if(isShow){
			Element thumb = new DOMElementCreator().getThumbPrint();
			auxAreaPanel.getElement().setInnerText("");
			auxAreaPanel.getElement().appendChild(thumb);
		}else{
			auxAreaPanel.getElement().setInnerText("");
		}
	}

	private void doTabLineShow(){
		if(tabCounter <= 0){
			tabLineElement.removeClassName("tab-line-active");
			isTabLineShow = false;
			showThumbPrint(false);

		}else if(tabCounter > 0 && !isTabLineShow){
			tabLineElement.addClassName("tab-line-active");
			isTabLineShow = true;
			//showThumbPrint(true);
		}
	}

	private void deSelectAllTabs(TabWidget widget){
		for(String key : transactionTabContentHash.keySet()){
			TabWidget tabWidget = (TabWidget) transactionTabContentHash.get(key);
			if(widget != tabWidget){
				tabWidget.setActive(false);
			}
		}
		for(String key : recordTabContentHash.keySet()){
			TabWidget tabWidget = (TabWidget) recordTabContentHash.get(key);
			if(widget != tabWidget){
				tabWidget.setActive(false);
			}
		}

		for(String key : reportTabContentHash.keySet()){
			TabWidget tabWidget = (TabWidget) reportTabContentHash.get(key);
			if(widget != tabWidget){
				tabWidget.setActive(false);
			}
		}
	}

	private void createForm(){
		FormPanel formPanel = new FormPanel();
		miscDiv.clear();
		miscDiv.add(formPanel);

		Utils.setGlobalForm(formPanel);
	}
	
	private void hideLogo(){
		Element logoSection = DOM.getElementById("welcomeSection");
		Element displaySection = DOM.getElementById("userPanel");
		
		displaySection.removeClassName("gwt-hide");
		logoSection.addClassName("gwt-hide");
	}
	
	private void showLogo(){
		Element logoSection = DOM.getElementById("welcomeSection");
		Element displaySection = DOM.getElementById("userPanel");
		
		displaySection.addClassName("gwt-hide");
		logoSection.removeClassName("gwt-hide");
	}

	public void setAddButtonElement(RootPanel addElement){
		//this.addElement = addElement;
	}

	public void setStatusPanel(RootPanel statusPanel){
		this.statusPanel = statusPanel;
	}

	public void setItemListPanel(RootPanel itemsPanel){
		//this.itemsPanel = itemsPanel;
	}

	public void setContentArea(RootPanel contentArea){
		this.contentArea = contentArea;
		UserReportGlobalUI.getInstance().setRootPanel(contentArea);
	}

	public void setModuleOptionWidget(RootPanel optionPanel){
		this.optionPanel = optionPanel;
	}

	public void setListBoxPanel(RootPanel listBoxPanel){
		this.listBoxPanel = listBoxPanel;
	}

	public void setNotificationDisplayPanel(RootPanel historyDisplayPanel){
		this.notificationDisplayPanel = historyDisplayPanel;
	}

	public void load(){
		GWT.log("In Load!");
		doDataTypeCallBackCall();
	}

	private void doDataTypeCallBackCall(){
		GlobalResource.getInstance().getAdminRPC().getAllBookDataTypesList(new AsyncCallback<ArrayList<BookColumnObject>>() {

			@Override
			public void onSuccess(ArrayList<BookColumnObject> result) {
				if(result != null){
					GlobalResource.getInstance().setBookColumnsList(result);
					
					initGroupsList();
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void initGroupsList(){
		ArrayList<QueryObject> groupsList = new ArrayList<QueryObject>();
		groupsList.add(new QueryObject(QueryEnum.G_ID, null, true, null));
		groupsList.add(new QueryObject(QueryEnum.G_NAME, null, true, null));
		groupsList.add(new QueryObject(QueryEnum.G_STATUS, "A", true, QueryOperatorEnum.EQUALS));
		groupsList.add(new QueryObject(QueryEnum.G_PERMS, null, true, null));


		GlobalResource.getInstance().getAdminRPC().getGroupList(groupsList, new AsyncCallback<ArrayList<GroupRowObject>>() {

			@Override
			public void onSuccess(ArrayList<GroupRowObject> result) {
				if(result != null){
					GlobalResource.getInstance().setGroupsList(result);
					bind();

					doGetLicenseDetails();
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void doGetLicenseDetails(){
		GlobalResource.getInstance().getUserRPC().getLicenseObject(new AsyncCallback<LicenseObject>() {

			@Override
			public void onSuccess(LicenseObject result) {
				licenseObject = result;
				doModuleCreation();	
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}
		});
	}
	
	private void hideItemListDiv(boolean isHide){
		if(isHide){
			itemListDiv.addStyleName("gwt-hide");
			listBoxPanel.removeStyleName("gwt-hide");
		}else{
			itemListDiv.removeStyleName("gwt-hide");
			listBoxPanel.addStyleName("gwt-hide");
		}
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		String token = event.getValue().trim();
		currentUrl = Utils.getClientURLObject(token);	

		switch(currentUrl){
		case HELP:
			break;
		case LOGOUT:
			eventBus.fireEvent(new LogoutEvent());
			break;
		case RECORDS:
			hideItemListDiv(false);
			showRecordsBooksItemList(true);
			break;
		case SETTINGS:
			break;
		case TRANSACTION:
			//showBooksListBox(true);
			//showRecordsBooksItemList(false);
			hideItemListDiv(true);
			initBooksListBox();
			break;
		case REPORTS:
			//showBooksListBox(false);
			hideItemListDiv(false);
			showReportsBooksItemList(true);
			break;
		default:
			break;

		}
	}

	public void setTabsPanel(RootPanel tabsPanel) {
		this.tabsPanel = tabsPanel;
	}

	public void setItemListDiv(RootPanel itemListDiv) {
		this.itemListDiv = itemListDiv;
	}

	public void setSaveButtonId(String string) {
		saveBtn = DOM.getElementById(string);
		DOM.sinkEvents(saveBtn, Event.ONCLICK);
		DOM.setEventListener(saveBtn, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				transactionContent.saveFeed();
			}
		});
	}

	private void showSaveBtn(boolean isShow){
		if(isShow){
			saveBtn.removeClassName("not-show");
		}else{
			saveBtn.addClassName("not-show");
		}
	}

	public void getTitleDisplayId(String id){
		h3Title = DOM.getElementById(id);
	}

	public void setTabLineId(String string) {
		tabLineElement = DOM.getElementById(string);
	}

	public void setAuxArea(RootPanel auxAreaPanel) {
		this.auxAreaPanel = auxAreaPanel;
	}

	public void setUserName(RootPanel userSpan){
		this.userSpan = userSpan;
	}

	public void setAuxUserName(RootPanel auxUserSpan){
		this.auxUserSpan = auxUserSpan;
	}

	public void setMiscDiv(RootPanel rootPanel) {
		this.miscDiv = rootPanel;
	}

}
