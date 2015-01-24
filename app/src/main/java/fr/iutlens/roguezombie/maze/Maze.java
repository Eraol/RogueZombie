package fr.iutlens.roguezombie.maze;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import fr.iutlens.roguezombie.util.Coordinate;

/**
 * Created by dubois on 22/01/15.
 */
public class Maze {

    public final Coordinate coordinate;

    private int[] door; // combinaison de bits = 0..3 porte dans la direction ; 4 déjà visité
    private int last; // dernière case visitée

    public int getLast() {
        return last;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public int get(int i, int j){
        return door[coordinate.getNdx(i,j)];
    }

    /***
     * Retourne le code de l'image à afficher pour la case.
     *
     * @param i
     * @param j
     * @return
     */
    public int getCode(int i, int j){
        int d = door[coordinate.getNdx(i,j)];
        if ((d & 16) ==0) return 0;
        return d & 15;
    }

    /***
     * Indique qu'une case vient d'être visitée.
     *
     * @param ndx
     */
    public void visit(int ndx){
        if (ndx<0) return;
        last = ndx;
        door[ndx] |= 16;
    }

    /***
     * Construit le labyrinthe.
     *
     * @param coordinate Définit la taille du labyrinthe.
     */
    public Maze(Coordinate coordinate) {
        this.coordinate = coordinate;

        last = -1;

        // instanciations

        door = new int[coordinate.getSize()];

        Set<Integer> done, todo;

        todo = new HashSet<Integer>();
        done = new HashSet<Integer>();

        // choix de la case de départ
        todo.add((int) (Math.random()*coordinate.getSize()));

        // Le labyrinthe est créé en creusant des portes entre les salles,
        // jusqu'à ce que toutes les salles soient accessibles
        while(!todo.isEmpty()){

            // Choix d'un élément au hasard dans les cases voisines des cases déjà visitées
            int n = (int) (Math.random()*todo.size());
            Iterator<Integer> iterator = todo.iterator();
            int ndx =iterator.next();
            for(int i = 0; i < n-1; ++i){
                ndx = iterator.next();
            }
            // la case candidate à l'ajout a comme numéro ndx

            //On compte les voisins non visités (ceux sans portes)
            int d = 0;
            int i = 1;
            int cpt = 0;
            for(int dir = 0; dir<4; ++dir){
                int next = coordinate.getNext(ndx,dir);
                if (next != -1 && door[next] == 0){
                    d |= i;
                    ++cpt;
                }
                i <<= 1;
            }

            // Si aucun voisin sans porte
            if (cpt == 0) {
                done.add(ndx); // on a fini avec ndx
                todo.remove(ndx);

            } else {


                //Choix aléatoire d'une direction amenant à une case non visitée
                cpt = (int) (Math.random() * cpt) + 1;

                i = 0;
                while (cpt > 0 && d > 0) {
                    if ((d & 1) == 1) --cpt;
                    ++i;
                    d >>>= 1;
                }
                --i;
                // la direction retenue est dans i

                // On creuse la porte...
                door[ndx] |= 1 << i;
                // ... des deux cotés !
                int next = coordinate.getNext(ndx, i);
                door[next] |= 1 << (i ^ 2);

                // La case que l'on vient de visiter pourra à son tour servir de départ pour ajouter
                // une porte.
                todo.add(next);
            }
        }
    }

    /***
     * Indique la présence d'une porte dans la direction indiquée
     * @param ndx numéro de la salle
     * @param dir direction
     * @return
     */
    public boolean canMove(int ndx, int dir) {
        return ((door[ndx]>>>dir)&1) == 1 ;
    }
}
