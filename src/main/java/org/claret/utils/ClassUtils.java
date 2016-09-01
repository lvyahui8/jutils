package org.claret.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 类工具
 *
 * @author samlv
 */
public class ClassUtils extends Utils {
    public static ClassLoader getDefaultClassLoader() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = ClassUtils.class.getClassLoader();
        }
        return loader;
    }

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

    public static boolean fill(Object bean, Map<String, Object> values) {
        Class clazz = bean.getClass();
        Field fields[] = clazz.getDeclaredFields();
        try {
            for (Field field : fields) {
                String fieldName = field.getName();
                // 查找参数集合
                Object value = values.get(fieldName);
                if (value != null) {
                    String methodName = "set" + fieldName.substring(0, 1).toUpperCase()
                            + fieldName.substring(1);
                    Class fieldType = field.getType();
                    Method method = clazz.getMethod(methodName, fieldType);
                    method.invoke(bean, fieldType.cast(value));
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }

    }
}
