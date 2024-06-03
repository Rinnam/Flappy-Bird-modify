
package flappybird;

import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.sound.sampled.*;




/**
 *
 * @author User
 */
public class FlappyBird implements ActionListener, KeyListener {
    
    public static final int FPS = 90, WIDTH = 800, HEIGHT =600;
    
    private Bird bird;
    private JFrame frame, frame1;
    private JPanel panel;
    private ArrayList<Rectangle> rects;
    private int time, scroll, point;
    private Timer t;
    
    private boolean paused, bgmusic;

    private GamePanel dk;


    private Clip jumpSound;
    private Clip backgroundMusic;
    private Clip dieSound;
    private Clip passSound;
    private Clip hitSound;

   
    

    public void go() {
        frame = new JFrame("Flappy Bird");
        bird = new Bird();
        rects = new ArrayList<Rectangle>();
        panel = new GamePanel(this, bird, rects);
        
        
     

        frame.add(panel);
        frame.setSize(WIDTH, HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.addKeyListener(this);
        frame.setLocationRelativeTo(null);
        paused = true;


        
        t = new Timer(1000/FPS, this);
        t.start();

        // am thanh khi nhay
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File("audio/sfx_wing.wav"));
            jumpSound = AudioSystem.getClip();
            jumpSound.open(ais);
        } catch (UnsupportedAudioFileException ex) {
            ex.printStackTrace();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        } catch (LineUnavailableException ex) {
            ex.printStackTrace();
        }
        // am thanh khi roi
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File("audio/sfx_die.wav"));
            dieSound = AudioSystem.getClip();
            dieSound.open(ais);
        } catch (UnsupportedAudioFileException ex) {
            ex.printStackTrace();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        } catch (LineUnavailableException ex) {
            ex.printStackTrace();
        }
        //am thanh khi die
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File("audio/sfx_hit.wav"));
            hitSound = AudioSystem.getClip();
            hitSound.open(ais);
        } catch (UnsupportedAudioFileException ex) {
            ex.printStackTrace();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        } catch (LineUnavailableException ex) {
            ex.printStackTrace();
        }
        // am thanh khi pass
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File("audio/sfx_point.wav"));
            passSound = AudioSystem.getClip();
            passSound.open(ais);
        } catch (UnsupportedAudioFileException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (LineUnavailableException ex) {
            ex.printStackTrace();
        }

        // am thanh bg
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File("audio/background_music.wav"));
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(ais);
            backgroundMusic.start();
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY); // Loop the music continuously
        } catch (UnsupportedAudioFileException ex) {
            ex.printStackTrace();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        } catch (LineUnavailableException ex) {
            ex.printStackTrace();
        }


    }

    

    public static void main(String[] args) {
        new FlappyBird().go();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        panel.repaint();
        if(!paused) {
            backgroundMusic.stop();
            backgroundMusic.setFramePosition(0);
            bird.physics();
            if(scroll % 90 == 0) {
                Rectangle r = new Rectangle(WIDTH, 0, GamePanel.PIPE_W, (int) ((Math.random()*HEIGHT)/5f + (0.2f)*HEIGHT));
                int h2 = (int) ((Math.random()*HEIGHT)/5f + (0.2f)*HEIGHT);
                Rectangle r2 = new Rectangle(WIDTH, HEIGHT - h2, GamePanel.PIPE_W, h2);
                rects.add(r);
                rects.add(r2);
            }
            ArrayList<Rectangle> toRemove = new ArrayList<Rectangle>();
            boolean game = true;

            for(Rectangle r : rects) {
                r.x-=3;
                if(r.x + r.width <= 0) {
                    toRemove.add(r);
                }
                if(r.intersects(bird.x - Bird.COLLISION_SIZE / 2, bird.y - Bird.COLLISION_SIZE / 2,
                            Bird.COLLISION_SIZE, Bird.COLLISION_SIZE)) {
                    
                    if (!hitSound.isRunning()) { // Check if the clip is currently playing
                        hitSound.setFramePosition(0); // Reset the frame position to the beginning
                        hitSound.start(); // Start the clip
                    }
                    
                    JOptionPane.showMessageDialog(frame, "You lose!\n"+"Your time: "+time+" ms."+"\nYour score: "+point+".");
                    
                    game = false;
                    
                    
                }
                
                
            }
            rects.removeAll(toRemove);
            time++;
            scroll++;

            if(time >= 95) {
                if(scroll % 90 == 0) {
                    if (!passSound.isRunning()) { // Check if the clip is currently playing
                        passSound.setFramePosition(0); // Reset the frame position to the beginning
                        passSound.start(); // Start the clip
                    
                }
                point++;
            }
        }



            if(bird.y > HEIGHT || bird.y+bird.RAD < 0) {
                if (!dieSound.isRunning()) { // Check if the clip is currently playing
                    dieSound.setFramePosition(0); // Reset the frame position to the beginning
                    dieSound.start(); // Start the clip
                }
                JOptionPane.showMessageDialog(frame, "You lose!\n"+"Your time: "+time+" ms."+"\nYour score: "+point+".");
                game = false;
                
            }

            if(!game) {
                if (bgmusic) {
                    backgroundMusic.start();
                } else {
                    backgroundMusic.stop();
                }
                // backgroundMusic.start();
                rects.clear();
                bird.reset();
                time = 0;
                scroll = 0;
                point = 0;
                
                paused = true;
            }
        }
        else {
            
        }
    }


    
    
    public int getScore() {
        return time;
    }

    public int getPoint() {
        return point;
    }
    
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_SPACE) {
            paused = false;
        if(!paused){    
            bird.jump();
            if (!jumpSound.isRunning()) { // Check if the clip is currently playing
                jumpSound.setFramePosition(0); // Reset the frame position to the beginning
                jumpSound.start(); // Start the clip
            }
            
        }
    }
        else if(e.getKeyCode()==KeyEvent.VK_ENTER) {
            paused = false;
        }
        
        
    }
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_M) {
        if (paused){
            bgmusic = !bgmusic;
            }
        if (bgmusic) {
            backgroundMusic.start();
        } else {
            backgroundMusic.stop();
        }
        
        }
        else if (e.getKeyCode() == KeyEvent.VK_P) {
            paused = true;
        
        }
        else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if(paused){
            System.exit(0);
            }
        }
        
    }
    @Override
    public void keyTyped(KeyEvent e) {
        
    }
    
    public boolean paused() {
        return paused;
    }



    
}
