
package Game;

import GameEngine.GameState.State;
import GameEngine.Graphics.BufferedImageLoader;
import GamePanel.GameData;
import GamePanel.PanelItem;
import Model.Asteroid;
import Model.Bullet;
import Model.Ship;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Controller {
    
    private Mode mode = Mode.timer;
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
    private int lives;
    private int score;
    private int highScore;
    
    private PanelItem livesLabel, scoreLabel, highScoreLabel;
    
    public Controller(Game game) {
        this.game = game;
        
        livesLabel = new PanelItem("Lives", "3");
        scoreLabel = new PanelItem("Score", "0");
        highScoreLabel = new PanelItem("High Score", "0");
        
        game.addGamePanel(new GameData() {
            @Override
            public void initiate() {
                addItem(livesLabel);
                addItem(scoreLabel);
                addItem(highScoreLabel);
            }
        });
        
        ship = new Ship(game);
        
        try {
            BufferedImageLoader loader = new BufferedImageLoader();
            space = loader.loadImage("/img/space.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    public void reset() {
        
        ship.resetPosition();
        
        bullets = new ArrayList<>();
        asteroids= new ArrayList<>();
        /*
        for(int i=0; i<4; i++)
            asteroids.add(new Asteroid(game, 50));
       
        for(int i=0; i<asteroids.size(); i++) {
            if(asteroids.get(i)!=null) asteroids.get(i).setPosition();
        }
        */
        respawn();
        game.setGameSet(true);
        
        setLives(3);
        setScore(0);
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
    
    public void setScore(int score) {
        this.score = score;
        scoreLabel.setData(Integer.toString(score));
        
        if(score > highScore) {
            highScore = score;
            highScoreLabel.setData(Integer.toString(score));
        }
    }
    
    public void setLives(int lives) {
        this.lives = lives;
        livesLabel.setData(Integer.toString(lives));
    }

    public int getLives() {
        return lives;
    }

    public int getScore() {
        return score;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }
    
}
