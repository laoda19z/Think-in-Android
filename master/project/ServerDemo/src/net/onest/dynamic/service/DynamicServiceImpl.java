package net.onest.dynamic.service;

import java.util.List;

import net.onest.dynamic.dao.DynamicDaoImpl;
import net.onest.entity.Comment;
import net.onest.entity.Dynamic;
import net.onest.util.Page;

public class DynamicServiceImpl {
	/**
	 * 展示所有动态
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public Page<Dynamic> showDynamicImpl(int pageNum,int pageSize){
		Page<Dynamic> page = new Page<Dynamic>(pageNum,pageSize);
		DynamicDaoImpl dynamicDaoImpl = new DynamicDaoImpl();
		int count = dynamicDaoImpl.countByPage();
		List<Dynamic> dynamiclist = dynamicDaoImpl.findByPage(pageNum, pageSize);
		page.setList(dynamiclist);
		page.setTotalCount(count);
		return page;
	}
	/**
	 * 展示个人动态
	 * @param pageNum
	 * @param pageSize
	 * @param userId
	 * @return
	 */
	public Page<Dynamic> showDynamicImpl(int pageNum,int pageSize,int userId){
		Page<Dynamic> page = new Page<Dynamic>(pageNum,pageSize);
		DynamicDaoImpl dynamicDaoImpl = new DynamicDaoImpl();
		int count = dynamicDaoImpl.countOwnerByPage(userId);
		List<Dynamic> dynamiclist = dynamicDaoImpl.findOwnerByPage(pageNum, pageSize, userId);
		page.setList(dynamiclist);
		page.setTotalCount(count);
		return page;
	}
	/**
	 * 发布动态
	 * @param dynamic
	 * @return
	 */
	public boolean publishDynamicImpl(Dynamic dynamic) {
		boolean b = false;
		DynamicDaoImpl dynamicDaoImpl = new DynamicDaoImpl();
		int result = dynamicDaoImpl.publishDynamic(dynamic);
		if(result != 0) {
			b = true;
		}
		return b;
	}
	/**
	 * 发表评论
	 * @param comment
	 * @return
	 */
	public boolean publishCommentImpl(Comment comment) {
		boolean b = false;
		
		DynamicDaoImpl dynamicDaoImpl = new DynamicDaoImpl();
		int result = dynamicDaoImpl.publishComment(comment);
		if(result!=0) {
			b = true;
		}
		return b;
	}
	public boolean deleteDynamicImpl(int dynamicid) {
		DynamicDaoImpl dynamicDaoImpl= new DynamicDaoImpl();
		return dynamicDaoImpl.deleteDynamic(dynamicid);
	}
}
