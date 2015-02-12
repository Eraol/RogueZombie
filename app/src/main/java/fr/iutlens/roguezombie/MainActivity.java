package fr.iutlens.roguezombie;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.lang.ref.WeakReference;

import fr.iutlens.roguezombie.maze.Maze;
import fr.iutlens.roguezombie.maze.MiniMapView;
import fr.iutlens.roguezombie.room.OnRoomOutListener;
import fr.iutlens.roguezombie.room.RoomView;
import fr.iutlens.roguezombie.joystick.JoystickView;
import fr.iutlens.roguezombie.util.Coordinate;


public class MainActivity extends ActionBarActivity implements OnRoomOutListener {


    private Maze maze;
    private Coordinate coordinate;
    private RoomView roomView;
    private MiniMapView miniMapView;
    private JoystickView JoystickView;


    @Override
    public void onRoomOut( int x, int y, int dir) {
        //Calcul de la prochaine salle dans la direction indiquée
        int ndx = coordinate.getNext(coordinate.getNdx(x, y), dir);

        //mise à jour de la minimap
        maze.visit(ndx);
        miniMapView.invalidate();

        //mise à jour de la salle affichée
        roomView.setRoom(coordinate.getI(ndx), coordinate.getJ(ndx), dir);
    }


    //Gestion du temps réel
    static class RefreshHandler extends Handler {
        WeakReference<MainActivity> weak;

        RefreshHandler(MainActivity animator){
            weak = new WeakReference(animator);
        }

        @Override
        public void handleMessage(Message msg) {
            if (weak.get() == null) return;
            weak.get().update();
        }

        public void sleep(long delayMillis) {
            this.removeMessages(0);
            sendMessageDelayed(obtainMessage(0), delayMillis);
        }
    }

    private RefreshHandler handler = new RefreshHandler(this);

    private void update() {
        handler.sleep(40);

    Log.d("Pad", "" + 2);
        roomView.act();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Création du layrinthe 10x10
        coordinate = new Coordinate(10,10);
        maze = new Maze(coordinate);

        // Configuration de la minimap
        miniMapView = (MiniMapView) findViewById(R.id.view);
        miniMapView.setMaze(maze);
        maze.visit(coordinate.getNdx(5,5)); // On commence en 5x5

        // Configuration de la salle
        roomView = (RoomView) findViewById(R.id.view2);
        roomView.setListener(this);
        roomView.setMaze(maze, new Coordinate(10, 10));
        roomView.setRoom(5, 5, -1);


        JoystickView = (JoystickView) findViewById(R.id.joystick);


        // On démarre le jeu !
        update();
    }

   // public void onButtonClick(View view){

         // Détection de la direction choisie
       // int dir = -1;
       // switch (view.getId()){
         //   case R.id.buttonRight :
               // dir = 0;
              //  break;
            //case R.id.buttonDown :
            //    dir = 1;
              //  break;
            //case R.id.buttonLeft :
             //   dir = 2;
           //     break;
         //   case R.id.buttonUp :
        //        dir = 3;
       //         break;
     //   }

        // Demande le déplacement dans la direction
    //    roomView.move(dir);
   // }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}


