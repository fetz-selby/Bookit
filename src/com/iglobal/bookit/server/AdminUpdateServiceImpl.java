package com.iglobal.bookit.server;

import java.sql.SQLException;
import java.sql.Statement;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.iglobal.bookit.client.AdminUpdateService;
import com.iglobal.bookit.server.utils.TableQueryMagic;
import com.iglobal.bookit.server.utils.Utils;
import com.iglobal.bookit.shared.AdminRowObject;
import com.iglobal.bookit.shared.BookRowObject;
import com.iglobal.bookit.shared.GroupRowObject;
import com.iglobal.bookit.shared.UserRowObject;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

@SuppressWarnings("serial")
public class AdminUpdateServiceImpl extends RemoteServiceServlet implements AdminUpdateService{

	private static Connection con = DBConnection.getConnection();

	@Override
	public boolean isGroupsUpdated(GroupRowObject object, int userId) {
		PreparedStatement prstmt = null;

		try{
			con.setCachePreparedStatements(false);
			con.setAutoCommit(false);
			prstmt = (PreparedStatement) con.prepareStatement("update groups set name = ?, perms = ?, status = ?, modified_by = ?, is_notification = ? where id = ?");
			prstmt.setString(1, object.getName());
			//prstmt.setString(2, object.getPerms());

			System.out.println("[isGroupsUpdated] perm id => "+object.getPerms());


			if(object.getPerms().trim().contains(",")){
				System.out.println("[isGroupsUpdated] combined is "+Utils.getCombinedPerms(object.getPerms()));
				prstmt.setString(2, Utils.getCombinedPerms(object.getPerms()));
			}else{
				prstmt.setString(2, object.getPerms());
			}

			prstmt.setString(3, object.getStatus());
			prstmt.setInt(4, userId);
			
			if(object.isNotification()){
				prstmt.setString(5, "Y");
			}else{
				prstmt.setString(5, "N");
			}
			
			prstmt.setInt(6, Integer.parseInt(object.getId()));

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
	public boolean isUsersUpdated(UserRowObject object, int userId) {
		PreparedStatement prstmt = null;

		try{
			con.setCachePreparedStatements(false);
			con.setAutoCommit(false);
			if(isPersonUpdated(object.getPersonId(), object.getEmail(), object.getPassword(), object.getSystemPassword(), object.getStatus())){

				prstmt = (PreparedStatement) con.prepareStatement("update users set name = ?, groups = ?, status = ?, modified_by = ? where id = ?");
				prstmt.setString(1, object.getName());
				prstmt.setString(2, object.getGroups());
				prstmt.setString(3, object.getStatus());
				prstmt.setInt(4, userId);
				prstmt.setInt(5, Integer.parseInt(object.getId()));

				int success = prstmt.executeUpdate();
				System.out.println("[User] success is "+success);
				if(success >= 0){
					con.commit();
					return true;
				}
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}		return false;
	}

	@Override
	public boolean isAdminsUpdated(AdminRowObject object, int userId) {
		PreparedStatement prstmt = null;

		try{
			con.setCachePreparedStatements(false);
			con.setAutoCommit(false);
			if(isPersonUpdated(object.getPersonId(), object.getEmail(), object.getPassword(), object.getSystemPassword(), object.getStatus())){

				prstmt = (PreparedStatement) con.prepareStatement("update admins set name = ?, perms = ?, status = ?, modified_by = ?, is_sa = ? where id = ?");
				prstmt.setString(1, object.getName());

				//prstmt.setString(2, object.getPerms());

				System.out.println("[isAdminsUpdated] perm id => "+object.getPerms());

				if(object.getPerms().trim().contains(",")){
					System.out.println("[isAdminsUpdated] combined is "+Utils.getCombinedPerms(object.getPerms()));
					prstmt.setString(2, Utils.getCombinedPerms(object.getPerms()));
				}else{
					prstmt.setString(2, object.getPerms());
				}
				prstmt.setString(3, object.getStatus());
				prstmt.setInt(4, userId);
				if(object.isSuperAdmin()){
					prstmt.setString(5, "Y");
				}else{
					prstmt.setString(5, "N");
				}

				prstmt.setInt(6, Integer.parseInt(object.getId()));

				int success = prstmt.executeUpdate();
				if(success >= 0){
					con.commit();
					return true;
				}
			}


		}catch(SQLException sql){
			sql.printStackTrace();
		}		return false;
	}

	@Override
	public boolean isBooksUpdated(BookRowObject object) {
		PreparedStatement prstmt = null;

		try{
			con.setCachePreparedStatements(false);
			con.setAutoCommit(false);

			if(object.getAddColumnString() == null || object.getAddColumnString().trim().isEmpty()){

				prstmt = (PreparedStatement) con.prepareStatement("update books set table_name = ?, column_name = ?, groups = ?, modified_by = ?, status = ? where id = ?");
				prstmt.setString(1, object.getName());

				if(object.getAddColumnString() != null){
					prstmt.setString(2, object.getColumnString()+","+object.getAddColumnString());
				}else{
					prstmt.setString(2, object.getColumnString());
				}

				prstmt.setString(3, object.getGroups());
				prstmt.setInt(4, Integer.parseInt(object.getModifiedBy()));
				prstmt.setString(5, object.getStatus());
				prstmt.setInt(6, Integer.parseInt(object.getId()));

				int success = prstmt.executeUpdate();

				if(success >= 0){
					con.commit();
					return true;
				}

			}else if(!object.getAddColumnString().trim().isEmpty()){

				if(isAddBookColumns(object)){

					prstmt = (PreparedStatement) con.prepareStatement("update books set table_name = ?, column_name = ?, groups = ?, modified_by = ?, status = ? where id = ?");
					prstmt.setString(1, object.getName());

					if(object.getAddColumnString() != null){
						prstmt.setString(2, object.getColumnString()+","+object.getAddColumnString());
					}else{
						prstmt.setString(2, object.getColumnString());
					}

					prstmt.setString(3, object.getGroups());
					prstmt.setInt(4, Integer.parseInt(object.getModifiedBy()));
					prstmt.setString(5, object.getStatus());
					prstmt.setInt(6, Integer.parseInt(object.getId()));

					int success = prstmt.executeUpdate();

					if(success >= 0){
						con.commit();
						return true;
					}
				}
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}		
		return false;
	}

	private boolean isAddBookColumns(BookRowObject object){
		if(object == null){
			return false;
		}

		TableQueryMagic tableQuery = new TableQueryMagic(object.getName(), object.getColumnString(), object.getAddColumnString());

		try{
			con.setCacheCallableStatements(false);
			Statement stmt = (Statement) con.createStatement();
			System.out.println("Alter query is :"+tableQuery.getAlterColumnQuery());
			int success = stmt.executeUpdate(tableQuery.getAlterColumnQuery());
			System.out.println("SUCCESS is "+success);

			if(success >= 0){
				con.commit();
				System.out.println("Book columns added[server-side] successfully");
				return true;
			}else{
				return false;
			}

		}catch(SQLException sql){
			sql.printStackTrace();
		}

		return false;
	}

	private boolean isPersonUpdated(String id, String email, String password, String systemPassword, String status){
		PreparedStatement prstmt = null;

		try{
			con.setCachePreparedStatements(false);
			con.setAutoCommit(false);
			prstmt = (PreparedStatement) con.prepareStatement("update persons set email = ?, password = ?, status = ? where id = ?");
			prstmt.setString(1, email);
			if(password.equals(systemPassword)){
				prstmt.setString(2, password);
			}else{
				prstmt.setString(2, Utils.getSHA256(email, password));
			}
			prstmt.setString(3, status);
			prstmt.setInt(4, Integer.parseInt(id));

			int success = prstmt.executeUpdate();
			System.out.println("[isPersonUpdated] success is "+success);

			if(success >= 0){
				con.commit();
				return true;
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}
		return false;
	}

//	private String getCurrentTimestamp(){
//		Date date = new Date();
//		SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
//
//		return ft.format(date);
//	}

	@Override
	public boolean isColumnRemoved(String tableName, String columnName) {
		final String APPENDER = "_";
		
		try{
			con.setCacheCallableStatements(false);
			Statement stmt = (Statement) con.createStatement();
			System.out.println("alter table "+tableName.trim().toUpperCase().trim()+APPENDER+" drop column "+columnName+APPENDER);
			int success = stmt.executeUpdate("alter table "+tableName.toUpperCase().trim()+APPENDER+" drop column "+columnName+APPENDER);
			System.out.println("SUCCESS is "+success);

			if(success >= 0){
				//con.commit();
				System.out.println("Book columns removed[server-side] successfully");
				return true;
			}else{
				return false;
			}

		}catch(SQLException sql){
			sql.printStackTrace();
		}

		return false;
	}

	@Override
	public boolean isBookColumnStringPartiallyUpdated(BookRowObject object) {
		PreparedStatement prstmt = null;

		try{
			con.setCachePreparedStatements(false);
			con.setAutoCommit(false);

			if(object.getColumnString() != null && (object.getAddColumnString() == null || object.getAddColumnString().isEmpty())){

				prstmt = (PreparedStatement) con.prepareStatement("update books set table_name = ?, column_name = ?, modified_by = ?, status = ? where id = ?");
				prstmt.setString(1, object.getName());
				prstmt.setString(2, object.getColumnString());
				prstmt.setInt(3, Integer.parseInt(object.getModifiedBy()));
				prstmt.setString(4, object.getStatus());
				prstmt.setInt(5, Integer.parseInt(object.getId()));

				int success = prstmt.executeUpdate();

				if(success >= 0){
					con.commit();
					return true;
				}
			}else{
				if(isAddBookColumns(object)){

					prstmt = (PreparedStatement) con.prepareStatement("update books set table_name = ?, column_name = ?, modified_by = ?, status = ? where id = ?");
					prstmt.setString(1, object.getName());
					prstmt.setString(2, doRemoveForeComma(object.getColumnString()+","+object.getAddColumnString()));
					prstmt.setInt(3, Integer.parseInt(object.getModifiedBy()));
					prstmt.setString(4, object.getStatus());
					prstmt.setInt(5, Integer.parseInt(object.getId()));

					int success = prstmt.executeUpdate();

					if(success >= 0){
						con.commit();
						return true;
					}
				}
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}		
		return false;
	}
	
	private String doRemoveForeComma(String text){
		if(text.startsWith(",")){
			return text.substring(1);
		}
		return text;
	}

}