package org.claret.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Console工具类
 * Created by lvyahui on 2016/8/26.
 */
public class ShellUtils {

    public enum OSType {
        WINDOWS, LINUX, MAC, FREEBSD, OTHER;

        private String osName;

        @Override
        public String toString() {
            return osName;
        }

        public void setOsName(String osName) {
            this.osName = osName;
        }
    }

    private static OSType osType;

    /**
     * 获取操作系统类型
     *
     * @return OSType类型
     */
    public static OSType getOsType() {
        if (osType == null) {
            // 初始化系统类型
            String osName = System.getProperty("os.name");
            if (osName.startsWith("Windows")) {
                osType = OSType.WINDOWS;
            } else if (osName.startsWith("Linux")) {
                osType = OSType.LINUX;
            } else if (osName.startsWith("Mac")) {
                osType = OSType.MAC;
            } else if (osName.startsWith("FreeBSD")) {
                osType = OSType.FREEBSD;
            } else {
                osType = OSType.OTHER;
            }
            osType.setOsName(osName);
        }
        return osType;
    }

    /**
     * 执行一条命令
     *
     * @param cmd 命令分组
     * @return 标准输出
     */

    public static String execCommand(final String... cmd) throws IOException {
        ProcessBuilder builder = new ProcessBuilder(getExecString(cmd));
        builder.redirectErrorStream(true);
        Process process = builder.start();

        final BufferedReader errReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        final BufferedReader outReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        final StringBuilder errMsg = new StringBuilder();
        final StringBuilder outMsg = new StringBuilder();


        new Thread(new Runnable() {
            public void run() {
                String line;
                try {
                    while ((line = errReader.readLine()) != null) {
                        errMsg.append(line)
                                .append(System.getProperty("line.separator"));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        try {
            String line;
            while((line = outReader.readLine()) != null){
                outMsg.append(line).append(IOUtils.SYS_FILE_SP);
            }
            // 等待进程结束
            int exitCode = process.waitFor();
            if(exitCode != 0){
                // 执行出错
            }
            System.out.println(outMsg);
            System.out.println(errMsg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            synchronized (process.getErrorStream()){
                errReader.close();
            }
            synchronized (process.getInputStream()){
                outReader.close();
            }
        }

        return null;
    }

    public static void execCommandNotWait(String ... cmd){
        ProcessBuilder builder = new ProcessBuilder(getExecString(cmd));
        try {
            builder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String[] getExecString(String[] cmd) {
        String [] commandParser = getCommandParser();
        String [] commands = new String [commandParser.length + cmd.length];
        System.arraycopy(commandParser,0,commands,0,commandParser.length);
        System.arraycopy(cmd,0,commands,commandParser.length,cmd.length);
        return commands;
    }

    /**
     * 执行一条命令
     *
     * @param cmd 命令
     * @return 标准输出
     */
    public static String execCommand(final String cmd) {

        return null;
    }


    @SuppressWarnings("unused")
    private static String[] getCommandParser() {
        return getOsType() == OSType.WINDOWS ? new String[]{"cmd", "/c"} : new String[]{"bash", "-c"};
    }

}
