package com.iglobal.bookit.server.utils;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.TreeSet;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.iglobal.bookit.server.DBConnection;
import com.iglobal.bookit.shared.AdminRowObject;
import com.iglobal.bookit.shared.BookColumnObject;
import com.iglobal.bookit.shared.BookRowObject;
import com.iglobal.bookit.shared.DataTypeRowObject;
import com.iglobal.bookit.shared.GroupRowObject;
import com.iglobal.bookit.shared.PermsRowObject;
import com.iglobal.bookit.shared.Person;
import com.iglobal.bookit.shared.QueryConstants;
import com.iglobal.bookit.shared.QueryObject;
import com.iglobal.bookit.shared.QueryOperatorEnum;
import com.iglobal.bookit.shared.ReportGeneratorQueryObject;
import com.iglobal.bookit.shared.SessionsRowObject;
import com.iglobal.bookit.shared.User;
import com.iglobal.bookit.shared.UserRowObject;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;

//import com.google.appengine.api.utils.SystemProperty;
//import com.twilio.sdk.TwilioRestClient;
//import com.twilio.sdk.TwilioRestException;
//import com.twilio.sdk.resource.factory.MessageFactory;
//import com.twilio.sdk.resource.instance.Account;

public class Utils {
	private static final String SALT = "7919";
	private static final String HASH_ALGO = "SHA-256";
	private static Properties props = new Properties();
	private static Session session = Session.getDefaultInstance(props, null);
	private static final String FROM = "eLibrary";
	private static final String EMAIL = "fetz.selby@gmail.com";
	private static SecureRandom random = new SecureRandom(); 
	private static Connection con = DBConnection.getConnection();

	//	private static final String TWILIO_SID = "AC651e6592c4fff5c623fe8c832ce32b33";
	//	private static final String TWILIO_TOKEN = "7af5697b399a9b94cd8a7643f07aef8b";
	//	private static final String TWILIO_NUMBER = "+18478028463";



	public static String getSHA256(String username, String text){
		if(text == null){
			return null;
		}

		String SHA_256 = null;

		text += SALT + username;
		try{
			MessageDigest digest = MessageDigest.getInstance(HASH_ALGO);
			digest.update(text.getBytes(), 0, text.length());
			SHA_256 = new BigInteger(1, digest.digest()).toString(16);
		} catch(NoSuchAlgorithmException e){
			e.printStackTrace();
		}
		return SHA_256;
	}

	public static boolean isDevMode(){
		//		if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Development) {
		//			return true;
		//		}

		return false;
	}

