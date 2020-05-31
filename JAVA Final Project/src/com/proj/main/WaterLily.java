package com.proj.main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class WaterLily extends EnvironmentObject {

    public WaterLily(int x, int y, ID id) {
        super(x, y, id);
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/Images/waterLily.png"));
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void tick() {

    }

    public Rectangle getCollider() {
        return new Rectangle(x, y, 50, 33);
    }

    public void render(Graphics g) throws IOException {
//        Graphics2D g2d = (Graphics2D) g;
//        g.setColor(Color.green);
//        g2d.draw(getCollider());
        g.drawImage(image, x, y, null);
    }
}
