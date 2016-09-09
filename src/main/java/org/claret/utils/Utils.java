package org.claret.utils;

import org.apache.log4j.Logger;

/**
 * @author samlv
 * Created by samlv on 2016/8/30.
 */
abstract public class Utils {
    protected static Logger logger ;

    static {
        logger = Logger.getLogger(Utils.class);
    }
}
