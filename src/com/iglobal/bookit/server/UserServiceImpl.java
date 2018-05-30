package com.iglobal.bookit.server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.iglobal.bookit.client.UserService;
import com.iglobal.bookit.server.utils.BookTableQueryMagic;
import com.iglobal.bookit.server.utils.ReportGenerator;
import com.iglobal.bookit.server.utils.ReportGenerator.ReportGeneratorHandler;
import com.iglobal.bookit.server.utils.Utils;
import com.iglobal.bookit.shared.BookRowObject;
import com.iglobal.bookit.shared.DataTypeConstants;
import com.iglobal.bookit.shared.DynamicBookQueryObject;
import com.iglobal.bookit.shared.DynamicRecordObject;
import com.iglobal.bookit.shared.LicenseObject;
import com.iglobal.bookit.shared.QueryConstants;
import com.iglobal.bookit.shared.ReportFilterLoadObject;
import com.iglobal.bookit.shared.ReportGeneratorQueryObject;
import com.iglobal.bookit.shared.User;
import com.iglobal.bookit.shared.UserPermissionEnum;
import com.mysql.jdbc.Connection;

@SuppressWarnings("serial")
public class UserServiceImpl extends RemoteServiceServlet implements UserService{
	private static Connection con = DBConnection.getConnection();
	private String reportFileName;

	@Override
	public boolean addToBook(DynamicBookQueryObject bookQueryObject) {

		BookTableQueryMagic queryMagic = new BookTableQueryMagic(bookQueryObject.getTableName(), bookQueryObject.getFieldValueHash(), bookQueryObject.getFieldDatatypeHash());
		System.out.println(queryMagic.getUpdateConstructedCall());

		try{
			con.setCacheCallableStatements(false);
			Statement stmt = (Statement) con.createStatement();
			int success = stmt.executeUpdate(queryMagic.getUpdateConstructedCall());

			if(success > 0){
				return true;
			}

		}catch(SQLException sql){
			sql.printStackTrace();
		}

		return false;
	}

	@Override
	public DynamicRecordObject searchFromBook(DynamicBookQueryObject bookQueryObject) {
		final String APPENDER = "_";
		BookTableQueryMagic queryMagic = new BookTableQueryMagic(bookQueryObject.getTableName(), bookQueryObject.getFieldValueHash(), bookQueryObject.getFieldDatatypeHash());
		queryMagic.setAllFieldsHashMap(bookQueryObject.getAllFieldDatatypeHash());

		for(String field : queryMagic.getAllFieldsHashMap().keySet()){
			System.out.println("field is "+field+", and datatype is "+queryMagic.getAllFieldsHashMap().get(field));
		}

		HashMap<String, String> fieldAlias = getBookAliasColumnMapping(bookQueryObject.getBookId());

		System.out.println(queryMagic.getAddConstructedCall());		
		try{
			con.setCacheCallableStatements(false);
			Statement stmt = (Statement) con.createStatement();
			ResultSet result = stmt.executeQuery(queryMagic.getAddConstructedCall());

			if(result != null){
				ArrayList<HashMap<String, String>> rows = new ArrayList<HashMap<String, String>>();
				ArrayList<HashMap<String, DataTypeConstants>> dataTypeListRows = new ArrayList<HashMap<String, DataTypeConstants>>();

				while(result.next()){
					HashMap<String, String> rowMap = new HashMap<String, String>();
					HashMap<String, DataTypeConstants> dataMap = new HashMap<String, DataTypeConstants>();

					rowMap.put("id", ""+result.getInt("id"));
					dataMap.put("id", DataTypeConstants.INT);
					for(String field : queryMagic.getAllFieldsHashMap().keySet()){
						String dataType = queryMagic.getAllFieldsHashMap().get(field);
						if(dataType.trim().equals("INT")){
							System.out.println(field+" => "+result.getInt(field+APPENDER));
							rowMap.put(field, ""+result.getInt(field+APPENDER));
							dataMap.put(field, DataTypeConstants.INT);
						}else if(dataType.trim().equals("TEXT")){
							System.out.println(field+" => "+result.getString(field+APPENDER));
							rowMap.put(field, result.getString(field+APPENDER));
							dataMap.put(field, DataTypeConstants.STRING);
						}else if(dataType.trim().equals("TIME")){
							System.out.println(field+" => "+result.getString(field+APPENDER));
							rowMap.put(field, result.getString(field+APPENDER));
							dataMap.put(field, DataTypeConstants.TIME);
						}else if(dataType.trim().equals("DATE")){
							System.out.println(field+" => "+result.getString(field+APPENDER));
							rowMap.put(field, result.getString(field+APPENDER));
							dataMap.put(field, DataTypeConstants.DATE);
						}else if(dataType.trim().equals("MEDIUMBLOB")){
							System.out.println(field+" => "+result.getString(field+APPENDER));
							if(result.getString(field+APPENDER) == null || result.getString(field+APPENDER).trim().isEmpty()){
								rowMap.put(field, "");
							}else{
								rowMap.put(field, getImageUrl(result.getBytes(field+APPENDER)));
							}
							dataMap.put(field, DataTypeConstants.BLOB);
						}else if(dataType.trim().equals("ID")){
							System.out.println(field+" => "+result.getString(field+APPENDER));
							rowMap.put(field, result.getString(field+APPENDER));
							dataMap.put(field, DataTypeConstants.ID);
						}else if(dataType.trim().equals("ALERT")){
							System.out.println(field+" => "+result.getString(field+APPENDER));
							rowMap.put(field, result.getString(field+APPENDER));
							dataMap.put(field, DataTypeConstants.ALERT);
						}else if(dataType.trim().equals("LOGIN")){
							System.out.println(field+" => "+result.getString(field+APPENDER));
							rowMap.put(field, result.getString(field+APPENDER));
							dataMap.put(field, DataTypeConstants.LOGIN);
						}
					}

					rows.add(rowMap);
					dataTypeListRows.add(dataMap);
				}
				System.out.println("[userserviceimpl] dataTypeMapLength is "+dataTypeListRows.size());
				return new DynamicRecordObject(rows, fieldAlias, dataTypeListRows);
			}

		}catch(SQLException sql){
			sql.printStackTrace();
		}

		return null;
	}

