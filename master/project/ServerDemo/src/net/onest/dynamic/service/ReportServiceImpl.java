package net.onest.dynamic.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import net.onest.dynamic.dao.ReportDaoImpl;
import net.onest.entity.Report;

public class ReportServiceImpl {
	/**
	 * 像数据库添加报告
	 * @param report
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public boolean addReport(Report report) {
		ReportDaoImpl reportDaoImpl = new ReportDaoImpl();
		int result = reportDaoImpl.addReport(report);
		return result != 0 ? true : false;
	}
	/**
	 * 根据传递过来的JSON对象生成报告
	 * @param data
	 * @return
	 * @throws IOException 
	 * @throws BiffException 
	 */
	public Report createReport(JsonObject data,String filePath) throws BiffException, IOException {
		//定义初始变量
		int i0,i1,i2,i3,i4,i5,i6;
		int bodyScore = 0;
		int upScore = 0;
		int downScore = 0;
		int overallScore = 0;
		int BMIScore = 0;
		int feihuoliangScore = 0;
		int wushimipaoScore = 0;
		int zuoweitiqianquScore = 0;
		int yifenzhongtiaoshengScore = 0;
		int yifenzhongyangwoqizuoScore = 0;
		int wushimichengbawangfanpaoScore = 0;
		//获取数据文件
		File file = new File(filePath);
		InputStream in = new FileInputStream(file);
		Workbook book = Workbook.getWorkbook(in);
		//获取data携带的数据
		//孩子基础信息
		int childId = Integer.parseInt(data.get("id").getAsString());
		String sex = data.get("xb").getAsString();
		int grade = Integer.parseInt(data.get("nj").getAsString());
		/**
		 * 一二年级部分
		 */
		if (grade <= 2) {
			//测评信息
			float height = Float.parseFloat(data.get("h").getAsString());
			float weight = Float.parseFloat(data.get("w").getAsString());
			float zwtqq = Float.parseFloat(data.get("zwtqq").getAsString());
			float wsmp = Float.parseFloat(data.get("wsmp").getAsString());
			int fhl = Integer.parseInt(data.get("fhl").getAsString());
			int yfzts = Integer.parseInt(data.get("yfzts").getAsString());
			//性别判断
			Sheet sheet = null;
			if (sex.equals("男")) {
				sheet = book.getSheet((grade-1)*2);
			}else {
				sheet = book.getSheet((grade-1)*2+1);
			}
			//获取BMI得分
			float a = weight/(height*height);
			float BMI = (float)(Math.round(a*100)/100);
			for (i0=1;i0<5;i0++) {
				float lowBMI = Float.parseFloat(sheet.getCell(0,i0).getContents());
				float highBMI = Float.parseFloat(sheet.getCell(0,i0+1).getContents());
				if (BMI>=lowBMI && BMI<highBMI) {
					BMIScore = Integer.parseInt(sheet.getCell(1,i0).getContents());
				}
			}
			//获取肺活量得分
			for (i1=1;i1<22;i1++) {
				int lowVC = Integer.parseInt(sheet.getCell(2,i1).getContents());
				int highVC = Integer.parseInt(sheet.getCell(2,i1+1).getContents());
				if (fhl>=lowVC && fhl<highVC) {
					feihuoliangScore = Integer.parseInt(sheet.getCell(3,i1).getContents());
				}
			}
			//获取50米跑得分
			for (i2=1;i2<22;i2++) {
				float lowRun = Float.parseFloat(sheet.getCell(4,i2).getContents());
				float highRun = Float.parseFloat(sheet.getCell(4,i2+1).getContents());
				if (wsmp>=lowRun && wsmp<highRun) {
					wushimipaoScore = Integer.parseInt(sheet.getCell(5,i2).getContents());
				}
			}
			//获取坐位体前屈得分
			for (i3=1;i3<22;i3++) {
				float lowSR = Float.parseFloat(sheet.getCell(6,i3).getContents());
				float highSR = Float.parseFloat(sheet.getCell(6,i3+1).getContents());
				if (zwtqq>=lowSR && zwtqq<highSR) {
					zuoweitiqianquScore = Integer.parseInt(sheet.getCell(7,i3).getContents());
				}
			}
			//获取一分钟跳绳得分
			for (i4=1;i4<22;i4++) {
				int lowSK = Integer.parseInt(sheet.getCell(8,i4).getContents());
				int highSK = Integer.parseInt(sheet.getCell(8,i4+1).getContents());
				if (yfzts>=lowSK && yfzts<highSK) {
					yifenzhongtiaoshengScore = Integer.parseInt(sheet.getCell(9,i4).getContents());
				}
			}
			//生成报告所需数据
			bodyScore = (int)(BMIScore*0.25+feihuoliangScore*0.25+zuoweitiqianquScore*0.5);
			upScore = yifenzhongtiaoshengScore;
			downScore = wushimipaoScore;
			overallScore = (int)(BMIScore*0.15+feihuoliangScore*0.15+wushimipaoScore*0.2+zuoweitiqianquScore*0.3+yifenzhongtiaoshengScore*0.2);
			//生成报告类返回数据
			Report report = new Report(0, childId, bodyScore, upScore, downScore, overallScore);
			return report;
		}
		/**
		 * 三四年级部分
		 */
		else if (grade >= 2 && grade <= 4) {
			//测评信息
			float height = Float.parseFloat(data.get("h").getAsString());
			float weight = Float.parseFloat(data.get("w").getAsString());
			float zwtqq = Float.parseFloat(data.get("zwtqq").getAsString());
			float wsmp = Float.parseFloat(data.get("wsmp").getAsString());
			int fhl = Integer.parseInt(data.get("fhl").getAsString());
			int yfzts = Integer.parseInt(data.get("yfzts").getAsString());
			int yfzywqz = Integer.parseInt(data.get("yfzywqz").getAsString());
			//性别判断
			Sheet sheet = null;
			if (sex.equals("男")) {
				sheet = book.getSheet((grade-1)*2);
			}else {
				sheet = book.getSheet((grade-1)*2+1);
			}
			//获取BMI得分
			float a = weight/(height*height);
			float BMI = (float)(Math.round(a*100)/100);
			for (i0=1;i0<5;i0++) {
				float lowBMI = Float.parseFloat(sheet.getCell(0,i0).getContents());
				float highBMI = Float.parseFloat(sheet.getCell(0,i0+1).getContents());
				if (BMI>=lowBMI && BMI<highBMI) {
					BMIScore = Integer.parseInt(sheet.getCell(1,i0).getContents());
				}
			}
			//获取肺活量得分
			for (i1=1;i1<22;i1++) {
				int lowVC = Integer.parseInt(sheet.getCell(2,i1).getContents());
				int highVC = Integer.parseInt(sheet.getCell(2,i1+1).getContents());
				if (fhl>=lowVC && fhl<highVC) {
					feihuoliangScore = Integer.parseInt(sheet.getCell(3,i1).getContents());
				}
			}
			//获取50米跑得分
			for (i2=1;i2<22;i2++) {
				float lowRun = Float.parseFloat(sheet.getCell(4,i2).getContents());
				float highRun = Float.parseFloat(sheet.getCell(4,i2+1).getContents());
				if (wsmp>=lowRun && wsmp<highRun) {
					wushimipaoScore = Integer.parseInt(sheet.getCell(5,i2).getContents());
				}
			}
			//获取坐位体前屈得分
			for (i3=1;i3<22;i3++) {
				float lowSR = Float.parseFloat(sheet.getCell(6,i3).getContents());
				float highSR = Float.parseFloat(sheet.getCell(6,i3+1).getContents());
				if (zwtqq>=lowSR && zwtqq<highSR) {
					zuoweitiqianquScore = Integer.parseInt(sheet.getCell(7,i3).getContents());
				}
			}
			//获取一分钟跳绳得分
			for (i4=1;i4<22;i4++) {
				int lowSK = Integer.parseInt(sheet.getCell(8,i4).getContents());
				int highSK = Integer.parseInt(sheet.getCell(8,i4+1).getContents());
				if (yfzts>=lowSK && yfzts<highSK) {
					yifenzhongtiaoshengScore = Integer.parseInt(sheet.getCell(9,i4).getContents());
				}
			}
			//获取一分钟仰卧起坐得分
			for (i5=1;i5<22;i5++) {
				int lowSU = Integer.parseInt(sheet.getCell(10,i5).getContents());
				int highSU  =Integer.parseInt(sheet.getCell(10,i5+1).getContents());
				if (yfzywqz>=lowSU && yfzywqz<highSU) {
					yifenzhongyangwoqizuoScore = Integer.parseInt(sheet.getCell(11,i5).getContents());
				}
			}
			bodyScore = (int)(BMIScore*0.25+feihuoliangScore*0.25+zuoweitiqianquScore*0.33+yifenzhongyangwoqizuoScore*0.17);
			upScore = yifenzhongtiaoshengScore;
			downScore = wushimipaoScore;
			overallScore = (int)(BMIScore*0.15+feihuoliangScore*0.15+wushimipaoScore*0.2+zuoweitiqianquScore*0.2+yifenzhongtiaoshengScore*0.2+yifenzhongyangwoqizuoScore*0.1);
			//生成报告类返回数据
			Report report = new Report(0, childId, bodyScore, upScore, downScore, overallScore);
			return report;
		}
		/**
		 * 五六年级部分
		 */
		else {
			//测评信息
			float height = Float.parseFloat(data.get("h").getAsString());
			float weight = Float.parseFloat(data.get("w").getAsString());
			float zwtqq = Float.parseFloat(data.get("zwtqq").getAsString());
			float wsmp = Float.parseFloat(data.get("wsmp").getAsString());
			int fhl = Integer.parseInt(data.get("fhl").getAsString());
			int yfzts = Integer.parseInt(data.get("yfzts").getAsString());
			int yfzywqz = Integer.parseInt(data.get("yfzywqz").getAsString());
			int wsmcbwfp = Integer.parseInt(data.get("wsmcbwfp").getAsString());
			//性别判断
			Sheet sheet = null;
			if (sex.equals("男")) {
				sheet = book.getSheet((grade-1)*2);
			}else {
				sheet = book.getSheet((grade-1)*2+1);
			}
			//获取BMI得分
			float a = weight/(height*height);
			float BMI = (float)(Math.round(a*100)/100);
			for (i0=1;i0<5;i0++) {
				float lowBMI = Float.parseFloat(sheet.getCell(0,i0).getContents());
				float highBMI = Float.parseFloat(sheet.getCell(0,i0+1).getContents());
				if (BMI>=lowBMI && BMI<highBMI) {
					BMIScore = Integer.parseInt(sheet.getCell(1,i0).getContents());
				}
			}
			//获取肺活量得分
			for (i1=1;i1<22;i1++) {
				int lowVC = Integer.parseInt(sheet.getCell(2,i1).getContents());
				int highVC = Integer.parseInt(sheet.getCell(2,i1+1).getContents());
				if (fhl>=lowVC && fhl<highVC) {
					feihuoliangScore = Integer.parseInt(sheet.getCell(3,i1).getContents());
				}
			}
			//获取50米跑得分
			for (i2=1;i2<22;i2++) {
				float lowRun = Float.parseFloat(sheet.getCell(4,i2).getContents());
				float highRun = Float.parseFloat(sheet.getCell(4,i2+1).getContents());
				if (wsmp>=lowRun && wsmp<highRun) {
					wushimipaoScore = Integer.parseInt(sheet.getCell(5,i2).getContents());
				}
			}
			//获取坐位体前屈得分
			for (i3=1;i3<22;i3++) {
				float lowSR = Float.parseFloat(sheet.getCell(6,i3).getContents());
				float highSR = Float.parseFloat(sheet.getCell(6,i3+1).getContents());
				if (zwtqq>=lowSR && zwtqq<highSR) {
					zuoweitiqianquScore = Integer.parseInt(sheet.getCell(7,i3).getContents());
				}
			}
			//获取一分钟跳绳得分
			for (i4=1;i4<22;i4++) {
				int lowSK = Integer.parseInt(sheet.getCell(8,i4).getContents());
				int highSK = Integer.parseInt(sheet.getCell(8,i4+1).getContents());
				if (yfzts>=lowSK && yfzts<highSK) {
					yifenzhongtiaoshengScore = Integer.parseInt(sheet.getCell(9,i4).getContents());
				}
			}
			//获取一分钟仰卧起坐得分
			for (i5=1;i5<22;i5++) {
				int lowSU = Integer.parseInt(sheet.getCell(10,i5).getContents());
				int highSU  =Integer.parseInt(sheet.getCell(10,i5+1).getContents());
				if (yfzywqz>=lowSU && yfzywqz<highSU) {
					yifenzhongyangwoqizuoScore = Integer.parseInt(sheet.getCell(11,i5).getContents());
				}
			}
			//获取50米x8往返跑得分
			for (i6=0;i6<22;i6++) {
				int lowRS = Integer.parseInt(sheet.getCell(12,i6).getContents());
				int highRS = Integer.parseInt(sheet.getCell(12,i6+1).getContents());
				if (wsmcbwfp>=lowRS && wsmcbwfp<highRS) {
					wushimichengbawangfanpaoScore = Integer.parseInt(sheet.getCell(13,i6).getContents());
				}
			}
			bodyScore = (int)(BMIScore*0.25+feihuoliangScore*0.25+zuoweitiqianquScore*0.17+yifenzhongyangwoqizuoScore*0.33);
			upScore = yifenzhongtiaoshengScore;
			downScore = (int)(wushimipaoScore*0.66+wushimichengbawangfanpaoScore*0.34);
			overallScore = (int)(BMIScore*0.15+feihuoliangScore*0.15+wushimipaoScore*0.2+zuoweitiqianquScore*0.1+yifenzhongtiaoshengScore*0.1+yifenzhongyangwoqizuoScore*0.2+wushimichengbawangfanpaoScore*0.1);
			//生成报告类返回数据
			Report report = new Report(0, childId, bodyScore, upScore, downScore, overallScore);
			return report;
		}
	}
}
