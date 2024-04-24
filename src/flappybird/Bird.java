
package flappybird;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author User
 */
public class Bird {
    public float x, y, vx, vy;
    public static final int RAD = 25;
    public static final int COLLISION_SIZE = 40;
    private Image img;

    
    public Bird() {
        x = FlappyBird.WIDTH/3;
        y = FlappyBird.WIDTH/3;
        try {
            img = ImageIO.read(new File("pic/sticker,375x360.u2.png"));
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
    public void physics() {
        x+=vx;
        y+=vy ;
        vy+=0.5f ;
    }
    public void update(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawImage(img, Math.round(x-RAD),Math.round(y-RAD),2*RAD,2*RAD, null);
    }
    public void jump() {
        vy = -8;
        
    }
    
    public void reset() {
        x = 800/3;
        y = 800/3;
        vx = vy = 0;
    }
}
