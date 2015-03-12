package fr.iutlens.roguezombie;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


public class ScoreActivity extends ActionBarActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_score);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        SharedPreferences settings = getSharedPreferences("scoreOr", 0);
        SharedPreferences settings2 = getSharedPreferences("scoreEnvoyer", 0);
        int DernierScore = settings2.getInt("scoreEnvoyer",0);
        int MeilleurScore = settings.getInt("scoreOr",0);



        if(DernierScore > MeilleurScore){
            ((TextView) findViewById(R.id.textScore1)).setText("1 : "+DernierScore);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("scoreEnvoyer", MeilleurScore);
            // Commit the edits!
            editor.commit();
        }
        else{
            ((TextView) findViewById(R.id.textScore1)).setText("1 : "+MeilleurScore);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_score, menu);
        return true;
    }


    }
