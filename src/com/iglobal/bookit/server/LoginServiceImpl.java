package com.iglobal.bookit.server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.iglobal.bookit.client.LoginService;
import com.iglobal.bookit.server.constants.Constants;
import com.iglobal.bookit.server.utils.QueryMagic;
import com.iglobal.bookit.server.utils.Utils;
import com.iglobal.bookit.shared.Admin;
import com.iglobal.bookit.shared.IsUser;
import com.iglobal.bookit.shared.LoggedInUser;
import com.iglobal.bookit.shared.MaximumUserLimit;
import com.iglobal.bookit.shared.Person;
import com.iglobal.bookit.shared.QueryObject;
import com.iglobal.bookit.shared.User;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

@SuppressWarnings("serial")
public class LoginServiceImpl extends RemoteServiceServlet implements LoginService{

	private static Connection con = DBConnection.getConnection();
	private static int userCounter = 0;

	public LoginServiceImpl(){
		initLoggedInUserCount();
	}
	
	private void initLoggedInUserCount(){
		PreparedStatement prstmt = null;
		try{
			con.setCachePreparedStatements(false);
			con.setAutoCommit(false);
			prstmt = (PreparedStatement) con.prepareStatement("select id from app_logins where status = ?");
			prstmt.setString(1, "A");

			ResultSet result = prstmt.executeQuery();
			con.commit();
			if(result != null){
				while(result.next()){
					userCounter ++;
				}
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}

	}
	
	private IsUser getSpecificUser(Person person){
		System.out.println("ID is "+ person.getId());

		System.out.println("Type is "+ person.getType());

		PreparedStatement prstmt = null;
		try{
			if(person.getType().equals(Constants.USER)){
				con.setCachePreparedStatements(false);
				con.setAutoCommit(false);
				prstmt = (PreparedStatement) con.prepareStatement("select id, name, groups from users where person_id = ?");
				prstmt.setString(1, person.getId());

				ResultSet result = prstmt.executeQuery();
				con.commit();
				if(result != null){
					User user = new User();
					while(result.next()){
						String name = result.getString("name");
						String id = ""+result.getInt("id");
						ArrayList<String> rolesList = Utils.getExplodedList(result.getString("groups"), ",");

						user.setId(id);
						user.setName(name);
						user.setPermsList(rolesList);
						user.setType(person.getType());
						user.setEmail(person.getEmail());
						user.setPersonId(person.getId());
						user.setPassword(person.getPassword());
					}
					System.out.println("Got some feed back!");
					return user;
				}

			}else if(person.getType().equals(Constants.ADMIN)){
				con.setCachePreparedStatements(false);
				con.setAutoCommit(false);
				prstmt = (PreparedStatement) con.prepareStatement("select id, name, perms from admins where person_id = ?");
				prstmt.setString(1, person.getId());

				ResultSet result = prstmt.executeQuery();
				con.commit();
				if(result != null){
					Admin admin = new Admin();
					while(result.next()){
						String name = result.getString("name");
						String id = ""+result.getInt("id");
						ArrayList<String> rolesList = Utils.getExplodedList(result.getString("perms"), ",");

						admin.setId(id);
						admin.setName(name);
						admin.setPermsList(rolesList);
						admin.setType(person.getType());
						admin.setEmail(person.getEmail());
						admin.setPersonId(person.getId());
						admin.setPassword(person.getPassword());
					}
					System.out.println("Got some feed back!");
					return admin;
				}
			}

		}catch(SQLException sql){
			sql.printStackTrace();
		}
		return null;
	}

	@Override
	public IsUser getHasQueryUser(ArrayList<QueryObject> queryList, QueryObject userQueryObject, QueryObject passwordQuery) {
		System.out.println("Password = "+Utils.getSHA256(userQueryObject.getValue(), passwordQuery.getValue()));

		try{
			
			con.setAutoCommit(false);
			con.setCacheCallableStatements(false);

			passwordQuery.setValue(Utils.getSHA256(userQueryObject.getValue(), passwordQuery.getValue()));

			queryList.add(passwordQuery);
			queryList.add(userQueryObject);
			QueryMagic queryMagic = new QueryMagic(queryList);

			System.out.println("select "+queryMagic.getQueryReturnFieldsString()+" from "+queryMagic.getQueryTablesString()+" where "+queryMagic.getQueryFilterString());

			Statement stmt = (Statement) con.createStatement();
			ResultSet result = stmt.executeQuery("select "+queryMagic.getQueryReturnFieldsString()+" from "+queryMagic.getQueryTablesString()+" where "+queryMagic.getQueryFilterString());
			con.commit();
			if(result != null){
				Person person = null;
				IsUser user = null;
				while(result.next()){
					person = Utils.getPerson(result, queryMagic.getQueryList());

					break;
				}

				if(person != null){
					user = getSpecificUser(person);

					if(user instanceof User && hasSessionAlready(user.getEmail())){
						System.out.println("User already logged in");
						return new LoggedInUser();
					}

					//Register presence.
					if((userCounter+1) <= Integer.parseInt(ServerGlobalResources.getInstance().getUserCounts())){
						System.out.println("Counter assertion ");
					}
					
					if(user instanceof User && ((userCounter+1) <= Integer.parseInt(ServerGlobalResources.getInstance().getUserCounts()))){
						if(isUserPresenceNoticed(user.getEmail())){
							userCounter ++;
							return user;
						}
					}else if(user instanceof User && ((userCounter+1) > Integer.parseInt(ServerGlobalResources.getInstance().getUserCounts()))){
						return new MaximumUserLimit();
					}
					

					//Check if is User instance, if so check if already logged in.
				}

				return user;
			}else{
				System.out.println("Result is NULL");
			}

		}catch(SQLException sql){
			sql.printStackTrace();
		}
		System.gc();

		return null;
	}

	private boolean hasSessionAlready(String email){

		PreparedStatement prstmt = null;
		try{
			//con.setAutoCommit(false);
			con.setCachePreparedStatements(false);
			prstmt = (PreparedStatement) con.prepareStatement("select email, status from app_logins where email = ? and status = ?");
			prstmt.setString(1, email);
			prstmt.setString(2, "A");

			ResultSet result = prstmt.executeQuery();
			//con.commit();
			if(result != null){
				while(result.next()){
					System.out.println("[hasSessionAlready]Already logged in");
					return true;
				}
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}

		return false;
	}

	private boolean isUserPresenceNoticed(String email){
		PreparedStatement prstmt = null;

		try{
			con.setCachePreparedStatements(false);
			con.setAutoCommit(false);
			prstmt = (PreparedStatement) con.prepareStatement("insert into app_logins (email,status,start_time) values (?,?,?)");
			prstmt.setString(1, email);
			prstmt.setString(2, "A");
			prstmt.setString(3, Utils.getTodayDateTime());

			int success = prstmt.executeUpdate();
			System.out.println("[isUserPresenceNoticed]Success is "+success);
			con.commit();
			if(success >= 0){
				return true;
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}

		return false;
	}

	@Override
	public boolean isLogout(String email) {
		PreparedStatement prstmt = null;

		try{
			con.setCachePreparedStatements(false);
			con.setAutoCommit(false);
			prstmt = (PreparedStatement) con.prepareStatement("update app_logins set status = ?, end_time = ? where email = ? and status = ?");
			prstmt.setString(1, "D");
			prstmt.setString(2, Utils.getTodayDateTime());
			prstmt.setString(3, email);
			prstmt.setString(4, "A");

			int success = prstmt.executeUpdate();
			System.out.println("[isLogout] success is "+success+", and email is "+email);
			con.commit();
			if(success >= 0){
				userCounter --;
				return true;
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}	

		return false;
	}

}
