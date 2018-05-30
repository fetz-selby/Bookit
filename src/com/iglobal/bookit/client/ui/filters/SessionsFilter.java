package com.iglobal.bookit.client.ui.filters;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.Widget;
import com.iglobal.bookit.client.GlobalResource;
import com.iglobal.bookit.client.parents.Filter;
import com.iglobal.bookit.shared.QueryEnum;
import com.iglobal.bookit.shared.QueryObject;
import com.iglobal.bookit.shared.QueryOperatorEnum;

public class SessionsFilter extends Composite implements Filter{

	private String query;
	private MultiWordSuggestOracle oracle;
	private SuggestBox suggest;
	private HashMap<String, String> oracleMap;
	private static SessionsFilterUiBinder uiBinder = GWT
			.create(SessionsFilterUiBinder.class);

	interface SessionsFilterUiBinder extends UiBinder<Widget, SessionsFilter> {
	}

	@UiField LabelElement label;
	@UiField SimplePanel searchContainer;
	
	public SessionsFilter() {
		initWidget(uiBinder.createAndBindUi(this));
		initSearchContainer();
	}
	
	private void initSearchContainer(){
		GlobalResource.getInstance().getAdminRPC().getAllLoggedInUsers(new AsyncCallback<HashMap<String, String>>() {
			
			@Override
			public void onSuccess(HashMap<String, String> result) {
				if(result != null){
					oracle = new MultiWordSuggestOracle();
					oracleMap = new HashMap<String, String>();
					
					for(String key : result.keySet()){
						oracle.add(result.get(key));
						oracleMap.put(result.get(key), key);
					}
					
					suggest = new SuggestBox(oracle);
					suggest.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
						
						@Override
						public void onSelection(SelectionEvent<Suggestion> event) {
							query = event.getSelectedItem().getReplacementString();
						}
					});
					suggest.getTextBox().setStyleName("form-control");
					//searchContainer.setWidget(suggest);
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		} );
	}

	@Override
	public ArrayList<QueryObject> getFilterQuery() {
		ArrayList<QueryObject> queryList = new ArrayList<QueryObject>();

		//queryList.add(new QueryObject(QueryEnum.U_NAME, null, true, null));
		queryList.add(new QueryObject(QueryEnum.LOGIN_START_TIME, null, true, null));
		queryList.add(new QueryObject(QueryEnum.LOGIN_END_TIME, null, true, null));

		queryList.add(new QueryObject(QueryEnum.LOGIN_STATUS, "A", true, null));
		
		QueryObject loggedEmail = null;
		
		if(query != null){
			loggedEmail = new QueryObject(QueryEnum.LOGIN_EMAIL, query, true, QueryOperatorEnum.LIKE);
		}else{
			loggedEmail = new QueryObject(QueryEnum.LOGIN_EMAIL, null, true, null);
		}
		loggedEmail.setOrderField(QueryEnum.LOGIN_EMAIL.getField());
		
		queryList.add(loggedEmail);

		return queryList;
	}

	@Override
	public ArrayList<QueryObject> getSearchFilterQuery(String query) {
		ArrayList<QueryObject> queryList = new ArrayList<QueryObject>();

		//queryList.add(new QueryObject(QueryEnum.U_NAME, null, true, null));
		queryList.add(new QueryObject(QueryEnum.LOGIN_START_TIME, null, true, null));
		queryList.add(new QueryObject(QueryEnum.LOGIN_END_TIME, null, true, null));

		queryList.add(new QueryObject(QueryEnum.LOGIN_STATUS, "A", true, null));
		
		QueryObject loggedEmail = null;
		
		if(query != null){
			loggedEmail = new QueryObject(QueryEnum.LOGIN_EMAIL, query, true, QueryOperatorEnum.LIKE);
		}else{
			loggedEmail = new QueryObject(QueryEnum.LOGIN_EMAIL, null, true, QueryOperatorEnum.LIKE);
		}
		loggedEmail.setOrderField(QueryEnum.LOGIN_EMAIL.getField());
		
		queryList.add(loggedEmail);

		return queryList;
	}

}
