package com.proj.main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class Rock extends EnvironmentObject {

    public Rock(int x, int y, ID id) {
        super(x, y, id);
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/Images/rock.png"));
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void tick() {

    }

    public Rectangle getCollider() {
        return new Rectangle(x, y, 56, 45);
    }

    public void render(Graphics g) {
//        Graphics2D g2d = (Graphics2D) g;
//        g.setColor(Color.RED);
//        g2d.draw(getCollider());
        g.drawImage(image, x, y, 56, 45, null);
    }
}
