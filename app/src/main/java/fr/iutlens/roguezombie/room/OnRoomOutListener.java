package fr.iutlens.roguezombie.room;

/**
 * Created by dubois on 24/01/15.
 */
public interface OnRoomOutListener {

    /***
     * gestion de la sortie d'une salle de coordonnées (x,y), avec la direction dir.
     * @param x
     * @param y
     * @param dir
     */
    public void onRoomOut(int x, int y, int dir);
}
