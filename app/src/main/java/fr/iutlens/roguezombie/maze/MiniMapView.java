package fr.iutlens.roguezombie.maze;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.RectF;

import android.util.AttributeSet;
import android.view.View;

import fr.iutlens.roguezombie.util.Coordinate;
import fr.iutlens.roguezombie.R;
import fr.iutlens.roguezombie.util.SpriteSheet;

/**
 * Created by dubois on 22/01/15.
 */
public class MiniMapView extends View {

    Maze maze;
    Coordinate coordinate;

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


    // 3 constructeurs obligatoires pour une vue. Les 3 appellent init() pour ne pas dupliquer le code.

    public MiniMapView(Context context) {
        super(context);
        init();
    }

    public MiniMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MiniMapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init(){
        transform = new Matrix();
        reverse = new Matrix();

        sprite = SpriteSheet.get(this.getContext(), R.drawable.room);
        src = new Rect(0,0, sprite.w, sprite.h);
        tmp = new RectF();

        paint = new Paint();
        paint.setColor(0x7777ff77);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(20f);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setAntiAlias(true);


        setMaze(new Maze(new Coordinate(6, 6)));
    }

    public void setMaze(Maze maze){
        this.maze = maze;
        this.coordinate = maze.getCoordinate();

        setZoom(w,h);
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

      //  canvas.drawColor(0xFF000000);

        //On parcours la carte
        for(int i = 0; i < coordinate.getWidth(); ++i){
            for(int j = 0; j < coordinate.getHeight(); ++j){
                tmp.set(i,j,i+1,j+1);
                canvas.drawBitmap(sprite.getBitmap(maze.getCode(i, j)), src,tmp,null);
            }
        }

        //On affiche la position actuelle
        int last = maze.getLast();
        if (last>=0){
            int i = coordinate.getI(last);
            int j = coordinate.getJ(last);

           canvas.drawPoint(i + 0.5f, j + 0.5f, paint);
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

        // Calcul de la transfromation désirée (et de son inverse)
        transform.setRectToRect(src,dst, Matrix.ScaleToFit.CENTER);
        transform.invert(reverse);

        // Calcul de la taille du marqueur de position.
        float[] s = {0.5f,0.5f};
        float[] d = new float[2];
        transform.mapVectors(d,s);
        paint.setStrokeWidth(d[0]);
    }


}
