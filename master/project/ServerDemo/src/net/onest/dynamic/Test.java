package net.onest.dynamic;

import cn.hutool.crypto.SecureUtil;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String mima = SecureUtil.md5("123456");
		System.out.println(mima);
	}

}
