
package Model;

import Game.Game;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.TexturePaint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import java.util.ArrayList;
import java.util.Random;

public class Asteroid {
    
    private Game game;
    
    private int radius;
    private int vertices;
    private float[] xVertices;
    private float[] yVertices;
    
    private float x, y;
    private double dx, dy;
    
    private double rotationAngle = 0;
    private float speed = 50f;
    
    BufferedImage texture;
    
    private Random rand = new Random();
    
    private boolean exploded = false;
    
    public Asteroid(Game game, int radius) {
        
        this.game = game;
        this.radius = radius;
        
        rotationAngle = rand.nextInt(3);
        
        setPosition();
        
        construct();
    }
    
    public Asteroid(Game game, int radius, double rotationAngle, float x, float y) {
        
        this.game = game;
        this.radius = radius;
        
        this.rotationAngle = rotationAngle;
        this.x = x;
        this.y = y;
        
        construct();
    }
    
    public Asteroid(Game game, int radius, float x, float y, int location) {
        
        this.game = game;
        this.radius = radius;
        
        double offset = 0;
        
        switch(location) {
            case 1: //top left
                offset = PI * 1.5;
                break;
                
            case 2: //top right
                offset = PI;
                break;
                
            case 3: // bottom left
                offset = 0;
                break;
                
            case 4: // bottom right
                offset = PI / 2.0;
                break;
        }
        
        
        do {
            rotationAngle =  offset + rand.nextFloat() * (PI / 2.0);
        }while(rotationAngle==0 || rotationAngle==PI/2.0 || rotationAngle==PI || rotationAngle==PI*1.5 || rotationAngle==PI*2.0);
        
        this.x = x;
        this.y = y;
        
        construct();
    }
    
    
    public void setPosition() {
        
        Shape ship = game.getController().getShip().getRotatedBounds();
        
        do {
            x = rand.nextInt(game.getWidth());
            y = rand.nextInt(game.getHeight());  
        }while(intersects(ship));
        
    }
    
    public void construct() {
        
        vertices = rand.nextInt(15) + 5; 
        
        xVertices = new float[vertices];
        yVertices = new float[vertices];
        
        dx = sin(rotationAngle) * speed;
        dy = - cos(rotationAngle) * speed;
        
        for(int i=0; i<vertices; i++) {
            
            float offset = rand.nextInt(radius + (radius / 4)) - ( radius / 4.0f); 
            
            xVertices[i] = ( radius + offset ) * (float) cos( ( (float)i / (float)vertices ) * (2.0 * PI) ); 
            yVertices[i] = ( radius + offset ) * (float) sin( ( (float)i / (float)vertices ) * (2.0 * PI) ); 
            
        }
        
        texture = game.getResources().getAsteroidTexture();
        
    }
    
    public void update() {
        
        rotationAngle += 0.5 * game.getElapsedTime();
        
        x += dx * game.getElapsedTime();
        y += dy * game.getElapsedTime();
        
        if(x < - radius) x = game.getWidth() + radius;
        if(y < - radius) y = game.getHeight() + radius;
        
        if(x > game.getWidth() + radius) x = - radius;
        if(y > game.getHeight() + radius) y = - radius;
        
        if(game.getController().getShip().isAlive()) resolveCollisions();
    }
    
    public void render(Graphics2D g) {
        
        Shape body = getBounds();

        AffineTransform def = g.getTransform();
       
        AffineTransform a = AffineTransform.getRotateInstance(rotationAngle, x, y);        
        g.setTransform(a);
        
        g.setPaint(new TexturePaint(texture, new Rectangle((int)x, (int)y, texture.getWidth(), texture.getHeight())));
        g.fill(body);
        
        g.setColor(Color.black);
        g.draw(body);
        
        g.setTransform(def);
        
    }
    
    public Shape getBounds() {
        
        int[] xTransformed = new int[vertices];
        int[] yTransformed = new int[vertices];
        
        for(int i=0; i<vertices; i++) {
            xTransformed[i] = (int)(xVertices[i] + x);
            yTransformed[i] = (int)(yVertices[i] + y);
        }
        
        return new Polygon(xTransformed, yTransformed, vertices);
    }
    
    public Shape getRotatedBounds() {
        
        int[] xTransformed = new int[vertices];
        int[] yTransformed = new int[vertices];
        
        for(int i=0; i<vertices; i++) {
            xTransformed[i] = (int)transformX(xVertices[i], yVertices[i]);
            yTransformed[i] = (int)transformY(xVertices[i], yVertices[i]);
        }
        
        return new Polygon(xTransformed, yTransformed, vertices);
    }
    
    public void resolveCollisions() {
        
        ArrayList<Bullet> bullets = game.getController().getBullets();
        
        for(int i=0; i<bullets.size(); i++) {
            if(bullets.get(i)!=null){
                if(intersects(bullets.get(i).getBounds()) && !bullets.get(i).isExploded()){
                    bullets.get(i).setExploded(true, this);
                    break;
                }
            }
        }
        
        if(intersects(game.getController().getShip().getRotatedBounds())){
            game.getController().setLives(game.getController().getLives() - 1);
            
            if(game.getController().getLives() > 0) {
                game.getController().retry();
            }
            else {
                game.getController().getShip().setAlive(false);
            }    
        }        
    }
    
    public float transformX(float xOriginal, float yOriginal) {
        return (float)(xOriginal * cos(rotationAngle) - yOriginal * sin(rotationAngle)) + x;
    }
    
    public float transformY(float xOriginal, float yOriginal) {        
        return (float)(xOriginal * sin(rotationAngle) + yOriginal * cos(rotationAngle)) + y;
    }
    
    public void explode() {
        if(!exploded) {
            game.getController().removeAsteroid(this);

            if(radius > 20) {
                game.getController().addAsteroid(new Asteroid(game, radius - 15, (float)(rotationAngle - PI/2.0f), x, y));
                game.getController().addAsteroid(new Asteroid(game, radius - 15, (float)(rotationAngle + PI/2.0f), x, y));
            }  

            game.getController().setScore(game.getController().getScore() + 100);
            
            exploded = true;
        }
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
    
    public double getDx() {
        return dx;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public double getDy() {
        return dy;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    public int getRadius() {
        return radius;
    }
    
    public boolean intersects(Shape shape) {
        Area a = new Area(getRotatedBounds());
        a.intersect(new Area(shape));
        return !a.isEmpty();
    }
    
}
