package org.claret.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 类工具
 *
 * @author samlv
 */
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

//        // 获取所有字段，包括private的
//        Field fields[] = clazz.getDeclaredFields();
//
//        for (Field field : fields) {
//            String fieldName = field.getName();
//            // 查找参数集合
//            Object value = values.get(fieldName);
//            if (value != null) {
//                String methodName = "set" + fieldName.substring(0, 1).toUpperCase()
//                        + fieldName.substring(1);
//                Class fieldType = field.getType();
//                Method method;
//                try {
//                    method = clazz.getMethod(methodName, fieldType);
//                } catch (NoSuchMethodException e) {
//                    method = null;
//                }
//                if(method != null){
//                    try {
//                        // method.setAccessible(true);
//                        method.invoke(bean, fieldType.cast(value));
//                    } catch (ReflectiveOperationException e){}
//                }
//            }
//        }

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

            }
        }
    }
}
