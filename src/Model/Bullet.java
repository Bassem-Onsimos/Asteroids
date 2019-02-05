
package Model;

import Game.Game;
import GameEngine.Graphics.BufferedImageLoader;
import GameEngine.Graphics.SpriteSheet;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Bullet {
    
    Game game;
    
    private double x, y;
    private double radius = 5.0;
     
    private double dx = 0, dy = 0;
    private float speed = 700f;
    
    private float bulletTime = 0;
    private float maxTime =  0.48f;
    
    private BufferedImage bullet;
    
    private BufferedImage explosion;
    private SpriteSheet explosionSprite;
    private float column = 1;
    private float row = 1;
   
    private boolean exploded = false;
    private Asteroid collidedAsteroid = null;
    private float scale;
    
    public Bullet(Game game, float x, float y, float shipDx, float shipDy, float angle) {
        this.game = game;
        this.x = x;
        this.y = y;
        
        dx = sin(angle) * speed + shipDx;
        dy = - cos(angle) * speed + shipDy;
          
        bullet = game.getResources().getBulletImage();
        explosionSprite = new SpriteSheet(game.getResources().getExplosionSprite());
        explosion = explosionSprite.cropImage((int)column, (int)row, 100, 100);   
        
    }
    
    public void update() {       
        
        if(exploded) {
            
            column += 100 * game.getElapsedTime();
            
            if(column >= 10) {
                column = 1;
                row++;
            }
            
            if(row == 2 && collidedAsteroid!=null) {
                collidedAsteroid.explode();
                collidedAsteroid = null;
            }
            else if(row >= 10) {
                game.getController().removeBullet(this);
            }
            else {
                explosion = explosionSprite.cropImage((int)column, (int)row, 100, 100);
            }
        }
        else {
            bulletTime += game.getElapsedTime();
            
            x += dx * game.getElapsedTime();
            y += dy * game.getElapsedTime();
            
            if(bulletTime >= maxTime)// || x<0 || x>game.getWidth() || y<0 || y> game.getHeight())
                game.getController().removeBullet(this);
        }
        
    }
    
    public void render(Graphics2D g) {
        //g.setColor(Color.cyan);
        //g.fill(getBounds());
        
        if(exploded) {
            g.drawImage(explosion, (int)(x - scale), (int)(y - scale), (int)(scale * 2.0), (int)(scale * 2.0), null);
        }
        else {
            
            if(bulletTime >= 0.45f) {
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
            }
            else if(bulletTime > 0.42f) {
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
            }
            else if(bulletTime > 0.39f) {
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            }
            else {
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            }
                
            g.drawImage(bullet, (int)(x - radius), (int)(y - radius), (int)(radius * 2.0), (int)(radius * 2.0), null);
            
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            
        }
    }
    
    public Shape getBounds() {
        return new Ellipse2D.Double(x - radius, y - radius, radius * 2.0, radius * 2.0);
    }

    public boolean isExploded() {
        return exploded;
    }

    public void setExploded(boolean exploded, Asteroid asteroid) {
        this.exploded = exploded;
        this.collidedAsteroid = asteroid;
        
        x = asteroid.getX();
        y = asteroid.getY();
        
        scale = asteroid.getRadius();
        
    }
    
    
    
}
