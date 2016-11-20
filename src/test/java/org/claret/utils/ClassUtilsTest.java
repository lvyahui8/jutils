package org.claret.utils;

import junit.framework.TestCase;
import org.claret.vo.User;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * @author samlv
 */
public class ClassUtilsTest extends TestCase {

    public void testGetDefaultClassLoader() throws Exception {
        System.out.println(ClassUtils.getDefaultClassLoader());
    }


    public void testIsVisible() throws Exception {
        System.out.println(ClassUtils.isVisible("org.claret.utils.IOUtils",null));
        System.out.println(ClassUtils.isVisible("org.claret.utils.AppUtils",null));
    }

    public void testFill() throws Exception {
        User user = new User();
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("id",1);
        params.put("username","lvyahui");
        params.put("lastLogin",new Timestamp(System.currentTimeMillis()));
        ClassUtils.fill(user,params);
        System.out.println(user);

        user = new User();
        params.put("id","1");
        params.put("username","lvyahui");
        params.put("lastLogin","2016-09-01 13:41:04");
        ClassUtils.fill(user,params);
        System.out.println(user);
    }

    public void testGetFieldsAsString() throws Exception {
        String [] fileds = ClassUtils.getFieldsAsString("org.claret.vo.User");
        for (String field : fileds){
            System.out.println(field);
        }
    }


    public void testLoadClass() throws Exception {
        System.out.println(ClassUtils.loadClass("org.claret.vo.User"));
    }
}