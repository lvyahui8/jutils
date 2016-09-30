package org.claret.utils;

import org.apache.log4j.Logger;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author samlv
 * Created by samlv on 2016/8/30.
 */
abstract public class Utils {
    protected static Logger logger ;

    static {
        logger = Logger.getLogger(Utils.class);
    }

    public static void closeStream(Closeable stream){
        if(stream != null){
            try {
                stream.close();
            } catch (IOException e) {
                throw new RuntimeException("unable to close stream " + stream.toString());
            }
        }
    }
}
