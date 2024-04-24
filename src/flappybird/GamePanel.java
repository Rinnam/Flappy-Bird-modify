
package flappybird;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 *
 * @author User
 */
public class GamePanel extends JPanel {

    //background
    
    private int backgroundX = 0;
    private int blueBackgroundX = 0;

    private Bird bird;
    private ArrayList<Rectangle> rects;
    private FlappyBird fb;
    private Font scoreFont, pauseFont, font1;
    public static final Color bg = new Color(0, 158, 158);
    public static final int PIPE_W = 50, PIPE_H = 30;
    private Image pipeHead, pipeLength;

    public GamePanel(FlappyBird fb, Bird bird, ArrayList<Rectangle> rects) {
        this.fb = fb;
        this.bird = bird;
        this.rects = rects;
        scoreFont = new Font("Comic Sans MS", Font.BOLD, 18);
        pauseFont = new Font("Arial", Font.BOLD, 18);
        font1 = new Font("Arial", Font.BOLD, 14);
        
        try {
        	pipeHead = ImageIO.read(new File("pic/78px-Pipe.png"));
        	pipeLength = ImageIO.read(new File("pic/pipe_part.png"));
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        
    }


    // Vẻ background
    private void drawBackground(Graphics g, int backgroundX) {
        if (backgroundX <= -FlappyBird.WIDTH) {
            backgroundX = 0;
        }
    
        try {
            Image background = ImageIO.read(new File("pic/bg.png"));
            g.drawImage(background, backgroundX, 0, FlappyBird.WIDTH, FlappyBird.HEIGHT, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (backgroundX > -FlappyBird.WIDTH && backgroundX < 0) {
            try {
                Image background = ImageIO.read(new File("pic/bg.png"));
                g.drawImage(background, backgroundX + FlappyBird.WIDTH, 0, FlappyBird.WIDTH, FlappyBird.HEIGHT, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

        // Thêm hàm menuBackground()
    private void menuBackground(Graphics g, int blueBackgroundX) {
        try {
            Image background = ImageIO.read(new File("pic/menugame.png"));
            g.drawImage(background, 500, 0, 285, 565, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getreadyBackground(Graphics g, int blueBackgroundX) {
        try {
            Image background = ImageIO.read(new File("pic/getready.png"));
            g.drawImage(background, 120, 70, 285, 565, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    

    
    
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBackground(g, backgroundX);
        backgroundX = (backgroundX - 1) % 800; 
        backgroundX = (backgroundX - FlappyBird.WIDTH) % FlappyBird.WIDTH;

        
        

        bird.update(g);
        for(Rectangle r : rects) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.GREEN);
            // g2d.fillRect(r.x, r.y, r.width, r.height);
            AffineTransform old = g2d.getTransform();
            g2d.translate(r.x+PIPE_W/2, r.y+PIPE_H/2);
            if(r.y < FlappyBird.HEIGHT/2) {
                g2d.translate(0, r.height);
                g2d.rotate(Math.PI);
            }
            g2d.drawImage(pipeHead, -PIPE_W/2, -PIPE_H/2, GamePanel.PIPE_W, GamePanel.PIPE_H, null);
            g2d.drawImage(pipeLength, -PIPE_W/2, PIPE_H/2, GamePanel.PIPE_W, r.height, null);
            g2d.setTransform(old);
        }
        
        
        if(fb.paused()) {
            g.setFont(pauseFont);
            g.setColor(new Color(0,0,0,170));
            // g.drawString("PAUSED", FlappyBird.WIDTH/2-100, FlappyBird.HEIGHT/2-100);
            menuBackground(g, blueBackgroundX);
            // getreadyBackground(g, blueBackgroundX);
            g.drawString("PRESS SPACE TO PLAY", FlappyBird.WIDTH/2-243, FlappyBird.HEIGHT/2+30);
            g.setFont(font1);
            g.drawString("M: MUSIC(ON/OFF)", 5, 550);
            g.drawString("ESC: EXIT", 5, 530);
            
            
        }

        else if(!fb.paused()){

            g.setFont(scoreFont);
            g.setColor(Color.BLACK);
            g.drawString("Time: "+fb.getScore(), 10, 20);
            g.drawString("Point: "+fb.getPoint(), 10, 40);
            g.setFont(font1);
            g.drawString("P: PAUSE", 5, 550);

        }

        
    }
}
