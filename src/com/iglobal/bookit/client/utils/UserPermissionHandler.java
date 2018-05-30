package com.iglobal.bookit.client.utils;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.iglobal.bookit.client.GlobalResource;
import com.iglobal.bookit.shared.UserPermissionEnum;

public class UserPermissionHandler {
	private UserPermissionEventHandler handler;
	private String userId;
	private HashMap<String, ArrayList<UserPermissionEnum>> groupPermsHash;
	
	public interface UserPermissionEventHandler{
		void onLoadComplete();
	}
	
	public UserPermissionHandler(String userId){
		this.userId = userId;
	}
	
	private void doProcessing(){
		GlobalResource.getInstance().getUserRPC().getGroupPermission(userId, new AsyncCallback<HashMap<String,ArrayList<UserPermissionEnum>>>() {
			
			@Override
			public void onSuccess(HashMap<String, ArrayList<UserPermissionEnum>> result) {
				if(result != null){
					groupPermsHash = result;
					if(handler != null){
						handler.onLoadComplete();
					}
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				
			}
		});
	}
	
	public void load(){
		doProcessing();
	}
	
	public boolean hasWritePermission(String groupId){
		if(groupPermsHash.containsKey(groupId)){
			ArrayList<UserPermissionEnum> permsList = groupPermsHash.get(groupId);
			if(permsList.contains(UserPermissionEnum.WRITE)){
				return true;
			}
		}else{
			return false;
		}
		return false;
	}
	
	public boolean hasUpdatePermission(String groupId){
		if(groupPermsHash.containsKey(groupId)){
			ArrayList<UserPermissionEnum> permsList = groupPermsHash.get(groupId);
			if(permsList.contains(UserPermissionEnum.UPDATE)){
				return true;
			}
		}else{
			return false;
		}
		return false;
	}
	
	public boolean hasReadPermission(String groupId){
		if(groupPermsHash.containsKey(groupId)){
			ArrayList<UserPermissionEnum> permsList = groupPermsHash.get(groupId);
			if(permsList.contains(UserPermissionEnum.READ)){
				return true;
			}
		}else{
			return false;
		}
		return false;
	}
	
	public void setUserPermissionEventHandler(UserPermissionEventHandler handler){
		this.handler = handler;
	}
}
