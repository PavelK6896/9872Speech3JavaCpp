
#include "app_web_pavelk_speech3_Speech.h"
#include <thread>
#include <jni.h>
#include <atlbase.h>
#include <atlcom.h>
#include <sapi.h>
#include <xstring>
#include <codecvt>

extern CComModule _Module;
using namespace std;

JNIEXPORT void JNICALL Java_app_web_pavelk_speech3_Speech_speechText
        (JNIEnv *env, jobject, jstring text, jint size) {

    jboolean isCopy;
    const char *convertedValue = (env)->GetStringUTFChars(text, &isCopy);

    std::string stringText = std::string(convertedValue, size);
    std::string stringS = "<voice required='Language=419;Name=IVONA 2 Tatyana OEM;'><rate speed='10'/>";
    stringS += stringText;

    std::wstring_convert<std::codecvt_utf8_utf16<wchar_t>> convert;
    std::wstring  wid = convert.from_bytes(stringS);
    LPCWSTR lpcwstr = wid.c_str();

    ISpVoice *pVoice = NULL;
    if (FAILED(::CoInitialize(NULL)))
        return;

    HRESULT hr = CoCreateInstance(CLSID_SpVoice, NULL, CLSCTX_ALL, IID_ISpVoice, (void **) &pVoice);

    if (SUCCEEDED(hr)) {

        hr = pVoice->Speak(lpcwstr, 0, NULL);
        pVoice->Release();
        pVoice = NULL;
    }
    ::CoUninitialize();
}
