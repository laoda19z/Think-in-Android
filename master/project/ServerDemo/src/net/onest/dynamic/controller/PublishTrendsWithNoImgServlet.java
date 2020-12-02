package net.onest.dynamic.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.onest.dynamic.service.DynamicServiceImpl;
import net.onest.entity.Dynamic;

/**
 * Servlet implementation class PublishTrendsWithNoImgServlet
 */
@WebServlet("/PublishTrendsWithNoImgServlet")
public class PublishTrendsWithNoImgServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PublishTrendsWithNoImgServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println(request.getContentType());
		InputStream in = request.getInputStream();
		String name = request.getParameter("username");
		String content = request.getParameter("content");
		String location = request.getParameter("location");
		String time = request.getParameter("time");
		String img = request.getParameter("img");
		Dynamic dynamic = new Dynamic();
		dynamic.setUserId(1);
		dynamic.setImg("dynamics/" + img);
		dynamic.setLocation(location);
		DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		try {
			date = simpleDateFormat.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dynamic.setTime(date);
		dynamic.setContent(content);
		DynamicServiceImpl dynamicServiceImpl = new DynamicServiceImpl();
		boolean b = dynamicServiceImpl.publishDynamicImpl(dynamic);
		if (b) {
			response.getWriter().write("OK");
		} else {
			response.getWriter().write("FALSE");
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
