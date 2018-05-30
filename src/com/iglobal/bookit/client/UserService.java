package com.iglobal.bookit.client;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.iglobal.bookit.shared.BookRowObject;
import com.iglobal.bookit.shared.DynamicBookQueryObject;
import com.iglobal.bookit.shared.DynamicRecordObject;
import com.iglobal.bookit.shared.LicenseObject;
import com.iglobal.bookit.shared.ReportFilterLoadObject;
import com.iglobal.bookit.shared.ReportGeneratorQueryObject;
import com.iglobal.bookit.shared.User;
import com.iglobal.bookit.shared.UserPermissionEnum;

@RemoteServiceRelativePath("user")
public interface UserService extends RemoteService{
	public boolean addToBook(DynamicBookQueryObject bookQueryObject);
	public DynamicRecordObject searchFromBook(DynamicBookQueryObject bookQueryObject);
	public HashMap<String, ArrayList<UserPermissionEnum>> getGroupPermission(String userId);
	public ArrayList<String> getBookGroups(String bookId);
	public ArrayList<String> getAggregatedPerms(ArrayList<String> groupIds);
	public HashMap<String, ArrayList<String>> getAggregatedPermsHash(ArrayList<String> groupIds);
	public boolean isUpdateSuccessful(int queryDT, String tableName, String field, String value, String id);
	public String getGeneratedReportHandler(ReportGeneratorQueryObject reportObject);
	public ReportFilterLoadObject getReportFilterItemList(ReportGeneratorQueryObject reportObject);
	public boolean isUserSessionActive(String email);
	public LicenseObject getLicenseObject();
	public ArrayList<String> getAllNotificationEnabledGroups(ArrayList<String> notificationList);
	public HashMap<String, String> getNotificationInfoHash(ArrayList<BookRowObject> booksList);
	public User getUser(String username, String password);
}
