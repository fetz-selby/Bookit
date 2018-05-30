package com.iglobal.bookit.server.utils;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import jxl.Workbook;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.iglobal.bookit.shared.DataTypeConstants;

public class ReportGenerator {
	private ReportGeneratorHandler handler;
	private HashMap<String, DataTypeConstants> fieldsMap;
	private ResultSet result;
	private ArrayList<String> fieldsList;
	private String tableName, userId, timestamp;
	private HashMap<String, String> returnFieldHash;
	private ArrayList<String> orderedList;
	
	public interface ReportGeneratorHandler{
		void onGenerateComplete(String fileName);
	}

	public ReportGenerator(String userId, String timestamp, String tableName, ResultSet result, HashMap<String, DataTypeConstants> fieldsMap, HashMap<String, String> returnFieldHash, ArrayList<String> orderedList){
		this.returnFieldHash = returnFieldHash;
		this.userId = userId;
		this.timestamp = timestamp;
		this.tableName = tableName;
		this.fieldsMap = fieldsMap;
		this.result = result;
		this.orderedList = orderedList;
	}

	public void run(){
		doSorting();
	}

	private void doSorting(){
		if(fieldsMap != null){
			fieldsList = new ArrayList<String>();
			
			
			for(String field : orderedList){
				if(returnFieldHash.containsValue(field)){
					fieldsList.add(field);
				}
			}
			
//			for(String key : returnFieldHash.keySet()){
//				System.out.println("[ReportGenerator]keys are "+returnFieldHash.get(key));
//				fieldsList.add(returnFieldHash.get(key));
//			}
		}

		//Greping field and value
		if(result == null){
			return;
		}

		try {
			//Create output file

			ExcelCreator excel = new ExcelCreator(userId, timestamp, tableName, handler);
			createHeaders(excel);

			int rowIndex = 1;
			while(result.next()){

				int columnIndex = 0;
				for(String field : fieldsList){
					String value = "";

					switch(fieldsMap.get(field)){
					case BLOB:
						break;
					case DATE:
						value = result.getString(field);
						if(value != null){
							try {
								Date date = new SimpleDateFormat("yyyy-MM-dd").parse(value);
								//excel.addDate(columnIndex, rowIndex, date);
								excel.addLabel(columnIndex, rowIndex, value);


								System.out.println("added");
							} catch (ParseException e) {
								e.printStackTrace();
							}
						}
						System.out.println("date value is "+value);

						break;
					case DATETIME:
						value = result.getString(field);
						if(value != null){
							excel.addLabel(columnIndex, rowIndex, value);
							System.out.println("added");
						}
						System.out.println("datetime value is "+value);

						break;
					case INT:
						value = ""+result.getInt(field);
						if(value != null){
							excel.addNumber(columnIndex, rowIndex, value);
							System.out.println("added");

						}
						System.out.println("int value is "+value);

						break;
					case STRING:
						value = result.getString(field);

						if(value != null){
							excel.addLabel(columnIndex, rowIndex, value);
							System.out.println("added");

						}
						System.out.println("string value is "+value);

						break;
					case TIME:
						value = result.getString(field);
						if(value != null){
							excel.addLabel(columnIndex, rowIndex, value);
							System.out.println("added");

						}
						System.out.println("time value is "+value);

						break;
					case ALERT:
						value = result.getString(field);
						if(value != null){
							excel.addLabel(columnIndex, rowIndex, value);
							System.out.println("added");

						}
						System.out.println("time value is "+value);

						break;
					case ID:
						value = result.getString(field);
						if(value != null){
							excel.addLabel(columnIndex, rowIndex, value);
							System.out.println("added");

						}
						System.out.println("time value is "+value);

						break;
					case LOGIN:
						value = result.getString(field);
						if(value != null){
							excel.addLabel(columnIndex, rowIndex, value);
							System.out.println("added");

						}
						System.out.println("time value is "+value);

						break;
					default:
						break;

					}

					columnIndex ++;
				}

				rowIndex ++;
			}

			excel.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void createHeaders(ExcelCreator excel){
		int column = 0;
		for(String fields : fieldsList){
			if(!(fieldsMap.get(fields) == DataTypeConstants.BLOB)){
				excel.addLabel(column, 0, fields);
				column ++;
			}
		}
	}

	private class ExcelCreator{
		private ReportGeneratorHandler innerHandler;
		private WritableWorkbook workBook;
		private WritableSheet sheet;
		private File file;
		private String innerUserId, innerTimestamp;

		public ExcelCreator(String userId, String timestamp, String title, ReportGeneratorHandler handler){
			this.innerUserId = userId;
			this.innerTimestamp = timestamp;
			this.innerHandler = handler;
			createWorkBook(title);
		}

		private void createWorkBook(final String title){
			try {
				final String SLASH = System.getProperty("file.separator");
				System.out.println("Catalina base is => "+System.getProperty("catalina.base"));
				String fileName = "";
				
				if(System.getProperty("catalina.base") != null && !System.getProperty("catalina.base").trim().isEmpty()){
					String base = System.getProperty("catalina.base");
					base += SLASH+"webapps"+SLASH+"war_bookit"+SLASH+"reports"+SLASH;
					
					fileName = innerUserId.trim()+"_"+innerTimestamp.trim()+"_output.xls";

					System.out.println("file location => "+base+fileName);
					file = new File(base+fileName);
				}else{
					fileName = innerUserId.trim()+"_"+innerTimestamp.trim()+"_output.xls";
					file = new File("./reports/"+fileName);
				}
				workBook = Workbook.createWorkbook(file);
				sheet = workBook.createSheet(title, 0);
				
				if(innerHandler != null){
					innerHandler.onGenerateComplete(fileName);
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		public void addLabel(int column, int row, String value){
			Label label = new Label(column, row, value);
			try {
				sheet.addCell(label);
			} catch (RowsExceededException e) {
				e.printStackTrace();
			} catch (WriteException e) {
				e.printStackTrace();
			}
		}

		public void addNumber(int column, int row, String value){
			jxl.write.Number number = new jxl.write.Number(column, row, Double.parseDouble(value));
			try {
				sheet.addCell(number);
			} catch (RowsExceededException e) {
				e.printStackTrace();
			} catch (WriteException e) {
				e.printStackTrace();
			}
		}

		public void addDate(int column, int row, Date date){
			jxl.write.DateFormat customDateFormat = new jxl.write.DateFormat ("yyyy-MMM-dd"); 

			WritableCellFormat dateFormat = new WritableCellFormat(customDateFormat); 
			DateTime dateCell = new DateTime(column, row, date, dateFormat); 
			try {
				sheet.addCell(dateCell);
			} catch (RowsExceededException e) {
				e.printStackTrace();
			} catch (WriteException e) {
				e.printStackTrace();
			} 
		}

		public void close(){
			try {
				workBook.write();
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				workBook.close();
			} catch (WriteException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		//		public File getFile(){
		//			return file;
		//		}
	}
	
	public void setReportGeneratorHandler(ReportGeneratorHandler handler){
		this.handler = handler;
	}
}
