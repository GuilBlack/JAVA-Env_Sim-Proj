package com.proj.main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;

public class Duck extends EnvironmentObject {

    Handler handler;

    //duck dimensions
    public static final int DUCK_WIDTH = 44;
    public static final int DUCK_HEIGHT = 46;

    //to give an alphaDuck ID
    public static int alphaDuckCounter = 0;

    //to have images for the ducks
    //static not to take too muck memory
    private static BufferedImage SMALL_DUCK;
    private static BufferedImage ADULT_DUCK;
    private static BufferedImage ALPHA_DUCK;

    //for the lining up of the ducks
    public LinkedList<Integer> linePosX = new LinkedList<>();
    public LinkedList<Integer> linePosY = new LinkedList<>();
    public LinkedList<Integer> lineVelX = new LinkedList<>();
    public LinkedList<Integer> lineVelY = new LinkedList<>();

    static {
        try {
            SMALL_DUCK = ImageIO.read(Duck.class.getResourceAsStream("/Images/smallDuck.png"));
            ADULT_DUCK = ImageIO.read(Duck.class.getResourceAsStream("/Images/adultDuck.png"));
            ALPHA_DUCK = ImageIO.read(Duck.class.getResourceAsStream("/Images/alphaDuck.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //vars useful for the alpha && the ducks in line
    private int alphaDuckID;


    private boolean isLinedUp = false;
    public boolean isLinedUp() {
        return isLinedUp;
    }
    public void setLinedUp(boolean linedUp) {
        isLinedUp = linedUp;
    }

    private int lineNumber;
    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }
    public int getLineNumber() {
        return lineNumber;
    }

    private int lineID;
    public void setLineID(int lineID) {
        this.lineID = lineID;
    }
    public int getLineID() {
        return lineID;
    }



    private Whistle whistle;
    private boolean isAlpha = false;

    //stats vars
    private int level = 0;
    private int tempWidth;
    private int healthBarX;

    private double maxHealth;
    public double getMaxHealth() {
        return maxHealth;
    }
    public void setMaxHealth(double maxHealth) {
        this.maxHealth = maxHealth;
    }

    private int tempX;

    //constructor
    public Duck(int x, int y, ID id, Handler handler) {
        super(x, y, id);
        this.handler = handler;
        image = SMALL_DUCK;
        health = 50;
        maxHealth = 50;
        whistle = new Whistle();

        //initializing velocities
        velX = getRandomInt(-3, 3);
        velY = getRandomInt(-3, 3);
        if (velY == 0 && velX == 0) {
            int r = getRandomInt(0, 1);
            if (r == 0) {
                velX = getRandomInt(-3, 3);
                if(velX == 0) {
                    velX += 2;
                }
            }else {
                velY = getRandomInt(-3, 3);
                if(velY == 0) {
                    velY += 2;
                }
            }
        }
        for (int i = 0; i < 120; i++) {
            linePosX.add(0);
            linePosY.add(0);
            lineVelX.add(0);
            lineVelY.add(0);
        }
    }

    //update object properties
    public void tick() {

        //only do this if they aren't lined up
        if (!isLinedUp) {
            maxHealth -= 0.005;
            borderDetection();
            collisionDetection();
            checkEvolution();

            x += velX;
            y += velY;
        }

        //usefull for the alpha
        prevStatsRecorder();
        if (isAlpha) {
            //will make the ducks follow him
            ducksFollow();
        }

        //only do this if they aren't lined up
        if (!isLinedUp) {
            health -= 0.05;
        }

        if (health <=0 && isAlpha) {
            releaseDucks();
        }
    }

    private static int getRandomInt(double min, double max){
        int x = (int)((Math.random()*((max-min)+1))+min);
        return x;
    }

    private void checkEvolution() {
        if (level == 4) {
            image = ADULT_DUCK;
            setId(ID.adultDuck);
        }else if (level == 12) {
            image = ALPHA_DUCK;
            setId(ID.alphaDuck);

            if (!isAlpha) {
                whistle.whistleStart();
                System.out.println("it's out");
                ++alphaDuckCounter;
                alphaDuckID = alphaDuckCounter;
                System.out.println(alphaDuckCounter);
                System.out.println(alphaDuckID);
                System.out.println(x + " " + y + " " + velX + " " + velY);
                System.out.println(linePosX);
                System.out.println(linePosY);
                System.out.println(lineVelX);
                System.out.println(lineVelY);
                createDuckLine();
                isAlpha = true;
            }
        }
    }

    private void borderDetection() {
        if (x <= DUCK_WIDTH || x >= (Environment.WIDTH - 58)) {
            velX *= -1;
            velY = getRandomInt(-3, 3);
            if (x <= DUCK_WIDTH) {
                x += 5;
            }else {
                x -= 5;
            }
        }
        if (y <= 0 || y >= (Environment.HEIGHT - 80)) {
            velY *= -1;
            velX = getRandomInt(-3, 3);
            if (y <= 0) {
                y += 5;
            }else {
                y -= 5;
            }
        }
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
        g.fillRect((healthBarX - 5), (y - 10), (int)(maxHealth), 5);
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
    private void collisionDetection() {

        for (int i = 0; i < handler.object.size(); i++) {

            EnvironmentObject tempObject = handler.object.get(i);

            if (tempObject.getId() == ID.Rock) {
                //see if there is a collision with the rock
                //if yes, it will go in another direction
                if (getCollider().intersects(tempObject.getCollider())) {
                    velX *= -1;
                    velY *= -1;
                    if (velX > 0) {
                        velX = getRandomInt(1, 3);
                        x += 5;
                    }else {
                        velX = getRandomInt(-1, -3);
                        x -= 5;
                    }

                    if (velY > 0) {
                        velY = getRandomInt(1, 3);
                        y += 5;
                    }else {
                        velY = getRandomInt(-1, -3);
                        y -= 5;
                    }
                }
            }else if (tempObject.getId() == ID.WaterLily) {
                if (getCollider().intersects(tempObject.getCollider())) {
                    handler.removeObject(tempObject);
                    health = maxHealth;
                    level++;
                }
            }
        }
    }

    private void prevStatsRecorder() {
        for (int i = 119; i > -1; i--) {
            if (i > 0) {
                linePosX.set(i, linePosX.get(i - 1));
                linePosY.set(i, linePosY.get(i - 1));
                lineVelX.set(i, lineVelX.get(i - 1));
                lineVelY.set(i, lineVelY.get(i - 1));
            }else {
                linePosX.set(i, x);
                linePosY.set(i, y);
                lineVelX.set(i, velX);
                lineVelY.set(i, velY);
            }
        }
    }

    //sets up the line of ducks
    private void createDuckLine() {
        int fullLine = 0;
        for (int i = 0; i < handler.object.size(); i++) {
            if (fullLine < 6) {
                EnvironmentObject tempObject = handler.object.get(i);
                if (tempObject.getId() == ID.smallDuck || tempObject.getId() == ID.adultDuck) {
                    Duck tempDuck = (Duck)tempObject;
                    System.out.println("create duck temp object");
                    if (!tempDuck.isLinedUp()) {
                        ++fullLine;
                        System.out.println("is lined up");
                        tempDuck.setLinedUp(true);
                        tempDuck.setLineNumber(fullLine);
                        tempDuck.setLineID(alphaDuckID);
                        System.out.println("lineID: " + tempDuck.getLineID() + ", isLinedUp: " + tempDuck.isLinedUp() + ", lineNumber: " + tempDuck.getLineNumber());
                        setFollowDucksPos(tempDuck, fullLine);
                    }
                }
            }else {
                break;
            }
        }
    }

    private void ducksFollow() {
        int fullLine = 0;
        for (int i = 0; i < handler.object.size(); i++) {
            EnvironmentObject tempObject = handler.object.get(i);
            if (tempObject.getId() == ID.smallDuck || tempObject.getId() == ID.adultDuck) {
                Duck tempDuck = (Duck)tempObject;
                if (tempDuck.isLinedUp() && tempDuck.getLineID() == alphaDuckID && fullLine < 6) {
                    ++fullLine;
                    tempDuck.setMaxHealth(tempDuck.getMaxHealth() - 0.0025);
                    tempDuck.setHealth(tempDuck.getMaxHealth());
                    setFollowDucksPos(tempDuck, fullLine);
                }
            }
        }
    }

    private void setFollowDucksPos(Duck tempDuck, int fullLine) {
        tempDuck.setX(linePosX.get(fullLine * 20 - 1));
        tempDuck.setY(linePosY.get(fullLine * 20 - 1));
        tempDuck.setVelX(lineVelX.get(fullLine * 20 - 1));
        tempDuck.setVelY(lineVelY.get(fullLine * 20 - 1));
    }

    //when the alpha dies it will release the ducks
    private void releaseDucks() {
        for (int i = 0; i < handler.object.size(); i++) {
            EnvironmentObject tempObject = handler.object.get(i);
            if (tempObject.id == ID.adultDuck || tempObject.id == ID.smallDuck) {
                Duck tempDuck = (Duck)tempObject;
                if (tempDuck.isLinedUp() && tempDuck.getLineID() == alphaDuckID) {
                    tempDuck.setLinedUp(false);
                    tempDuck.setLineID(-1);
                    tempDuck.setLineNumber(-1);
                }

            }
        }
    }


}
