package fr.iutlens.roguezombie.room.sprite;

import fr.iutlens.roguezombie.room.RoomView;
import fr.iutlens.roguezombie.util.Coordinate;

/**
 * Created by felicia.garciafernandes on 27/01/15.
 */
public class FuyardSprite extends MonsterSprite {


    public FuyardSprite(int x, int y, int id, RoomView room) {
        super(x, y, id, room);
    }

    @Override
    protected int chooseDir() {
        int d = 0; ;

        // - recuperer les coordonnes du héros
        HeroSprite hero = room.hero;
        hero.getX();
        hero.getY();

        float coord = 0;

        float dX, dY;
        dX = hero.getX() - getX();
        dY = hero.getY() - getY();


        float max=-1;



        for(int i=0; i<4; i++){
            // Calcul du produit scalaire
            coord = -ProduitScalaire(dX, dY, Coordinate.DIR[i][0], Coordinate.DIR[i][1]);



            if(coord > max) {
                max=coord;
                d=i;
            }
        }

        if (!room.isFree(x+Coordinate.DIR[d][0],y+Coordinate.DIR[d][1])) d = -1;
        return d;
    }

    /* Methode calcul produit scalaire */
    public float ProduitScalaire(float x, float y, float x2, float y2) {
           float result;
        result = (x*x2)+(y*y2);
        return result;
    }
}


