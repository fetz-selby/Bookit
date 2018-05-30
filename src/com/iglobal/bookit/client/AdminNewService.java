package com.iglobal.bookit.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.iglobal.bookit.shared.AdminRowObject;
import com.iglobal.bookit.shared.BookRowObject;
import com.iglobal.bookit.shared.GroupRowObject;
import com.iglobal.bookit.shared.UserRowObject;

@RemoteServiceRelativePath("adminnew")
public interface AdminNewService extends RemoteService{
	public boolean isGroupsCreated(GroupRowObject object, int userId);
	public boolean isUsersCreated(UserRowObject object, int userId);
	public boolean isAdminsCreated(AdminRowObject object, int userId);
	public boolean isBooksCreated(BookRowObject object);
}
