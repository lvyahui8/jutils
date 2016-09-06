package org.claret.utils;

import junit.framework.TestCase;
import org.claret.utils.config.Config;

/**
 * @author samlv
 */
public class ConfigUtilsTest extends TestCase {

    public void testLoad() throws Exception {
        Config config = ConfigUtils.load("config.properties");
        System.out.println(config.getConfs());
    }
}