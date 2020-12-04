package net.onest.record.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import net.onest.entity.Relation;
import net.onest.record.service.RelationService;


/**
 * Servlet implementation class GetChildidServlet
 */
@WebServlet("/GetChildidServlet")
public class GetChildidServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetChildidServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		InputStream in=request.getInputStream();
		OutputStream out=response.getOutputStream();
		
		BufferedReader buffer=new BufferedReader(new InputStreamReader(in,"utf-8"));
		String str=buffer.readLine();
		System.out.println(str+"getchildid");
		int userid=Integer.parseInt(str);
		
		String sql="select * from relation where  userid="+userid;
		System.out.println(sql);
		List<Relation> list=new ArrayList<>();
		list=new RelationService().getlist(sql);
		//转为json串发给客户端
		if(list!=null) {
			Gson gson=new Gson();
			String json=gson.toJson(list);
			out.write(json.getBytes());
		}
		out.flush();
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
