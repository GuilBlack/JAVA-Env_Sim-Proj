package com.proj.main;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class Environment extends Canvas implements Runnable {

    public static final int WIDTH = 1280, HEIGHT = WIDTH / 16 * 9;

    private Thread thread;
    private boolean running = false;
    private Menu menu;

    //states of the environment
    public enum STATE {
        Menu,
        Game
    }

    //beginning state
    public STATE envState = STATE.Menu;

    private Handler handler;

    public Environment() {
        handler = new Handler();

        //creating a new menu
        menu = new Menu(this, handler);
        this.addMouseListener(menu);

        new Window(WIDTH, HEIGHT, "Duck Environment", this); //this will take the new environment object to create a new window

    }

    public static int getRandomInt(double min, double max){
        int x = (int)((Math.random()*((max-min)+1))+min);
        return x;
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
        double amountOfTicks = 61.0; // tics/sec we want (for the game it's the number of FPS wanted)
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
                render();
                frames++;
                delta--;
            }

            //if you don't want to cap your frames to 61
            //comment out render() and frames++ in the while loop above
            //uncomment the 4 lines under this
//            if(running) {
//                render();
//                frames++;
//            }

            //display in console number of fps
            if(System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                System.out.println("FPS: "+ frames);
                frames = 0;
            }
        }
        //stop the thread if the simulation isn't running anymore
        stop();
    }

    private void tick() {
        if (envState == STATE.Game) {
            //update each objects in the environment
            handler.tick();
        }else {
            menu.tick();
        }
    }

    private void render() {
        BufferStrategy bs = this.getBufferStrategy(); //get a buffer strategy to update our display
        if(bs == null) {
            this.createBufferStrategy(2); //create a double buffering strategy
            return;
        }

        // Get a new graphics context every time through the loop
        // to make sure the strategy is validated
        Graphics g = bs.getDrawGraphics();



        //draw the objects in the game
        if (envState == STATE.Game) {
            g.setColor(Color.CYAN);
            g.fillRect(0, 0, WIDTH, HEIGHT);
            handler.render(g);
        }else {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, WIDTH, HEIGHT);
            menu.render(g);
        }

        g.dispose(); //dispose of the graphics
        bs.show(); //display the buffer
    }

    //main
    public static void main(String args[]) {
        new Environment();
    }
}
