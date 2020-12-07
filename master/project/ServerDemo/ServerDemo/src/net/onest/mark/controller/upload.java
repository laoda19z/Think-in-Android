package net.onest.mark.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
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

import com.google.gson.Gson;


import net.onest.entity.ReturnMarkPic;
import net.onest.entity.TotalMark;
import net.onest.util.DBUtil;




/**
 * Servlet implementation class upload
 */
@WebServlet("/upload")
public class upload extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public upload() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		// TODO Auto-generated method stub
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		List<String> lists=new ArrayList<>();
		try {
			List<FileItem> list= upload.parseRequest(request);
			for(FileItem item:list) {
				if(item.isFormField()) {
					lists.add(item.getString());
					System.out.println("获得数据"+item.getFieldName()+":"+item.getString());
				}
				else {
					String path=getServletContext().getRealPath("/file");
					//获得表单数据
					String total=lists.get(0);
					String total1=URLDecoder.decode(total,"utf-8");
					System.out.println("total1"+total1);
					Gson gson=new Gson();
					TotalMark totalmark=gson.fromJson(total1, TotalMark.class);
					//插入数据库
					String sql="select * from mark where userId="+totalmark.getUsername()+" and childId="+totalmark.getChild()+" and markdate='"+totalmark.getDate()+"'";
					Connection conn = null;
					PreparedStatement pstm = null;
					ResultSet rs = null;
					int count = 0;
					try {
						String sql1="insert into mark(userId,markdate,sporttype,sporttime,impression,background,childId) values("+totalmark.getUsername()+",'"+totalmark.getDate()+"','"+totalmark.getSporttype()+"',"+totalmark.getMinutes()+",'"+totalmark.getImpression()+"','"+totalmark.getPicname()+"',"+totalmark.getChild()+")";

						conn =DBUtil.getConn();
						pstm = conn.prepareStatement(sql);
						rs = pstm.executeQuery();
						if(rs.next()) {
							
						}else {
							//没有打卡过
							pstm = conn.prepareStatement(sql1);
							pstm.executeUpdate();
							//同时写入图片
							item.write(new File(path+"/"+totalmark.getPicname()+".jpg"));
						}
					}catch (Exception e) {
						e.printStackTrace();
					}finally {
						DBUtil.close(rs, pstm, conn);
					}
					//查询数据
				}
			}
		} catch (FileUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
