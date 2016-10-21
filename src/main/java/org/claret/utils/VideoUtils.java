package org.claret.utils;

import java.io.File;
import java.io.IOException;

/**
 * 视频工具类，此工具需要依赖ffmpeg程序存在PATH路径下
 * 暂时是linux版本
 * Created by lvyahui on 2016/8/26.
 */
public class VideoUtils extends CommonUtils {
    public static final String FFMPEG_EXECUTOR = "ffmpeg";

    /**
     * 提取视频缩略图
     *
     * @param videoFile 视频文件
     * @param thumbnailOutFileName 输出目录
     * @param width 宽度
     * @param height 高度
     * @return 是否提取成功
     */
    public static boolean extractThumbnail(File videoFile,String thumbnailOutFileName,int width ,int height){
        StringBuilder command = new StringBuilder();

        System.out.println(videoFile.getAbsolutePath());
        command.append(FFMPEG_EXECUTOR);
        command.append(" -i ");
        command.append(videoFile.getAbsolutePath());
        command.append(" -y -f image2 -ss 10 -t 0.001 -s ");
        command.append(width).append("*").append(height).append(" ");
        command.append(thumbnailOutFileName);
        String cmd = command.toString();
        String out;
        try {
            out = ShellUtils.execCommand(cmd);
        } catch (IOException e) {
            return false;
        }
        return out.length() > 0;
    }

}
