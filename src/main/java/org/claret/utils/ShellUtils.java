package org.claret.utils;

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
     * @return OSType类型
     */
    public static OSType getOsType() {
        if (osType == null){
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
     *  执行一条命令
     * @param cmd 命令分组
     * @return 标准输出
     */

    public static String execCommand(final String ... cmd){

        return null;
    }

    /**
     * 执行一条命令
     * @param cmd 命令
     * @return 标准输出
     */
    public static String execCommand(final String cmd){

        return null;
    }


    @SuppressWarnings("unused")
    private static String [] getCommandParser(){
        return osType == OSType.WINDOWS ? new String [] {"cmd","/c"} : new String [] {"bash","-c"};
    }

}
