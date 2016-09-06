package org.claret.utils.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author samlv
 */
public abstract class Config {
    public enum Type {
        JSON(".json"), XML(".xml"), YAML(".yaml"), PROPERTIES(".properties");

        private String ext;

        Type(String ext) {
            this.ext = ext;
        }

        @Override
        public String toString() {
            return ext;
        }
    }

    protected Map<String, Object> confs;

    public abstract boolean load(InputStream confFileStream) throws IOException;

    protected void initMap(){
        if(this.confs == null){
            confs = new HashMap<String, Object>();
        }else{
            confs.clear();
        }
    }


    public Map<String, Object> getConfs() {
        return confs;
    }

    public Object get(String key) {
        return confs.get(key);
    }

    public Object get(String key, Object defaultValue) {
        return confs.containsKey(key) ? confs.get(key) : defaultValue;
    }
}
