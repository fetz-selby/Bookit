package com.iglobal.bookit.client.utils;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.Widget;
import com.iglobal.bookit.client.GlobalResource;
import com.iglobal.bookit.client.constants.ClientURLConstants;
import com.iglobal.bookit.client.constants.URLConstants;
import com.iglobal.bookit.client.events.ModelLoadComplete;
import com.iglobal.bookit.client.events.RPCCompleteHandler;
import com.iglobal.bookit.client.events.SingleEntityLoadCompleteHandler;
import com.iglobal.bookit.client.user.events.BookLoadEventHandler;
import com.iglobal.bookit.shared.BookRowObject;
import com.iglobal.bookit.shared.GroupRowObject;
import com.iglobal.bookit.shared.PermsRowObject;
import com.iglobal.bookit.shared.QueryEnum;
import com.iglobal.bookit.shared.QueryObject;
import com.iglobal.bookit.shared.QueryOperatorEnum;
import com.iglobal.bookit.shared.User;

public class Utils {
	private static FormPanel userGlobalForm;
	
	public static void setGlobalForm(FormPanel formPanel){
		userGlobalForm = formPanel;
	}
	
	public static void setFeed(Widget widget, String url, boolean isPost, final FormSaveEventHandler handler){
		if(userGlobalForm == null){
			return;
		}
	
		userGlobalForm.setAction(GWT.getModuleBaseURL()+url);
		if(isPost){
			userGlobalForm.setMethod(FormPanel.METHOD_POST);
			userGlobalForm.setEncoding(FormPanel.ENCODING_MULTIPART);
		}else{
			userGlobalForm.setMethod(FormPanel.METHOD_GET);
		}
		
		userGlobalForm.clear();
		userGlobalForm.setWidget(widget);
		userGlobalForm.addSubmitCompleteHandler(new SubmitCompleteHandler() {
			
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				if(handler != null){
					handler.onSuccessfulSave(event);
				}
			}
		});
		userGlobalForm.submit();
	}
	
	public static URLConstants getURLObject(String url){

		if(url.equals("login")){
			return URLConstants.LOGIN;
		}else if(url.equals("admins")){
			return URLConstants.ADMINS;
		}else if(url.equals("books")){
			return URLConstants.BOOKS;
		}else if(url.equals("groups")){
			return URLConstants.GROUPS;
		}else if(url.equals("users")){
			return URLConstants.USERS;
		}else if(url.equals("help")){
			return URLConstants.HELP;
		}else if(url.equals("settings")){
			return URLConstants.SETTINGS;
		}else if(url.equals("logout")){
			return URLConstants.LOGOUT;
		}else if(url.equals("sessions")){
			return URLConstants.SESSIONS;
		}

		return null;
	}

	public static ClientURLConstants getClientURLObject(String url){

		if(url.equals("records")){
			return ClientURLConstants.RECORDS;
		}else if(url.equals("transactions")){
			return ClientURLConstants.TRANSACTION;
		}else if(url.equals("settings")){
			return ClientURLConstants.SETTINGS;
		}else if(url.equals("logout")){
			return ClientURLConstants.LOGOUT;
		}else if(url.equals("reports")){
			return ClientURLConstants.REPORTS;
		}

		return null;
	}
	
	public static ErrorIndicator isNameValid(String name){
		if(name == null || name.isEmpty()){
			return ErrorIndicator.VALUE_ISEMPTY;
		}else if(Character.isDigit(name.charAt(0))){
			return ErrorIndicator.VALUE_INVALID;
		}else if(name.length() <= 2){
			return ErrorIndicator.VALUE_SHORT;
		}

		return ErrorIndicator.VALUE_OK;
	}
	
	public static void isEmailValid(String email, final RPCCompleteHandler<ErrorIndicator> handler){
		if(email.trim().matches("^([a-z0-9_\\.-]+)@([\\d\\p{L}\\a-z\\.-]+)\\.([a-z\\.]{2,6})$")){
			ArrayList<QueryObject> queryList = new ArrayList<QueryObject>();
			QueryObject emailQuery = new QueryObject(QueryEnum.PER_EMAIL, email, true, QueryOperatorEnum.EQUALS);

			queryList.add(emailQuery);
			queryList.add(new QueryObject(QueryEnum.PER_ID, null, true, null));

			GlobalResource.getInstance().getAdminRPC().isEmailExist(queryList, new AsyncCallback<Boolean>() {

				@Override
				public void onSuccess(Boolean result) {
					if(result != null){
						if(result){
							if(handler != null){
								handler.onProccessComplete(ErrorIndicator.VALUE_EXISTS);
							}
						}else{
							if(handler != null){
								handler.onProccessComplete(ErrorIndicator.VALUE_OK);
							}
						}
					}
				}

				@Override
				public void onFailure(Throwable caught) {
					if(handler != null){
						handler.onProccessComplete(ErrorIndicator.BAD_REQUEST);
					}
				}
			});
		}else{
			if(handler != null){
				handler.onProccessComplete(ErrorIndicator.VALUE_INVALID);
			}
		}

	}
	
	public static void isTableExist(String tableName, String id, final RPCCompleteHandler<ErrorIndicator> handler){
		ArrayList<QueryObject> queryList = new ArrayList<QueryObject>();

		queryList.add(new QueryObject(QueryEnum.B_ID, null, true, null));
		queryList.add(new QueryObject(QueryEnum.B_NAME, null, true, null));
		
		GlobalResource.getInstance().getAdminRPC().isTableExist(queryList, tableName, id, new AsyncCallback<Boolean>() {
			
			@Override
			public void onSuccess(Boolean result) {
				if(result){
					if(handler != null){
						handler.onProccessComplete(ErrorIndicator.VALUE_EXISTS);
					}
				}else{
					if(handler != null){
						handler.onProccessComplete(ErrorIndicator.VALUE_OK);
					}
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				if(handler != null){
					handler.onProccessComplete(ErrorIndicator.BAD_REQUEST);
				}				
			}
		});
	}

	public static ErrorIndicator isPasswordValid(String password){
		if(password == null || password.isEmpty()){
			return ErrorIndicator.VALUE_ISEMPTY;
		}else if(password.length() <= 4){
			return ErrorIndicator.VALUE_SHORT;
		}else if(password.length() > 30){
			return ErrorIndicator.VALUE_TOO_LONG;
		}

		return ErrorIndicator.VALUE_OK;
	}

	//	public static String getPermsName(String permValues, String delimiter){
	//		String permName = "";
	//		String[] tokens = permValues.split(delimiter);
	//		
	//		for(String token : tokens){
	//			if(token.trim().equals(DBConstants.READ)){
	//				permName += "Read"+delimiter;
	//			}else if(token.trim().equals(DBConstants.WRITE)){
	//				permName += "Write"+delimiter;
	//			}else if(token.trim().equals(DBConstants.UPDATE)){
	//				permName += "Update"+delimiter;
	//			}
	//		}
	//		
	//		return permName.substring(0, permName.lastIndexOf(delimiter));
	//	}

	public static String getPermsName(String permValues, String delimiter){

		if(permValues.contains(delimiter)){
			final String LOCAL_DELIMITER = ", ";
			String permName = "";

			ArrayList<String> permsList = new ArrayList<String>();

			String[] permTokens = permValues.split(delimiter);
			for(String token : permTokens){
				permsList.add(token);
			}

			for(PermsRowObject object : GlobalResource.getInstance().getPermsList()){
				if(permsList.contains(object.getId().trim())){
					permName += object.getName()+LOCAL_DELIMITER;
				}
			}

			return permName.substring(0, permName.lastIndexOf(LOCAL_DELIMITER));
		}else{

			for(PermsRowObject object : GlobalResource.getInstance().getPermsList()){
				if(object.getId().equals(permValues)){
					return object.getName();
				}
			}
			return "";
		}
	}

	public static String getGroupName(String groupValues, String delimiter){
		if(groupValues.contains(delimiter)){
			final String LOCAL_DELIMITER = ", ";
			String groupName = "";

			ArrayList<String> groupsList = new ArrayList<String>();

			String[] groupTokens = groupValues.split(delimiter);
			for(String token : groupTokens){
				groupsList.add(token);
			}

			for(GroupRowObject object : GlobalResource.getInstance().getGroupList()){
				if(groupsList.contains(object.getId().trim())){
					groupName += object.getName()+LOCAL_DELIMITER;
				}
			}

			return groupName.substring(0, groupName.lastIndexOf(LOCAL_DELIMITER));

		}else{
			for(GroupRowObject object : GlobalResource.getInstance().getGroupList()){
				if(object.getId().equals(groupValues)){
					return object.getName();
				}
			}
			return "";
		}
	}
	
	public static String getGroupName(String groupId){
		for(GroupRowObject object : GlobalResource.getInstance().getGroupList()){
			if(groupId.equals(object.getId().trim())){
				return object.getName();
			}
		}
		
		return null;
	}
	
	public static String getGroupNameWithPerm(String groupId){
		for(GroupRowObject object : GlobalResource.getInstance().getGroupList()){
			if(groupId.equals(object.getId().trim())){
				String permsNames = "";

				if(object.getPerms() == null){
					Window.alert("IS NULL");
				}
				
				if(object.getPerms().contains(",")){
					for(String permId : object.getPerms().split(",")){
						permsNames += getPerm(permId)+",";
					}
					
					permsNames = permsNames.substring(0, permsNames.lastIndexOf(","));
				}else{
					permsNames = getPerm(object.getPerms());
				}
				
				
				
				return object.getName()+"("+permsNames+")";
			}
		}
		
		return null;
	}

	public static String getPerm(String permId){
		for(PermsRowObject perm : GlobalResource.getInstance().getPermsList()){
			if(perm.getId().trim().equals(permId.trim())){
				return perm.getName();
			}
		}
		return "";
	}
	
	public static String getPermName(String permId){
		for(PermsRowObject object : GlobalResource.getInstance().getPermsList()){
			if(permId.equals(object.getId().trim())){
				return object.getName();
			}
		}
		
		return null;
	}
	
	public static String getTruncatedText(String text, int limit){
		if(text == null || text.trim().isEmpty()){
			return "empty";
		}else if(text.length() > limit){
			return text.substring(0, limit - 3)+" ...";
		}
		
		return text;
	}
	
	public static String getCompoundedName(String name){
		if(name != null){
			if(name.trim().contains(" ")){
				return name.replaceAll(" ", "_");
			}else{
				return name;
			}
		}
		
		return name;
	}
	
	public static String getDecodedCompoundedName(String name){
		if(name != null){
			if(name.trim().contains("_")){
				return name.replaceAll("_", " ");
			}else{
				return name;
			}
		}
		
		return name;
	}
	
	public static String getTodayDate(){
		DateTimeFormat dateFormatter = DateTimeFormat.getFormat("yyyy-MM-dd");
		return dateFormatter.format(new Date());
	}
	
	public static String getDateString(Date date){
		DateTimeFormat dateFormatter = DateTimeFormat.getFormat("yyyy-MM-dd");
		return dateFormatter.format(date);
	}
	
	public static void doBooksLoading(final ModelLoadComplete<BookRowObject> handler){
		ArrayList<String> groupList = GlobalResource.getInstance().getUser().getPermsList();
		
		ArrayList<QueryObject> queryList = new ArrayList<QueryObject>();

		for(int i = 0; i < groupList.size(); i++){
			String groupId = groupList.get(i);
			QueryObject query = new QueryObject(QueryEnum.B_GROUPS, groupId, false, QueryOperatorEnum.LIKE);
			
			//if it's the last, donot use OR.
			if(i != groupList.size() - 1){
				query.setJoinOperator(QueryOperatorEnum.OR);
			}
			
			queryList.add(query);
		}
		
		queryList.add(new QueryObject(QueryEnum.B_STATUS, "A", true, QueryOperatorEnum.EQUALS));
		queryList.add(new QueryObject(QueryEnum.B_NAME, null, true, null));
		queryList.add(new QueryObject(QueryEnum.B_ID, null, true, null));
		queryList.add(new QueryObject(QueryEnum.B_GROUPS, null, true, null));
		queryList.add(new QueryObject(QueryEnum.B_TABLE_COL, null, true, null));

		
		GlobalResource.getInstance().getAdminRPC().getBookList(queryList, new AsyncCallback<ArrayList<BookRowObject>>() {
			
			@Override
			public void onSuccess(ArrayList<BookRowObject> result) {
				if(handler != null){
					handler.onModuleLoadComple(result);
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public static void doBookLoad(String bookId, final SingleEntityLoadCompleteHandler<BookRowObject> handler){
		ArrayList<QueryObject> queryList = new ArrayList<QueryObject>();

		queryList.add(new QueryObject(QueryEnum.B_NAME, null, true, null));
		queryList.add(new QueryObject(QueryEnum.B_ID, bookId, true, QueryOperatorEnum.EQUALS));
		queryList.add(new QueryObject(QueryEnum.B_TABLE_NAME, null, true, null));
		queryList.add(new QueryObject(QueryEnum.B_TABLE_COL, null, true, null));
		queryList.add(new QueryObject(QueryEnum.B_GROUPS, null, true, null));
		queryList.add(new QueryObject(QueryEnum.B_CREATED_TS, null, true, null));
		queryList.add(new QueryObject(QueryEnum.B_MODIFIED_TS, null, true, null));
		queryList.add(new QueryObject(QueryEnum.B_STATUS, null, true, null));

		queryList.add(new QueryObject(QueryEnum.A_NAME, null, true, null));
		queryList.add(new QueryObject(QueryEnum.A_ID, QueryEnum.B_CREATED_BY.getField(), true, QueryOperatorEnum.EQUALS));

		GlobalResource.getInstance().getAdminRPC().getBookList(queryList, new AsyncCallback<ArrayList<BookRowObject>>() {
			
			@Override
			public void onSuccess(ArrayList<BookRowObject> result) {
				if(handler != null && result != null){
					if(result.size() == 1){
						handler.onEntityLoadComplete(result.get(0));
					}else{
						GWT.log("[Utils.doBookLoad] multiple book ids found!");
					}
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public static void getAssociatedBooks(ArrayList<String> groupList, final BookLoadEventHandler handler){
		
		ArrayList<QueryObject> queryList = new ArrayList<QueryObject>();

		for(int i = 0; i < groupList.size(); i++){
			String groupId = groupList.get(i);
			QueryObject query = new QueryObject(QueryEnum.B_GROUPS, groupId, false, QueryOperatorEnum.LIKE);
			
			//if it's the last, donot use OR.
			if(i != groupList.size() - 1){
				query.setJoinOperator(QueryOperatorEnum.OR);
			}
			
			queryList.add(query);
		}
		
		queryList.add(new QueryObject(QueryEnum.B_STATUS, "A", true, QueryOperatorEnum.EQUALS));
		queryList.add(new QueryObject(QueryEnum.B_NAME, null, true, null));
		queryList.add(new QueryObject(QueryEnum.B_ID, null, true, null));
		queryList.add(new QueryObject(QueryEnum.B_GROUPS, null, true, null));
		queryList.add(new QueryObject(QueryEnum.B_TABLE_COL, null, true, null));

		
		GlobalResource.getInstance().getAdminRPC().getBookList(queryList, new AsyncCallback<ArrayList<BookRowObject>>() {
			
			@Override
			public void onSuccess(ArrayList<BookRowObject> result) {
				if(result != null){
					
					
					if(handler != null){
						handler.onLoadComplete(result);
					}
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public static String getBookAliasName(String bookName){
		if(bookName.contains(":")){
			return bookName.split("[:]")[1];
		}
		return null;
	}
	
	public static String getBookTableName(String bookName){
		if(bookName.contains(":")){
			return bookName.split("[:]")[0];
		}
		return null;
	}
}
