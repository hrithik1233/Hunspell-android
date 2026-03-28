#include <jni.h>
#include <string>
#include <vector>
#include "hunspell.hxx"

Hunspell* hunspell = nullptr;

extern "C"
JNIEXPORT void JNICALL
Java_com_fitmind_hunsspell_Utils_SpellChecker_init(
        JNIEnv* env,
jobject thiz,
        jstring affPath,
jstring dicPath) {

const char* aff = env->GetStringUTFChars(affPath, 0);
const char* dic = env->GetStringUTFChars(dicPath, 0);

hunspell = new Hunspell(aff, dic);

env->ReleaseStringUTFChars(affPath, aff);
env->ReleaseStringUTFChars(dicPath, dic);
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_fitmind_hunsspell_Utils_SpellChecker_check(
        JNIEnv* env,
        jobject thiz,
jstring word) {

const char* w = env->GetStringUTFChars(word, 0);
std::string wordStr(w);
env->ReleaseStringUTFChars(word, w);

int result = hunspell->spell(wordStr);

return result != 0;
}

extern "C"
JNIEXPORT jobjectArray JNICALL
        Java_com_fitmind_hunsspell_Utils_SpellChecker_suggest(
        JNIEnv* env,
        jobject thiz,
jstring word) {

const char* w = env->GetStringUTFChars(word, 0);
std::string wordStr(w);
env->ReleaseStringUTFChars(word, w);

std::vector<std::string> suggestions = hunspell->suggest(wordStr);

jobjectArray result = env->NewObjectArray(
        suggestions.size(),
        env->FindClass("java/lang/String"),
        nullptr
);

for (int i = 0; i < suggestions.size(); i++) {
env->SetObjectArrayElement(
        result,
        i,
        env->NewStringUTF(suggestions[i].c_str())
);
}

return result;
}