package org.claret.utils;

import org.claret.utils.config.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author samlv
 */
public class ConfigUtils {
    public static Config load(String confFilePath) throws IOException{
        String ext = confFilePath.substring(confFilePath.lastIndexOf('.'));
        Config config = null;
        File file = new File(confFilePath);
        InputStream stream;
        if(file.exists()){
            stream = new FileInputStream(file);
        }else{
            stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(confFilePath);
        }
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
