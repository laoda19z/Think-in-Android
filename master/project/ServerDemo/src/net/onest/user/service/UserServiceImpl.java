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
	/**
	 * 修改邮箱
	 * @param id,email
	 * @return
	 * */
	public boolean updateEmail(int id,String email) {
		UserDaoImpl userDao = new UserDaoImpl();
		return userDao.updateEmail(id, email);
	}
	/**
	 * 修改个人信息
	 * @param id,realName,sex,phone,email
	 * @return
	 * */
	public boolean updateInfo(int id,String name,String sex,String phone,String email) {
		UserDaoImpl userDao = new UserDaoImpl();
		return userDao.updateInfo(id, name, sex, phone, email);
	}
	/**
	 * 修改昵称
	 * @param id,newName
	 * @return
	 * */
	public boolean updateName(int id,String newName) {
		UserDaoImpl userDao = new UserDaoImpl();
		return userDao.updateName(id, newName);
	}
	/**
	 * 修改电话号码
	 * @param id,newPhone
	 * */
	public boolean updatePhone(int id,String newPhone) {
		UserDaoImpl userDao = new UserDaoImpl();
		return userDao.updatePhone(id, newPhone);
	}	
	/**
	 * 修改密码
	 * @param id,newPwd
	 * @return
	 * */
	public boolean updatePwd(int id,String newPwd) {
		UserDaoImpl userDao = new UserDaoImpl();
		return userDao.updatePwd(id, newPwd);
	}	
	
	/**
	 * 根据用户id查找用户信息
	 * */
	public User findUser(int id) {
		UserDaoImpl userDao = new UserDaoImpl();
		return userDao.findUser(id);
	}
	
	/**
	 * 根据用户id，更改孩子列表
	 * */
	public boolean updateKids(int id,String kidsList) {
		UserDaoImpl userDao = new UserDaoImpl();
		return userDao.updateKids(id, kidsList);
	}
	
}
