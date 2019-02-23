
package GameMenus;

import Game.Game;
import GameEngine.GameState.State;
import GameMenu.AbstractMenu;
import GameMenu.MenuItem;

public class PostGameMenu extends AbstractMenu {
    
    private Game game;
    
    public PostGameMenu(Game game) {
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
        
        addItem(new MenuItem("Main Menu") {
            @Override
            public void function() {
                game.getController().endGame();
                game.setState(State.startMenu);
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
