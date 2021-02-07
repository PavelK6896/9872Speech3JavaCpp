package app.web.pavelk.speech3;

public class Speech {
    native void speechText(String text, int size);

    static {
        System.load("bin\\d1.dll");
    }
}

