package com.proj.main;

import javax.sound.sampled.*;
import java.io.File;

public class Whistle implements Runnable {

    String whistleFileName;
    Thread whistleThread;

    Whistle() {
        whistleFileName = "./src/Audio/bbwd1.wav";
    }

    public void playWhistle() {
        try {
            File whistleFile = new File(whistleFileName);
            AudioInputStream ais = AudioSystem.getAudioInputStream(whistleFile); //new audio input stream
            AudioFormat format = ais.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            Clip clip = (Clip)AudioSystem.getLine(info); //creates audio clip
            clip.open(ais);
            clip.start(); //play the clip
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        playWhistle();
        System.out.println("whistle finished");
        stop();
        System.out.println("thread stopped");
    }

    public synchronized void whistleStart() {
        whistleThread = new Thread(this); //run a thread for the whistle
        whistleThread.start();
    }

    public synchronized void stop() {
        try {
            whistleThread.join(); //stop the thread for the whistle
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

}
