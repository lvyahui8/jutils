# jutils

[![GitHub release](http://movesun.com/images/svg/jutils.svg?324234)](https://github.com/lvyahui8/jutils/releases)

Java开发中常用的一些工具类，免去重复造轮子的辛苦

## 组件类
 * NetUtils
 * IOUtils
 * NIOUtils
 * StringUtils
 * MailUtils
 * MathUtils
 * ArchiveUtils
 * VideoUtils
 * ...
 
## 文档

## Maven

```xml
<dependency>
    <groupId>org.claret</groupId>
    <artifactId>jutils</artifactId>
    <version>1.0</version>
</dependency>
```

windows make
cl -I"%JAVA_HOME%\include" -I"%JAVA_HOME%\include\win32" -LD src/main/jni/org_claret_utils_StringUtils.cpp -Felib/native/win32/jutils_native.dll
cl -I"%JAVA_HOME%\include" -I"%JAVA_HOME%\include\win32" -LD src/main/jni/org_claret_utils_StringUtils.cpp -Felib/native/win64/jutils_native.dll

linux make

