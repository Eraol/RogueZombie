package fr.iutlens.roguezombie.room.sprite;

/**
 * Created by dubois on 23/01/15.
 */
public class DecorSprite implements Sprite {

    private int x,y,id, ndx;

    public DecorSprite(int x, int y, int ndx, int id) {
        this.x = x;
        this.y = y;
        this.ndx = ndx;
        this.id = id;
    }

    @Override
    public void act() {
        // Le décor ne bouge pas.
    }

    @Override
    public int getSpriteId() {
        return id;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public int getNdx() {
        return ndx;
    }

    @Override
    public boolean isDead() {
        return false;
    }
}
