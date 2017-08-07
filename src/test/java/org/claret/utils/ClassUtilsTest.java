package org.claret.utils;

import junit.framework.TestCase;
import org.claret.vo.User;

import java.io.File;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

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

    private User createSimpleUser(){
        User user = new User();
        user.setId(1);
        user.setUsername("lvyahui");
        user.setLastLogin(new Timestamp(System.currentTimeMillis()));
        return user;
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

    public void testSerialize() throws Exception {
        User user = createSimpleUser();

        byte [] userData = ClassUtils.serialize(user);
        User reUser = (User) ClassUtils.deserialize(userData);
        System.out.println(reUser);
    }

    public void testDeserialize() throws Exception {

    }

    public void testSerializeToFile() throws Exception {
        User user = createSimpleUser();
        ClassUtils.serializeToFile(user,new File("E:/tmp/obj"));
    }

    public void testDeserializeFromFile() throws Exception {
        System.out.println(ClassUtils.deserializeFromFile(new File("E:/tmp/obj")));
    }

    public void testGetFields() throws Exception {

    }


    public void testGetInvokeClassName() throws Exception {

    }

    public void testGetInvokeMethodName() throws Exception {

    }

    public void testGetInvokeLineNumber() throws Exception {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        System.out.println(new Date());
        TimeZone.setDefault(TimeZone.getTimeZone("America/Los_Angeles"));
        System.out.println(new Date());
    }
}