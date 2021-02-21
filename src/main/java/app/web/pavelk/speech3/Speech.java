package app.web.pavelk.speech3;

public class Speech {
    native void speechText(String text, int size);

    static {
        System.load( "D:\\D.BackEnd.1\\java\\9872Speech3JavaCpp\\bin\\d1.dll");
    }
}

