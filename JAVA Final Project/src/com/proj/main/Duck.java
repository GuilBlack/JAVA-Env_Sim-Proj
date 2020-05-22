package com.proj.main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class Duck extends EnvironmentObject {
    Handler handler;
    public static final int DUCK_WIDTH = 44;
    public static final int DUCK_HEIGHT = 46;
    private int tempWidth;
    private int healthBarX;
    private int tempX;

    public Duck(int x, int y, ID id, Handler handler) {
        super(x, y, id);
        this.handler = handler;
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/Images/smallDuck.png"));
        }catch(IOException e) {
            e.printStackTrace();
        }
        health = 50;
        velX = getRandomInt(-2, 2);
        velY = getRandomInt(-2, 2);
        if (velY == 0 && velX == 0) {
            int r = getRandomInt(0, 1);
            if (r == 0) {
                velX = getRandomInt(-2, 2);
                if(velX == 0) {
                    velX += 1;
                }
            }else {
                velY = getRandomInt(-2, 2);
                if(velY == 0) {
                    velY += 1;
                }
            }
        }
    }

    public void tick() {
        if (x <= DUCK_WIDTH || x >= (Environment.WIDTH - 58)) {
            velX *= -1;
            velY = getRandomInt(-2, 2);
        }
        if (y <= 0 || y >= (Environment.HEIGHT - 80)) {
            velY *= -1;
            velX = getRandomInt(-2, 2);
        }
        collisionDetection();
        x += velX;
        y += velY;
        health -= 0.05;
    }

    private static int getRandomInt(double min, double max){
        int x = (int)((Math.random()*((max-min)+1))+min);
        return x;
    }

    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        if (velX < 0) {
            tempWidth = DUCK_WIDTH * -1;
            tempX = x - 44;
            healthBarX = x - 44;
        }else {
            tempWidth = DUCK_WIDTH;
            tempX = x;
            healthBarX = x;
        }
        g.setColor(Color.MAGENTA);
        g2d.draw(getCollider());
        g.drawImage(image, x, y, tempWidth, DUCK_HEIGHT, null);
        g.setColor(Color.GRAY);
        g.fillRect((healthBarX - 5), (y - 10), 50, 5);
        g.setColor(Color.green);
        g.fillRect((healthBarX - 5), (y - 10), (int)(health), 5);
    }

    public Rectangle getCollider() {
        if (velX < 0) {
            tempX = x - 44;
        }else {
            tempX = x;
        }
        return new Rectangle(tempX, y, DUCK_WIDTH, DUCK_HEIGHT);
    }

    //detects a collisions between the duck and a rock or a water lily
    public void collisionDetection() {

        for (int i = 0; i < handler.object.size(); i++) {

            EnvironmentObject tempObject = handler.object.get(i);

            if (tempObject.getId() == ID.Rock) {
                //see if there is a collision with the rock
                //if yes, it will go in another direction
                if (getCollider().intersects(tempObject.getCollider())) {
                    velX *= -1;
                    velY *= -1;
                    if (velX > 0) {
                        velX = getRandomInt(1, 2);
                    }else {
                        velX = getRandomInt(-1, -2);
                    }

                    if (velY > 0) {
                        velY = getRandomInt(1, 2);
                    }else {
                        velY = getRandomInt(-1, -2);
                    }
                }
            }
        }
    }
}
