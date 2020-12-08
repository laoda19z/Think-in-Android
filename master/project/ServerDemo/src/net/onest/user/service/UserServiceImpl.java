package net.onest.user.service;

import java.util.List;

import net.onest.entity.User;
import net.onest.user.dao.UserDaoImpl;

public class UserServiceImpl {
	/**
	 * ��ѯ�û��б�
	 * @param s
	 * @return
	 */
	public List<User> searchTrendUserInfo(String s){
		UserDaoImpl userDaoImpl = new UserDaoImpl();
		List<User> list = userDaoImpl.searchUserList(s);
		return list;
	}
	/**
	 * ע���û�
	 * @param user
	 * @return
	 */
	public boolean signUpUser(User user) {
		UserDaoImpl userDaoImpl = new UserDaoImpl();
		return userDaoImpl.signUpUser(user);
	}
	/**
	 * ��ѯ�û�id
	 * @param userName
	 * @return
	 */
	public int searchUserId(String userName) {
		UserDaoImpl userDaoImpl = new UserDaoImpl();
		return userDaoImpl.searchUserId(userName);
	}
	/**
	 * �����û���userid��ɾ���û�
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
	 * �޸�����
	 * @param id,email
	 * @return
	 * */
	public boolean updateEmail(int id,String email) {
		UserDaoImpl userDao = new UserDaoImpl();
		return userDao.updateEmail(id, email);
	}
	/**
	 * �޸ĸ�����Ϣ
	 * @param id,realName,sex,phone,email
	 * @return
	 * */
	public boolean updateInfo(int id,String name,String sex,String phone,String email) {
		UserDaoImpl userDao = new UserDaoImpl();
		return userDao.updateInfo(id, name, sex, phone, email);
	}
	/**
	 * �޸��ǳ�
	 * @param id,newName
	 * @return
	 * */
	public boolean updateName(int id,String newName) {
		UserDaoImpl userDao = new UserDaoImpl();
		return userDao.updateName(id, newName);
	}
	/**
	 * �޸ĵ绰����
	 * @param id,newPhone
	 * */
	public boolean updatePhone(int id,String newPhone) {
		UserDaoImpl userDao = new UserDaoImpl();
		return userDao.updatePhone(id, newPhone);
	}	
	/**
	 * �޸�����
	 * @param id,newPwd
	 * @return
	 * */
	public boolean updatePwd(int id,String newPwd) {
		UserDaoImpl userDao = new UserDaoImpl();
		return userDao.updatePwd(id, newPwd);
	}	
	
	/**
	 * �����û�id�����û���Ϣ
	 * */
	public User findUser(int id) {
		UserDaoImpl userDao = new UserDaoImpl();
		return userDao.findUser(id);
	}
	
	/**
	 * �����û�id�����ĺ����б�
	 * */
	public boolean updateKids(int id,String kidsList) {
		UserDaoImpl userDao = new UserDaoImpl();
		return userDao.updateKids(id, kidsList);
	}
	
}
