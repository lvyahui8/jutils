package org.claret.utils;

import java.io.File;
import java.io.IOException;

/**
 * 视频工具类，此工具需要依赖ffmpeg程序存在PATH路径下
 * 暂时是linux版本
 * Created by lvyahui on 2016/8/26.
 */
public class VideoUtils {
    public static final String FFMPEG_EXECUTOR = "ffmpeg";

    public static boolean extractThumbnail(File inputFile,String thumbnailOutput,int width ,int height){
        StringBuilder command = new StringBuilder();

        System.out.println(inputFile.getAbsolutePath());
        command.append(FFMPEG_EXECUTOR);
        command.append(" -i ");
        command.append(inputFile.getAbsolutePath());
        command.append(" -y -f image2 -ss 10 -t 0.001 -s ");
        command.append(width+"*"+height+" ");
        command.append(thumbnailOutput);
        String cmd = command.toString();
        return runProccess(cmd);
    }

    private static boolean runProccess(String cmd) {
        ProcessBuilder builder = new ProcessBuilder("/bin/bash","-c",cmd);
        builder.redirectErrorStream(true);
        try {
            Process process = builder.start();
            process.exitValue();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
