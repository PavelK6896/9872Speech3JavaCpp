
#include "app_web_pavelk_speech3_Speech.h"
#include <thread>
#include <jni.h>
#include <atlbase.h>
#include <atlcom.h>
#include <sapi.h>
#include <xstring>

extern CComModule _Module;
using namespace std;

JNIEXPORT std::wstring convertUtf8ToWide(const std::string &str) {
    int count = MultiByteToWideChar(CP_UTF8, 0, str.c_str(), str.length(), NULL, 0);
    std::wstring wstr(count, 0);
    MultiByteToWideChar(CP_UTF8, 0, str.c_str(), str.length(), &wstr[0], count);
    return wstr;
}

void JNICALL Java_app_web_pavelk_speech3_Speech_speechText
        (JNIEnv *env, jobject, jstring text, jint size) {

    jboolean isCopy;
    const char *convertedValue = (env)->GetStringUTFChars(text, &isCopy);

    std::string stringText = std::string(convertedValue, size);
    std::string stringS = "<voice required='Language=419;Name=IVONA 2 Tatyana OEM;'><rate speed='10' absspeed='10'/>"
                          "<pitch absmiddle='0'/>";
    stringS += stringText;
    std::wstring wid = convertUtf8ToWide(stringS);
    LPCWSTR lpcwstr = wid.c_str();

    ISpVoice *pVoice = NULL;
    if (FAILED(::CoInitialize(NULL)))
        return;

    HRESULT hr = CoCreateInstance(CLSID_SpVoice, NULL, CLSCTX_ALL, IID_ISpVoice, (void **) &pVoice);

    if (SUCCEEDED(hr)) {
        pVoice->Speak(lpcwstr, 0, NULL);
        pVoice->Release();
        pVoice = NULL;
    }
    ::CoUninitialize();
}

