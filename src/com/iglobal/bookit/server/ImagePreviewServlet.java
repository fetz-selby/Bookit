package com.iglobal.bookit.server;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.iglobal.bookit.server.utils.Utils;

@SuppressWarnings("serial")
public class ImagePreviewServlet extends HttpServlet{

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
			
			System.out.println("Got here");
			
			try{
				List<FileItem> items = upload.parseRequest(req);
				for(FileItem item : items){
					if(item.isFormField()){
						System.out.println("is form field");
						continue;
					}else{
						System.out.println("is NOT form field");
						processPreview(item, resp);
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			
			//super.doPost(req, resp);
	}
	
	private void processPreview(FileItem item, HttpServletResponse resp){
		if(item != null){
			String image = getImageUrl(getShrinkedImage(item, 120, 120));
			
			//String image = getImageUrl(item.get());
			try {
				resp.getWriter().print(image);
				resp.flushBuffer();
				System.out.print("Base image sent is"+image);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private byte[] getShrinkedImage(FileItem item, int width, int height){
		try {
			InputStream instream = item.getInputStream();
		    
		    BufferedImage scaledImg = Utils.scale(ImageIO.read(instream), width, height);
		    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		    
		    ImageIO.write(scaledImg, "jpg", outputStream);
		    return outputStream.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	private String getImageUrl(byte[] blob){
		String base64 = new String(org.apache.commons.codec.binary.Base64.encodeBase64(blob));
		base64 = "data:image/png;base64,"+base64;
	    return base64;
	}
}
