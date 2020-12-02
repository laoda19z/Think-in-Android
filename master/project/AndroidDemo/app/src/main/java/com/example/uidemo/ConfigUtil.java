package com.example.uidemo;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigUtil {                      //192.168.1.104   10.7.89.131
    public static final String SERVER_ADDR = "http://10.7.89.131:8080/DynamicDemo/";

    /**
     * 判断输入内容是否数字
     * @param data
     * @return
     */
    public static boolean isDataSuitable(String data){
        Pattern pattern = Pattern.compile("-?[0-9]+(\\.[0-9]+)?");
        String bigStr;
        try {
            bigStr = new BigDecimal(data).toString();
        } catch (Exception e) {
            return false;//异常 说明包含非数字。
        }

        Matcher isNum = pattern.matcher(bigStr); // matcher是全匹配
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }
}