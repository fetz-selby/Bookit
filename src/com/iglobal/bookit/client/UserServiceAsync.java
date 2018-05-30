package com.iglobal.bookit.client;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.iglobal.bookit.shared.BookRowObject;
import com.iglobal.bookit.shared.DynamicBookQueryObject;
import com.iglobal.bookit.shared.DynamicRecordObject;
import com.iglobal.bookit.shared.LicenseObject;
import com.iglobal.bookit.shared.ReportFilterLoadObject;
import com.iglobal.bookit.shared.ReportGeneratorQueryObject;
import com.iglobal.bookit.shared.User;
import com.iglobal.bookit.shared.UserPermissionEnum;

public interface UserServiceAsync {

	void addToBook(DynamicBookQueryObject bookQueryObject,
			AsyncCallback<Boolean> callback);

	void searchFromBook(DynamicBookQueryObject bookQueryObject,
			AsyncCallback<DynamicRecordObject> callback);

	void getGroupPermission(
			String userId,
			AsyncCallback<HashMap<String, ArrayList<UserPermissionEnum>>> callback);

	void getBookGroups(String bookId, AsyncCallback<ArrayList<String>> callback);

	void getAggregatedPerms(ArrayList<String> groupIds,
			AsyncCallback<ArrayList<String>> callback);

	void getAggregatedPermsHash(ArrayList<String> groupIds,
			AsyncCallback<HashMap<String, ArrayList<String>>> callback);

	void isUpdateSuccessful(int queryDT, String tableName, String field,
			String value, String id, AsyncCallback<Boolean> callback);

	void getReportFilterItemList(ReportGeneratorQueryObject reportObject,
			AsyncCallback<ReportFilterLoadObject> callback);

	void getGeneratedReportHandler(ReportGeneratorQueryObject reportObject,
			AsyncCallback<String> callback);

	void isUserSessionActive(String email, AsyncCallback<Boolean> callback);

	void getLicenseObject(AsyncCallback<LicenseObject> callback);

	void getAllNotificationEnabledGroups(ArrayList<String> notificationList,
			AsyncCallback<ArrayList<String>> callback);

	void getNotificationInfoHash(ArrayList<BookRowObject> booksList,
			AsyncCallback<HashMap<String, String>> callback);

	void getUser(String username, String password, AsyncCallback<User> callback);

}
