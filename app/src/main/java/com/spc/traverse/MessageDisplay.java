package com.spc.traverse;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by tcottz7 on 22/08/15.
 * just displays a simple message in a floating activity
 */
public class MessageDisplay extends Activity {

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.message_display);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("Message","");
            if (!value.equals("")) {
                // Display extras message key contents on screen
                TextView resultView = findViewById(R.id.textview_message_display);
                resultView.setText(value);
            } else {
                // No message key in extras, therefore just finish activity
                finish();
            }
        } else {
            // No extras passed, therefore just finish activity
            finish();
        }
    }

    // clicking anywhere on layout should just close it
    public void onClick(View v) {
        finish();
    }
}
