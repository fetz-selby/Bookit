package com.iglobal.bookit.client;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.iglobal.bookit.shared.AdminRowObject;
import com.iglobal.bookit.shared.Book;
import com.iglobal.bookit.shared.BookColumnObject;
import com.iglobal.bookit.shared.BookRowObject;
import com.iglobal.bookit.shared.GroupRowObject;
import com.iglobal.bookit.shared.PermsRowObject;
import com.iglobal.bookit.shared.QueryObject;
import com.iglobal.bookit.shared.SessionsRowObject;
import com.iglobal.bookit.shared.User;
import com.iglobal.bookit.shared.UserRowObject;
import com.iglobal.bookit.shared.VerifierObject;

@RemoteServiceRelativePath("admin")
public interface AdminService extends RemoteService{
	public Book getBook(ArrayList<QueryObject> queryList);
	//public boolean addBook();
	public User getUser(ArrayList<QueryObject> queryList);
	public ArrayList<AdminRowObject> getAdminList(ArrayList<QueryObject> queryList);
	public ArrayList<UserRowObject> getUserList(ArrayList<QueryObject> queryList);
	public ArrayList<BookRowObject> getBookList(ArrayList<QueryObject> queryList);
	public ArrayList<GroupRowObject> getGroupList(ArrayList<QueryObject> queryList);
	public ArrayList<PermsRowObject> getPermsList(ArrayList<QueryObject> queryList);
	public ArrayList<BookColumnObject> getAllBookDataTypesList();
	public ArrayList<SessionsRowObject> getSessionsList(ArrayList<QueryObject> queryList);
	public HashMap<String, String> getAllLoggedInUsers();
	
	public boolean isEmailExist(ArrayList<QueryObject> queryList);
	boolean isTableExist(ArrayList<QueryObject> queryList, String tableName, String id);
	public VerifierObject getIfGroupNameExist(String groupName);
	public int getSessionId(String email);
	public boolean isSessionClosed(String id);
}
