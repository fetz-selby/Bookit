package com.iglobal.bookit.client;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
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

public interface AdminServiceAsync {

	void getBook(ArrayList<QueryObject> queryList, AsyncCallback<Book> callback);

	void getUser(ArrayList<QueryObject> queryList, AsyncCallback<User> callback);

	void isEmailExist(ArrayList<QueryObject> queryList,
			AsyncCallback<Boolean> callback);

	void getAdminList(ArrayList<QueryObject> quer,
			AsyncCallback<ArrayList<AdminRowObject>> callback);

	void getUserList(ArrayList<QueryObject> queryList,
			AsyncCallback<ArrayList<UserRowObject>> callback);

	void getBookList(ArrayList<QueryObject> queryList,
			AsyncCallback<ArrayList<BookRowObject>> callback);

	void getGroupList(ArrayList<QueryObject> queryList,
			AsyncCallback<ArrayList<GroupRowObject>> callback);

	void getPermsList(ArrayList<QueryObject> queryList,
			AsyncCallback<ArrayList<PermsRowObject>> callback);

	void getAllBookDataTypesList(
			AsyncCallback<ArrayList<BookColumnObject>> callback);

	void getSessionId(String email, AsyncCallback<Integer> callback);

	void isSessionClosed(String id, AsyncCallback<Boolean> callback);

	void isTableExist(ArrayList<QueryObject> queryList, String tableName,
			String id, AsyncCallback<Boolean> callback);

	void getSessionsList(ArrayList<QueryObject> queryList,
			AsyncCallback<ArrayList<SessionsRowObject>> callback);

	void getAllLoggedInUsers(AsyncCallback<HashMap<String, String>> callback);

	void getIfGroupNameExist(String groupName,
			AsyncCallback<VerifierObject> callback);
}
