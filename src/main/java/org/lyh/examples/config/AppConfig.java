package org.lyh.examples.config;

import org.claret.utils.config.PropertiesConfig;

/**
 * @author lvyahui (lvyahui8@gmail.com,lvyahui8@126.com)
 * @since 2016/9/9 21:53
 */
public class AppConfig extends PropertiesConfig {

    /**
     * 应用名称
     */
    public static final String APP_NAME             =   "app.name";
    public static final Object APP_NAME_SCHEMA      =   String.class;

    /**
     * 作者
     */
    public static final String APP_AUTHOR           =   "app.author";
    public static final Object APP_AUTHOR_SCHEMA    =   String.class;

}
