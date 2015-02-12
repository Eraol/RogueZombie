package fr.iutlens.roguezombie.room.sprite;

/**
 * Created by dubois on 23/01/15.
 */
public interface Sprite {

    public void act();
    public int getSpriteId();
    public float getX();
    public float getY();
    public int getNdx();

    boolean isDead();
}
