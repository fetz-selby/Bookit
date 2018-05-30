package com.iglobal.bookit.client.ui.components;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;
import com.iglobal.bookit.client.GlobalResource;
import com.iglobal.bookit.client.constants.EntityEnum;
import com.iglobal.bookit.shared.AdminRowObject;
import com.iglobal.bookit.shared.GroupRowObject;
import com.iglobal.bookit.shared.PermsRowObject;
import com.iglobal.bookit.shared.QueryEnum;
import com.iglobal.bookit.shared.QueryObject;
import com.iglobal.bookit.shared.QueryOperatorEnum;
import com.iglobal.bookit.shared.UserRowObject;

public class CustomSuggestBox extends Composite {

	private String label = "Group";
	private MultiWordSuggestOracle oracle;
	private SuggestBox suggest;
	private HashMap<String, String> oracleMap;
	private CustomSuggestBoxEventHandler handler;

	private static CustomSuggestBoxUiBinder uiBinder = GWT
			.create(CustomSuggestBoxUiBinder.class);

	interface CustomSuggestBoxUiBinder extends UiBinder<Widget, CustomSuggestBox> {
	}

	public interface CustomSuggestBoxEventHandler{
		void onSuggestSelected(String suggestWord, String value, TextBoxBase textBoxBase);
	}
	
	@UiField SimplePanel suggestContainer;
	@UiField LabelElement labelName;
	
	public CustomSuggestBox() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public SuggestOracle getOracle() {
		return oracle;
	}
	
	public HashMap<String, String> getOracleMap() {
		return oracleMap;
	}

	public void setOracleMap(HashMap<String, String> oracleMap) {
		this.oracleMap = oracleMap;
	}

	public void load(){
		//Create oracle from hash
		labelName.setInnerText(label);
		//GlobalResource.getInstance().getGroupList();
		
		if(oracleMap == null){
			suggest = new SuggestBox();
			initSuggestBox();
			
			return;
		}
		
		oracle = new MultiWordSuggestOracle();
		for(String key : oracleMap.keySet()){
			oracle.add(key);
		}
		
		if(oracle != null){
			suggest = new SuggestBox(oracle);
		}else{
			suggest = new SuggestBox();
		}
		
		initSuggestBox();
		initSuggestEvent();
	}
	
	public void load(EntityEnum entity){
		if(entity == null){
			return;
		}
		labelName.setInnerText(label);

		switch(entity){
		case ADMINS:
			doAdminsLoading();
			break;
		case BOOKS:
			doBooksLoading();
			break;
		case GROUPS:
			doGroupsLoading();
			break;
		case USERS:
			doUsersLoading();
			break;
		case PERMS:
			doPermsLoading();
			break;
		default:
			break;
		
		}
	}
	
	private void doBooksLoading() {
		// TODO Auto-generated method stub
		
	}

