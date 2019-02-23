
package GameMenus;

import Game.Game;
import Game.Mode;
import GameEngine.GameState.State;
import GameMenu.AbstractMenu;
import GameMenu.MenuItem;
import GameMenu.SubMenuInitializer;

public class StartMenu extends AbstractMenu {

    private Game game;
    
    public StartMenu(Game game) {
        super(game);
        this.game = game;
    }

    @Override
    public void initiate() {
        
        addItem(new MenuItem("New Game") {
            @Override
            public void function() {
                game.reset();
                game.setState(State.inGame);
            }
        });
        
        addItem(new SubMenuInitializer("Mode") {
            @Override
            public void initiate() {
                addSubMenuItem(new MenuItem("Relaxed") {
                    @Override
                    public void function() {
                        game.setMode(Mode.relaxed);
                    }
                });
                
                addSubMenuItem(new MenuItem("Intense") {
                    @Override
                    public void function() {
                        game.setMode(Mode.timer);
                    }
                });
            }
        });
        
        addItem(new MenuItem("Exit") {
            @Override
            public void function() {
                System.exit(0);
            }
        });
        
    }
    
}
