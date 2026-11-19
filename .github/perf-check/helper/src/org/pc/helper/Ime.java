package org.pc.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.inputmethodservice.InputMethodService;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.view.inputmethod.InputConnection;
import java.nio.charset.StandardCharsets;

/**
 * debug/w27-cat: a test keyboard without a keyboard view (like ADBKeyboard) — `input text` cannot type
 * Cyrillic. Commits the letters one by one to the focused field, as a keyboard does:
 *   am broadcast -a org.pc.helper.TYPE -p org.pc.helper --es b64 <UTF-8 text, base64> --ei delay 350
 *   am broadcast -a org.pc.helper.TYPE -p org.pc.helper --es del <N> --ei delay 300   (N × backspace)
 */
public class Ime extends InputMethodService {
    private final Handler handler = new Handler(Looper.getMainLooper());
    private BroadcastReceiver receiver;

    @Override
    public void onCreate() {
        super.onCreate();
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context c, Intent i) {
                handle(i);
            }
        };
        registerReceiver(receiver, new IntentFilter("org.pc.helper.TYPE"));
    }

    @Override
    public void onDestroy() {
        if (receiver != null) unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    public boolean onEvaluateFullscreenMode() {
        return false;
    }

    private void handle(Intent i) {
        final int delay = i.getIntExtra("delay", 350);
        final String b64 = i.getStringExtra("b64");
        final String del = i.getStringExtra("del");
        if (b64 != null) {
            final String text = new String(Base64.decode(b64, Base64.DEFAULT), StandardCharsets.UTF_8);
            final int[] cps = text.codePoints().toArray();
            for (int k = 0; k < cps.length; k++) {
                final String ch = new String(cps, k, 1);
                final boolean last = k == cps.length - 1;
                handler.postDelayed(() -> {
                    InputConnection ic = getCurrentInputConnection();
                    boolean ok = ic != null && ic.commitText(ch, 1);
                    if (last || !ok) Log.w("PCIME", "type '" + text + "' letter '" + ch + "' ok=" + ok);
                }, (long) k * delay);
            }
        } else if (del != null) {
            final int n = Integer.parseInt(del.trim());
            for (int k = 0; k < n; k++) {
                final boolean last = k == n - 1;
                handler.postDelayed(() -> {
                    InputConnection ic = getCurrentInputConnection();
                    boolean ok = ic != null && ic.deleteSurroundingText(1, 0);
                    if (last || !ok) Log.w("PCIME", "del ok=" + ok);
                }, (long) k * delay);
            }
        }
    }
}
