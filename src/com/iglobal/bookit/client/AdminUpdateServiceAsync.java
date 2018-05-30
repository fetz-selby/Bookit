package com.iglobal.bookit.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.iglobal.bookit.shared.AdminRowObject;
import com.iglobal.bookit.shared.BookRowObject;
import com.iglobal.bookit.shared.GroupRowObject;
import com.iglobal.bookit.shared.UserRowObject;

public interface AdminUpdateServiceAsync {

	void isAdminsUpdated(AdminRowObject object, int userId,
			AsyncCallback<Boolean> callback);

	void isBooksUpdated(BookRowObject object, AsyncCallback<Boolean> callback);

	void isGroupsUpdated(GroupRowObject object, int userId,
			AsyncCallback<Boolean> callback);

	void isUsersUpdated(UserRowObject object, int userId,
			AsyncCallback<Boolean> callback);

	void isColumnRemoved(String tableName, String columnName,
			AsyncCallback<Boolean> callback);

	void isBookColumnStringPartiallyUpdated(BookRowObject object,
			AsyncCallback<Boolean> callback);

}
