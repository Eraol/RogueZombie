package fr.iutlens.roguezombie.room.sprite;

import fr.iutlens.roguezombie.room.RoomView;
import fr.iutlens.roguezombie.util.Coordinate;

/**
 * Created by dubois on 23/01/15.
 */
public class HeroSprite extends MonsterSprite {

    private int nextDir;

    public HeroSprite(int x, int y, int id, RoomView room) {
        super(x, y, id, room);
        nextDir = -1;
    }


    @Override
    protected void onMoved() {
        int outDir =-1;
        if (x == 0) outDir =2;
        else if (y ==0) outDir = 3;
        else if (x == room.getCoordinate().getWidth()-1) outDir = 0;
        else if (y == room.getCoordinate().getHeight()-1) outDir = 1;

        if (outDir != -1 && room.getListener() != null){
            x = (x+ room.getCoordinate().getWidth()+ Coordinate.DIR[dir][0]*2) % room.getCoordinate().getWidth();
            y = (y+ room.getCoordinate().getHeight()+Coordinate.DIR[dir][1]*2) % room.getCoordinate().getHeight();
            ndx = room.getCoordinate().getNdx(x, y);


            int ndx = room.getMaze().getLast();
            room.getListener().onRoomOut(room.getMaze().coordinate.getI(ndx),
                    room.getMaze().coordinate.getJ(ndx), dir);


        }
    }

    @Override
    protected int chooseDir() {
        int d = nextDir;
        nextDir = -1;
        if (d != -1 && !room.isFree(x+Coordinate.DIR[d][0],y+Coordinate.DIR[d][1])) d = -1;

        return d;
    }

    public void setDir(int dir){
        this.nextDir = dir;
    }
}