	private String getImageUrl(byte[] blob){
		if(blob == null){
			return "data:image/png;base64,";
		}
		String base64 = new String(org.apache.commons.codec.binary.Base64.encodeBase64(blob));
		base64 = "data:image/png;base64,"+base64;
		return base64;
	}

	private HashMap<String, String> getBookAliasColumnMapping(String bookId){

		try{
			con.setCacheCallableStatements(false);
			Statement stmt = (Statement) con.createStatement();
			ResultSet result = stmt.executeQuery("select column_name from books where id="+bookId);

			if(result != null){
				HashMap<String, String> fieldAliasMap = null;
				while(result.next()){
					//avatar:avatar:1:A,class:class:1:A,rate:rate:1:A,age:age:1:A
					String[] columnTokens = result.getString("column_name").split("[,]");
					fieldAliasMap = new HashMap<String, String>();

					for(String column : columnTokens){
						//avatar:avatar:1:A
						System.out.println("[server] columns "+column);
						if(column.contains(":") && column.split("[:]").length == 5){
							if(column.split("[:]")[3].trim().equals("A")){
								fieldAliasMap.put(column.split("[:]")[0], column.split("[:]")[1]);
								System.out.println("columnName "+column.split("[:]")[0]+", alias "+column.split("[:]")[1]);
							}
						}
					}
				}
				return fieldAliasMap;
			}

		}catch(SQLException sql){
			sql.printStackTrace();
		}

		return null;
	}

	@Override
	public HashMap<String, ArrayList<UserPermissionEnum>> getGroupPermission(
			String userId) {
		try{
			con.setCacheCallableStatements(false);
			Statement stmt = (Statement) con.createStatement();
			ResultSet result = stmt.executeQuery("select groups from users where id="+userId+" and status = 'A'");

			if(result != null){
				HashMap<String, ArrayList<UserPermissionEnum>> groupPermHash = null;
				while(result.next()){
					groupPermHash = new HashMap<String, ArrayList<UserPermissionEnum>>();
					String groups = result.getString("groups");
					if(groups.contains(",")){
						//Multiply groups
						String[] groupTokens = groups.split("[,]");
						for(String group : groupTokens){
							ArrayList<UserPermissionEnum> permEnumList = getGroupPerms(group);
							groupPermHash.put(group, permEnumList);
						}
					}else{
						//Single group
						groupPermHash.put(groups, getGroupPerms(groups));
					}
				}

				return groupPermHash;
			}

		}catch(SQLException sql){
			sql.printStackTrace();
		}

		return null;
	}

