package com.iglobal.bookit.server;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.iglobal.bookit.server.helpers.EntryObject;
import com.iglobal.bookit.server.utils.Utils;
import com.iglobal.bookit.shared.DataTypeConstants;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

@SuppressWarnings("serial")
public class PostServer extends HttpServlet{
	private Connection con = DBConnection.getConnection();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		super.doGet(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		final String COMMA = ",";
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setFileSizeMax(10000000);

		try {
			ArrayList<FileItem> fileList = new ArrayList<FileItem>();

			List<FileItem> items = upload.parseRequest(req);
			String tableName = "";
			String fields = "";
			String values = "";

			for (FileItem item : items) {
				if (item.isFormField()) {
					System.out.println("Field name is " + item.getFieldName() + ", value is " + item.getString());
					if (item.getFieldName().trim().equals("data")) {
						ArrayList<EntryObject> dataList = getEntryObjectList(item.getString());

						for(EntryObject object : dataList){
							DataTypeConstants dataType = object.getDataType();

							switch(dataType){
							case BLOB:
								break;
							case DATE:
								fields += object.getField()+COMMA;
								values += "'"+object.getValue()+"'"+COMMA;
								break;
							case DATETIME:
								fields += object.getField()+COMMA;
								//values += "'"+object.getValue()+"'"+COMMA;
								break;
							case INT:
								fields += object.getField()+COMMA;
								values += object.getValue()+COMMA;
								break;
							case STRING:
								fields += object.getField()+COMMA;
								values += "'"+object.getValue()+"'"+COMMA;
								break;
							case TIME:
								fields += object.getField()+COMMA;
								values += "'"+object.getValue()+"'"+COMMA;
								break;
							case ALERT:
								fields += object.getField()+COMMA;
								values += "'"+object.getValue()+"'"+COMMA;
								break;
							case LOGIN:
								fields += object.getField()+COMMA;
								values += "'"+object.getValue()+"'"+COMMA;
								break;
							case ID:
								fields += object.getField()+COMMA;
								values += object.getValue()+COMMA;
								break;
							default:
								break;
							}
						}

					}else if(item.getFieldName().trim().equals("table")){
						tableName = item.getString();
					}
				} else {
					System.out.println("***"+item.getFieldName());
					fields += item.getFieldName().trim()+COMMA;
					values += "?"+COMMA;
					fileList.add(item);
				}
				// doCompleteSave(item, resp);
			}
			
			values = values + "'"+Utils.getTodayDateTime()+"'";
			fields = fields + "created_ts";
			
			doDynamicSave(tableName, values, fields, fileList, resp);
			System.out.println("values "+values+"fields "+fields);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void doDynamicSave(String tableName, String values, String fields, ArrayList<FileItem> itemList, HttpServletResponse resp){
		PreparedStatement prstmt = null;

		try{
			con.setAutoCommit(false);
			System.out.println("insert into "+tableName+" ("+fields+") values ("+values+")");
			prstmt = (PreparedStatement) con.prepareStatement("insert into "+tableName+" ("+fields+") values ("+values+")");
			if(itemList != null && itemList.size() > 0){
				int i = 1;
				for(FileItem item : itemList){
					
					try {
						prstmt.setBinaryStream(i, item.getInputStream(), item.getSize());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					
					i ++;
				}
			}

			int success = prstmt.executeUpdate();
			if(success >= 0){
				con.commit();
				resp.setStatus(HttpServletResponse.SC_CREATED);
				try {
					resp.getWriter().print("S");
					resp.flushBuffer();
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println("Entry Added Successfully !!!");
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}

	}
	
	private ArrayList<EntryObject> getEntryObjectList(String list){
		if(list != null && list.contains(",")){
			//name:shallom:TEXT,age:24:INT,address:Akure:TEXT

			String[] fieldsValueTokens = list.split(",");
			ArrayList<EntryObject> entryObjectList = new ArrayList<EntryObject>();

			for(String fieldsValueToken : fieldsValueTokens){
				//name:shallom:TEXT
				if(fieldsValueToken.contains(":")){
					String field = fieldsValueToken.split(":")[0];
					String value = fieldsValueToken.split(":")[1];
					String dataType = fieldsValueToken.split(":")[2];

					if(dataType.trim().equals("INT")){
						entryObjectList.add(new EntryObject(field, value, DataTypeConstants.INT));
					}else if(dataType.trim().equals("TEXT")){
						entryObjectList.add(new EntryObject(field, value, DataTypeConstants.STRING));
					}else if(dataType.trim().equals("MEDIUMBLOB")){
						entryObjectList.add(new EntryObject(field, value, DataTypeConstants.BLOB));
					}else if(dataType.trim().equals("DATE")){
						entryObjectList.add(new EntryObject(field, value, DataTypeConstants.DATE));
					}else if(dataType.trim().equals("TIME")){
						entryObjectList.add(new EntryObject(field, value, DataTypeConstants.TIME));
					}else if(dataType.trim().equals("ALERT")){
						entryObjectList.add(new EntryObject(field, value, DataTypeConstants.ALERT));
					}else if(dataType.trim().equals("ID")){
						entryObjectList.add(new EntryObject(field, value, DataTypeConstants.ID));
					}else if(dataType.trim().equals("LOGIN")){
						entryObjectList.add(new EntryObject(field, value, DataTypeConstants.LOGIN));
					}
				}
			}

			return entryObjectList;
		}

		return null;
	}

}



//if (item.getFieldName().trim().equals("party")) {
//	name = item.getString();
//} else if (item.getFieldName().trim().equals("color")) {
//	color = item.getString();
//} else if (item.getFieldName().trim().equals("idswitch")) {
//	idSwitch = item.getString();
//} else if (item.getFieldName().trim().equals("id")) {
//	id = item.getString();
//} else if (item.getFieldName().trim().equals("checker")) {
//	checker = item.getString();
//}
