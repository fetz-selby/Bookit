package com.iglobal.bookit.server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.iglobal.bookit.client.AdminNewService;
import com.iglobal.bookit.server.utils.TableQueryMagic;
import com.iglobal.bookit.server.utils.Utils;
import com.iglobal.bookit.shared.AdminRowObject;
import com.iglobal.bookit.shared.BookRowObject;
import com.iglobal.bookit.shared.GroupRowObject;
import com.iglobal.bookit.shared.UserRowObject;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

@SuppressWarnings("serial")
public class AdminNewServiceImpl extends RemoteServiceServlet implements AdminNewService{

	private static Connection con = DBConnection.getConnection();
	private ArrayList<String> booksList;

	@Override
	public boolean isGroupsCreated(GroupRowObject object, int userId) {
		PreparedStatement prstmt = null;

		try{
			con.setCachePreparedStatements(false);
			con.setAutoCommit(false);
			prstmt = (PreparedStatement) con.prepareStatement("insert into groups (name,created_by,perms,status,created_ts,modified_by,is_notification) values (?, ?, ?, ?, ?, ?, ?)");
			prstmt.setString(1, object.getName());
			prstmt.setInt(2, Integer.parseInt(object.getCreatedBy()));
			//prstmt.setString(3, object.getPerms());
			if(object.getPerms().contains(",")){
				prstmt.setString(3, Utils.getCombinedPerms(object.getPerms()));
			}else{
				prstmt.setString(3, object.getPerms());
			}

			prstmt.setString(4, object.getStatus());
			prstmt.setString(5, getCurrentTimestamp());
			prstmt.setInt(6, userId);
			
			if(object.isNotification()){
				prstmt.setString(7, "Y");
			}else{
				prstmt.setString(7, "N");
			}

			int success = prstmt.executeUpdate();
			if(success >= 0){
				con.commit();
				return true;
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean isUsersCreated(UserRowObject object, int userId) {
		PreparedStatement prstmt = null;

		try{
			int personId = getNewPersonId(object.getEmail(), object.getPassword(), "U", object.getStatus());
			if(personId == 0l){
				return false;
			}
			con.setCachePreparedStatements(false);
			con.setAutoCommit(false);
			prstmt = (PreparedStatement) con.prepareStatement("insert into users (name,created_by,groups,person_id,status,modified_by,created_ts) values (?, ?, ?, ?, ?, ?, ?)");
			prstmt.setString(1, object.getName());
			prstmt.setInt(2, Integer.parseInt(object.getCreatedBy()));
			prstmt.setString(3, object.getGroups());
			prstmt.setInt(4, personId);
			prstmt.setString(5, object.getStatus());
			prstmt.setInt(6, userId);
			prstmt.setString(7, Utils.getTodayDateTime());

			int success = prstmt.executeUpdate();
			if(success >= 0){
				con.commit();
				return true;
			}

			//con.commit();
		}catch(SQLException sql){
			sql.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}finally{

		}	
		return false;
	}

	@Override
	public boolean isAdminsCreated(AdminRowObject object, int userId) {
		PreparedStatement prstmt = null;

		try{
			int personId = getNewPersonId(object.getEmail(), object.getPassword(), "A", object.getStatus());
			if(personId == 0l){
				return false;
			}
			con.setCachePreparedStatements(false);
			con.setAutoCommit(false);
			prstmt = (PreparedStatement) con.prepareStatement("insert into admins (name,created_by,perms,person_id,status,modified_by,created_ts,is_sa) values (?, ?, ?, ?, ?, ?, ?, ?)");
			prstmt.setString(1, object.getName());
			prstmt.setInt(2, Integer.parseInt(object.getCreatedBy()));
			//prstmt.setString(3, object.getPerms());
			if(object.getPerms().contains(",")){
				prstmt.setString(3, Utils.getCombinedPerms(object.getPerms()));
			}else{
				prstmt.setString(3, object.getPerms());
			}
			prstmt.setInt(4, personId);
			prstmt.setString(5, object.getStatus());
			prstmt.setInt(6, userId);
			prstmt.setString(7, Utils.getTodayDateTime());
			if(object.isSuperAdmin()){
				prstmt.setString(8, "Y");
			}else{
				prstmt.setString(8, "N");
			}

			int success = prstmt.executeUpdate();
			if(success >= 0){
				con.commit();
				return true;
			}

			//con.commit();
		}catch(SQLException sql){
			sql.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}finally{

		}	
		return false;
	}

	private int getNewPersonId(String email, String password, String type, String status){
		PreparedStatement prstmt = null;

		try{
			con.setCachePreparedStatements(false);
			prstmt = (PreparedStatement) con.prepareStatement("insert into persons (email,password,type,status) values (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			prstmt.setString(1, email);
			prstmt.setString(2, Utils.getSHA256(email, password));
			prstmt.setString(3, type);
			prstmt.setString(4, status);

			int success = prstmt.executeUpdate();
			if(success >= 0){
				ResultSet tmpResultSet = prstmt.getGeneratedKeys();
				while(tmpResultSet.next()){
					return tmpResultSet.getInt(1);
				}
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}
		return 0;
	}

	private String getCurrentTimestamp(){
		Date date = new Date();
		SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");

		return ft.format(date);
	}

	@Override
	public boolean isBooksCreated(BookRowObject object) {

		if(object == null || isTableExits(object.getName().toLowerCase().trim())){
			return false;
		}

		PreparedStatement prstmt = null;

		try{
			if(doCreateNewBook(object)){
				
				con.setCachePreparedStatements(false);
				con.setAutoCommit(false);
				prstmt = (PreparedStatement) con.prepareStatement("insert into books (table_name,column_name,groups,display,created_by,created_ts,modified_by,status) values (?, ?, ?, ?, ?, ?, ?, ?)");
				prstmt.setString(1, object.getName().trim().toUpperCase());
				prstmt.setString(2, object.getColumnString());
				prstmt.setString(3, object.getGroups());
				prstmt.setString(4, object.getDisplayGrid());
				prstmt.setString(5, object.getCreatedBy());
				prstmt.setString(6, getCurrentTimestamp());
				prstmt.setInt(7, Integer.parseInt(object.getCreatedBy()));
				prstmt.setString(8, object.getStatus());

				int success = prstmt.executeUpdate();
				if(success > 0){
					con.commit();
					return true;
				}
			}



		}catch(SQLException sql){
			sql.printStackTrace();
		}

		return false;
	}

	private boolean doCreateNewBook(BookRowObject object){
		if(object == null){
			return false;
		}

		TableQueryMagic tableQuery = new TableQueryMagic(object.getName(), object.getColumnString(), null);

		try{
			con.setCacheCallableStatements(false);
			
			Statement stmt = (Statement) con.createStatement();
			System.out.println("Create query is :"+tableQuery.getCreateTableQuery());
			int success = stmt.executeUpdate(tableQuery.getCreateTableQuery());
			System.out.println("[******]SUCCESS is "+success);

			if(success == 0){
				System.out.println("Book added[server-side] successfully ****** success was "+success);
				return true;
			}else{
				return false;
			}

		}catch(SQLException sql){
			sql.printStackTrace();
			return false;
		}

		//return false;
	}

	private boolean isTableExits(String tableName){
		try{
			con.setCacheCallableStatements(false);
			Statement stmt = (Statement) con.createStatement();
			//System.out.println("Create query is :"+tableQuery.getCreateTableQuery());
			ResultSet result = stmt.executeQuery("select table_name from books where status = 'A'");

			if(result != null){
				booksList = new ArrayList<String>();
				while(result.next()){
					booksList.add(result.getString("table_name"));
				}

				if(booksList.contains(tableName)){
					return true;
				}else{
					return false;
				}
			}

		}catch(SQLException sql){
			sql.printStackTrace();
		}

		return true;
	}
}
