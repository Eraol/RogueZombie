package fr.iutlens.roguezombie.room;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

import fr.iutlens.roguezombie.R;
import fr.iutlens.roguezombie.maze.Maze;
import fr.iutlens.roguezombie.room.sprite.DecorSprite;
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
    private HeroSprite hero;

    private OnRoomOutListener listener;

    // Transformation permettant le centrage de la vue.
    private Matrix transform, reverse;

    // Rectangle réutilisable (pour éviter les instanciations)
    private RectF tmp;

    // Configuration du mode de dessin
    static PaintFlagsDrawFilter setfil= new PaintFlagsDrawFilter(0,
            Paint.FILTER_BITMAP_FLAG | Paint.ANTI_ALIAS_FLAG);
    private Rect src;

    private SpriteSheet sprite;
    private Paint paint;
    private int w;
    private int h;
    private int x;
    private int y;
    private int dir;
    private boolean roomChanged;

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
        src = new Rect(0,0, sprite.w, sprite.h);
        tmp = new RectF();

        paint = new Paint();
        paint.setColor(0xFFAABBFF);

        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        map = new HashMap<Integer,Sprite>();
        next = new HashMap<Integer,Sprite>();

        setMaze(new Maze(new Coordinate(6, 6)));
        setRoom(3,3,1);
    }

    public void setListener(OnRoomOutListener listener) {
        this.listener = listener;
    }

    public void setMaze(Maze maze){
        this.maze = maze;
        this.coordinate = new Coordinate(10,10);

        setZoom(w,h);
        invalidate();
    }

    public void act(){
        if (roomChanged) setRoom();

        for(Sprite sprite : map.values()) {
            sprite.act();
            int ndx = sprite.getNdx();
            next.put(ndx,sprite);
        }

        Map tmp = map;
        map = next;
        next = tmp;
        tmp.clear();

        invalidate();
    }
    

    public void setRoom(int x,int y, int dir) {
        this.x = x;
        this.y = y;
        this.dir = dir;

        this.roomChanged = true;
    }

    private void setRoom(){
        roomChanged = false;
        map.clear();

        int xm = (int) (Math.random()*(coordinate.getWidth()-2))+1;
        int ym = (int) (Math.random()*(coordinate.getHeight()-2))+1;
        map.put(coordinate.getNdx(xm,ym),new MonsterSprite(xm,ym,3,this));
        int door = maze.get(x,y);

        int p =1;
        for(int i = 0; i <4; ++i){

            if ((door & p)==0){
                int a = (coordinate.DIR[i][0]+coordinate.DIR[(i+1)&3][0]+1)*(coordinate.getWidth()-1)/2;
                int b = (coordinate.DIR[i][1]+coordinate.DIR[(i+1)&3][1]+1)*(coordinate.getHeight()-1)/2;

                int da = coordinate.DIR[(i+3)&3][0];
                int db = coordinate.DIR[(i+3)&3][1];

/*                if (i+dir == 3){
                    hero = new HeroSprite(a+5*da, b+5*db,3,this);
                    map.put(coordinate.getNdx(a+5*da,b+5*db),hero);
                } */


                int ndx = coordinate.getNdx(a,b);
                while (ndx>=0){
                    map.put(ndx,new DecorSprite(a,b,ndx,1));
                    a+= da;
                    b+= db;
                    ndx = coordinate.getNdx(a,b);
                }
            }

            p <<=1;
        }


        if (hero == null){
            hero = new HeroSprite(5, 5,2,this);
        }
        map.put(hero.getNdx(), hero);


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
        canvas.drawRect(0,0,coordinate.getHeight(),coordinate.getHeight(),paint);

        for(Sprite s : map.values()){
            float i = s.getX();
            float j = s.getY();
            tmp.set(i,j,i+1,j+1);
            canvas.drawBitmap(sprite.getBitmap(s.getSpriteId()), src,tmp,null);
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

    public boolean isFree(int x, int y) {
        final int ndx = coordinate.getNdx(x, y);
        if (ndx == -1) return false;

        return map.get(ndx) == null && next.get(ndx) == null;
    }

    public void move(int dir) {
        hero.setDir(dir);
    }
}
