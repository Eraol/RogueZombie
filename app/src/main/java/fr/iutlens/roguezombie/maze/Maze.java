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

    private int[] door;
    private int last;

    private Set<Integer> done, todo;

    public int getLast() {
        return last;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public int get(int i, int j){
        return door[coordinate.getNdx(i,j)];
    }

    public int getCode(int i, int j){
        int d = door[coordinate.getNdx(i,j)];
        if ((d & 16) ==0) return 0;
        return d & 15;
    }

    public void visit(int ndx){
        if (ndx<0) return;
        last = ndx;
        door[ndx] |= 16;
    }

    public Maze(Coordinate coordinate) {
        this.coordinate = coordinate;

        last = -1;

        door = new int[coordinate.getSize()];

        todo = new HashSet<Integer>();
        done = new HashSet<Integer>();

        todo.add((int) (Math.random()*coordinate.getSize()));

        while(!todo.isEmpty()){

            int n = (int) (Math.random()*todo.size());
            Iterator<Integer> iterator = todo.iterator();
            int ndx =iterator.next();
            for(int i = 0; i < n-1; ++i){
                ndx = iterator.next();
            }

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

            if (cpt == 0) {
                done.add(ndx);
                todo.remove(ndx);

            } else {

                cpt = (int) (Math.random() * cpt) + 1;

                i = 0;
                while (cpt > 0 && d > 0) {
                    if ((d & 1) == 1) --cpt;
                    ++i;
                    d >>>= 1;
                }
                --i;

                door[ndx] |= 1 << i;
                int next = coordinate.getNext(ndx, i);
                door[next] |= 1 << (i ^ 2);
                todo.add(next);

            }
        }
    }

    public boolean canMove(int ndx, int dir) {
        return ((door[ndx]>>>dir)&1) == 1 ;
    }
}
