
package Game;

import GameEngine.AbstractGame;
import Resources.Resources;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;

public class Game extends AbstractGame {
    
    private Controller controller;
    //
    private Resources resources;
    //
    private boolean gameStarted;
    private boolean gameSet;
    
    public Game(int width, int height, float scale) {
        super(width, height, scale);
    }

    @Override
    public void initiate() {
        
        setResizable(false);
        setDebugInfoDisplayed(false);
        
        resources = new Resources();
        controller = new Controller(this);
        
        gameStarted = false;
        gameSet = false;
        
        controller.reset();
    }

    @Override
    public void update() {
        
        if(!gameStarted) {          
            if(getInput().isKeyUp(KeyEvent.VK_SPACE)) 
                gameStarted = true;        
        }
        else 
            controller.update();
    }

    @Override
    public void render(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setStroke(new BasicStroke(1));
        controller.render(g);
    }
    
    @Override
    public void reset() {
        controller.reset();
        while(!gameSet) {
                        
        }
    }

    public Controller getController() {
        return controller;
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }

    public void setGameSet(boolean gameSet) {
        this.gameSet = gameSet;
    }

    public Resources getResources() {
        return resources;
    }
    
    
    
    
}
