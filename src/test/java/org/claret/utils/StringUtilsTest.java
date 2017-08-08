package org.claret.utils;

import junit.framework.TestCase;
import org.claret.utils.var.Charset;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * Created by lvyahui on 2016/8/26.
 */
public class StringUtilsTest extends TestCase {

    public void testLcfirst() throws Exception {
        System.out.println(StringUtils.lcfirst("LvYahui"));
        for(int i = 0 ;i < 10000000; i++){
            StringUtils.lcfirst("LvYahui");
        }
    }

    public void testUcfirst() throws Exception {
        System.out.println(StringUtils.ucfirst("lvYahui"));
        for(int i = 0 ;i < 10000000; i++){
            StringUtils.ucfirst("lvYahui");
        }
    }

    public void testMd5() throws Exception {
        System.out.println(StringUtils.md5("lvyahui"));
    }

    public void testEncodeType() throws Exception {
        System.out.println(StringUtils.encodeType("中文","GBK")); // flase
    }

    public void testHumpToSnake() throws Exception {
        System.out.println(StringUtils.humpToSnake("StringUtilsTest","-"));
    }

    public void testSnakeToHump() throws Exception {
        System.out.println(StringUtils.snakeToHump("string-utils-test","-"));
    }


    public void testUcwords() throws Exception {
        System.out.println(StringUtils.ucwords("string-utils-test","-"));
        System.out.println(StringUtils.ucwords("i will come back for you!"));
    }

    public void testJoin() throws Exception {
        System.out.println(StringUtils.join(new String[] {"i","will","come","back","for","you!"},"*"));
        System.out.println(StringUtils.join(new Integer[] {1,2,3,4,5}));
    }

    public void testIsEmpty() throws Exception {
        System.out.println(StringUtils.isEmpty(""));
    }

    public void testEqueals() throws Exception {
        System.out.println(StringUtils.equeals("lvyahui","samlv"));
    }

    public void testIsUpperCase() throws Exception {
        System.out.println(StringUtils.isUpperCase("lvyahuI"));
    }

    public void testIsLowerCase() throws Exception {
        System.out.println(StringUtils.isLowerCase("LVYAHUI"));
    }

    public void testConvParams() throws Exception {
        List<String> params = new ArrayList<String>();
        params.add("name=\"lv yahui\"");
        params.add("pfx=/usr/local");
        params.add("-f");
        System.out.println(StringUtils.convParams(params));
    }

    public void testToCharset() throws Exception {
        System.out.println(StringUtils.toCharset("吕亚辉","GBK"));
        System.out.println(StringUtils.toCharset("吕亚辉", Charset.GBK));
        BufferedReader reader = new BufferedReader(new InputStreamReader(IOUtils.getFileAsStream("data/gbk_text.txt")));
        if(reader.ready()){
            String line ;
            while((line = reader.readLine()) != null){
                System.out.println(StringUtils.toCharset(line,Charset.UTF8));
            }
        }
        IOUtils.closeStream(reader);
    }

    public void testGetEncoding() throws Exception {

    }
    public void testIsNumber() throws Exception {
        System.out.println(StringUtils.isNumber("120"));
        System.out.println(StringUtils.isNumber("120L"));
        System.out.println(StringUtils.isNumber("120"));
        System.out.println(StringUtils.isNumber("3.14E-5"));
        System.out.println(StringUtils.isNumber("0.0314"));
        System.out.println(StringUtils.isNumber("120F"));
    }

    public void testLjust() throws Exception {
        System.out.println(StringUtils.ljust("FF0B",8,'0'));
        System.out.println(StringUtils.ljust("FFFFFFAABB",8,'0'));
    }

    public void testRjust() throws Exception {
        System.out.println(StringUtils.rjust("FF0B",8,'0'));
        System.out.println(StringUtils.rjust("FFFFFFAABB",8,'0'));
    }

    public void testEach() throws Exception {
        byte[] b = {(byte) 99, (byte)97, (byte)116};
        String s = new String(b, "UTF-8");
        System.out.println(s);
    }

    public void testIsChinese() throws Exception {
        System.out.println(StringUtils.isChinese("abc==吕亚辉==adb"));
    }

    public void testReplaceEmoji() throws Exception {

        byte [][] emojis = new byte[][]{
                // 笑哭
                {(byte) 0xF0,(byte) 0x9F,(byte) 0x93,(byte) 0xA0},
                // 吻
                {(byte) 0xF0,(byte) 0x9F,(byte) 0x8E,(byte) 0x86},
        };
        byte [] randomChars = "abcdefg".getBytes();
        List<Byte> chars = new ArrayList<Byte>();

        Random r = new Random(System.currentTimeMillis());

        for (int i = 0; i < 10 ; i++){
            byte [] emoji = emojis[r.nextInt(emojis.length)];
            for (Byte b : emoji){
               chars.add(b);
            }
            chars.add(randomChars[r.nextInt(randomChars.length)]);
        }
        byte charsArr [] = new byte[chars.size()];
        for (int i = 0; i < chars.size() ; i ++ ){
            charsArr[i] = chars.get(i);
        }
        String str = new String(charsArr,0,charsArr.length,"UTF-8");

        System.out.println(str);

        str = StringUtils.replaceEmoji(str,"表情");

        System.err.println(str);
    }

    public void testContains() throws Exception {
        System.out.println(StringUtils.contains("sam;kel;den,jel","sa","[;,]"));
        System.out.println(StringUtils.contains("sam;kel;den,jel","sam","[;,]"));
    }

    public void testLtrim() throws Exception {
        System.out.println(StringUtils.ltrim("lvyahui","lv"));
    }

    public void testRtrim() throws Exception {
        System.out.println(StringUtils.rtrim("lvyahui","hui"));
    }
}