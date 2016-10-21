package org.claret.utils;

/**
 * @author samlv
 */
public class NumberUtils extends CommonUtils {

    public static final char [] literalSymol = {'l','L','f','F','d','D'};

    public static Double hexString2Double(String hexString){
        long i = 120L;
        float f = 23.4F;
        double d = 23D;

        return null;
    }


    /**
     * 是否是字面符号
     * @param ch 字母符号
     * @return
     */
    public static boolean isLiteralSymbol(char ch){
        for (char c : literalSymol ) {
            if(c == ch){
                return true;
            }
        }
        return false;
    }
}
