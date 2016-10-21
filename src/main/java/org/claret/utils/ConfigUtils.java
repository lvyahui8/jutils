package org.claret.utils;

import org.claret.utils.config.*;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author samlv
 */
public class ConfigUtils {

    /**
     * 加载配置文件
     * @param confFileName 配置文件
     * @return 配置对象
     * @throws IOException
     */
    public static Config load(String confFileName) throws IOException{
        String ext = confFileName.substring(confFileName.lastIndexOf('.'));
        Config config = null;
        InputStream stream = IOUtils.getFileAsStream(confFileName);
        if(stream != null){
            if(ext.equals(Config.Type.JSON.toString())){
                config = new JsonConfig();
            }else if(ext.equals(Config.Type.PROPERTIES.toString())){
                config = new PropertiesConfig();
            }else if(ext.equals(Config.Type.YAML.toString())){
                config = new YamlConfig();
            }else if(ext.equals(Config.Type.XML.toString())){
                config = new XmlConfig();
            }

            try {
                if(config != null){
                    config.load(stream);
                }
            }finally {
                stream.close();
            }
        }

        return config;
    }
}
