package com.iglobal.bookit.client;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.event.shared.HandlerManager;
import com.iglobal.bookit.client.constants.DBConstants;
import com.iglobal.bookit.shared.BookColumnObject;
import com.iglobal.bookit.shared.GroupRowObject;
import com.iglobal.bookit.shared.PermsRowObject;
import com.iglobal.bookit.shared.User;

public class GlobalResource {
	private static GlobalResource instance = new GlobalResource();
	private AdminServiceAsync adminRPC;
	private LoginServiceAsync loginRPC;
	private AdminUpdateServiceAsync adminUpdateRPC;
	private AdminNewServiceAsync adminNewRPC;
	private UserServiceAsync userRPC;
	private HandlerManager eventBus;
	private User user;
	private ArrayList<PermsRowObject> permsList, permsPrimaryList;
	private HashMap<String, ArrayList<String>> groupPermsHash;
	private ArrayList<GroupRowObject> groupsList;
	private ArrayList<BookColumnObject> bookColumnsList;
	private ArrayList<String> appUserPermsList;
	private ArrayList<String> notificationGroupList;
	private String sessionId;
	
	private GlobalResource(){}
	
	public static GlobalResource getInstance(){
		return instance;
	}

	public AdminServiceAsync getAdminRPC() {
		return adminRPC;
	}

	public void setAdminRPC(AdminServiceAsync adminRPC) {
		this.adminRPC = adminRPC;
	}

	public LoginServiceAsync getLoginRPC() {
		return loginRPC;
	}

	public void setLoginRPC(LoginServiceAsync loginRPC) {
		this.loginRPC = loginRPC;
	}

	public HandlerManager getEventBus() {
		return eventBus;
	}

	public void setEventBus(HandlerManager eventBus) {
		this.eventBus = eventBus;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setPermsList(ArrayList<PermsRowObject> result) {
		this.permsList = result;
	}
	
	public ArrayList<PermsRowObject> getPermsList(){
		return permsList;
	}

	public void setGroupsList(ArrayList<GroupRowObject> result) {
		this.groupsList = result;
	}
	
	public ArrayList<GroupRowObject> getGroupList(){
		return groupsList;
	}

	public AdminUpdateServiceAsync getAdminUpdateRPC() {
		return adminUpdateRPC;
	}

	public void setAdminUpdateRPC(AdminUpdateServiceAsync adminUpdateRPC) {
		this.adminUpdateRPC = adminUpdateRPC;
	}

	public AdminNewServiceAsync getAdminNewRPC() {
		return adminNewRPC;
	}

	public void setAdminNewRPC(AdminNewServiceAsync adminNewRPC) {
		this.adminNewRPC = adminNewRPC;
	}

	public ArrayList<BookColumnObject> getBookColumnsList() {
		return bookColumnsList;
	}

	public void setBookColumnsList(ArrayList<BookColumnObject> bookColumnsList) {
		this.bookColumnsList = bookColumnsList;
	}

	public UserServiceAsync getUserRPC() {
		return userRPC;
	}

	public void setUserRPC(UserServiceAsync userRPC) {
		this.userRPC = userRPC;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public ArrayList<PermsRowObject> getPermsPrimaryList() {
		return permsPrimaryList;
	}

	public void setPermsPrimaryList(ArrayList<PermsRowObject> permsPrimaryList) {
		this.permsPrimaryList = permsPrimaryList;
	}
	
	public boolean isCanAdminWrite(){
		if(user.getPermsList().contains(DBConstants.WRITE)){
			return true;
		}
		return false;
	}
	
	public boolean isCanAdminUpdate(){
		if(user.getPermsList().contains(DBConstants.UPDATE)){
			return true;
		}
		return false;
	}
	
	public boolean isCanUserWrite(){
		if(appUserPermsList.contains(DBConstants.WRITE)){
			return true;
		}
		
		return false;
	}
	
	public boolean isCanUserUpdate(){
		if(appUserPermsList.contains(DBConstants.UPDATE)){
			return true;
		}
		
		return false;
	}
	
	public void setAppUserPerms(ArrayList<String> perms){
		appUserPermsList = perms;
	}
	
	public void setGroupPermsHash(HashMap<String, ArrayList<String>> groupPermsHash){
		this.groupPermsHash = groupPermsHash;
	}
	
	public boolean isCanUserWrite(String groupId){
		if(groupPermsHash.containsKey(groupId) && groupPermsHash.get(groupId).contains(DBConstants.WRITE)){
			return true;
		}
		return false;
	}
	
	public boolean isCanUserUpdate(String groupId){
		if(groupPermsHash.containsKey(groupId) && groupPermsHash.get(groupId).contains(DBConstants.UPDATE)){
			return true;
		}
		return false;
	}

	public ArrayList<String> getNotificationGroupList() {
		return notificationGroupList;
	}

	public void setNotificationGroupList(ArrayList<String> notificationGroupList) {
		this.notificationGroupList = notificationGroupList;
	}
	
}
