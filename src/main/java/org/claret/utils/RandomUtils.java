package org.claret.utils;

import org.claret.utils.var.Constant;

import java.util.Random;

/**
 * @author lvyahui (lvyahui8@gmail.com,lvyahui8@126.com)
 * @since 2016/9/30 16:06
 */
public class RandomUtils extends CommonUtils {

    /**
     * 随机字符串最大长度
     */
    public static final int RANDOM_STR_MAX_LENGTH = 100;

    private static Random random ;

    static {
        random = new Random(System.currentTimeMillis());
    }

    /**
     * 生成随机颜色值
     *
     * @return 颜色值，如：95CA20
     */
    public static String hexColor(){
        StringBuilder colorBuilder = new StringBuilder();
        for (int i = 0; i < 6 ; i++){
            colorBuilder.append(Constant.HEX_CHAR[random.nextInt(Constant.HEX_CHAR.length)]);
        }
        return colorBuilder.toString();
    }

    /**
     * 生成长度与字母都随机的字符串，其中包含字符[a-zA-Z]
     *
     * @return 随机字符串
     */
    public static String randomString(){
        return randomString(random.nextInt(RANDOM_STR_MAX_LENGTH));
    }

    /**
     * 生成长度固定，字幕随机的字符串，其中包含字符[a-zA-Z]
     *
     * @param length 字符串长度
     * @return 随机字符串
     */
    public static String randomString(int length){
        int ch = 'a';
        StringBuilder builder = new StringBuilder();
        for (int i = 0 ; i < length ; i++){
            builder.append((char) (random.nextInt(26)  + ch));
        }
        return builder.toString();
    }

}
