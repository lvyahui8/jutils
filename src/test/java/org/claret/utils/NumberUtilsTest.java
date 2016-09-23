package org.claret.utils;

import junit.framework.TestCase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author samlv
 */
public class NumberUtilsTest extends TestCase {

    public void testHexString2Double() throws Exception {
        String hexString = "f0f040FF";
        System.out.println(Long.parseLong(hexString,16));

        Pattern pattern = Pattern.compile("(\\d+)\\s+([\\.\\w]+)\\s+([x\\w]+)\\s+([x\\w]+)");
        String line = "10 libobjc.A.dylib 0x18c2d0000 0x18c2f1710,";

        Matcher matcher = pattern.matcher(line);
        if(matcher.find()){
            for(int i = 1 ; i <= matcher.groupCount(); i++){
                System.out.println(matcher.group(i));
            }
        }
    }
}