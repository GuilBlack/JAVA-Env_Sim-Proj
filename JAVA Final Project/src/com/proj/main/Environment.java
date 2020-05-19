package com.proj.main;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class Environment extends Canvas implements Runnable {

    public static final int WIDTH = 1280, HEIGHT = WIDTH / 16 * 9;

    private Thread thread;
    private boolean running = false;

    public Environment() {
        new Window(WIDTH, HEIGHT, "Duck Environment", this); //this will take the new environment object to create a new window
    }

    //synchronized if we do multi-threading,
    //prevents a deadlock situation (only 1 thread can access 1 resource)
    public synchronized void start() {
        //create new thread with the environment object running in it
        thread = new Thread(this);
        thread.start();
        running = true;
    }

    public synchronized void stop() {
        //stops the thread
        try {
            thread.join();
            running = false;
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    //the game loop
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0; // tics/sec we want (for the game it's the number of FPS wanted)
        double ns = 1000000000 / amountOfTicks; //nanosecs per tick
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while(running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            //when delta >= 1, 1 tick has passed
            while(delta >= 1) {
                tick();
                delta--;
            }

            if(running) {
                render();
            }
            frames++;

            if(System.currentTimeMillis() - timer > 1000)
            {
                timer += 1000;
                System.out.println("FPS: "+ frames);
                frames = 0;
            }
        }
        stop();
    }

    private void tick() {

    }

    private void render() {
        BufferStrategy bs = this.getBufferStrategy(); //creates a buffer strategy to update our display
        if(bs == null) {
            this.createBufferStrategy(3); //doing a triple buffering
            return;
        }

        // Get a new graphics context every time through the loop
        // to make sure the strategy is validated
        Graphics g = bs.getDrawGraphics();

        g.setColor(Color.CYAN);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.dispose(); //dispose of the graphics
        bs.show(); //display the buffer
    }

    public static void main(String args[]) {
        new Environment();
    }
}
