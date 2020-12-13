package net.onest.backstage.service;

import java.util.List;

import net.onest.backstage.dao.BackStageUserDaoImpl;
import net.onest.entity.Dynamic;
import net.onest.entity.User;
import net.onest.util.Page;

public class BackStageUserServiceImpl {
	/**
	 * 添加用户
	 */
	public boolean addUser(User user) {
		BackStageUserDaoImpl backGroundUserDaoImpl = new BackStageUserDaoImpl();
		return backGroundUserDaoImpl.addUser(user);
	}
	/**
	 * 删除用户
	 */
	public boolean deleteUser(int userid) {
		BackStageUserDaoImpl backGroundUserDaoImpl = new BackStageUserDaoImpl();
		return backGroundUserDaoImpl.deleteUser(userid);
	}
	/**
	 * 查询所有用户
	 */
	public Page<User> searchAllUser(int pageNum,int pageSize){
		BackStageUserDaoImpl backGroundUserDaoImpl = new BackStageUserDaoImpl();
		Page<User> page = new Page<User>(pageNum,pageSize);
		int count = backGroundUserDaoImpl.countByPage();
		List<User> userList = backGroundUserDaoImpl.searchAllUser(pageNum, pageSize);
		page.setList(userList);
		page.setTotalCount(count);
		return page;
	}
}