	private ArrayList<UserPermissionEnum> getGroupPerms(String groupId){
		try{
			con.setCacheCallableStatements(false);
			Statement stmt = (Statement) con.createStatement();
			ResultSet result = stmt.executeQuery("select perms from groups where id="+groupId+" and status = 'A'");

			if(result != null){
				ArrayList<UserPermissionEnum> permEnumList = null;
				while(result.next()){
					permEnumList = new ArrayList<UserPermissionEnum>();
					String perms = result.getString("perms");
					if(perms.contains(",")){
						//Multiply perms
						String[] permsToken = perms.split("[,]");
						for(String perm : permsToken){
							if(perm.trim().equals("1")){
								permEnumList.add(UserPermissionEnum.READ);
							}else if(perm.trim().equals("2")){
								permEnumList.add(UserPermissionEnum.WRITE);
							}else if(perm.trim().equals("3")){
								permEnumList.add(UserPermissionEnum.UPDATE);
							}
						}
					}else{
						//Single perm
						if(perms.trim().equals("1")){
							permEnumList.add(UserPermissionEnum.READ);
						}else if(perms.trim().equals("2")){
							permEnumList.add(UserPermissionEnum.WRITE);
						}else if(perms.trim().equals("3")){
							permEnumList.add(UserPermissionEnum.UPDATE);
						}
					}
				}

				return permEnumList;
			}

		}catch(SQLException sql){
			sql.printStackTrace();
		}

		return null;
	}

	@Override
	public ArrayList<String> getBookGroups(String bookId) {
		try{
			con.setCacheCallableStatements(false);
			Statement stmt = (Statement) con.createStatement();
			ResultSet result = stmt.executeQuery("select groups from books where id="+bookId+" and status = 'A'");

			if(result != null){
				ArrayList<String> groupList = null;
				while(result.next()){
					groupList = new ArrayList<String>();
					String groups = result.getString("groups");
					if(groups.contains(",")){
						String[] groupToken = groups.split("[,]");
						for(String group : groupToken){
							groupList.add(group);
						}
					}else{
						groupList.add(groups);
					}
				}

				return groupList;
			}

		}catch(SQLException sql){
			sql.printStackTrace();
		}

		return null;
	}

	@Override
	public ArrayList<String> getAggregatedPerms(ArrayList<String> groupIds) {
		try{
			con.setCacheCallableStatements(false);
			Statement stmt = (Statement) con.createStatement();
			ResultSet result = stmt.executeQuery(getGroupsQuery(groupIds));

			if(result != null){
				ArrayList<String> permList = new ArrayList<String>();
				while(result.next()){
					permList.add(Utils.getPrimaryPerms(result.getString("perms")));
				}

				return getAggregatedList(permList);
			}

		}catch(SQLException sql){
			sql.printStackTrace();
		}

		return null;
	}

	private ArrayList<String> getAggregatedList(ArrayList<String> perms){

		ArrayList<String> aggregatedList = new ArrayList<String>();
		for(String perm : perms){
			if(perm.contains(",")){
				String[] locPerms = perm.split("[,]");
				for(String locPerm : locPerms){
					if(!aggregatedList.contains(locPerm)){
						aggregatedList.add(locPerm);
					}
				}
			}else{
				if(!aggregatedList.contains(perm)){
					aggregatedList.add(perm);
				}
			}
		}

		return aggregatedList;
	}

	private HashMap<String, ArrayList<String>> getAggregatedHash(HashMap<String, String> perms){

		HashMap<String, ArrayList<String>> aggregatedHash = new HashMap<String, ArrayList<String>>();

		for(String key : perms.keySet()){
			String perm = perms.get(key);
			ArrayList<String> permsList = new ArrayList<String>();

			if(perm.contains(",")){
				String[] locPerms = perm.split("[,]");
				for(String locPerm : locPerms){
					if(!permsList.contains(locPerm)){
						permsList.add(locPerm);
					}
				}
			}else{
				if(!permsList.contains(perm)){
					permsList.add(perm);
				}
			}

			aggregatedHash.put(key, permsList);
		}

		return aggregatedHash;
	}

	private String getGroupsQuery(ArrayList<String> groupIds){
		String idString = "";
		final String OR = " or ";

		for(String id : groupIds){
			idString += "id="+id+OR;
		}

		idString = idString.substring(0, idString.lastIndexOf(OR));
		System.out.print("[getGroupsQuery] "+ "select id,perms from groups where "+idString);

		return "select id,perms from groups where "+idString;
	}

