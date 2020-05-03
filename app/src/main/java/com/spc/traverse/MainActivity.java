package com.spc.traverse;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends Activity {

    public static final String PREF_FILE = "MyPrefsFile";
    public static final String TAG = "TRAVERSE";
    public RadioGroup rg_numplayers;
    public int num_players;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // check to see if 'continue saved game' is an option, otherwise grey it out
        Button continueButton = findViewById(R.id.continue_button);
        SharedPreferences settings = getSharedPreferences(PREF_FILE, MODE_PRIVATE);
        if (settings.getString("redBoard", "").length() == 0) {
            Log.i(TAG, "...no saved game; greying out 'continue' image");
            continueButton.setTextColor(Color.GRAY);
        } else {
            Log.i(TAG, "...saved game present; enabling 'continue' image");
            continueButton.setTextColor(Color.BLACK);
        }
    }

    public void startGame(View view)
    {
        // Grab the current #Players from the RadioGroup
        rg_numplayers = findViewById(R.id.numplayers);
        switch(rg_numplayers.getCheckedRadioButtonId()) {
            case R.id.numplayers0:
                num_players=0;
                break;
            case R.id.numplayers1:
                num_players=1;
                break;
            case R.id.numplayers2:
                num_players=2;
                break;
            case R.id.numplayers3:
                num_players=3;
                break;
            case R.id.numplayers4:
                num_players=4;
                break;
        }
        Log.i(TAG, "...starting the game with "+num_players+" players....");

        //  start the game!
        Intent intent = new Intent(MainActivity.this, BoardDisplay.class);
        intent.putExtra("newgame", true);
        intent.putExtra("players", num_players);
        startActivity(intent);

    }

    public void continueGame(View view)
    {
        Log.i(TAG, "...continuing the game");
        SharedPreferences settings = getSharedPreferences(PREF_FILE, MODE_PRIVATE);
        String redBoard=settings.getString("redBoard","");

        if(redBoard.length()==0)
        {
            showToast("No Game Previously Saved.");
            return;
        }

        //  continue a saved game
        showToast("TODO - continue the game");
        Intent intent = new Intent(MainActivity.this, BoardDisplay.class);
        intent.putExtra("newgame", false);
        startActivity(intent);
    }

    public void aboutGame (View view) {
        Log.i(TAG, "...displaying about the game");
        Intent intent = new Intent(this, MessageDisplay.class);
        intent.putExtra("Message", "Developed by TCottz 2015");
        startActivity(intent);
    }

    public void exit(View view)
    {
        Log.i(TAG, "...exiting the game");
        this.finish();
    }

    private void showToast(String msgToDisplay)
    {
        //Displaying Toast
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, msgToDisplay, Toast.LENGTH_SHORT);
        toast.show();
    }

}