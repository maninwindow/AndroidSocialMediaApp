package com.tn.tnclient.Utils;

import java.security.MessageDigest;

/**
 * Created by alimjan on 10/19/17.
 */

public class MD5 {

    public static String getMD5(String message){
        String mdSstr = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] input = message.getBytes();
            byte[] buff = md.digest(input);
            mdSstr = bytesToHex(buff);
        }catch (Exception e){
            e.printStackTrace();
        }
        return mdSstr;
    }

    public static String bytesToHex(byte[] bytes){
        StringBuffer mdSstr = new StringBuffer();
        int digital;
        for(int i = 0; i < bytes.length; i++){
            digital = bytes[i];
            if (digital < 0){
                digital += 256;
            }if (digital < 16){
                mdSstr.append("0");
            }
            mdSstr.append(Integer.toHexString(digital));
        }
        return mdSstr.toString().toUpperCase();
    }

}
