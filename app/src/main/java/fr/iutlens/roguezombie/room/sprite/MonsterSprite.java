package fr.iutlens.roguezombie.room.sprite;

import fr.iutlens.roguezombie.room.RoomView;
import fr.iutlens.roguezombie.util.Coordinate;

/**
 * Created by dubois on 23/01/15.
 */
public class MonsterSprite implements Sprite {
    public MonsterSprite(int x, int y, int id, RoomView room) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.room = room;
        this.dir = -1;
        ndx = room.getCoordinate().getNdx(x, y);
    }

    protected int x,y,ndx;
    private int id;
    protected int dir;
    protected float progress;

    RoomView room;

    @Override
    public void act() {
        progress += 0.1f;
        if (progress>1){
            progress = 0;
            if (dir != -1){
                x+= Coordinate.DIR[dir][0];
                y+=Coordinate.DIR[dir][1];
                
                onMoved();
            }
            dir = chooseDir();

            if (dir != -1){
                ndx = room.getCoordinate().getNdx(x + Coordinate.DIR[dir][0], y + Coordinate.DIR[dir][1]);
            }
        }
    }

    protected void onMoved() {
    }

    protected int chooseDir() {
        int d = (int) (Math.random()*4);
        if (!room.isFree(x+Coordinate.DIR[d][0],y+Coordinate.DIR[d][1])) d = -1;

        return d;
    }

    @Override
    public int getSpriteId() {
        return id;
    }

    @Override
    public float getX() {
        if (dir == -1) return x;
        return x+progress*Coordinate.DIR[dir][0];
    }

    @Override
    public float getY() {
        if (dir == -1) return y;
        return y+progress*Coordinate.DIR[dir][1];
    }

    @Override
    public int getNdx() {
        return ndx;
    }
}
