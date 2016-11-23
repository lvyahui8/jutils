package org.claret.utils;

import org.claret.utils.var.Charset;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字符串工具类
 * Created by lvyahui on 2016/8/26.
 */
public class StringUtils extends CommonUtils {

    /**
     * 蛇形字符串缓存
     */
    private static Map<String,String> snakeCache = new HashMap<String,String>();

    /**
     * 驼峰字符串缓存
     */
    private static Map<String,String> humpCache = new HashMap<String,String>();

    /**
     * 首字母小写
     *
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
     *
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

    /**
     * 驼峰转蛇形
     *
     * @param str 驼峰字符串
     * @param delimiter 分隔符
     * @return 蛇形字符串
     */
    public static String humpToSnake(String str,String delimiter){
        String key = str.concat(delimiter);
        if(snakeCache.containsKey(key)){
            return snakeCache.get(key);
        }

        String replace = "$1".concat(delimiter).concat("$2");
        str = str.replaceAll("([A-Za-z])([A-Z])",replace).toLowerCase();

        snakeCache.put(key,str);
        return str;
    }


    /**
     * 蛇形转驼峰
     *
     * @param str 蛇形字符串
     * @param delimiter 分隔符
     * @return 驼峰字符串
     */
    public static String snakeToHump(String str,String delimiter){
        String key = str.concat(delimiter);
        if(humpCache.containsKey(key)){
            return humpCache.get(key);
        }
        str = ucwords(str,delimiter).replaceAll(delimiter,"");
        humpCache.put(key,str);
        return str;
    }

    public static String ucwords(String str){
        return ucwords(str," ");
    }

    /**
     * 将语句中的单词的首字母大写
     *
     * @param str 空格分隔的英文单词语句
     * @param delimiter 分隔符
     * @return 单词首字母大写的串
     */
    public static String ucwords(String str,String delimiter){
        String [] words = str.split(delimiter);
        StringBuilder ucStr = new StringBuilder();
        for (String word : words){
            ucStr.append(ucfirst(word)).append(delimiter);
        }
        return ucStr.toString().trim();
    }

    /**
     * 以,连接对象，将调用对象的toString方法获得字符串
     *
     * @param strs 待连接对象数组
     * @return 连接之后的字符串
     */
    public static String join(Object [] strs){
        return join(strs,",");
    }

    /**
     * 以指定分隔符连接对象，将调用对象的toString方法获得字符串
     *
     * @param strs 待连接对象数组
     * @param delimiter 分隔符
     * @return 连接之后的字符串
     */
    public static String join(Object [] strs,CharSequence delimiter){
        StringBuilder joiner = new StringBuilder();
        for (int i = 0;i < strs.length ; i++){
            if(i > 0){
                joiner.append(delimiter);
            }
            if(strs[i] != null){
                joiner.append(strs[i]);
            }
        }
        return joiner.toString();
    }

    /**
     * 检测字符对象是否是null或者长度为0的字符串
     *
     * @param sequence 待检测字符串
     * @return 检测结果
     */
    public static boolean isEmpty(CharSequence sequence){
        return sequence != null && sequence.length() == 0;
    }

    public static boolean equeals(CharSequence str1,CharSequence str2){
        return str1 == null ? str2 == null : str1.equals(str2);
    }

    public static boolean isUpperCase(CharSequence cs){
        return each(cs, new Comparable<Character>() {
            public int compareTo(Character o) {
                return Character.isUpperCase(o) ? 1 : 0;
            }
        });
    }

    public static boolean isLowerCase(CharSequence cs){
        return each(cs, new Comparable<Character>() {
            public int compareTo(Character o) {
                return Character.isLowerCase(o) ? 1 : 0;
            }
        });
    }

    /**
     * 遍历字符串，使用比较器检测每个字符
     *
     * @param cs 待检测字符串
     * @param comparable 比较器
     * @return 当所有字符都满足比较器的结果为0时返回true
     */
    public static boolean each(CharSequence cs, Comparable<Character> comparable){
        if(comparable == null){
            return false;
        }
        if(cs != null && cs.length() != 0){
            for (int i = 0; i < cs.length(); i++){
                if(comparable.compareTo(cs.charAt(i)) == 0){
                    return false;
                }
            }
            return true;
        }else{
            return false;
        }
    }

    /**
     * 解析命令行参数，主要解析main方法传递进来的参数
     *
     * @param params 参数
     * @return kv形式的参数
     */
    public static Map<String,Object> convParams(List<String> params){
        Map<String,Object> paramMap = new HashMap<String, Object>();
        for (String param : params){
            String comps [] = param.split("=");
            if(comps.length > 1){
                paramMap.put(comps[0],comps[1]);
            }else{
                paramMap.put(comps[0],true);
            }
        }
        return paramMap;
    }

    public static String toCharset(final String src , final Charset charset){
        return toCharset(src,charset.toString());
    }
    public static String toCharset(final String src , final String charset){
        String oldCharset = getEncoding(src).toString();
        if(Charset.UNKOWN.toString().equals(oldCharset)){
            return src;
        }
        try {
            return new String(src.getBytes(oldCharset),charset);
        } catch (UnsupportedEncodingException e) {
            return src;
        }
    }



    public static Charset getEncoding(final String str) {
        for (Charset charset : Charset.values()) {
            try {
                if (str.equals(new String(str.getBytes(charset.toString()), charset.toString()))) {
                    return charset;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return Charset.UNKOWN;
    }
    /**
     * 检测字符串是否是数字
     * @param cs 字符串
     * @return 是否是字符串
     */
    public static boolean isNumber(CharSequence cs) {
//        return !isEmpty(cs) && cs.matches("[0-9]+(\\.[0-9]+)+[eE]?[lLfFdD]");
        if(isEmpty(cs)) {
            return false;
        } else {
            int sz = cs.length();

            for(int i = 0; i < sz; ++i) {
                if(!Character.isDigit(cs.charAt(i))) {
                    return false;
                }
            }

            return true;
        }
    }


}
