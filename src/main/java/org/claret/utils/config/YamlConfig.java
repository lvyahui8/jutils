package org.claret.utils.config;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * @author samlv
 */
public class YamlConfig extends Config {
    @Override
    public boolean load(InputStream confFileStream) throws IOException {
        Yaml yaml = new Yaml(new SafeConstructor());
        Map ret = (Map) yaml.load(new InputStreamReader(confFileStream));
        initMap();
        this.confs.putAll(ret);
        return false;
    }
}
