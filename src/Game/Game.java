
package Game;

import GameEngine.AbstractGame;
import GameMenus.PauseMenu;
import GameMenus.PostGameMenu;
import GameMenus.StartMenu;
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
    //
    private Mode mode = Mode.relaxed;
    //
    private PostGameMenu postGameMenu;
    //
    
    public Game(int width, int height, float scale) {
        super(width, height, scale);
    }

    @Override
    public void initiate() {
        
        setResizable(false);
        setDebugInfoDisplayed(false);
        setPausable(true);
        
        resources = new Resources();
        controller = new Controller(this);
        
        gameStarted = false;
        gameSet = false;
        
        postGameMenu = new PostGameMenu(this);
        
        setStartMenu(new StartMenu(this));
        setPauseMenu(new PauseMenu(this));
        setPostGameMenu(postGameMenu);
        
    }

    @Override
    public void update() {
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
        controller.reset(mode);
        while(!gameSet) {
                        
        }
        gameStarted = true;
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

    public void setMode(Mode mode) {
        this.mode = mode;
    }
    
}
