package net.onest.backstage.service;

import java.util.List;

import net.onest.backstage.dao.UserPageDaoImpl;
import net.onest.entity.User;
import net.onest.util.Page;

public class UserPageServiceImpl {
	public Page<User> listByPage(int pageNum, int pageSize){
		Page<User> page = new Page<User>(pageNum, pageSize);
		UserPageDaoImpl userPageDaoImpl = new UserPageDaoImpl();
		int count = userPageDaoImpl.countByPage();
		List<User> list = userPageDaoImpl.findByPage(pageNum, pageSize);
		page.setList(list);
		page.setTotalCount(count);
		return page;
	}
	
	public boolean signUpUser(User user) {
		UserPageDaoImpl userDaoImpl = new UserPageDaoImpl();
		return userDaoImpl.signUpUser(user);
	}
	
	public boolean deleteUserById(int userid) {
		UserPageDaoImpl userDaoImpl = new UserPageDaoImpl();
		return userDaoImpl.deleteUser(userid);
	}
}
