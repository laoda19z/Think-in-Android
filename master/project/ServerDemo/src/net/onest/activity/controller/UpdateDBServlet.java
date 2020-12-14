package net.onest.activity.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.onest.util.NewDbUtil;





/**
 * Servlet implementation class UpdateDBServlet
 */
@WebServlet("/UpdateDBServlet")
public class UpdateDBServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateDBServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			String content1 = "      开心玩国儿童主题乐园介绍:集购物、乐园、游乐、亲子、培训、儿童体验职业馆、儿童DIY、儿童餐饮、图书室基本都有，突出体验消费，营造好的消费氛围，为儿童提供一条龙的.服务。\r\n" + 
					"      我们儿童乐园造型新奇灵巧，由圆柱、方屋组合而成。开心玩国儿童乐园墙面上绘着猴王闹天宫、动物世界、哪吒闹海等彩色图案。儿童乐园还有组合滑梯、动物造型滑梯、人工转椅、电动转椅、脚踩转筒、各种造型的电动碰碰车、碰转结合的电动警车等娱乐玩具。而且这些游乐设施小巧别致，色彩明快，为儿童所喜闻乐见。";
			String content2 = "      欢迎您来到主题乐园，参加美食街活动。主题乐园拥有1832m²大型室内游玩场所。\r\n" + 
					"\r\n" + 
					"      ⭐主题咖乐园融参与性、观赏性、娱乐性、趣味性于一体，绝对的网红打卡圣地⭐致力打造成为中国最新、最具个性魅力的主题乐园，是主题咖度假区的重要组成部分。它根据天然优美的海岸线，自然天成的山水，巧妙地将多种精彩刺激的高科技游乐设备与地中海风格建筑相融合，形成了热情奔放的幸运大道区、惊险刺激的冒险丛林区、神秘诡异的海盗城堡区、梦幻童话的美人鱼湖区、激情四溢的神秘岛区和碧海银滩的加勒比海岸区等六大主题区域，多种高科技游乐项目，带你飞跃翻腾。\r\n" + 
					"\r\n" + 
					"　　主题咖拥有亚洲第一的\"E型战车\"；中国第一座\"疯狂逃生船\"、\"云霄飞车\"、\"垂直极限\"等高科技的大型游乐设施，配合多项中小型游乐项目和近10种充满异国风情的表演，让游客在互动中享受刺激与欢乐。";
			NewDbUtil util = new NewDbUtil();
			String sql1 = "update activity set activityContent = '"+content1+"'"
					+ " where activityId = 1";
			String sql2 = "update activity set activityContent = '"+content2+"'"
					+ " where activityId = 2";
			String sql3 = "update activity set activityContent = '"+content1+"'"
					+ " where activityId = 3";
			String sql4 = "update activity set activityContent = '"+content2+"'"
					+ " where activityId = 4";
			String sql5 = "update activity set activityContent = '"+content1+"'"
					+ " where activityId = 5";
			String sql6 = "update activity set activityContent = '"+content2+"'"
					+ " where activityId = 6";
			String sql7 = "update activity set activityContent = '"+content1+"'"
					+ " where activityId = 7";
			String sql8 = "update activity set activityContent = '"+content2+"'"
					+ " where activityId = 8";
			String sql9 = "update activity set activityContent = '"+content1+"'"
					+ " where activityId = 9";
			String sql10 = "update activity set activityContent = '"+content2+"'"
					+ " where activityId = 10";
			String sql11 = "update activity set activityContent = '"+content1+"'"
					+ " where activityId = 11";
			util.updateData(sql1);
			util.updateData(sql2);
			util.updateData(sql3);
			util.updateData(sql4);
			util.updateData(sql5);
			util.updateData(sql6);
			util.updateData(sql7);
			util.updateData(sql8);
			util.updateData(sql9);
			util.updateData(sql10);
			util.updateData(sql11);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}