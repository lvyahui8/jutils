package org.claret.utils;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 类工具
 *
 * @author samlv
 */
@SuppressWarnings("unused")
public class ClassUtils extends CommonUtils {

    /**
     * 获取默认的类加载器
     *
     * @return 类是否存在
     */
    public static ClassLoader getDefaultClassLoader() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = ClassUtils.class.getClassLoader();
        }
        return loader;
    }

    /**
     * 检测类可见性
     *
     * @param className 类名
     * @param loader 类加载器
     * @return 是否可见
     */
    public static boolean isVisible(String className, ClassLoader loader) {
        if (loader == null) {
            loader = getDefaultClassLoader();
        }
        try {
            Class<?> actualClass = loader.loadClass(className);
            return actualClass != null;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * 使用map填充对象属性，待填充对象需要符合java bean标准
     *
     * @param bean 待填充对象
     * @param values 值map
     */
    public static void fill(Object bean, Map<String, Object> values)  {
        Class<?> clazz = bean.getClass();

        for (Map.Entry<String,Object> item : values.entrySet()){
            try {
                Class<?> fieldType ;
                Method method ;
                String fieldName  = item.getKey();
                Object value  = item.getValue();
                Field field = clazz.getDeclaredField(fieldName);
                fieldType = field.getType();
                method = clazz.getMethod("set".concat(StringUtils.ucfirst(fieldName)),fieldType);

                method.invoke(bean,fieldType.cast(value));
            } catch (ReflectiveOperationException e) {
                //
            }
        }
    }

    /**
     * 以字符串数组的形式获取类的所有字段
     *
     * @param className 类名
     * @return 所有字段
     */
    public static String [] getFieldsAsString(String className){
        List<String> strFields = new ArrayList<String>();
        for (Field field : getFields(className)){
            strFields.add(field.getName());
        }
        return strFields.toArray(new String[strFields.size()]);
    }

    /**
     * 获取类的所有字段
     *
     * @param className 类名
     * @return 字段数组
     */
    public static Field [] getFields(String className){
        Class<?> clazz = loadClass(className);
        return clazz != null ?  clazz.getDeclaredFields() : new Field[]{};
    }

    /**
     * 加载类
     *
     * @param className 类名
     * @return 类的字节码对象
     */
    public static Class<?> loadClass(String className){
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    /**
     * 序列化对象为字节数组
     * @param obj 待序列化对象
     * @return 序列化数据
     */
    public static byte [] serialize(Object obj){
        ByteArrayOutputStream objBytes = new ByteArrayOutputStream();
        try {
            ObjectOutputStream out = new ObjectOutputStream(objBytes);
            out.writeObject(obj);
            return objBytes.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 序列化对象为字节数组
     * @param obj 待序列化对象
     * @param file 目标文件
     * @return 是否序列化成功
     */
    public static boolean serializeToFile(Object obj,File file) throws IOException{
        if(obj == null || file == null) return false;

        if(!file.exists()){
            try {
                if(file.createNewFile()){
                    return false;
                }
            } catch (IOException e) {
                throw new IOException("No permission to create file : " + file.getName());
            }
        } else if(file.isDirectory()){
            throw new IOException("There is a directory with the same name : " + file.getName());
        }

        BufferedOutputStream outputStream = null;
        try{
            outputStream = new BufferedOutputStream(new FileOutputStream(file));
            byte [] data = serialize(obj);
            if(data != null){
                outputStream.write(data);
                outputStream.flush();
                return true;
            }
        } finally {
            closeStream(outputStream);
        }


        return false;
    }

    /**
     * 反序列化字节数组为对象
     * @param data 序列化数据
     * @return 反序列化对象
     */
    public static Object deserialize(byte [] data){
        if(data == null){
            return null;
        }
        ByteArrayInputStream bin = new ByteArrayInputStream(data);
        try
        {
            ObjectInputStream obin = new ObjectInputStream(bin);
            return obin.readObject();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 反序列化数据文件为对象
     * @param file 序列化数据文件
     * @return 反序列化对象
     */
    public static Object deserializeFromFile(File file) throws IOException{
        BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file));
        byte [] data = new byte[4096];
        int len;
        try{
            if((len = inputStream.read(data)) >= 0){
                return deserialize(Arrays.copyOf(data,len));
            } else {
                return null;
            }
        } finally {
            closeStream(inputStream);
        }
    }

    /**
     * 获取调用者类名
     * @return 调用者类名
     */
    public static String getInvokeClassName(){
        return new Exception().getStackTrace()[2].getClassName();
    }

    /**
     * 获取调用者方法名
     * @return 调用者方法名
     */
    public static String getInvokeMethodName(){
        return new Exception().getStackTrace()[2].getMethodName();
    }

    public static int getInvokeLineNumber(){
        return new Exception().getStackTrace()[2].getLineNumber();
    }
}
