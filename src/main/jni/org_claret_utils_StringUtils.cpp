#include "org_claret_utils_StringUtils.h"

JNIEXPORT jstring JNICALL Java_org_claret_utils_StringUtils_ltrim
  (JNIEnv * env, jclass cls, jstring j_str, jstring j_chars){
const char *c_str = NULL;
    const char * c_str = NULL;
    const char * c_chars = NULL;
    c_str = env->GetStringUTFChars(env, j_str, NULL);
    c_chars =  env->GetStringUTFChars(env,j_chars, NULL);


    return NULL;
}

JNIEXPORT jstring JNICALL Java_org_claret_utils_StringUtils_rtrim
  (JNIEnv * env, jclass cls, jstring j_str, jstring j_chars){

    return NULL;
}

