package app.web.pavelk.speech3;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;


public class Main {
    public static void main(String[] args) throws IOException, AWTException {
        System.out.println("main");
        AtomicReference<Boolean> b1 = new AtomicReference<>(Boolean.TRUE);
        //--
        PopupMenu trayMenu = new PopupMenu();
        MenuItem menuItem2 = new MenuItem("on");
        menuItem2.addActionListener(e -> {
            if(b1.get()){
                b1.set(Boolean.FALSE);
                trayMenu.getItem(1).setName("off");

            }else{
                b1.set(Boolean.TRUE);
                trayMenu.getItem(1).setName("on");

            }
        });
        trayMenu.add(menuItem2);
        //**
        MenuItem menuItem1 = new MenuItem("exit");
        menuItem1.addActionListener(e -> System.exit(0));
        trayMenu.add(menuItem1);
        //**

        TrayIcon trayIcon = new TrayIcon(ImageIO.read(new File("ic.png")), "name", trayMenu);
        trayIcon.setImageAutoSize(true);
        SystemTray systemTray = SystemTray.getSystemTray();
        systemTray.add(trayIcon);
        trayIcon.displayMessage("name", "Application started!",
                TrayIcon.MessageType.INFO);
        //--
        JFrame jFrame = new JFrame();
        jFrame.setBounds(500, 500, 50, 50);
        JButton jButton = new JButton("->");

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
                if(!b1.get()){
                    return;
                }

                if (atomicBoolean.get()) {
                    atomicBoolean.set(false);
                    return;
                }

                atomicBoolean.set(false);

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
                speech.speechText(text.get(), text.get().getBytes().length);
            });

        });
    }
}
