package fr.iutlens.roguezombie.room;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

import fr.iutlens.roguezombie.R;
import fr.iutlens.roguezombie.maze.Maze;
import fr.iutlens.roguezombie.room.sprite.DecorSprite;
import fr.iutlens.roguezombie.room.sprite.EnnemiSprite;
import fr.iutlens.roguezombie.room.sprite.FuyardSprite;
import fr.iutlens.roguezombie.room.sprite.HeroSprite;
import fr.iutlens.roguezombie.room.sprite.MonsterSprite;
import fr.iutlens.roguezombie.room.sprite.Sprite;
import fr.iutlens.roguezombie.util.Coordinate;
import fr.iutlens.roguezombie.util.SpriteSheet;

/**
 * Created by dubois on 23/01/15.
 */
public class RoomView extends View {

    Maze maze;
    Coordinate coordinate;

    private Map<Integer,Sprite> map,next;
    public HeroSprite hero;

    private OnRoomOutListener listener;

    // Transformation permettant le centrage de la vue.
    private Matrix transform, reverse;

    // Rectangle réutilisable (pour éviter les instanciations)
    private RectF tmp;

    // Configuration du mode de dessin
    static PaintFlagsDrawFilter setfil= new PaintFlagsDrawFilter(0,
            Paint.FILTER_BITMAP_FLAG | Paint.ANTI_ALIAS_FLAG);
    private Rect src;
    private Rect src2;

    private SpriteSheet sprite;
    private SpriteSheet spriteFond;
    private SpriteSheet spriteDeco;
    private Paint paint;

    private int w,h,x,y,dir;
    private boolean roomChanged;
    private int xtrappe = 2;
    private int ytrappe = 3;

    public RoomView(Context context) {
        super(context);
        init();
    }

    public RoomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RoomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        transform = new Matrix();
        reverse = new Matrix();

        sprite = SpriteSheet.get(this.getContext(), R.drawable.sprite);
        spriteFond = SpriteSheet.get(this.getContext(), R.drawable.sprite_carrelage);
        spriteDeco = SpriteSheet.get(this.getContext(), R.drawable.sprite_deco);
        src = new Rect(0,0, sprite.w, sprite.h);
        src2 = new Rect(0,0, spriteFond.w, spriteFond.h);
        tmp = new RectF();

        paint = new Paint();
        paint.setColor(0xFFAABBFF);

        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        map = new HashMap<Integer,Sprite>();
        next = new HashMap<Integer,Sprite>();

