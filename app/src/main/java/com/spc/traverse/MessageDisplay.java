package com.spc.traverse;

import android.app.Activity;
import android.os.Bundle;
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

        String value = "empty";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            value = extras.getString("Message");
        }

        TextView resultView = (TextView) findViewById(R.id.textview_message_display);
        resultView.setText(value);
    }
}
