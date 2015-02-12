package fr.iutlens.roguezombie.room.sprite;

import fr.iutlens.roguezombie.room.RoomView;
import fr.iutlens.roguezombie.util.Coordinate;

/**
 * Created by dubois on 23/01/15.
 */
public class MonsterSprite implements Sprite {
    private boolean dead;

    public MonsterSprite(int x, int y, int id, RoomView room) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.room = room;
        this.dir = -1;
        dead=false;
        ndx = room.getCoordinate().getNdx(x, y);
    }

    protected int x,y,ndx; // position
    public int id; // numéro d'image
    protected int dir; // direction
    protected float progress; // 0 : position précédente / 1 : prochaine position

    RoomView room;

    @Override
    public void act() {
        progress += 0.1f;
        if (progress>1){ // On vient d'atteindre une nouvelle case.
            progress = 0;
            if (dir != -1){ // Si on bougeait
                x+= Coordinate.DIR[dir][0];
                y+= Coordinate.DIR[dir][1];
                
                onMoved(); // Appel d'un écouteur, conçu pour les descendants de cette classe.
            }
            // choix de la nouvelle direction
            dir = chooseDir();

            // Mise à jour du numéro de case (en cas de mouvement)
            if (dir != -1){
                ndx = room.getCoordinate().getNdx(x + Coordinate.DIR[dir][0], y + Coordinate.DIR[dir][1]);
            }
        }
    }

    protected void onMoved() {
        // A redéfinir par les descendants.
    }

    /***
     * Choix d'une direction (ici, au hasard).
     * @return
     */
    protected int chooseDir() {
        int d = (int) (Math.random()*4);
        if (!room.isFree(x+Coordinate.DIR[d][0],y+Coordinate.DIR[d][1])) d = -1;

        return d;
    }

    @Override
    public int getSpriteId() {
        if (dir==2) {
            return id+1;
        }
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

    @Override
    public boolean isDead() {
        return dead;
    }

    public void kill() {
        dead=true;
    }
}
