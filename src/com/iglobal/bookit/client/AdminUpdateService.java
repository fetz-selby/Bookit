package com.iglobal.bookit.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.iglobal.bookit.shared.AdminRowObject;
import com.iglobal.bookit.shared.BookRowObject;
import com.iglobal.bookit.shared.GroupRowObject;
import com.iglobal.bookit.shared.UserRowObject;

@RemoteServiceRelativePath("adminupdate")
public interface AdminUpdateService extends RemoteService{
	public boolean isGroupsUpdated(GroupRowObject object, int userId);
	public boolean isUsersUpdated(UserRowObject object, int userId);
	public boolean isAdminsUpdated(AdminRowObject object, int userId);
	public boolean isBooksUpdated(BookRowObject object);
	public boolean isColumnRemoved(String tableName, String columnName);
	public boolean isBookColumnStringPartiallyUpdated(BookRowObject object);
}
