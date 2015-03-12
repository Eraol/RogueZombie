package fr.iutlens.roguezombie.room.sprite;

import fr.iutlens.roguezombie.room.RoomView;
import fr.iutlens.roguezombie.util.Coordinate;

/**
 * Created by simon.florysiak on 17/02/15.
 */

public class EnnemiSprite extends MonsterSprite {

    public EnnemiSprite(int x, int y, int id, RoomView room) {
        super(x, y, id, room);
    }

    @Override
    protected int chooseDir() {
        int d = 0;

        // - recuperer les coordonnes du héros
        HeroSprite hero = room.hero;
        hero.getX();
        hero.getY();


        float ScoreCoord = 0;

        float dX, dY;
        dX = hero.getX() - getX();
        dY = hero.getY() - getY();

        float max=-1;

        for(int i=0; i<8; i++){
            // Calcul du produit scalaire
            ScoreCoord = ProduitScalaire(dX, dY, Coordinate.DIR8[i][0], Coordinate.DIR8[i][1]);

            // teste si loin
            if(room.isFree(x+Coordinate.DIR8[i][0],y+Coordinate.DIR8[i][1]))  {
                if(ScoreCoord > max) { // si possible et meilleur
                    max=ScoreCoord;
                    d=i;
                }
            }
        }


        if (!room.isFree(x+Coordinate.DIR8[d][0],y+Coordinate.DIR8[d][1])) d = -1;
        return d;
    }

    /* Methode calcul produit scalaire */
    public float ProduitScalaire(float x, float y, float x2, float y2) {
        float result;
        result = (x*x2)+(y*y2);
        return result;
    }

}
