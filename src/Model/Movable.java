
package Model;

import java.awt.Graphics2D;
import java.awt.Shape;

public abstract class Movable {
    
    protected float x, y;
    protected float dX, dY;
    protected float aX, aY;
    
    public abstract Shape getBounds();
    
    public void update() {
        
        x += dX;
        y += dY;
        
    }
    
    public abstract void render(Graphics2D g);
    
}
