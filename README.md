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
    <version>1.0.5</version>
</dependency>
```

## 编译native库

在jutils根目录编译，如果目标目录不存在，先建立

### windows make

#### win32

打开VS 2012 x86 命令行工具编译

cl -I"%JAVA_HOME%\include" -I"%JAVA_HOME%\include\win32" -LD src/main/jni/org_claret_utils_StringUtils.cpp -Felib/native/win32/jutils_native.dll

#### win64

打开vs 2012 x64 命令行工具编译

cl -I"%JAVA_HOME%\include" -I"%JAVA_HOME%\include\win32" -LD src/main/jni/org_claret_utils_StringUtils.cpp -Felib/native/win64/jutils_native.dll

### linux make

#### linux x86_64

gcc -I/usr/java/jdk1.8.0_91/include -I/usr/java/jdk1.8.0_91/include/linux -fPIC -shared  src/main/jni/org_claret_utils_StringUtils.cpp -o lib/native/linux_x86_64/jutils_native.so

