package net.onest.contact.service;

import java.util.List;

import net.onest.contact.dao.ContactDaoImpl;
import net.onest.entity.Contact;

public class ContactServiceImpl {
	/**
	 * 查找我的好友
	 * @param userid
	 * @return
	 */
	public List<Contact> searchMyContact(int userid){
		ContactDaoImpl contactDaoImpl = new ContactDaoImpl();
		List<Contact> contacts = contactDaoImpl.findMyContact(userid);
		return contacts;
	}
	/**
	 * 添加联系人
	 * @param userid
	 * @param contactid
	 * @return
	 */
	public boolean addContact(int userid,int contactid) {
		ContactDaoImpl contactDaoImpl = new ContactDaoImpl();
		return contactDaoImpl.addContact(userid, contactid);
	}
}
