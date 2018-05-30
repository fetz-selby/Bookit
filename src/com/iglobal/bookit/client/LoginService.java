package com.iglobal.bookit.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.iglobal.bookit.shared.IsUser;
import com.iglobal.bookit.shared.QueryObject;

@RemoteServiceRelativePath("login")
public interface LoginService extends RemoteService{
	IsUser getHasQueryUser(ArrayList<QueryObject> userQueryObjects, QueryObject userQuery, QueryObject passwordQuery);
	boolean isLogout(String email);
}
