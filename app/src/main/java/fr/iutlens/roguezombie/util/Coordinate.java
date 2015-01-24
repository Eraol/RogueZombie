package fr.iutlens.roguezombie.util;

/**
 * Created by dubois on 22/01/15.
 */
public class Coordinate {

    public static int[][] DIR = {{1,0},{0,1},{-1,0},{0,-1}};

    int width,height;

    public Coordinate(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public int getSize(){
        return height*width;
    }

    public int getNdx(int i, int j){
        if (i<0 || j<0 || i>=width || j >= height) return -1;
        return i+j*width;
    }

    public int getI(int ndx){
        if (ndx <0) return -1;
        return ndx%width;
    }

    public int getJ(int ndx){
        if (ndx <0) return -1;
        return ndx/width;
    }

    public int getNext(int ndx, int dir){
        return getNdx(
                getI(ndx)+DIR[dir][0],
                getJ(ndx)+DIR[dir][1]);
    }
}
