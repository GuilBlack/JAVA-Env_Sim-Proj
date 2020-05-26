package com.proj.main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public abstract class EnvironmentObject {

    protected int x, y;
    protected int velX, velY;
    protected double health;
    protected BufferedImage image;
    protected ID id;

    public EnvironmentObject(int x, int y, ID id) {
        this.x = x;
        this.y = y;
        this.id = id;
    }

    public abstract void tick();
    public abstract void render(Graphics g) throws IOException;
    public abstract Rectangle getCollider();

    //getter and setter for coordinates
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    //getter and setter for health
    public void setHealth(double health) {
        this.health = health;
    }
    public double getHealth() {
        return health;
    }

    //getter and setter for image
    public void setImage(BufferedImage image) {
        this.image = image;
    }
    public BufferedImage getImage() {
        return image;
    }

    //getter and setter for ID
    public void setId(ID id) {
        this.id = id;
    }
    public ID getId() {
        return id;
    }
}
