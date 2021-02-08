package app.web.pavelk.speech3;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class Main {
    public static void main(String[] args) {
        System.out.println("main");

        JFrame jFrame = new JFrame();
        jFrame.setBounds(500, 500, 50, 50);
        JButton jButton = new JButton("->");
        SystemTray sT = SystemTray.getSystemTray();

        jButton.setSize(50, 50);
        jFrame.getContentPane().add(jButton);
        //JDialog JFrame
        jFrame.setType(javax.swing.JFrame.Type.UTILITY);
        jFrame.setUndecorated(true);
        jFrame.setAlwaysOnTop(true);

        ExecutorService es = Executors.newFixedThreadPool(1);
        Speech speech = new Speech();

        es.submit(() -> {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            AtomicReference<String> text = new AtomicReference<>();
            AtomicBoolean atomicBoolean = new AtomicBoolean(false);

            clipboard.addFlavorListener(e -> {
                if (atomicBoolean.get()) {
                    atomicBoolean.set(false);
                    return;
                }

                String string = null;
                Transferable contents = clipboard.getContents(new Object());
                try {
                    string = (String) contents.getTransferData(DataFlavor.stringFlavor);
                } catch (UnsupportedFlavorException | IOException unsupportedFlavorException) {
                    unsupportedFlavorException.printStackTrace();
                }
                //content
                System.out.println("-- " + string);
                text.set(string);

                jFrame.setLocation((int) MouseInfo.getPointerInfo().getLocation().getX(),
                        (int) MouseInfo.getPointerInfo().getLocation().getY());
                jFrame.setVisible(true);

                clipboard.setContents(contents, null);
                atomicBoolean.set(true);
            });


            jButton.addActionListener(e -> {
                jFrame.setVisible(false);
                speech.speechText(text.get(), text.get().getBytes(StandardCharsets.UTF_8).length);
            });

        });
    }
}
