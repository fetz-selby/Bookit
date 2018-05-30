package com.iglobal.bookit.client.user.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.iglobal.bookit.client.GlobalResource;
import com.iglobal.bookit.client.constants.EntityEnum;
import com.iglobal.bookit.shared.BookRowObject;
import com.iglobal.bookit.shared.QueryEnum;
import com.iglobal.bookit.shared.QueryObject;
import com.iglobal.bookit.shared.QueryOperatorEnum;

public class CustomDataListBox extends Composite {

	private CustomDataListBoxEventHandler handler;
	private HashMap<String, String> itemsMap; 
	private boolean isEmpty = false;
	private static CustomDataListBoxUiBinder uiBinder = GWT
			.create(CustomDataListBoxUiBinder.class);

	interface CustomDataListBoxUiBinder extends
			UiBinder<Widget, CustomDataListBox> {
	}
	
	public interface CustomDataListBoxEventHandler{
		void onItemSelected(String value, String itemName);
	}

	@UiField ListBox listBox;
	private String userId;
	private EntityEnum type;
	private ArrayList<String> idList;
	
	public CustomDataListBox(String userId, EntityEnum type, ArrayList<String> idList) {
		this.userId = userId;
		this.type = type;
		this.idList = idList;
		initWidget(uiBinder.createAndBindUi(this));
		initEvent();
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public EntityEnum getType() {
		return type;
	}

	public void setType(EntityEnum type) {
		this.type = type;
	}

	public void load(){
		if(idList == null || type == null) return;
		
		initComponent();
	}
	
	private void initComponent(){
		
		switch(type){
		case BOOKS:
			doBooksCall();
			break;
		case USERS:
			doUsersCall();
			break;
		default:
			break;
			}
		
		listBox.addStyleName("form-control");
	}
	
	private void initEvent(){
		listBox.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				if(handler != null && !isEmpty){
					handler.onItemSelected(listBox.getValue(listBox.getSelectedIndex()), listBox.getItemText(listBox.getSelectedIndex()));
				}
			}
		});
	}

	private void doBooksCall(){
		
		ArrayList<QueryObject> queryList = new ArrayList<QueryObject>();

		for(int i = 0; i < idList.size(); i++){
			String groupId = idList.get(i);
			QueryObject query = new QueryObject(QueryEnum.B_GROUPS, groupId, false, QueryOperatorEnum.LIKE);
			
			//if it's the last, donot use OR.
			if(i != idList.size() - 1){
				query.setJoinOperator(QueryOperatorEnum.OR);
			}
			
			queryList.add(query);
		}
		
		
		queryList.add(new QueryObject(QueryEnum.B_STATUS, "A", true, QueryOperatorEnum.EQUALS));
		queryList.add(new QueryObject(QueryEnum.B_NAME, null, true, null));
		queryList.add(new QueryObject(QueryEnum.B_ID, null, true, null));
		queryList.add(new QueryObject(QueryEnum.B_GROUPS, null, true, null));

		listBox.addItem("Loading ...");
		GlobalResource.getInstance().getAdminRPC().getBookList(queryList, new AsyncCallback<ArrayList<BookRowObject>>() {
			
			@Override
			public void onSuccess(ArrayList<BookRowObject> result) {
				
				listBox.clear();
				listBox.addItem("");
				if(result != null){
					itemsMap = new HashMap<String, String>();
					ArrayList<String> itemCheckList = new ArrayList<String>();
					for(BookRowObject book : result){
						
						//Only show writable books
						ArrayList<String> groupsList = new ArrayList<String>();
						if(book.getGroups().contains(",")){
							String[] groups = book.getGroups().split("[,]");
							for(String group : groups){
								groupsList.add(group);
							}
						}else{
							groupsList.add(book.getGroups());
						}
						
						
						for(String group : groupsList){
							if(GlobalResource.getInstance().isCanUserWrite(group)){
								String bookName = "";
								if(book.getName().contains(":")){
									bookName = book.getName().split("[:]")[1];
								}
								
								if(!itemCheckList.contains(bookName)){
									//listBox.addItem(bookName, book.getId());
									itemsMap.put(bookName, book.getId());
									
									itemCheckList.add(bookName);
								}
								
								
							}
						}
						
//						String bookName = "";
//						if(book.getName().contains(":")){
//							bookName = book.getName().split("[:]")[1];
//						}
//						listBox.addItem(bookName, book.getId());
//						itemsMap.put(bookName, book.getId());
					}
					
					doRearrangeItems(itemsMap, listBox);
					
					if(listBox.getItemCount() == 1){
						if(handler != null){
							//handler.onItemSelected(listBox.getValue(listBox.getSelectedIndex()), listBox.getItemText(listBox.getSelectedIndex()));
						}
					}else if(listBox.getItemCount() == 1){
						isEmpty = true;
						listBox.clear();
						listBox.addItem("-- empty --", "0");
					}
				}else{
					GWT.log("Call returned NULL");
					isEmpty = true;
					listBox.clear();
					listBox.addItem("-- empty --", "0");
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				GWT.log("[CustomDataListBox] call failed!");
			}
		});
	}
	
	private void doRearrangeItems(HashMap<String, String> itemsHash, ListBox listBox){
		TreeSet<String> itemSet = new TreeSet<String>();
		
		for(String key : itemsHash.keySet()){
			itemSet.add(key);
		}
		
		for(String item : itemSet){
			if(itemsHash.containsKey(item)){
				listBox.addItem(item, itemsHash.get(item));
			}
		}
	}
	
	private void doUsersCall(){
		
	}
	
	public int getItemCount(){
		return listBox.getItemCount();
	}
	
	public void setCustomDataListBoxEventHandler(CustomDataListBoxEventHandler handler){
		this.handler = handler;
	}
	
}
