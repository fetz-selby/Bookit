package com.iglobal.bookit.server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.iglobal.bookit.client.AdminService;
import com.iglobal.bookit.server.utils.QueryMagic;
import com.iglobal.bookit.server.utils.Utils;
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
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

@SuppressWarnings("serial")
public class AdminServiceImpl extends RemoteServiceServlet implements AdminService{

	private static Connection con = DBConnection.getConnection();

	@Override
	public Book getBook(ArrayList<QueryObject> queryList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getUser(ArrayList<QueryObject> queryList) {
		try{
			System.out.println("[getUser]filter is "+Utils.getQueryFilterString(queryList));
			QueryMagic queryMagic = new QueryMagic(queryList);

			con.setCacheCallableStatements(false);
			Statement stmt = (Statement) con.createStatement();
			
			System.out.println("### select "+queryMagic.getQueryReturnFieldsString()+" from "+queryMagic.getQueryTablesString()+" where "+queryMagic.getQueryFilterString());
			
			ResultSet result = stmt.executeQuery("select "+queryMagic.getQueryReturnFieldsString()+" from "+queryMagic.getQueryTablesString()+" where "+queryMagic.getQueryFilterString());

			if(result != null){
				User user = null;
				while(result.next()){
					user = Utils.getUser(result, queryMagic.getQueryList());
				}
				return user;
			}

		}catch(SQLException sql){
			sql.printStackTrace();
		}
		System.gc();

		return null;
	}

	@Override
	public boolean isEmailExist(ArrayList<QueryObject> queryList) {
		try{
			System.out.println("[isEmailExist]filter is "+Utils.getQueryFilterString(queryList)+", returnables "+Utils.getQueryReturnFields(queryList));
			QueryMagic queryMagic = new QueryMagic(queryList);

			con.setCacheCallableStatements(false);
			Statement stmt = (Statement) con.createStatement();
			
			System.out.println("[isEmailExist]"+ "select "+queryMagic.getQueryReturnFieldsString()+" from "+queryMagic.getQueryTablesString()+" where "+queryMagic.getQueryFilterString());
			ResultSet result = stmt.executeQuery("select "+queryMagic.getQueryReturnFieldsString()+" from "+queryMagic.getQueryTablesString()+" where "+queryMagic.getQueryFilterString());

			if(result != null){
				while(result.next()){

					int id = result.getInt("id");
					if(id >= 0){
						System.out.println("[isEmailExist] Email exist with id "+id);
						return true;
					}else{
						System.out.println("[isEmailExist] Email does not exist with id "+id);
						return false;
					}
				}

				return false;
			}else{
				return true;
			}

		}catch(SQLException sql){
			sql.printStackTrace();
		}
		System.gc();

		return true;
	}

	@Override
	public ArrayList<AdminRowObject> getAdminList(
			ArrayList<QueryObject> queryList) {
		try{
			System.out.println("[getAdminList]filter is "+Utils.getQueryFilterString(queryList));
			QueryMagic queryMagic = new QueryMagic(queryList);
			System.out.println("[Query] "+"select "+queryMagic.getQueryReturnFieldsString()+" from "+queryMagic.getQueryTablesString()+" where "+queryMagic.getQueryFilterString()+(queryMagic.getQueryOrderString() != null ?(" order by "+queryMagic.getQueryOrderString()):""));

			con.setCacheCallableStatements(false);
			Statement stmt = (Statement) con.createStatement();
			ResultSet result = stmt.executeQuery("select "+queryMagic.getQueryReturnFieldsString()+" from "+queryMagic.getQueryTablesString()+" where "+queryMagic.getQueryFilterString()+(queryMagic.getQueryOrderString() != null ?(" order by "+queryMagic.getQueryOrderString()):""));

			if(result != null){
				ArrayList<AdminRowObject> list = new ArrayList<AdminRowObject>();
				while(result.next()){
					AdminRowObject admin = Utils.getAdminRowObject(result, queryMagic.getQueryList());
					list.add(admin);
				}
				return list;
			}

		}catch(SQLException sql){
			sql.printStackTrace();
		}
		System.gc();

		return null;
	}

	@Override
	public ArrayList<UserRowObject> getUserList(ArrayList<QueryObject> queryList) {
		try{
			QueryMagic queryMagic = new QueryMagic(queryList);

			con.setCacheCallableStatements(false);
			Statement stmt = (Statement) con.createStatement();
			System.out.println("select "+queryMagic.getQueryReturnFieldsString()+" from "+queryMagic.getQueryTablesString()+" where "+queryMagic.getQueryFilterString()+(queryMagic.getQueryOrderString() != null ?(" order by "+queryMagic.getQueryOrderString()):""));
			ResultSet result = stmt.executeQuery("select "+queryMagic.getQueryReturnFieldsString()+" from "+queryMagic.getQueryTablesString()+" where "+queryMagic.getQueryFilterString()+(queryMagic.getQueryOrderString() != null ?(" order by "+queryMagic.getQueryOrderString()):""));

			if(result != null){
				ArrayList<UserRowObject> list = new ArrayList<UserRowObject>();
				while(result.next()){
					UserRowObject user = Utils.getUserRowObject(result, queryMagic.getQueryList());
					list.add(user);
				}
				return list;
			}

		}catch(SQLException sql){
			sql.printStackTrace();
		}
		System.gc();

		return null;
	}

	@Override
	public ArrayList<BookRowObject> getBookList(ArrayList<QueryObject> queryList) {
		try{
			QueryMagic queryMagic = new QueryMagic(queryList);

			Statement stmt = (Statement) con.createStatement();
			System.out.println("select "+queryMagic.getQueryReturnFieldsString()+" from "+queryMagic.getQueryTablesString()+" where "+queryMagic.getQueryFilterString()+(queryMagic.getQueryOrderString() != null ?(" order by "+queryMagic.getQueryOrderString()):""));
			ResultSet result = stmt.executeQuery("select "+queryMagic.getQueryReturnFieldsString()+" from "+queryMagic.getQueryTablesString()+" where "+queryMagic.getQueryFilterString()+(queryMagic.getQueryOrderString() != null ?(" order by "+queryMagic.getQueryOrderString()):""));

			if(result != null){
				ArrayList<BookRowObject> list = new ArrayList<BookRowObject>();
				while(result.next()){
					BookRowObject book = Utils.getBookRowObject(result, queryMagic.getQueryList());
					list.add(book);
				}
				return list;
			}

		}catch(SQLException sql){
			sql.printStackTrace();
		}
		System.gc();

		return null;	
	}

	@Override
	public ArrayList<GroupRowObject> getGroupList(
			ArrayList<QueryObject> queryList) {
		try{
			QueryMagic queryMagic = new QueryMagic(queryList);
			System.out.println(("select "+queryMagic.getQueryReturnFieldsString()+" from "+queryMagic.getQueryTablesString()+" where "+queryMagic.getQueryFilterString())+(queryMagic.getQueryOrderString() != null ?(" order by "+queryMagic.getQueryOrderString()):""));
			Statement stmt = (Statement) con.createStatement();
			ResultSet result = stmt.executeQuery("select "+queryMagic.getQueryReturnFieldsString()+" from "+queryMagic.getQueryTablesString()+" where "+queryMagic.getQueryFilterString()+(queryMagic.getQueryOrderString() != null ?(" order by "+queryMagic.getQueryOrderString()):""));

			if(result != null){
				ArrayList<GroupRowObject> list = new ArrayList<GroupRowObject>();
				while(result.next()){
					GroupRowObject group = Utils.getGroupRowObject(result, queryMagic.getQueryList());
					if(group.getPerms() != null){
						group.setPermedName(Utils.getPermPrimaryCharacterString(group.getPerms()));
					}
					System.out.println("[getGroupList] PermedName is "+group.getPermedName());
					
					list.add(group);
				}
				System.out.println("Size is "+list.size());
				return list;
			}

		}catch(SQLException sql){
			sql.printStackTrace();
		}
		System.gc();

		return null;
	}

	@Override
	public ArrayList<PermsRowObject> getPermsList(
			ArrayList<QueryObject> queryList) {
		try{
			QueryMagic queryMagic = new QueryMagic(queryList);
			System.out.println(("select "+queryMagic.getQueryReturnFieldsString()+" from "+queryMagic.getQueryTablesString()+" where "+queryMagic.getQueryFilterString()));
			
			con.setCacheCallableStatements(false);
			Statement stmt = (Statement) con.createStatement();
			ResultSet result = stmt.executeQuery("select "+queryMagic.getQueryReturnFieldsString()+" from "+queryMagic.getQueryTablesString()+" where "+queryMagic.getQueryFilterString());

			if(result != null){
				ArrayList<PermsRowObject> list = new ArrayList<PermsRowObject>();
				while(result.next()){
					PermsRowObject group = Utils.getPermsRowObject(result, queryMagic.getQueryList());
					list.add(group);
				}
				System.out.println("length is "+list.size());
				return list;
			}

		}catch(SQLException sql){
			sql.printStackTrace();
		}
		System.gc();

		return null;
	}

	@Override
	public ArrayList<BookColumnObject> getAllBookDataTypesList() {
		try{
			con.setCacheCallableStatements(false);
			Statement stmt = (Statement) con.createStatement();
			ResultSet result = stmt.executeQuery("select id,alias,table_name,column_name,dataType,status,description,is_lockable from dataTypes where status = 'A'");

			if(result != null){
				ArrayList<BookColumnObject> list = new ArrayList<BookColumnObject>();
				while(result.next()){
					String id = ""+result.getInt("id");
					String alias = result.getString("alias");
					String table_name = result.getString("table_name");
					String column_name = result.getString("column_name");
					String dataType = result.getString("dataType");
					String status = result.getString("status");
					String description = result.getString("description");
					boolean isLockable = false;
					if(result.getString("is_lockable") != null && result.getString("is_lockable").trim().equals("T")){
						isLockable = true;
					}
					
					BookColumnObject object = new BookColumnObject(id, alias, dataType, status, table_name, column_name, description);
					object.setLockable(isLockable);
					list.add(object);
				}
				return list;
			}

		}catch(SQLException sql){
			sql.printStackTrace();
		}
		System.gc();	
		return null;
	}

	@Override
	public int getSessionId(String email) {
		if(!isSessionAlreadyActive(email)){
			PreparedStatement prstmt = null;
			
				try{
					con.setCachePreparedStatements(false);
					prstmt = (PreparedStatement) con.prepareStatement("insert into app_logins (email,start_time,status) values (?,?,?)", Statement.RETURN_GENERATED_KEYS);
					prstmt.setString(1, email);
					prstmt.setString(2, Utils.getTodayDateTime());
					prstmt.setString(3, "A");
					
					int success = prstmt.executeUpdate();
					if(success >= 0){
						ResultSet tmpResultSet = prstmt.getGeneratedKeys();
						while(tmpResultSet.next()){
							System.out.println("New session created "+tmpResultSet.getInt(1));
							return tmpResultSet.getInt(1);
						}
					}
				}catch(SQLException sql){
					sql.printStackTrace();
				}
				System.out.println("New session not created ");
				return 0;
		}else{
			System.out.println("Session already active");
		}
		
		return 0;
	}
	
	private boolean isSessionAlreadyActive(String email){
		try{
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
	public boolean isSessionClosed(String id) {
		try{
			con.setCacheCallableStatements(false);
			Statement stmt = (Statement) con.createStatement();
			System.out.println("update app_logins set status = 'D', end_time = '"+Utils.getTodayDateTime()+"' where id = "+id);
			int success = stmt.executeUpdate("update app_logins set status = 'D', end_time = '"+Utils.getTodayDateTime()+"' where id = "+id);

			if(success >= 0){
				System.out.println("Session closed successfully");
				return true;
			}

		}catch(SQLException sql){
			sql.printStackTrace();
		}
		System.gc();	
		return false;
	}

	@Override
	public boolean isTableExist(ArrayList<QueryObject> queryList, String tableName, String id) {
		try{
			System.out.println("[isTableExist]filter is "+Utils.getQueryFilterString(queryList)+", returnables "+Utils.getQueryReturnFields(queryList));
			QueryMagic queryMagic = new QueryMagic(queryList);

			con.setCacheCallableStatements(false);
			Statement stmt = (Statement) con.createStatement();
			
			System.out.println("[isTableExist]"+ "select "+queryMagic.getQueryReturnFieldsString()+" from "+queryMagic.getQueryTablesString());
			ResultSet result = stmt.executeQuery("select "+queryMagic.getQueryReturnFieldsString()+" from "+queryMagic.getQueryTablesString());

			if(result != null){
				while(result.next()){
					String table = result.getString("table_name").split(":")[0];
					String tmpId = ""+result.getInt("id");
					
					if(tableName.toLowerCase().trim().equals(table.toLowerCase().trim()) && id == null){
						return true;
					}else if(tableName.toLowerCase().trim().equals(table.toLowerCase().trim()) && tmpId.equals(id)){
						return false;
					}
				}

				return false;
			}else{
				return true;
			}

		}catch(SQLException sql){
			sql.printStackTrace();
		}
		System.gc();

		return true;
	}

	@Override
	public HashMap<String, String> getAllLoggedInUsers() {
		try{
			con.setCacheCallableStatements(false);
			Statement stmt = (Statement) con.createStatement();
			ResultSet result = stmt.executeQuery("select id,email from app_logins where status = 'A'");

			if(result != null){
				HashMap<String, String> emailMap = new HashMap<String, String>();
				while(result.next()){
					emailMap.put(""+result.getInt("id"), result.getString("email"));
				}
				
				return emailMap;
			}

		}catch(SQLException sql){
			sql.printStackTrace();
		}
		System.gc();	
		return null;
	}

	@Override
	public ArrayList<SessionsRowObject> getSessionsList(ArrayList<QueryObject> queryList) {
		try{
			QueryMagic queryMagic = new QueryMagic(queryList);

			con.setCacheCallableStatements(false);
			Statement stmt = (Statement) con.createStatement();
			System.out.println("select "+queryMagic.getQueryReturnFieldsString()+" from "+queryMagic.getQueryTablesString()+(queryMagic.getQueryFilterString().trim().isEmpty()?" where status = 'A'":"where status = 'A' and "+queryMagic.getQueryFilterString())+(queryMagic.getQueryOrderString() != null ?(" order by "+queryMagic.getQueryOrderString()):""));
			ResultSet result = stmt.executeQuery("select "+queryMagic.getQueryReturnFieldsString()+" from "+queryMagic.getQueryTablesString()+(queryMagic.getQueryFilterString().trim().isEmpty()?" where status = 'A'":" where status = 'A' and "+queryMagic.getQueryFilterString())+(queryMagic.getQueryOrderString() != null ?(" order by "+queryMagic.getQueryOrderString()):""));

			if(result != null){
				ArrayList<SessionsRowObject> list = new ArrayList<SessionsRowObject>();
				while(result.next()){
					SessionsRowObject session = Utils.getSessionsRowObject(result, queryMagic.getQueryList());
					list.add(session);
				}
				return list;
			}

		}catch(SQLException sql){
			sql.printStackTrace();
		}
		System.gc();

		return null;
	}

	@Override
	public VerifierObject getIfGroupNameExist(String groupName) {
		try{
			con.setCacheCallableStatements(false);
			Statement stmt = (Statement) con.createStatement();
			ResultSet result = stmt.executeQuery("select id, name from groups where name = '"+groupName+"'");

			if(result != null){
				while(result.next()){
					return new VerifierObject(""+result.getInt("id"), result.getString("name"), true);
				}
			}else{
				return new VerifierObject("", "", true);
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}
		System.gc();	
		return null;
	}
}
