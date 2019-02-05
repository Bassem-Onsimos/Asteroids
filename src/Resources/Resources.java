
package Resources;

import GameEngine.Graphics.BufferedImageLoader;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Resources {
    
    BufferedImage bullet = null;
    BufferedImage explosion = null;
    BufferedImage asteroid = null;
    BufferedImage ship = null;
    BufferedImage fire = null;
    
    BufferedImageLoader loader;
    
    public Resources() {
        loader = new BufferedImageLoader();
    }
    
    public BufferedImage getBulletImage() {       
        if(bullet==null) {
            try {
                bullet = loader.loadImage("/img/bullet.png");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }       
        return bullet;        
    }
    
    public BufferedImage getExplosionSprite() {       
        if(explosion==null) {
            try {
                explosion = loader.loadImage("/img/explosion2.png");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }       
        return explosion;        
    }
    
    public BufferedImage getAsteroidTexture() {       
        if(asteroid==null) {
            try {
                asteroid = loader.loadImage("/img/asteroid.png");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }       
        return asteroid;        
    }
    
    public BufferedImage getShipImage() {       
        if(ship==null) {
            try {
                ship = loader.loadImage("/img/ship1.png");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }       
        return ship;        
    }
    
    public BufferedImage getFireSprite() {       
        if(fire==null) {
            try {
                fire = loader.loadImage("/img/fire.png");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }       
        return fire;        
    }
    
}
