package com.proj.main;

public class Whistle implements Runnable {

    Thread whistleThread;

    Whistle() {

    }

    public void run() {

    }

    public void start() {
        whistleThread = new Thread(this);
    }

    public void stop() {
        try {
            whistleThread.join();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

}
