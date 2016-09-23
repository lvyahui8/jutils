package org.claret.utils.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

/**
 * @author samlv
 */
public class PropertiesConfig extends Config {
    @Override
    public boolean load(InputStream confFileStream) throws IOException{
        Properties properties = new Properties();
        properties.load(confFileStream);
        for (Enumeration props = properties.propertyNames(); props.hasMoreElements(); ) {
            String prop = (String) props.nextElement();
            this.put(prop, properties.getProperty(prop));
        }
        return true;
    }
}

