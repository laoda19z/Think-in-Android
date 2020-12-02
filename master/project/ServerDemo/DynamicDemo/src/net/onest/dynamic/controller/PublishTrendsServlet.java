package net.onest.dynamic.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.parser.MediaType;

import com.google.gson.Gson;

import net.onest.dynamic.service.DynamicServiceImpl;
import net.onest.entity.Dynamic;

/**
 * Servlet implementation class PublishTrendsServlet
 */
@WebServlet("/PublishTrendsServlet")
public class PublishTrendsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PublishTrendsServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setHeaderEncoding("utf-8");
		List<String> lists = new ArrayList<>();
		List<FileItem> list;
		Gson gson = new Gson();
		try {
			list = upload.parseRequest(request);
			for(FileItem item:list) {
				if(item.isFormField()) {
					lists.add(item.getString());
					System.out.println("得到的数据"+item.getFieldName()+item.getString());
				}
				else {
					String path=getServletContext().getRealPath("/dynamics/");
					//获得表单数据
					String total=lists.get(0);
					System.out.println(total);
					String total1=URLDecoder.decode(total,"UTF-8");
					Dynamic dynamic = gson.fromJson(total1, Dynamic.class);
					item.write(new File(path+dynamic.getImg()+".jpg"));
					dynamic.setImg("/dynamics/"+dynamic.getImg()+".jpg");
					DynamicServiceImpl dynamicServiceImpl = new DynamicServiceImpl();
					boolean b = dynamicServiceImpl.publishDynamicImpl(dynamic);
					if(b) {
						response.getWriter().write("OK");
					}else {
						response.getWriter().write("FALSE");
					}
				}
			}
		} catch (FileUploadException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