	private void doGroupsLoading(){
		ArrayList<QueryObject> queryList = new ArrayList<QueryObject>();
		
		queryList.add(new QueryObject(QueryEnum.G_ID, null, true, null));
		queryList.add(new QueryObject(QueryEnum.G_NAME, null, true, null));
		queryList.add(new QueryObject(QueryEnum.G_PERMS, null, true, null));
		queryList.add(new QueryObject(QueryEnum.G_STATUS, "A", true, QueryOperatorEnum.EQUALS));
		
		GlobalResource.getInstance().getAdminRPC().getGroupList(queryList, new AsyncCallback<ArrayList<GroupRowObject>>() {
			
			@Override
			public void onSuccess(ArrayList<GroupRowObject> result) {
				if(result != null){

					oracleMap = new HashMap<String, String>();
					oracle = new MultiWordSuggestOracle();
					
					
					String str = "";
					for(GroupRowObject object : result){
//						oracleMap.put(object.getName(), object.getId());
//						oracle.add(object.getName());
						
						str = object.getName()+"("+object.getPermedName()+")";
						
						oracleMap.put(str, object.getId());
						oracle.add(str);

					}
					
					initSuggestBox();
					initSuggestEvent();
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				GWT.log("Failed to load groups");

			}
		});
	}
	
	private void doUsersLoading(){
		ArrayList<QueryObject> queryList = new ArrayList<QueryObject>();
		
		queryList.add(new QueryObject(QueryEnum.U_ID, null, true, null));
		queryList.add(new QueryObject(QueryEnum.U_NAME, null, true, null));
		queryList.add(new QueryObject(QueryEnum.U_STATUS, "A", true, QueryOperatorEnum.EQUALS));
		
		GlobalResource.getInstance().getAdminRPC().getUserList(queryList, new AsyncCallback<ArrayList<UserRowObject>>() {
			
			@Override
			public void onSuccess(ArrayList<UserRowObject> result) {
				if(result != null){

					oracleMap = new HashMap<String, String>();
					oracle = new MultiWordSuggestOracle();
					
					for(UserRowObject object : result){
						oracleMap.put(object.getName(), object.getId());
						oracle.add(object.getName());

					}
					
					initSuggestBox();
					initSuggestEvent();
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				GWT.log("Failed to load users");

			}
		});
	}
	
	private void doAdminsLoading(){
		ArrayList<QueryObject> queryList = new ArrayList<QueryObject>();
		
		queryList.add(new QueryObject(QueryEnum.A_ID, null, true, null));
		queryList.add(new QueryObject(QueryEnum.A_NAME, null, true, null));
		queryList.add(new QueryObject(QueryEnum.A_STATUS, "A", true, QueryOperatorEnum.EQUALS));
		
		GlobalResource.getInstance().getAdminRPC().getAdminList(queryList, new AsyncCallback<ArrayList<AdminRowObject>>() {
			
			@Override
			public void onSuccess(ArrayList<AdminRowObject> result) {
				if(result != null){

					oracleMap = new HashMap<String, String>();
					oracle = new MultiWordSuggestOracle();
					
					for(AdminRowObject object : result){
						oracleMap.put(object.getName(), object.getId());
						oracle.add(object.getName());

					}
					
					initSuggestBox();
					initSuggestEvent();
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				GWT.log("Failed to load admins");
				
			}
		});
	}
	
	private void doPermsLoading(){
		ArrayList<QueryObject> queryList = new ArrayList<QueryObject>();
		
		queryList.add(new QueryObject(QueryEnum.P_ID, null, true, null));
		queryList.add(new QueryObject(QueryEnum.P_NAME, null, true, null));
		queryList.add(new QueryObject(QueryEnum.P_STATUS, "A", true, QueryOperatorEnum.EQUALS));
		
		GlobalResource.getInstance().getAdminRPC().getPermsList(queryList, new AsyncCallback<ArrayList<PermsRowObject>>() {
			
			@Override
			public void onSuccess(ArrayList<PermsRowObject> result) {
				if(result != null){

					oracleMap = new HashMap<String, String>();
					oracle = new MultiWordSuggestOracle();
					
					for(PermsRowObject object : result){
						oracleMap.put(object.getName(), object.getId());
						oracle.add(object.getName());

					}
					
					initSuggestBox();
					initSuggestEvent();
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				GWT.log("Failed to load perms");

			}
		});
	}
	
	private void initSuggestBox(){
		suggest = new SuggestBox(oracle);
		suggest.getTextBox().setStyleName("form-control");
		
		suggestContainer.setWidget(suggest);
	}
	
	public void setGroupSuggestBoxEventHandler(CustomSuggestBoxEventHandler handler){
		this.handler = handler;
	}
	
	private void initSuggestEvent(){
		suggest.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
			
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				if(oracleMap == null){
					return;
				}
				
				String completedString = event.getSelectedItem().getReplacementString();
				String value = oracleMap.get(completedString.trim());
				
				if(handler != null){
					handler.onSuggestSelected(completedString, value, suggest.getTextBox());
				}
			}
		});
	}

	@Override
	public Element getElement() {
		if(suggest != null){
			return suggest.getTextBox().getElement();
		}else{
			return super.getElement();
		}
	}
	
	
}
