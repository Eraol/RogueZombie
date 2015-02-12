package fr.iutlens.roguezombie.room.sprite;

import fr.iutlens.roguezombie.room.RoomView;
import fr.iutlens.roguezombie.util.Coordinate;

/**
 * Created by dubois on 23/01/15.
 */
public class HeroSprite extends MonsterSprite {

    private int nextDir;
    public int score=0;

    public HeroSprite(int x, int y, int id, RoomView room) {
        super(x, y, id, room);
        nextDir = -1;
    }


    @Override
    protected void onMoved() {

        // Vérifie si un des bord de l'écran est atteint (= une porte)
        int outDir = -1;
        if (x == 0) outDir = 2;
        else if (y == 0) outDir = 3;
        else if (x == room.getCoordinate().getWidth() - 1) outDir = 0;
        else if (y == room.getCoordinate().getHeight() - 1) outDir = 1;

        //Si oui, et si une action est prévue dans ce cas...
        if (outDir != -1 && room.getListener() != null) {
            // Calcul de la position dans la nouvelle salle (+2 fois la direction, modulo la taille de la salle)
            x = (x + room.getCoordinate().getWidth() + Coordinate.DIR[dir][0] * 2) % room.getCoordinate().getWidth();
            y = (y + room.getCoordinate().getHeight() + Coordinate.DIR[dir][1] * 2) % room.getCoordinate().getHeight();
            ndx = room.getCoordinate().getNdx(x, y);

            // Lance l'action de sortie de salle.
            int ndx = room.getMaze().getLast();
            room.getListener().onRoomOut(room.getMaze().coordinate.getI(ndx),
                    room.getMaze().coordinate.getJ(ndx), dir);
        }
    }

    /**
     * La nouvelle direction est la dernière demandée.
     *
     * @return
     */
    @Override
    protected int chooseDir() {
        int d = nextDir;
        nextDir = -1;

        if(d==-1){return d;}

        int xSprite = x + Coordinate.DIR[d][0];
        int ySprite = y + Coordinate.DIR[d][1];
        if (d != -1 && !room.isFree(xSprite, ySprite)) {
            Sprite sprite = room.getSprite(xSprite, ySprite); // TODO Le combat

            //Si le sprite est un monstre alors le sprite est mort (Ne pas oublier ALT+Entree pour la méthode)
            if(sprite instanceof MonsterSprite) {
                ((MonsterSprite)sprite).kill();
                score++;
            }
            d = -1; // SI Il y a quelqchose on ce déplace pas.
        }

        return d;
    }

    /**
     * Choix d'une nouvelle direction, qui sera utilisée dès que possible.
     *
     * @param dir
     */
    public void setDir(int dir) {
        this.nextDir = dir;
    }

}