
package Model;

import Game.Game;
import GameEngine.Graphics.SpriteSheet;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Ship {
    
    private Game game;
    
    private float x, y;
    private float dx = 0, dy = 0;
    private float length = 35;
    private float width = 38;
    private float rotationAngle = 0;
    private float acceleration = 500f;
    private float steeringPower = 6f;
    
    private boolean accelerating = false;
    private boolean alive;
    
    private float lastShot = 0;
    private float shootingDelay = 0.08f;
    
    private BufferedImage ship;
    private BufferedImage fire;
    
    private SpriteSheet sprite;
    private int animation = 1;
    
    public Ship(Game game) {
        this.game = game;
        
        alive = true;

        ship = game.getResources().getShipImage();
        sprite = new SpriteSheet(game.getResources().getFireSprite());
        fire = sprite.cropImage(animation, 1, 25, 41);
    }
    
    public void resetPosition() {
        
        accelerating = false;
        alive = true;
        
        x = game.getWidth()/2.0f;
        y = game.getHeight()/2.0f;
        
        rotationAngle = 0;
        
        dx = 0;
        dy = 0;
        
    }
    
    public void update() {
        
        if(alive) {
            
            if(lastShot > 0) lastShot -= game.getElapsedTime();

            if(game.getInput().isKey(KeyEvent.VK_RIGHT)) {
                rotationAngle += steeringPower * game.getElapsedTime();
            }
            if(game.getInput().isKey(KeyEvent.VK_LEFT)) {
                rotationAngle -= steeringPower * game.getElapsedTime();
            }
            
            if(game.getInput().isKey(KeyEvent.VK_UP)) {
                accelerating = true;
                dx += sin(rotationAngle) * acceleration * game.getElapsedTime();
                dy -= cos(rotationAngle) * acceleration * game.getElapsedTime();

                animation++;
                if(animation > 8) animation = 1;

                fire = sprite.cropImage(animation, 1, 25, 41);
            }
            else {
                accelerating = false;
            }    
        }
        
        dx -= game.getElapsedTime() * 0.8 * dx;
        dy -= game.getElapsedTime() * 0.8 * dy;

        x += dx * game.getElapsedTime();
        y += dy * game.getElapsedTime();
        
        if(x < - width) x = game.getWidth() + width;
        if(y < - length) y = game.getHeight() + length;
        
        if(x > game.getWidth() + width) x = - width;
        if(y > game.getHeight() + length) y = - length;
        
        if(alive && game.getInput().isKey(KeyEvent.VK_SPACE) && lastShot <= 0) {
            game.getController().addBullet(new Bullet(game, transformX(0, -20), transformY(0, -20), dx, dy, rotationAngle));
            lastShot = shootingDelay;
        }
        
        if(!alive && game.getController().getBullets().isEmpty()) {
            game.getController().endGame();
        }       
    }
    
    public void render(Graphics2D g) {
        
        /*
        int fireRange = 7;
        int size = 25;
        
        if(accelerating) {
            g.setColor(Color.red);
            
            float[] xPoints = { -fireRange,   fireRange,                  0 };
            float[] yPoints = {       size,        size,   size + 2 * fireRange };
            
            int[] xTransformed = new int[3];
            int[] yTransformed = new int[3];
            

            for(int i=0; i<3; i++) {
                xTransformed[i] = (int)transformX(xPoints[i], yPoints[i]);
                yTransformed[i] = (int)transformY(xPoints[i], yPoints[i]);
            }

            g.drawPolygon(xTransformed, yTransformed, 3);
        }
        */
        
        AffineTransform def = g.getTransform();
        
        AffineTransform a = AffineTransform.getRotateInstance(rotationAngle, x, y);
        
        g.setTransform(a);
        
        if(accelerating) {
            
            g.drawImage(fire, (int)(x - 12.5), (int)(y + length / 2.0), null);
            
        }
        
        g.drawImage(ship, (int)(x - width), (int)(y - length), null);
        
        g.setTransform(def);
      
    }
    
    public Shape getBounds() {
        
        float[] xPoints = {        0,    width,    - width };
        float[] yPoints = { - length,   length,     length };
        
        int[] xTransformed = new int[3];
        int[] yTransformed = new int[3];
        
        for(int i=0; i<3; i++) {
            xTransformed[i] = (int)(xPoints[i] + x);
            yTransformed[i] = (int)(yPoints[i] + y);
        }
        
        return new Polygon(xTransformed, yTransformed, 3);
    }
    
    public Shape getRotatedBounds() {
        
        float[] xPoints = {        0,    width,    - width };
        float[] yPoints = { - length,   length,     length };
        
        int[] xTransformed = new int[3];
        int[] yTransformed = new int[3];
        
        for(int i=0; i<3; i++) {
            xTransformed[i] = (int)transformX(xPoints[i], yPoints[i]);
            yTransformed[i] = (int)transformY(xPoints[i], yPoints[i]);
        }
        
        return new Polygon(xTransformed, yTransformed, 3);
    }
    
    public float transformX(float xOriginal, float yOriginal) {
        return (float)(xOriginal * cos(rotationAngle) - yOriginal * sin(rotationAngle)) + x;
    }
    
    public float transformY(float xOriginal, float yOriginal) {    
        return (float)(xOriginal * sin(rotationAngle) + yOriginal * cos(rotationAngle)) + y;
    }

    public void setAccelerating(boolean accelerating) {
        this.accelerating = accelerating;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }
    
}
