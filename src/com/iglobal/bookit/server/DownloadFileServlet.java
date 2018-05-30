package com.iglobal.bookit.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class DownloadFileServlet extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String filePath = "";
		
		if(req.getParameter("filename") != null && !req.getParameter("filename").isEmpty()){
			filePath = req.getParameter("filename");

		}
		
		   // reads input file from an absolute path
        if(filePath.isEmpty()){
        	System.out.println("File path is empty");
        	return;
        }
        
        File downloadFile = null;
		final String SLASH = System.getProperty("file.separator");

        
        if(System.getProperty("catalina.base") != null && !System.getProperty("catalina.base").trim().isEmpty()){
			String base = System.getProperty("catalina.base");
			base += SLASH+"webapps"+SLASH+"war_bookit"+SLASH+"reports"+SLASH;
			

			System.out.println("[streaming server]file location => "+base+filePath);
			downloadFile = new File(base+filePath);
		}else{
			downloadFile = new File("./reports/"+filePath);
		}
        
        
        FileInputStream inStream = new FileInputStream(downloadFile);
         
        // if you want to use a relative path to context root:
        String relativePath = getServletContext().getRealPath("");
        System.out.println("relativePath = " + relativePath);
         
        // obtains ServletContext
        ServletContext context = getServletContext();
         
        // gets MIME type of the file
        String mimeType = context.getMimeType(filePath);
        if (mimeType == null) {        
            // set to binary type if MIME mapping not found
            mimeType = "application/octet-stream";
        }
        System.out.println("MIME type: " + mimeType);
         
        // modifies response
        resp.setContentType(mimeType);
        resp.setContentLength((int) downloadFile.length());
         
        // forces download
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
        resp.setHeader(headerKey, headerValue);
         
        // obtains response's output stream
        OutputStream outStream = resp.getOutputStream();
         
        byte[] buffer = new byte[4096];
        int bytesRead = -1;
         
        while ((bytesRead = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }
         
        inStream.close();
        outStream.close();  
		
	}

}
