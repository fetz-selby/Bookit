package com.iglobal.bookit.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.iglobal.bookit.shared.AdminRowObject;
import com.iglobal.bookit.shared.BookRowObject;
import com.iglobal.bookit.shared.GroupRowObject;
import com.iglobal.bookit.shared.UserRowObject;

public interface AdminNewServiceAsync {

	void isGroupsCreated(GroupRowObject object, int userId,
			AsyncCallback<Boolean> callback);

	void isAdminsCreated(AdminRowObject object, int userId,
			AsyncCallback<Boolean> callback);

	void isBooksCreated(BookRowObject object, AsyncCallback<Boolean> callback);

	void isUsersCreated(UserRowObject object, int userId,
			AsyncCallback<Boolean> callback);

}
