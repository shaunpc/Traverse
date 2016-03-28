package com.spc.traverse;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String PREF_FILE = "MyPrefsFile";
    public static final String TAG = "TRAVERSE";
    public ImageButton ib_np1, ib_np2, ib_np3, ib_np4;
    public int num_players;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // check to see if 'continue saved game' is an option, otherwise grey it out
        Button continueButton = (Button) findViewById(R.id.continue_button);
        SharedPreferences settings = getSharedPreferences(PREF_FILE, MODE_PRIVATE);
        if (settings.getString("redBoard", "").length() == 0) {
            Log.i(TAG, "...no saved game; greying out 'continue' button");
            continueButton.setTextColor(Color.GRAY);
        } else {
            Log.i(TAG, "...saved game present; enabling 'continue' button");
            continueButton.setTextColor(Color.BLACK);
        }

        // get the ImageButtons that represent NUmOfPlayers
        num_players = 1;
        ib_np1 = (ImageButton) findViewById(R.id.numplayers1);
        ib_np2 = (ImageButton) findViewById(R.id.numplayers2);
        ib_np3 = (ImageButton) findViewById(R.id.numplayers3);
        ib_np4 = (ImageButton) findViewById(R.id.numplayers4);

    }

    public void aboutGame (View view) {
        Log.i(TAG, "...displaying about the game");
        showMessage("Developed by TCottz 2015");
    }

    public void startGame(View view)
    {
        Log.i(TAG, "...starting the game with "+num_players+" players....");

        //  start the game!
        Intent intent = new Intent(MainActivity.this, BoardDisplay.class);
        intent.putExtra("type", "new");
        intent.putExtra("players", num_players);
        startActivity(intent);

    }

    public void setNumPlayers (View view) {
        // Turn them all off...
        ib_np1.setBackgroundResource(R.drawable.numplayers1_off);
        ib_np2.setBackgroundResource(R.drawable.numplayers2_off);
        ib_np3.setBackgroundResource(R.drawable.numplayers3_off);
        ib_np4.setBackgroundResource(R.drawable.numplayers4_off);

        switch (view.getId()) {
            case R.id.numplayers1:
                ib_np1.setBackgroundResource(R.drawable.numplayers1_on);
                num_players=1;
                break;
            case R.id.numplayers2:
                ib_np2.setBackgroundResource(R.drawable.numplayers2_on);
                num_players=2;
                break;
            case R.id.numplayers3:
                ib_np3.setBackgroundResource(R.drawable.numplayers3_on);
                num_players=3;
                break;
            case R.id.numplayers4:
                ib_np4.setBackgroundResource(R.drawable.numplayers4_on);
                num_players=4;
                break;
        }
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

        showToast("TODO - continue the game");
        //  continue a saved game
        Intent intent = new Intent(MainActivity.this, BoardDisplay.class);
        intent.putExtra("type", "cont");
        startActivity(intent);

    }

    public void exit(View view)
    {
        Log.i(TAG, "...exiting the game");
        this.finish();
    }

    private void showMessage(String messageString)
    {
        Intent intent = new Intent(this, MessageDisplay.class);
        intent.putExtra("Message", messageString);
        startActivity(intent);
    }

    private void showToast(String msgToDisplay)
    {
        //Displaying Toast
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, msgToDisplay, Toast.LENGTH_SHORT);
        toast.show();
    }



}