	public static void sendMail(String message, String title, ArrayList<String> emailList){
		try{
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(EMAIL, "admin@eLibrary.com"));

			for(String email : emailList){
				msg.addRecipient(Message.RecipientType.TO, new InternetAddress(email, "Hello!"));
			}

			msg.setSubject(title);
			msg.setText(message);
			Transport.send(msg);
		}catch(AddressException e){

		}catch(MessagingException e){

		}catch(UnsupportedEncodingException e){

		}
	}

	public static void sendMail(String message, String title, String email){
		try{
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(EMAIL, FROM));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(email, email));
			msg.setSubject(title);
			msg.setText(message);
			Transport.send(msg);
		}catch(AddressException e){
			if(isDevMode()){
				System.out.println("Wrong address");
			}
			e.printStackTrace();
		}catch(MessagingException e){
			if(isDevMode()){
				System.out.println("Bad Message");
			}
			e.printStackTrace();
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
	}

	public static String getToken(){
		return new BigInteger(130, random).toString(32);
	}

	public static String getTodayDate(){
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		return dateFormat.format(date);
	}

	public static String getTodayDateTime(){
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:s");

		return dateFormat.format(date);
	}

	public static String getExpireDate(int numberOfDays){
		Date date = new Date();
		date.setDate(date.getDate()+numberOfDays);

		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		return dateFormat.format(date);
	}

	public static ArrayList<String> getExplodedList(String str, String delimiter){
		String[] strArray = str.split(delimiter);

		ArrayList<String> strList = new ArrayList<String>();
		for(String token : strArray){
			strList.add(token);
		}
		return strList;
	}

	public static String getQueryFilterString(ArrayList<QueryObject> queryObjects){
		String filterString = "";
		final String AND = " and ";
		final String OR = " or ";

		int likeCounter = 0;

		for(QueryObject query : queryObjects){
			if(query != null && query.getOperator() != null){
				String field = query.getEnumField().getField();
				String value = query.getValue();

				if(value != null && !value.isEmpty()){
					String operator = query.getOperator().getOperator();

					if(query.getEnumField().getDataType() == QueryConstants.STRING || query.getEnumField().getDataType() == QueryConstants.CHAR){

						switch(query.getOperator()){
						case EQUALS:
							if(likeCounter > 1){

								//Remove the ending and
								if(filterString.length() > 1 && filterString.contains(AND)){
									filterString = filterString.substring(0, filterString.lastIndexOf(AND));
								}

								filterString += ")"+AND+field +operator+"'"+value+"'";
								likeCounter = 0;
							}else{
								filterString += field +operator+"'"+value+"'";
							}
							break;
						case LIKE:
							likeCounter++;

							if(likeCounter == 1){
								filterString += "("+field+operator+"'%"+value+"%'";
							}else{
								filterString += field +operator+"'%"+value+"%'";
							}
							break;
						default:
							break;
						}

						//filterString += field +operator+"'"+value+"'";
					}else if(query.getEnumField().getDataType() == QueryConstants.DATE){
						filterString += field + operator.split("[:]")[0] + "'" + value.split("[:]")[0] + "'"+ AND + field + operator.split("[:]")[1] + "'"+value.split("[:]")[1]+"'";
					}else{
						if(query.getOperator() == QueryOperatorEnum.LIKE){
							filterString += field + operator +"'%"+value+"%'";
						}else{
							filterString += field + operator + value;
						}
					}
					if(query.getJoinOperator() != null){
						filterString += OR;
					}else{
						filterString += AND;
					}
				}
			}
		}

		if(!(filterString.contains("(") && filterString.contains(")"))){
			filterString = filterString.replace("(", "").replace(")", "");
		}

		//Remove the ending and
		if(filterString.length() > 1 && filterString.contains(AND)){
			filterString = filterString.substring(0, filterString.lastIndexOf(AND));
		}

		return filterString;
	}

	public static String getQueryTables(ArrayList<QueryObject> queryObjects){
		String tableString = "";
		final String DELIMITER = ", ";

		TreeSet<String> tables = new TreeSet<String>();
		for(QueryObject query : queryObjects){
			if(query != null){
				if(query.getEnumField().getField().contains(".")){
					tables.add(query.getEnumField().getField().split("[.]")[0]);
				}
			}
		}

		for(String table : tables){
			tableString += table+DELIMITER;
		}

		tableString = tableString.substring(0, tableString.lastIndexOf(DELIMITER));
		return tableString;
	}

	public static String getQueryOrder(ArrayList<QueryObject> queryObjects){
		String orderField = null;
		
		for(QueryObject query : queryObjects){
			if(query != null && query.getOrderField() != null){
				orderField = query.getOrderField();
			}
		}
		System.out.println("order field is "+orderField);
		return orderField;
	}
	
	public static String getQueryReturnFields(ArrayList<QueryObject> queryObjects){
		String queryReturnFields = "";
		final String DELIMITER = ", ";

		for(QueryObject query : queryObjects){
			if(query != null){
				if(query.isReturnableField()){
					queryReturnFields += query.getEnumField().getField() + DELIMITER;
				}
			}
		}

		queryReturnFields = queryReturnFields.substring(0, queryReturnFields.lastIndexOf(DELIMITER));
		return queryReturnFields;
	}

	public static String getCreateTableQuery(String tableName, String bookQueryString){

		if(!tableName.contains(":")){
			return null;
		}

		final String APPENDER = "_";
		final String CREATE_TABLE = "CREATE TABLE "+tableName.split("[:]")[0].toUpperCase().trim()+APPENDER;
		final String PRIMARY_KEY = "PRIMARY KEY (id)";
		final String SEPARATOR = ",";

		final String ID_FIELD = "id int not null auto_increment";
		final String MODIFIED_TS_FIELD = "modified_ts timestamp";
		final String CREATED_TS_FIELD = "created_ts datetime not NULL";
		final String STATUS_FIELD = "status char(1) default 'A'";

		String fields = ID_FIELD+SEPARATOR+MODIFIED_TS_FIELD+SEPARATOR+CREATED_TS_FIELD+SEPARATOR+STATUS_FIELD+SEPARATOR;

		for(String columnString : bookQueryString.split(",")){
			fields += getColumnCreationString(columnString)+SEPARATOR;
		}

		fields = fields+PRIMARY_KEY;

		return CREATE_TABLE+"("+fields+")";
	}

	private static String getColumnCreationString(String columnString){
		final String APPENDER = "_";

		String[] tokens = columnString.split(":");

		//col:col:datatype:status:T
		if(tokens.length == 5){

			String columnName = tokens[0].trim()+APPENDER;
			BookColumnObject dataType = getBookColumnObject(tokens[2]);

			return columnName +" "+dataType.getDataType();
		}

		return null;
	}

	public static String getAlterTableQuery(String tableName, String bookQueryString){

		//ALTER TABLE contacts ADD email VARCHAR(60);
		final String APPENDER = "_";
		final String ADD = " add ";
		final String ALTER_TABLE = "ALTER TABLE "+tableName.split("[:]")[0].trim()+APPENDER;
		final String SEPARATOR = ",";

		String fields = ALTER_TABLE+ADD;

		for(String columnString : bookQueryString.split(",")){
			fields += getColumnCreationString(columnString)+SEPARATOR+ADD;
		}

		//fields = fields.substring(0, fields.lastIndexOf(ADD));
		return fields.substring(0, fields.lastIndexOf(SEPARATOR+ADD));
	}

	public static BookColumnObject getBookColumnObject(String id){
		PreparedStatement prstmt = null;

		try{
			prstmt = (PreparedStatement) con.prepareStatement("select id,alias,table_name,column_name,dataType,status,description from dataTypes where id = ?");
			prstmt.setInt(1, Integer.parseInt(id));

			ResultSet result = prstmt.executeQuery();
			if(result != null){
				while(result.next()){
					String tmpid = ""+result.getInt("id");
					String alias = result.getString("alias");
					String tableName = result.getString("table_name");
					String columnName = result.getString("column_name");
					String dataType = result.getString("dataType");
					String status = result.getString("status");
					String description = result.getString("description");

					//FIXME Please fix this hack, could excalate
					if(dataType.equals("TIME")){
						dataType = "TEXT";
					}else if(dataType.equals("ALERT")){
						dataType = "TEXT";
					}else if(dataType.equals("ID")){
						dataType = "INT";
					}else if(dataType.equals("LOGIN")){
						dataType = "TEXT";
					}

					return new BookColumnObject(tmpid, alias, dataType, status, tableName, columnName, description);
				}
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}

		return null;
	}

	public static User getUser(ResultSet result, ArrayList<QueryObject> queryObjects){

		User user = new User();
		try{
			for(QueryObject query : queryObjects){
				String field = query.getEnumField().getField();

				switch(query.getEnumField()){
				case U_ID:
					user.setId(""+result.getInt(field));
					break;
				case U_NAME:
					user.setName(result.getString(field));
					break;
				case A_ID:
					user.setId(""+result.getInt(field));
					break;
				case A_NAME:
					user.setName(result.getString(field));
					break;
				case A_PERMS:
					user.setPermsList(Utils.getExplodedList(Utils.getPrimaryPerms(result.getString(field)), ","));
					break;
				case A_PERSON_ID:
					user.setPersonId(""+result.getInt(field));
					break;
				case U_GROUPS:
					user.setPermsList(Utils.getExplodedList(result.getString(field), ","));
					break;
				case U_PERSON_ID:
					user.setPersonId(""+result.getInt(field));
					break;
				case PER_EMAIL:
					user.setEmail(result.getString(field));
					break;
				case PER_TYPE:
					user.setType(result.getString(field));
					break;
				case PER_PASSWORD:
					user.setPassword(result.getString(field));
					break;
				case PER_ID:
					user.setPersonId(""+result.getInt(field));
					break;
				case A_IS_SA:
					if(result.getString(field).trim().equals("N")){
						user.setSuperUser(false);
					}else if(result.getString(field).trim().equals("Y")){
						user.setSuperUser(true);
					}
				default:
					break;
				}

			}
			return user;

		}catch(SQLException sql){}
		return user;
	}

	public static Person getPerson(ResultSet result, ArrayList<QueryObject> queryObjects){

		Person person = new Person();
		try{
			for(QueryObject query : queryObjects){
				String field = query.getEnumField().getField();

				switch(query.getEnumField()){
				case PER_ID:
					person.setId(""+result.getInt(field));
					break;
				case PER_EMAIL:
					person.setEmail(result.getString(field));
					break;
//				case PER_CREATED_TS:
//					person.setCreatedTs(result.getString(field));
//					break;
				case PER_TYPE:
					person.setType(result.getString(field));
					break;
				case PER_STATUS:
					person.setStatus(result.getString(field));
					break;
				case PER_PASSWORD:
					person.setPassword(result.getString(field));
					break;
				default:
					break;
				}

			}
			return person;

		}catch(SQLException sql){}
		return person;
	}

	public static AdminRowObject getAdminRowObject(ResultSet result, ArrayList<QueryObject> queryObjects){

		AdminRowObject object = new AdminRowObject();
		try{
			for(QueryObject query : queryObjects){
				String field = query.getEnumField().getField();

				switch(query.getEnumField()){
				case A_ID:
					object.setId(""+result.getInt(field));
					break;
				case A_NAME:
					object.setName(result.getString(field));
					break;
				case PER_EMAIL:
					object.setEmail(result.getString(field));
					break;
				case A_STATUS:
					object.setStatus(result.getString(field));
					break;
				case A_CREATED_TS:
					object.setDate(result.getString(field));
					break;
				case S_NAME:
					object.setCreatedBy(result.getString(field));
					break;
				case A_PERMS:
					//object.setPerms(result.getString(field));
					object.setPerms(Utils.getPrimaryPerms(result.getString(field)));
					break;
				case PER_ID:
					object.setPersonId(""+result.getInt(field));
					break;
				case PER_PASSWORD:
					object.setSystemPassword(result.getString(field));
					object.setPassword(result.getString(field));
					break;
				case A_IS_SA:
					if(result.getString(field).trim().equals("Y")){
						object.setSuperAdmin(true);
					}else if(result.getString(field).trim().equals("N")){
						object.setSuperAdmin(false);
					}
				default:
					break;
				}
			}

			return object;
		}catch(SQLException sql){
			sql.printStackTrace();
		}

		return null;
	}

	public static BookRowObject getBookRowObject(ResultSet result, ArrayList<QueryObject> queryObjects){
		BookRowObject object = new BookRowObject();
		try{
			for(QueryObject query : queryObjects){
				String field = query.getEnumField().getField();

				switch(query.getEnumField()){
				case B_ID:
					object.setId(""+result.getInt(field));
					break;
				case B_NAME:
					object.setName(result.getString(field));
					break;
				case B_STATUS:
					object.setStatus(result.getString(field));
					break;
				case B_CREATED_TS:
					object.setDate(result.getString(field));
					break;
				case A_NAME:
					object.setCreatedBy(result.getString(field));
					break;
				case B_GROUPS:
					object.setGroups(result.getString(field));
					break;
				case B_TABLE_NAME:
					object.setName(result.getString(field));
					break;
				case B_TABLE_COL:
					object.setColumnString(result.getString(field));
					break;
				case B_MODIFIED_TS:
					object.setModifiedTs(result.getString(field));
					break;
				case B_CREATED_BY:
					object.setCreatedBy(result.getString(field));
					break;
				default:
					break;
				}
			}

			return object;
		}catch(SQLException sql){
			sql.printStackTrace();
		}

		return null;
	}

	public static GroupRowObject getGroupRowObject(ResultSet result, ArrayList<QueryObject> queryObjects){
		GroupRowObject object = new GroupRowObject();
		try{
			for(QueryObject query : queryObjects){
				String field = query.getEnumField().getField();

				switch(query.getEnumField()){
				case G_ID:
					object.setId(""+result.getInt(field));
					break;
				case G_NAME:
					object.setName(result.getString(field));
					break;
				case G_STATUS:
					object.setStatus(result.getString(field));
					break;
				case G_PERMS:
					//object.setPerms(result.getString(field));
					object.setPerms(Utils.getPrimaryPerms(result.getString(field)));
					break;
				case A_NAME:
					object.setCreatedBy(result.getString(field));
					break;
				case G_CREATED_TS:
					object.setDate(result.getString(field));
					break;
				case G_IS_NOTIFICATION:
					if(result.getString(field).equals("Y")){
						object.setNotification(true);
					}else{
						object.setNotification(false);
					}
				default:
					break;
				}
			}

			return object;
		}catch(SQLException sql){
			sql.printStackTrace();
		}

		return null;
	}

	public static UserRowObject getUserRowObject(ResultSet result, ArrayList<QueryObject> queryObjects){
		UserRowObject object = new UserRowObject();
		try{
			for(QueryObject query : queryObjects){
				String field = query.getEnumField().getField();

				switch(query.getEnumField()){
				case U_ID:
					object.setId(""+result.getInt(field));
					break;
				case U_NAME:
					object.setName(result.getString(field));
					break;
				case U_STATUS:
					object.setStatus(result.getString(field));
					break;
				case U_GROUPS:
					object.setGroups(result.getString(field));
					break;
				case A_NAME:
					object.setCreatedBy(result.getString(field));
					break;
				case U_CREATED_TS:
					object.setDate(result.getString(field));
					break;
//				case PER_CREATED_TS:
//					object.setDate(result.getString(field));
//					break;
				case PER_EMAIL:
					object.setEmail(result.getString(field));
					break;
				case PER_ID:
					object.setPersonId(""+result.getInt(field));
					break;
				case PER_PASSWORD:
					object.setSystemPassword(result.getString(field));
					object.setPassword(result.getString(field));
					break;
				default:
					break;
				}
			}

			return object;
		}catch(SQLException sql){
			sql.printStackTrace();
		}

		return null;
	}

	public static PermsRowObject getPermsRowObject(ResultSet result, ArrayList<QueryObject> queryObjects){
		PermsRowObject object = new PermsRowObject();
		try{
			for(QueryObject query : queryObjects){
				String field = query.getEnumField().getField();

				switch(query.getEnumField()){
				case P_ID:
					object.setId(""+result.getInt(field));
					break;
				case P_NAME:
					object.setName(result.getString(field));
					break;
				case P_STATUS:
					object.setStatus(result.getString(field));
					break;
				case P_STRING:
					object.setString(result.getString(field));
					break;
				case P_CATEGORY:
					object.setCategory(result.getString(field));
					break;
				default:
					break;
				}
			}

			return object;
		}catch(SQLException sql){
			sql.printStackTrace();
		}

		return null;
	}

	public static DataTypeRowObject getDataTypeRowObject(ResultSet result, ArrayList<QueryObject> queryObjects){
		DataTypeRowObject object = new DataTypeRowObject();
		try{
			for(QueryObject query : queryObjects){
				String field = query.getEnumField().getField();

				switch(query.getEnumField()){
				case DT_ID:
					object.setId(""+result.getInt(field));
					break;
				case DT_TABLE_NAME:
					object.setTableName(result.getString(field));
					break;
				case DT_COLUMN_NAME:
					object.setColumnName(result.getString(field));
					break;
				case DT_ALIAS:
					object.setAlias(result.getString(field));
					break;
				case DT_DATATYPE:
					object.setDataType(result.getString(field));
					break;
				case DT_DESC:
					object.setDescription(result.getString(field));
					break;
				case DT_STATUS:
					object.setStatus(result.getString(field));
				default:
					break;
				}
			}

			return object;
		}catch(SQLException sql){
			sql.printStackTrace();
		}

		return null;
	}
	
	public static SessionsRowObject getSessionsRowObject(ResultSet result, ArrayList<QueryObject> queryObjects){
		SessionsRowObject object = new SessionsRowObject();
		try{
			for(QueryObject query : queryObjects){
				String field = query.getEnumField().getField();

				switch(query.getEnumField()){
				case LOGIN_ID:
					object.setId(""+result.getInt(field));
					break;
				case LOGIN_EMAIL:
					object.setEmail(result.getString(field));
					break;
				case LOGIN_START_TIME:
					object.setStartDateTime(result.getString(field));
					break;
				case LOGIN_END_TIME:
					object.setEndDateTime(result.getString(field));
					break;
				case LOGIN_STATUS:
					object.setStatus(result.getString(field));
					break;
				default:
					break;
				}
			}

			return object;
		}catch(SQLException sql){
			sql.printStackTrace();
		}

		return null;
	}

	public static BufferedImage scale(BufferedImage img, int targetWidth, int targetHeight) {

		int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
		BufferedImage ret = img;
		BufferedImage scratchImage = null;
		Graphics2D g2 = null;

		int w = img.getWidth();
		int h = img.getHeight();

		int prevW = w;
		int prevH = h;

		do {
			if (w > targetWidth) {
				w /= 2;
				w = (w < targetWidth) ? targetWidth : w;
			}

			if (h > targetHeight) {
				h /= 2;
				h = (h < targetHeight) ? targetHeight : h;
			}

			if (scratchImage == null) {
				scratchImage = new BufferedImage(w, h, type);
				g2 = scratchImage.createGraphics();
			}

			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2.drawImage(ret, 0, 0, w, h, 0, 0, prevW, prevH, null);

			prevW = w;
			prevH = h;
			ret = scratchImage;
		} while (w != targetWidth || h != targetHeight);

		if (g2 != null) {
			g2.dispose();
		}

		if (targetWidth != ret.getWidth() || targetHeight != ret.getHeight()) {
			scratchImage = new BufferedImage(targetWidth, targetHeight, type);
			g2 = scratchImage.createGraphics();
			g2.drawImage(ret, 0, 0, null);
			g2.dispose();
			ret = scratchImage;
		}

		return ret;
	}

	public static String getCombinedPerms(String primaryPerm){
		final String READ = "1", WRITE = "2", UPDATE = "3", READ_WRITE = "4", READ_UPDATE = "5", WRITE_UPDATE = "6", ALL = "7";

		if(primaryPerm.contains(READ) && primaryPerm.contains(WRITE) && primaryPerm.contains(UPDATE)){
			return ALL;
		}else if(primaryPerm.contains(READ) && primaryPerm.contains(WRITE)){
			return READ_WRITE;
		}else if(primaryPerm.contains(READ) && primaryPerm.contains(UPDATE)){
			return READ_UPDATE;
		}else if(primaryPerm.contains(WRITE) && primaryPerm.contains(UPDATE)){
			return WRITE_UPDATE;
		}
		return primaryPerm;
	}

	public static String getPrimaryPerms(String combinedPerms){
		final String READ = "1", WRITE = "2", UPDATE = "3", READ_WRITE = "4", READ_UPDATE = "5", WRITE_UPDATE = "6", ALL = "7";

		if(combinedPerms.trim().equals(READ_WRITE)){
			return READ+","+WRITE;
		}else if(combinedPerms.trim().equals(READ_UPDATE)){
			return READ+","+UPDATE;
		}else if(combinedPerms.trim().equals(WRITE_UPDATE)){
			return WRITE+","+UPDATE;
		}else if(combinedPerms.trim().equals(ALL)){
			return READ+","+WRITE+","+UPDATE;
		}
		
		return combinedPerms;
	}
	
	public static String getPermPrimaryCharacterString(String permString){
		final String READ = "1", WRITE = "2", UPDATE = "3";

		String str = "";
		
		if(permString.contains(READ)){
			str += "R,";
		}
		
		if(permString.contains(WRITE)){
			str += "W,";
		}
		
		if(permString.contains(UPDATE)){
			str += "U,";
		}
		
		if(permString.length() > 0){
			str = str.substring(0, str.lastIndexOf(","));
		}
		
		System.out.println("[getPermPrimaryCharacterString] primary perms is "+str);
		return str;
	}
	
	public static String getReportGeneratorTable(ReportGeneratorQueryObject queryObject){
		final String APPENDER = "_";
		
		if(queryObject != null){
			return queryObject.getTableName().trim()+APPENDER;
		}
		return null;
	}
	
	public static String getReportGeneratorFilterQuery(ReportGeneratorQueryObject queryObject){
		final String APPENDER = "_";
		
		if(queryObject.getFilterWithValueHash() != null){
			final String AND = " and ";
			String query = "";
			
			for(String filter : queryObject.getFilterWithValueHash().keySet()){
				String value = queryObject.getFilterWithValueHash().get(filter);
				//query += filter+" = "+value+AND;
				
				if(queryObject.getFieldsWithDataTypeConstantHash().get(filter) == null){
					System.out.println("^^^ Bad filter[getReportGeneratorFilterQuery] "+filter);
					continue;
				}
				
				switch(queryObject.getFieldsWithDataTypeConstantHash().get(filter)){
				case INT:
					query += filter.trim()+APPENDER+" = "+value+AND;
					break;
				case STRING:
				//case DATE:
				case DATETIME:
				case TIME:
					query += filter.trim()+APPENDER+" = '"+value+"'"+AND;
					break;
				case DATE:
					String returnQuery = getRangeDate(filter, value);
					if(!returnQuery.trim().isEmpty()){
						query += getRangeDate(filter, value)+AND;
					}
					break;
				case BLOB:
					break;
				default:
					break;
				}
			}
			
			query += "status"+" = 'A'";
			
			return query;
		}
		return "status = 'A'";
	}
	
	private static String getRangeDate(String field, String value){
		
		//SELECT * FROM util_audit WHERE `DATED` BETWEEN "2012-03-15" AND "2012-03-31";
		
		//When date is empty
		String query = "";
		String action = value.split("[:]")[0];
		
		if(value.trim().isEmpty() || action.trim().equals(QueryConstants.EMPTY_DATE)){
			return "";
		}
		
		String dateValue = value.split("[:]")[1];
		
		if(action.trim().equals(QueryConstants.START_DATE)){
			query = field+" >= date('"+dateValue+"')";
		}else if(action.trim().equals(QueryConstants.END_DATE)){
			query = field+" <= date('"+dateValue+"')";
		}else if(action.trim().equals(QueryConstants.BOTH_DATE)){
			String startDate = value.split("[:]")[1];
			String endDate = value.split("[:]")[2];
			
			query = field+" >= date('"+startDate+"') and "+field+" <= date('"+endDate+"')";
		}else if(action.trim().equals(QueryConstants.EMPTY_DATE)){
			query = "";
		}
		
		return query;
	}
	
	public static String getReportGeneratorReturnFields(ReportGeneratorQueryObject queryObject){
		final String APPENDER = "_";
		if(queryObject.getReturnFieldsHash() != null){
			final String COMMA = ",";
			String query = "";
			
			for(String field : queryObject.getReturnFieldsHash().keySet()){
				String value = queryObject.getReturnFieldsHash().get(field);
				
				//Exception to created_ts
				if(field.trim().equals(QueryConstants.CREATED_TS)){
					query += field.trim()+" as '"+value+"'"+COMMA;
				}else{
					query += field.trim()+APPENDER+" as '"+value+"'"+COMMA;
				}
			}
			
			return query.substring(0, query.lastIndexOf(COMMA));
		}
		
		return null;
	}
}
