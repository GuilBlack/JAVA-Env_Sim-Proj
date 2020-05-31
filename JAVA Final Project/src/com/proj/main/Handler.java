package com.proj.main;

import java.awt.*;
import java.io.IOException;
import java.util.LinkedList;

public class Handler {

    //a list of all the objects in the environment
    LinkedList<EnvironmentObject> object = new LinkedList<EnvironmentObject>();

    //update all of the objects in each frames
    public void tick() {
        //will update each of our objects in each frames
        for(int i = 0; i < object.size(); i++) {
            EnvironmentObject tempObject = object.get(i);
            tempObject.tick();
            if (tempObject.id == ID.smallDuck || tempObject.id == ID.adultDuck || tempObject.id == ID.alphaDuck) {
                if(tempObject.getHealth() <= 0) {
                    System.out.println(tempObject.getHealth());
                    removeObject(tempObject);
                }
            }
        }

        //chances to add a duck in the environment
        if (getRandomDouble() <= 0.005) {
            addObject(new Duck(getRandomInt(392, 990), getRandomInt(40, 380), ID.smallDuck, this));
        }

        //chances to add a water lily in the environment
        if (getRandomDouble() <= 0.025) {
            addObject(new WaterLily(getRandomInt(44, 1200), getRandomInt(0, 600), ID.WaterLily));
        }
    }

    //will render each objects
    public void render(Graphics g) {
        for(int i = 0; i < object.size(); i++) {
            //render each of our objects
            EnvironmentObject tempObject = object.get(i);
            try {
                tempObject.render(g);
            }catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    //get a random double number
    private static double getRandomDouble() {
        double r = Math.random();
        return r;
    }

    //get a random int number
    private static int getRandomInt(double min, double max){
        int x = (int)((Math.random()*((max-min)+1))+min);
        return x;
    }

    //add an object to the environment
    public void addObject(EnvironmentObject object) {
        this.object.add(object);
    }

    //remove an object of our environment
    public void removeObject(EnvironmentObject object) {
        this.object.remove(object);
    }

}