        if (isInEditMode()) { // affiche quelque-chose dans l'éditeur.
            setMaze(new Maze(new Coordinate(6, 6)), new Coordinate(10, 10));
            setRoom(3, 3, 1);
        }
    }

    public void setListener(OnRoomOutListener listener) {
        this.listener = listener;
    }


    public void setMaze(Maze maze, Coordinate coordinate){
        this.maze = maze;
        this.coordinate = coordinate;
        this.hero=null;

        setZoom(w,h);
        invalidate();
    }

    /***
     * Anime la vue.
     * <p/>
     * Tous les éléments avancent d'une étape.
     */
    public void act(){

        //Si on est en train de changer de salle
        if (roomChanged) setRoom();

        // Parcours de tous les sprites
        for (Sprite sprite : map.values()) {


            if(!sprite.isDead()) {
                sprite.act(); // action
                int ndx = sprite.getNdx(); // Prise en compte de la nouvelle position
                next.put(ndx, sprite);
            }
        }

        // map <- next, et on recycle map pour limiter les instanciations inutiles.
        Map tmp = map;
        map = next;
        next = tmp;
        tmp.clear();

        // La vue a changé, on demande un rafraîchissement de l'affichage.
        invalidate();
    }

    /***
     * Demande un changement de salle, dans la direction indiquée.
     * Le changement ne sera effectif que lors du prochain appel à act().
     *
     * @param x
     * @param y
     * @param dir
     */
    public void setRoom(int x, int y, int dir) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        /*xtrappe = (int) (Math.random() * (6 - 1) + 1);
        ytrappe = (int) (Math.random() * (6 - 1) + 1);*/
        this.roomChanged = true;
    }


    /***
     * Réalise le changement de salle demandé précédemment.

     */
    private void setRoom(){
        roomChanged = false;
        map.clear();

        int z=0;
        int k= (int) (Math.random() * (6 - 1) + 1);
        //sauvegarder k => dans nbmonstre
        while (z < k) {
            z++;
            // Ajout d'un "monstre" à des coordonnées aléatoires
            int xm = (int) (Math.random() * (coordinate.getWidth() - 2)) + 1;
            int ym = (int) (Math.random() * (coordinate.getHeight() - 2)) + 1;
            if (Math.random() < 0.3) {
                map.put(coordinate.getNdx(xm, ym), new EnnemiSprite(xm, ym, 6, this));
            } else {
                map.put(coordinate.getNdx(xm, ym), new FuyardSprite(xm, ym, 4, this));

            }

        }

        int z1=0;
        int k1 = (int) (Math.random() * 3);
        while (z1 < k1) {
            z1++;
            int x1m = (int) (Math.random() * (coordinate.getWidth() - 4)) + 2;
            int y1m = (int) (Math.random() * (coordinate.getHeight() - 4)) + 2;
            int ndx = coordinate.getNdx(x1m,y1m);
            map.put(coordinate.getNdx(x1m, y1m), new DecorSprite(x1m, y1m, ndx, (int) (100+Math.random()*16)));


        }

        if (x== xtrappe && y== ytrappe) {
            int ndx = coordinate.getNdx(5,5);
            map.put(ndx,new DecorSprite(5,5,ndx,0)); /* affichage trappe */
        }

        int xm = (int) (Math.random() * (coordinate.getWidth() - 2));
        int ym = (int) (Math.random() * (coordinate.getHeight() - 2));
       // map.put(coordinate.getNdx(xm, ym), new DecorSprite(xm,ym, 4,this));
                // Affichage des murs partout où il n'y a pas de porte.
        int door = maze.get(x,y);
        int p =1;
        for(int i = 0; i <4; ++i){ // Pour chacune des 4 directions
  //          if ((door & p)==0){ // Si il y a un mur
                // Calcul d'un des coin (a,b)
                int a = (coordinate.DIR4[i][0]+coordinate.DIR4[(i+1)&3][0]+1)*(coordinate.getWidth()-1)/2;
                int b = (coordinate.DIR4[i][1]+coordinate.DIR4[(i+1)&3][1]+1)*(coordinate.getHeight()-1)/2;

                // Calcul de la direction (da,db) dans laquelle construire le mur
                int da = coordinate.DIR4[(i+3)&3][0];
                int db = coordinate.DIR4[(i+3)&3][1];


            // ajout d'un compteur de case
            int j = 1;

                // Ajout des sprites aux coordonnées correspondantes,
                int ndx = coordinate.getNdx(a,b);
                while (ndx>=0){ // Jusqu'au bord de la salle
                    if ((door & p)==0 || (j!=6 && j !=5))  // Si il y a un mur
                        map.put(ndx, new DecorSprite(a, b, ndx, 1));
                        a += da;
                        b += db;
                        ndx = coordinate.getNdx(a, b);
                    Log.d("j", "" + j);
                        j = j + 1;

                }
    //        }
            p <<=1;
        }

        // Si il n'existe pas déjà, crée le héro.
        if (hero == null){
            hero = new HeroSprite(5, 5,2,this);
        }

        // Insère le héro dans la liste des sprites, aux coordonnées adéquates.
        map.put(hero.getNdx(), hero);

        //demande un rafraîchissement de la vue
        invalidate();
    }

    public void onDraw(Canvas canvas){
        if (sprite == null || maze == null){ //si les sprites ne sont pas chargé, on ne fait rien.
            return;
        }

        canvas.setDrawFilter(setfil);

        // on sauvegarde la transformation en cours et on applique la transformation désirée
        canvas.save();
        canvas.concat(transform);

        //On peint le fond
        //canvas.drawRect(0,0,coordinate.getHeight(),coordinate.getHeight(),paint);
        tmp.set(0,0,coordinate.getHeight(),coordinate.getHeight());
        canvas.drawBitmap(spriteFond.getBitmap(0), src2,tmp,null);


        //On peint chacun des sprite
        for(Sprite s : map.values()){
            float i = s.getX();
            float j = s.getY();
            tmp.set(i,j,i+1,j+1);
            int spriteId = s.getSpriteId();

            if (spriteId<100) {
                canvas.drawBitmap(sprite.getBitmap(spriteId), src, tmp, null);
            }else {
                canvas.drawBitmap(spriteDeco.getBitmap(spriteId-100), src, tmp, null);
            }
        }


        // On restore la transformation originale.
        canvas.restore();
    }



    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.w = w;
        this.h = h;
        setZoom(w, h);
    }

    /***
     * Calcul du centrage du contenu de la vue
     * @param w
     * @param h
     */
    private void setZoom(int w, int h) {
        if (w<=0 ||h <=0 || maze == null) return;

        // Dimensions dans lesquelles ont souhaite dessiner
        RectF src = new RectF(0,0,coordinate.getWidth(),coordinate.getHeight());

        // Dimensions à notre disposition
        RectF dst = new RectF(0,0,w,h);

        // Calcule de la transfomrmation désirée (et de son inverse)
        transform.setRectToRect(src,dst, Matrix.ScaleToFit.CENTER);
        transform.invert(reverse);

    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public Maze getMaze() {
        return maze;
    }

    public OnRoomOutListener getListener() {
        return listener;
    }

    /***
     * Détermine si une case est libre.
     *
     * Une case est libre si les coordonnées sont valides,
     * et qu'aucun sprite ne les occupe.
     *
     * @param x
     * @param y
     * @return
     */
    public boolean isFree(int x, int y) {
        final int ndx = coordinate.getNdx(x, y);
        if (ndx == -1) return false;

        return map.get(ndx) == null && next.get(ndx) == null;
    }

    /*
    CECI EST LA FONCTION PERMETTANT DE CONNAITRE LE TYPE DE L'OBJET TOUCHE
     */

    public Sprite getSprite(int x, int y) {
        final int ndx = coordinate.getNdx(x, y);
        if (ndx == -1) return null;
        if (map.get(ndx) != null) {
            return map.get(ndx);
        } else {
            return next.get(ndx);
        }
    }

    /**
     * Demande un déplacement dans la direction indiquée
     *
     * @param dir
     * @param stop
     */

    public void move(float dir, boolean stop) {
        if (hero != null)
            hero.setDir(dir , stop);
    }
}
