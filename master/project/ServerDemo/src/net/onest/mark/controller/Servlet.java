package net.onest.mark.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import net.onest.entity.DateMark;
import net.onest.entity.NeedSearchDate;
import net.onest.mark.service.DateMarkService;


/**
 * Servlet implementation class Servlet
 */
@WebServlet("/date")
public class Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public Servlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		InputStream in=request.getInputStream();
		BufferedReader reader=new BufferedReader(new InputStreamReader(in,"utf-8"));
		PrintWriter writer=response.getWriter();
		//��ȡ�ַ���Ϣ
		String str=reader.readLine();
		if(str!=null&&!str.equals("")) {
			System.out.println("��ѯ����ҳ����"+str);
			//���ô�������JSON���õ��û��������꣬����
			Gson gson=new Gson();
			NeedSearchDate need=gson.fromJson(str,NeedSearchDate.class);
			//���õõ��Ĳ������в�ѯ
			DateMarkService service=new DateMarkService();
			List<DateMark> dateList=new ArrayList<>();
			dateList=service.SearchByMonthAndYear(need.getUsername(),need.getYear(),need.getMonth(),need.getChild());
			//����ѯ�Ľ����ΪJSON���ظ��ͻ���
			String jsonString=gson.toJson(dateList);
			System.out.println("�������ڵĲ��Դ�"+jsonString);
			writer.write(jsonString);
		}else {
			System.out.println("����");
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
