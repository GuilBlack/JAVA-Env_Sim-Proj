package com.proj.main;

import java.awt.*;

public abstract class EnvironmentObject {

    protected int x, y;
    protected ID id;
    protected int speedX, speedY;

    public EnvironmentObject(int x, int y, ID id) {
        this.x = x;
        this.y = y;
        this.id = id;
    }

    public abstract void tick();
    public abstract void render(Graphics g);

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

    //getter and setter for velocity
    public void setSpeedX(int speedX) {
        this.speedX = speedX;
    }
    public void setSpeedY(int speedY) {
        this.speedY = speedY;
    }
    public int getSpeedX() {
        return speedX;
    }
    public int getSpeedY() {
        return speedY;
    }

    //getter and setter for ID
    public void setId(ID id) {
        this.id = id;
    }
    public ID getId() {
        return id;
    }
}
