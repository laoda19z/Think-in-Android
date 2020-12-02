package net.onest.user.service;

import java.util.List;

import net.onest.entity.User;
import net.onest.user.dao.UserDaoImpl;

public class UserServiceImpl {
	/**
	 * 查询用户列表
	 * @param s
	 * @return
	 */
	public List<User> searchTrendUserInfo(String s){
		UserDaoImpl userDaoImpl = new UserDaoImpl();
		List<User> list = userDaoImpl.searchUserList(s);
		return list;
	}
	/**
	 * 注册用户
	 * @param user
	 * @return
	 */
	public boolean signUpUser(User user) {
		UserDaoImpl userDaoImpl = new UserDaoImpl();
		return userDaoImpl.signUpUser(user);
	}
	/**
	 * 查询用户id
	 * @param userName
	 * @return
	 */
	public int searchUserId(String userName) {
		UserDaoImpl userDaoImpl = new UserDaoImpl();
		return userDaoImpl.searchUserId(userName);
	}
	/**
	 * 根据用户的userid来删除用户
	 * @param userid
	 * @return
	 */
	public boolean deleteUserById(int userid) {
		UserDaoImpl userDaoImpl = new UserDaoImpl();
		return userDaoImpl.deleteUser(userid);
	}
	public User searchUser(String userName) {
		UserDaoImpl userDaoImpl = new UserDaoImpl();
		return userDaoImpl.searchUser(userName);
	}
}
