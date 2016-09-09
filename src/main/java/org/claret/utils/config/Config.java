package org.claret.utils.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * @author samlv
 */
public abstract class Config extends HashMap<String,Object>{
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

    public Object get(String key, Object defaultValue) {
        return this.containsKey(key) ? this.get(key) : defaultValue;
    }

    public abstract boolean load(InputStream confFileStream) throws IOException;

}
