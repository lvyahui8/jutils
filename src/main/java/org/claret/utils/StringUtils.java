package org.claret.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 字符串工具类
 * Created by lvyahui on 2016/8/26.
 */
public class StringUtils {

    /**
     * 首字母小写
     * @param str 待转字符串
     * @return 首字母小写的字符串
     */
    public static String lcfirst(String str){
        char [] chars = str.toCharArray();
        chars[0] = chars[0] >= 'A' && chars[0] <= 'Z' ? (char) (chars[0] + 32) : chars[0];
        return String.copyValueOf(chars);
    }

    /**
     * 首字母大写
     * @param str 待转字符串
     * @return 首字母大写的字符串
     */
    public static String ucfirst(String str){
        char ch = str.charAt(0);
        ch = ch >= 'a' && ch <= 'z' ? (char) (ch - 32) : ch;
        return ch + str.substring(1);
    }

    /**
     * 生成字符串的32位md5加密值
     *
     * @param str 要加密的字符串
     * @return md5串
     */
    public static String md5(String str){
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(str.getBytes());
            for (byte b : bytes){
                String toHexChar = Integer.toHexString(b & 0xff);
                sb.append(toHexChar.length() == 1 ? '0'+toHexChar : toHexChar );
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 判断是否是指定编码
     *
     * @param str 待检测字符串
     * @param encode 编码类型
     * @return 是否是指定编码
     */
    public static boolean encodeType(String str,String encode){
        boolean success = false;
        try {
            success = str != null && str.equals(new String(str.getBytes(),encode));
        } catch (UnsupportedEncodingException e) {
            //
        }
        return success;
    }


}
