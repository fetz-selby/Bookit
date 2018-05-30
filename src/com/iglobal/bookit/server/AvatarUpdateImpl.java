package com.iglobal.bookit.server;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

@SuppressWarnings("serial")
public class AvatarUpdateImpl extends HttpServlet{
	private Connection con = DBConnection.getConnection();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		super.doGet(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setFileSizeMax(10000000);
		
		HashMap<String, String> queryMap = new HashMap<String, String>();
		FileItem imageItem = null;

		System.out.println("Got here");

		try{
			List<FileItem> items = upload.parseRequest(req);
			for(FileItem item : items){
				if(item.isFormField()){
					//System.out.println("is form field");
					queryMap.put(item.getFieldName(), item.getString());
				}else{
					//System.out.println("is NOT form field");
					imageItem = item;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		doSaveImage(imageItem, queryMap, resp);
		//super.doPost(req, resp);
	}

	private void doSaveImage(FileItem item, HashMap<String, String> queryMap, HttpServletResponse resp){
		
		if(item == null){
			try {
				resp.getWriter().print("F");
				System.out.println("[doSaveImage] ImageItem is NULL");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return;
		}
		
		PreparedStatement prstmt = null;

		try{

			con.setAutoCommit(false);
			System.out.println("update "+queryMap.get("table_name")+" set "+queryMap.get("column_name")+" = ? where id = ?");
			prstmt = (PreparedStatement) con.prepareStatement("update "+queryMap.get("table_name")+" set "+queryMap.get("column_name")+" = ? where id = ?");

			try{
				prstmt.setBinaryStream(1, item.getInputStream(), item.getSize());
			}catch(IOException ioe){
				ioe.printStackTrace();
			}

			prstmt.setInt(2, Integer.parseInt(queryMap.get("id")));

			int success = prstmt.executeUpdate();
			if(success >= 0){
				con.commit();
				System.out.println("Saved successfully");
				resp.getWriter().print("S");
				resp.flushBuffer();
				//return true;
			}else{
				resp.getWriter().print("F");
				resp.flushBuffer();
			}

		}catch(SQLException sql){
			sql.printStackTrace();
		}		
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
