package net.onest.contact.service;

import java.util.List;

import net.onest.contact.dao.ContactDaoImpl;
import net.onest.entity.Contact;
import net.onest.entity.User;

public class ContactServiceImpl {
	/**
	 * �����ҵĺ���
	 * @param userid
	 * @return
	 */
	public List<Contact> searchMyContact(int userid){
		ContactDaoImpl contactDaoImpl = new ContactDaoImpl();
		List<Contact> contacts = contactDaoImpl.findMyContact(userid);
		return contacts;
	}
	/**
	 * �����ϵ��
	 * @param userid
	 * @param contactid
	 * @return
	 */
	public boolean addContact(int userid,int contactid) {
		ContactDaoImpl contactDaoImpl = new ContactDaoImpl();
		return contactDaoImpl.addContact(userid, contactid);
	}
	public User searchContact(String userName) {
		ContactDaoImpl contactDaoImpl = new ContactDaoImpl();
		return contactDaoImpl.searchContact(userName);
	}
	/**
	 * ɾ����ϵ��
	 * @param contact
	 * @return
	 */
	public boolean deleteContact(Contact contact) {
		ContactDaoImpl contactDaoImpl = new ContactDaoImpl();
		return contactDaoImpl.deleteContact(contact);
	}
}
