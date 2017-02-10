#include "org_claret_utils_StringUtils.h"
#include <string>
#include <cstring>

/*
 * 暂时不清楚jni是值传递还是引用传递，不过看JNIEnv的api返回的是const类型，估计应该操作的是同一块内存区，是引用传递
 */

bool is_start_with(const char * str1, const char * str2){
    while((*str2 != '\0') && * str1 ++ == * str2 ++ );

    return * str2 == '\0';
}

bool is_end_with(const char * str1, const char * str2){
    size_t len1 = strlen(str1);
    size_t len2 = strlen(str2);
    const char * pCh1 =  str1 + len1;
    const char * pCh2 = str2 + len2;
    while(pCh2 != str2 && * pCh1-- == * pCh2--);

    return pCh2 == str2;
}

extern "C"
JNIEXPORT jstring JNICALL Java_org_claret_utils_StringUtils_ltrim
  (JNIEnv * env, jclass cls, jstring j_str, jstring j_chars){
    const char * str = env->GetStringUTFChars(j_str, 0);
    const char * chars = env->GetStringUTFChars(j_chars, 0);

    size_t chars_len = strlen(chars);
    const char * pCh = str;
    for(; * pCh != '\0'; ){
       if (is_start_with(pCh,chars)){
           pCh += chars_len;
       } else{
           break;
       }
    }
    env->ReleaseStringUTFChars(str_, str);
    env->ReleaseStringUTFChars(chars_, chars);
    return env->NewStringUTF(pCh);
}

extern "C"
JNIEXPORT jstring JNICALL Java_org_claret_utils_StringUtils_rtrim
  (JNIEnv * env, jclass cls, jstring j_str, jstring j_chars){
    const char *str = env->GetStringUTFChars(j_str, 0);
    const char *chars = env->GetStringUTFChars(j_chars, 0);

    // TODO
    size_t chars_len = strlen(chars);
    const char * pCh = str;
    for(; pCh >= str; ){
        if (is_end_with(str,chars)){
            pCh -= chars_len;
            // * pCh = '\0';
        } else{
            break;
        }
    }
    env->ReleaseStringUTFChars(str_, str);
    env->ReleaseStringUTFChars(chars_, chars);
    return env->NewStringUTF(str);
}

