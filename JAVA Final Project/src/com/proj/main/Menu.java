package com.proj.main;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Menu extends MouseAdapter {

    private Environment env;
    private Handler handler;

    private int[] rockX = {30, 1048, 280, 290, 650, 800, 920};
    private int[] rockY = {40, 256, 180, 520, 430, 35, 600};
    private static int numDucks, numLilies;


    public Menu(Environment env, Handler handler) {
        this.env = env;
        this.handler = handler;

        numDucks = env.getRandomInt(5, 10);
        numLilies = (int)(numDucks * 1.5);

    }

    public void mousePressed(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();

        //start button
        if(mouseOver(mx, my, 450, 400, 250, 50)) {
            env.envState = Environment.STATE.Game;
            //creating some rocks
            for (int i = 0; i < 7; i++) {
                handler.addObject(new Rock(rockX[i], rockY[i], ID.Rock));
            }
            //creating some lilies
            for (int i = 0; i < numLilies; i++) {
                handler.addObject(new WaterLily(env.getRandomInt(44, 1200), env.getRandomInt(0, 600), ID.WaterLily));
            }
            //creating some ducks
            for (int i = 0; i < numDucks; i++) {
                handler.addObject(new Duck(env.getRandomInt(392, 990), env.getRandomInt(40, 380), ID.smallDuck, handler));
            }
        }

        //quit button
        if(mouseOver(mx, my, 450, 500, 250, 50)) {
            System.exit(0);
        }

    }

    public void mouseReleased(MouseEvent e) {

    }

    private boolean mouseOver(int mx, int my, int x, int y, int width, int height) {
        if (mx > x && mx < x + width) {
            if (my > y && my < y + height) {
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }

    public void tick() {

    }

    public void render(Graphics g) {
        Font fnt = new Font("arial", 1, 60);
        Font fnt2 = new Font("arial", 1, 30);

        //title
        g.setFont(fnt);
        g.setColor(new Color(225, 225,225));
        g.drawString("Duck Simulation", 380, 125);

        //start button
        g.setFont(fnt2);
        g.setColor(new Color(225, 225,225));
        g.drawString("START", 525, 435);

        g.setColor(new Color(225, 225,225));
        g.drawRect(450, 400, 250, 50);

        //quit button
        g.setFont(fnt2);
        g.setColor(new Color(225, 225,225));
        g.drawString("QUIT", 536, 535);

        g.setColor(new Color(225, 225,225));
        g.drawRect(450, 500, 250, 50);
    }

}
