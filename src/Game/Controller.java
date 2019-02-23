
package Game;

import GameEngine.GameState.State;
import GameEngine.Graphics.BufferedImageLoader;
import GamePanel.GameData;
import GamePanel.IntegerPanelItem;
import Model.Asteroid;
import Model.Bullet;
import Model.Ship;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Controller {
    
    private Mode mode;
    //
    private float timer;
    //
    private Game game;
    //
    private Ship ship;
    //
    private ArrayList<Bullet> bullets;
    private ArrayList<Asteroid> asteroids;
    //
    private BufferedImage space;
    //
    
    private IntegerPanelItem lives, score, highScore;
    
    public Controller(Game game) {
        this.game = game;
        
        lives = new IntegerPanelItem("Lives", 3);
        score = new IntegerPanelItem("Score", 0);
        highScore = new IntegerPanelItem("High Score", 0);
        
        game.addGamePanel(new GameData() {
            @Override
            public void initiate() {
                addItem(lives);
                addItem(score);
                addItem(highScore);
            }
        }, Color.black, Color.white, Color.cyan, 17);
        
        ship = new Ship(game);
        
        try {
            BufferedImageLoader loader = new BufferedImageLoader();
            space = loader.loadImage("/img/space.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    public void reset(Mode mode) {
        
        this.mode = mode;
        
        ship.resetPosition();
        
        bullets = new ArrayList<>();
        asteroids= new ArrayList<>();
        
        respawn();
        game.setGameSet(true);
        
        lives.setValue(3);
        score.setValue(0);
    }
    
    public void update() {
        
        ship.update();
        
        for(int i=0; i<asteroids.size(); i++) {
            if(asteroids.get(i)!=null) asteroids.get(i).update();
        }
        
        for(int i=0; i<bullets.size(); i++) {
            if(bullets.get(i)!=null) bullets.get(i).update();
        }
        
        switch(mode) {
            
            case relaxed: {
                if(asteroids.isEmpty())
                    respawn();
                
                break;
            }
            case timer: {
                timer += game.getElapsedTime();
                
                if(timer >= 6) {
                    timer = 0;
                    respawn();
                }
            
            }   
        }
    }
    
    public void respawn() {
        
        asteroids.add(new Asteroid(game, 50, -50, -50, 1));
        asteroids.add(new Asteroid(game, 50, game.getWidth() + 50, -50, 2));
        asteroids.add(new Asteroid(game, 50, -50, game.getHeight() + 50, 3));
        asteroids.add(new Asteroid(game, 50, game.getWidth() + 50, game.getHeight() + 50, 4));
        
        for(int i=asteroids.size()-1; i>asteroids.size() - 5; i--) {
            if(asteroids.get(i)!=null) { 
                if(asteroids.get(i).intersects(ship.getRotatedBounds()))
                    asteroids.get(i).setPosition();
            }
        }

    }
    
    public void endGame() {
        timer = 0;
        ship.setAccelerating(false);
        game.setGameSet(false);
        game.setGameStarted(false);
        game.getPostGameMenu().setTitle("Score = " + Integer.toString(score.getValue()));
        game.setState(State.postGame);
    }
    
    public void retry() {
        ship.resetPosition();
        timer = 0;
        
        for(int i=0; i<asteroids.size(); i++) {
            if(asteroids.get(i)!=null && asteroids.get(i).intersects(ship.getRotatedBounds())) asteroids.get(i).setPosition();
        }
    }
    
    public void render(Graphics2D g) {
        
        g.drawImage(space, 0, 0, null);
        
        for(int i=0; i<asteroids.size(); i++) {
            if(asteroids.get(i)!=null) asteroids.get(i).render(g);
        }
        
        for(int i=0; i<bullets.size(); i++) {
            if(bullets.get(i)!=null) bullets.get(i).render(g);
        }
        
        ship.render(g);
    }
    
    public void addBullet(Bullet bullet) {
        bullets.add(bullet);
    }
    
    public void removeBullet(Bullet bullet) {
        bullets.remove(bullet);
    }
    
    public void addAsteroid(Asteroid asteroid) {
        asteroids.add(asteroid);
    }
    
    public void removeAsteroid(Asteroid asteroid) {
        asteroids.remove(asteroid);
    }

    public Ship getShip() {
        return ship;
    }

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    public ArrayList<Asteroid> getAsteroids() {
        return asteroids;
    }
    
    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public IntegerPanelItem getLives() {
        return lives;
    }

    public IntegerPanelItem getScore() {
        return score;
    }

    public IntegerPanelItem getHighScore() {
        return highScore;
    }
    
}
