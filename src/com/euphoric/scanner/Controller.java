package com.euphoric.scanner;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Controller {

    @FXML
    public JFXTextField start;

    @FXML
    public JFXTextField end;

    @FXML
    public JFXTextArea console;

    @FXML
    public JFXButton button;

    @FXML
    public JFXTextField ip;

    public void onClick() {
        button.setDisable(true);
        console.clear();
        new Thread(() -> {
            final ExecutorService es = Executors.newFixedThreadPool(20);
            final int timeout = 150;
            final List<Future<ScanResult>> futures = new ArrayList<>();
            for (int port = Integer.parseInt(start.getText()); port <= Integer.parseInt(end.getText()); port++) {
                futures.add(portIsOpen(es, ip.getText(), port, timeout));
            }
            try {
                es.awaitTermination(150L, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int openPorts = 0;
            for (final Future<ScanResult> f : futures) {
                try {
                    if (f.get().isOpen()) {
                        openPorts++;
                        console.appendText("Port: " + f.get().getPort() + " is open!\n");
                        System.out.println(f.get().getPort());
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
            console.appendText("There are " + openPorts + " open ports on a host " + ip.getText() + " (probed with a timeout of " + timeout + "ms)");
            button.setDisable(false);
        }).start();
    }

    public static Future<ScanResult> portIsOpen(final ExecutorService es, final String ip, final int port, final int timeout) {
        return es.submit(() -> {
            try {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(ip, port), timeout);
                socket.close();
                return new ScanResult(port, true);
            } catch (Exception ex) {
                return new ScanResult(port, false);
            }
        });
    }

    public static class ScanResult {
        private int port;

        private boolean isOpen;

        public ScanResult(int port, boolean isOpen) {
            super();
            this.port = port;
            this.isOpen = isOpen;
        }

        public int getPort() {
            return port;
        }

        public boolean isOpen() {
            return isOpen;
        }
    }
}
