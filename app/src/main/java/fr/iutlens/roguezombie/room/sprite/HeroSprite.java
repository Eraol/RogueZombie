package fr.iutlens.roguezombie.room.sprite;

import android.util.Log;

import fr.iutlens.roguezombie.room.RoomView;
import fr.iutlens.roguezombie.util.Coordinate;

/**
 * Created by dubois on 23/01/15.
 */
public class HeroSprite extends MonsterSprite {

    private int nextDir;
    public int score;
    public int vie;


    public float x,y;
    public float v,angle;

    public HeroSprite(int x, int y, int id, RoomView room) {
        super(x, y, id, room);
        nextDir = -1;
        score=0;
        vie=5;
        v = 0.1f;
        angle = 0f;

        this.x = x;
        this.y = y;
        ndx = room.getCoordinate().getNdx(Math.round(x), Math.round(y));
    }

    @Override
    public void act() {


        //calcul nouveau x/y

        float xFutur = (float) (x + v * Math.cos(angle));
        float yFutur = (float) (y + v * Math.sin(angle));
        //modif x,y (cos/sin)

        // autorisé ? // chooseDir /


        int xSprite = Math.round(xFutur);
        int ySprite = Math.round(yFutur);

        int ndxFutur = room.getCoordinate().getNdx(xSprite, ySprite);

        Log.d("HeroSprite", "x:" + x + " y:" + y + " ndx:" + ndx + " ndxFutur:" + ndxFutur);

        if (ndxFutur != ndx) {

            if (!room.isFree(xSprite, ySprite)) {
                Sprite sprite = room.getSprite(xSprite, xSprite);

                //Si le sprite est un monstre alors le sprite est mort (Ne pas oublier ALT+Entree pour la méthode)
                if (sprite instanceof MonsterSprite) {
                    ((MonsterSprite) sprite).kill();
                    score++;
                }

            } else {
                x = xFutur;
                y = yFutur;
                ndx = ndxFutur;
            }
        } else {
            x = xFutur;
            y = yFutur;
        }
    }

        // Si changement case (nouveau ndx != ancien)

        // Alors // onMoved




    @Override
    public float getX() {

        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public boolean isDead() {
        return super.isDead();
    }

    @Override
    public void kill() {
        super.kill();
    }

//    @Override
//    protected void onMoved() {
//
//        // Vérifie si un des bord de l'écran est atteint (= une porte)
//        int outDir = -1;
//        if (x == 0) outDir = 2;
//        else if (y == 0) outDir = 3;
//        else if (x == room.getCoordinate().getWidth() - 1) outDir = 0;
//        else if (y == room.getCoordinate().getHeight() - 1) outDir = 1;
//
//        //Si oui, et si une action est prévue dans ce cas...
//        if (outDir != -1 && room.getListener() != null) {
//            // Calcul de la position dans la nouvelle salle (+2 fois la direction, modulo la taille de la salle)
//            x = (x + room.getCoordinate().getWidth() + Coordinate.DIR4[outDir][0] * 2) % room.getCoordinate().getWidth();
//            y = (y + room.getCoordinate().getHeight() + Coordinate.DIR4[outDir][1] * 2) % room.getCoordinate().getHeight();
//            ndx = room.getCoordinate().getNdx(x, y);
//
//            // Lance l'action de sortie de salle.
//            int ndx = room.getMaze().getLast();
//            room.getListener().onRoomOut(room.getMaze().coordinate.getI(ndx),
//                    room.getMaze().coordinate.getJ(ndx), outDir);
//        }
//    }

    /**
     * La nouvelle direction est la dernière demandée.
     *
     * @return
     */
//    @Override
//    protected int chooseDir() {
//        int d = nextDir;
//        nextDir = -1;
//
//        if(d==-1){return d;}
//
//        int xSprite = x + Coordinate.DIR8[d][0];
//        int ySprite = y + Coordinate.DIR8[d][1];
//        if (d != -1 && !room.isFree(xSprite, ySprite)) {
//            Sprite sprite = room.getSprite(xSprite, ySprite); // TODO Le combat
//
//
//
//
//            //Si le sprite est un monstre alors le sprite est mort (Ne pas oublier ALT+Entree pour la méthode)
//            if(sprite instanceof MonsterSprite) {
//                ((MonsterSprite)sprite).kill();
//                score++;
//            }
//
//            d = -1; // SI Il y a quelqchose on ce déplace pas.
//        }
//
//        return d;
//    }

    /**
     * Choix d'une nouvelle direction, qui sera utilisée dès que possible.
     *
     * @param dir
     */
    public void setDir(float dir) {
        this.angle = dir;
    }

}