	@Override
	public HashMap<String, ArrayList<String>> getAggregatedPermsHash(
			ArrayList<String> groupIds) {
		try{
			con.setCacheCallableStatements(false);
			Statement stmt = (Statement) con.createStatement();
			ResultSet result = stmt.executeQuery(getGroupsQuery(groupIds));

			if(result != null){
				HashMap<String, String> permList = new HashMap<String, String>();
				while(result.next()){
					System.out.println("group id => "+result.getInt("id")+" perms => "+Utils.getPrimaryPerms(result.getString("perms")));
					permList.put(result.getInt("id")+"", Utils.getPrimaryPerms(result.getString("perms")));
				}

				return getAggregatedHash(permList);
			}

		}catch(SQLException sql){
			sql.printStackTrace();
		}

		return null;
	}

	@Override
	public boolean isUpdateSuccessful(int queryDT, String tableName, String field, String value, String id) {

		final String APPENDER = "_";
		
		try{
			con.setCacheCallableStatements(false);
			String queryString = "";
			Statement stmt = (Statement) con.createStatement();

			if(queryDT == QueryConstants.INT){
				queryString = "update "+tableName+APPENDER+" set "+field+APPENDER+" = "+value+" where id = "+id;
			}else{
				queryString = "update "+tableName+APPENDER+" set "+field+APPENDER+" = '"+value+"' where id = "+id;
			}

			System.out.println("[isUpdateSuccessful] "+queryString);

			int success = stmt.executeUpdate(queryString);
			if(success >= 0){
				return true;
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}

		return false;
	}

	@Override
	public String getGeneratedReportHandler(ReportGeneratorQueryObject reportObject) {
		
		try{
			con.setCacheCallableStatements(false);
			Statement stmt = (Statement) con.createStatement();
			System.out.println("select "+Utils.getReportGeneratorReturnFields(reportObject)+" from "+Utils.getReportGeneratorTable(reportObject)+" where "+Utils.getReportGeneratorFilterQuery(reportObject));
			ResultSet result = stmt.executeQuery("select "+Utils.getReportGeneratorReturnFields(reportObject)+" from "+Utils.getReportGeneratorTable(reportObject)+" where "+Utils.getReportGeneratorFilterQuery(reportObject));

			String timestamp = Utils.getTodayDateTime();
			
			if(result != null){
				ReportGenerator report = new ReportGenerator(reportObject.getUserId(), timestamp, reportObject.getTableName(), result, reportObject.getAliasWithDataTypeConstantHash(), reportObject.getReturnFieldsHash(), reportObject.getOrderFieldValueList());
				report.setReportGeneratorHandler(new ReportGeneratorHandler() {
					
					@Override
					public void onGenerateComplete(String fileName) {
						 reportFileName = fileName;
					}
				});
				
				report.run();
				
				System.out.println("reportFilename is => "+reportFileName);
				
				return reportFileName;
			}
			

		}catch(SQLException sql){
			sql.printStackTrace();
		}

		return null;
	}

	@Override
	public ReportFilterLoadObject getReportFilterItemList(ReportGeneratorQueryObject reportObject) {
		try{
			con.setCacheCallableStatements(false);
			Statement stmt = (Statement) con.createStatement();
			System.out.println("select "+Utils.getReportGeneratorReturnFields(reportObject)+" from "+Utils.getReportGeneratorTable(reportObject)+" where status = 'A'");
			ResultSet result = stmt.executeQuery("select "+Utils.getReportGeneratorReturnFields(reportObject)+" from "+Utils.getReportGeneratorTable(reportObject)+" where status = 'A'");

			if(result != null){
				HashMap<String, ArrayList<String>> fieldsHash = new HashMap<String, ArrayList<String>>();
				HashMap<String, DataTypeConstants> fieldsConstantHash = new HashMap<String, DataTypeConstants>();

				for(String field : reportObject.getReturnFieldsHash().keySet()){
					String alias = reportObject.getReturnFieldsHash().get(field);
					
					fieldsHash.put(alias, new ArrayList<String>());
					fieldsConstantHash.put(alias, reportObject.getAliasWithDataTypeConstantHash().get(alias));
				}

				while(result.next()){
					for(String field : fieldsHash.keySet()){
						String value = "";
						
						if(reportObject.getAliasWithDataTypeConstantHash().get(field) == null){
							continue;
						}
						
						switch(reportObject.getAliasWithDataTypeConstantHash().get(field)){
						case BLOB:
							break;
						case DATE:
						case DATETIME:
						case TIME:
						case ALERT:
						case LOGIN:
						case STRING:
							value = result.getString(field);
							break;
						case ID:
						case INT:
							value = ""+result.getInt(field);
							break;
						default:
							break;

						}

						//Grab ArrayList
						if(!fieldsHash.get(field).contains(value)){
							fieldsHash.get(field).add(value);
						}
					}
				}

				return new ReportFilterLoadObject(fieldsHash, fieldsConstantHash);
			}

		}catch(SQLException sql){
			sql.printStackTrace();
		}

		return null;
	}

	@Override
	public boolean isUserSessionActive(String email) {
		try{
			con.setCacheCallableStatements(false);
			Statement stmt = (Statement) con.createStatement();
			ResultSet result = stmt.executeQuery("select id,email from app_logins where email = '"+email+"' and status = 'A'");

			if(result != null){
				while(result.next()){
					return true;
				}
				
				return false;
			}

		}catch(SQLException sql){
			sql.printStackTrace();
		}
		System.gc();	
		return false;
	}

	@Override
	public LicenseObject getLicenseObject() {
		return new LicenseObject(ServerGlobalResources.getInstance().getUserCounts(), ServerGlobalResources.getInstance().getTabCounts());
	}

	@Override
	public ArrayList<String> getAllNotificationEnabledGroups(ArrayList<String> notificationList) {
	
			if(notificationList != null){	
				ArrayList<String> tmpNotificationList = new ArrayList<String>();
				for(String groupId : notificationList){
					if(isNotificationEnabled(groupId)){
						tmpNotificationList.add(groupId);
					}
				}
				
				if(tmpNotificationList.size() == 0){
					return null;
				}
				
				return tmpNotificationList;
			}
		return null;
	}
	
	private boolean isNotificationEnabled(String groupId){
		try{
			con.setCacheCallableStatements(false);
			Statement stmt = (Statement) con.createStatement();
			
			ResultSet result = stmt.executeQuery("select id,is_notification from groups where id = '"+groupId+"' and status = 'A' and is_notification = 'Y'");

			if(result != null){
				while(result.next()){
					return true;
				}
				
				return false;
			}

			
		}catch(SQLException sql){
			
		}
		return false;
	}

	@Override
	public HashMap<String, String> getNotificationInfoHash(ArrayList<BookRowObject> booksList) {
		try{
			con.setCacheCallableStatements(false);
			Statement stmt = (Statement) con.createStatement();
			HashMap<String, String> notiHash = new HashMap<String, String>();
			
			for(BookRowObject obj : booksList){
				System.out.println("[UserServiceImpl=>getNotificationInfoHash] "+"select id,alert_ from "+obj.getName().split(":")[0]+"_"+" where alert_ = 'On' and status = 'A'");
				ResultSet result = stmt.executeQuery("select id,alert_ from "+obj.getName().split(":")[0]+"_"+" where alert_ = 'On' and status = 'A'");

				if(result != null){
					int counter = 0;
					while(result.next()){
						counter ++;
					}
					notiHash.put(obj.getName().split("[:]")[1], ""+counter);
				}
			}
			
			return notiHash;
		}catch(SQLException sql){
			
		}
		return null;
	}

	@Override
	public User getUser(String username, String password) {
		try{
			con.setCacheCallableStatements(false);
			Statement stmt = (Statement) con.createStatement();
			
			ResultSet result = stmt.executeQuery("select id,type from persons where email='"+username+"' and password='"+Utils.getSHA256(username, password)+"' and status='A'");

			if(result != null){
				User user = null;
				while(result.next()){
					String type = result.getString("type");
					String personId = result.getInt("id")+"";
					
					user = getAppUser(personId, type);
				}
				
				return user;
			}

			
		}catch(SQLException sql){
			
		}
		return null;
	}
	
	private User getAppUser(String personId, String type){
		final String ADMIN = "A";
		final String USER  = "U";
		
		try{
			con.setCacheCallableStatements(false);
			Statement stmt = (Statement) con.createStatement();
			
			String query = "";
			if(type.equals(ADMIN)){
				query = "select id, name from admins where person_id="+personId;
			}else if(type.equals(USER)){
				query = "select id, name from users where person_id="+personId;
			}
			
			ResultSet result = stmt.executeQuery(query);

			if(result != null){
				User user = null;
				
				while(result.next()){
					String name = result.getString("name");
					String id = result.getInt("id")+"";
					
					user = new User();
					user.setName(name);
					user.setId(id);
					user.setPersonId(personId);
				}
				
				return user;
			}

			
		}catch(SQLException sql){
			
		}
		return null;
	}

}
