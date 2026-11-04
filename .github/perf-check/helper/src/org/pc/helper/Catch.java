package org.pc.helper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import java.util.Arrays;

/** Хваща ACTION_SENDTO mailto: и записва действието, адреса и extras (тема, текст) в logcat. */
public class Catch extends Activity {
    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        Intent i = getIntent();
        StringBuilder sb = new StringBuilder();
        sb.append("action=").append(i.getAction()).append(" data=").append(i.getDataString());
        Bundle e = i.getExtras();
        if (e != null) {
            for (String k : e.keySet()) {
                Object v = e.get(k);
                sb.append(" | ").append(k).append('=').append(v instanceof String[] ? Arrays.toString((String[]) v) : String.valueOf(v));
            }
        }
        Log.i("PCCATCH", sb.toString());
        TextView t = new TextView(this);
        t.setText("PCCATCH " + sb);
        t.setTextSize(16);
        setContentView(t);
    }
}
