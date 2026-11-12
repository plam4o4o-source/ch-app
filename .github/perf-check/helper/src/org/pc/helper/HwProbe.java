package org.pc.helper;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.ImageView;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Проба за bitmap-ите (ВРЕМЕННО, само за емулатора): работят ли HARDWARE bitmap-ите и къде се
 * вижда паметта им в dumpsys meminfo.
 *   am start -n org.pc.helper/.HwProbe --es mode MODE --ei n 25 --ei size 1024
 * MODE: sw8888 / sw565 — софтуерни; hw — Bitmap.copy(HARDWARE); dechw / dec565 / dec8888 —
 * BitmapFactory.decodeByteArray(JPEG) с inPreferredConfig (както декодира Coil).
 * Bitmap-ите се държат до force-stop; резултатът — в logcat (PCHW).
 */
public class HwProbe extends Activity {
    private static final ArrayList<Bitmap> KEEP = new ArrayList<>();

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        String mode = getIntent().getStringExtra("mode");
        if (mode == null) mode = "hw";
        int n = getIntent().getIntExtra("n", 25);
        int size = getIntent().getIntExtra("size", 1024);
        int ok = 0, fail = 0;
        long bytes = 0;
        String cfg = "?", err = "";
        long t0 = SystemClock.uptimeMillis();
        try {
            Paint p = new Paint();
            p.setColor(0xffffffff);
            for (int i = 0; i < n; i++) {
                boolean c565 = "sw565".equals(mode);
                Bitmap sw = Bitmap.createBitmap(size, size, c565 ? Bitmap.Config.RGB_565 : Bitmap.Config.ARGB_8888);
                Canvas c = new Canvas(sw);
                c.drawColor(0xff000000 | (i * 0x10325));
                c.drawCircle(size / 2f, size / 2f, size / 3f, p);
                Bitmap b;
                if ("hw".equals(mode)) {
                    b = sw.copy(Bitmap.Config.HARDWARE, false);
                    sw.recycle();
                } else if (mode.startsWith("dec")) {
                    ByteArrayOutputStream o = new ByteArrayOutputStream();
                    sw.compress(Bitmap.CompressFormat.JPEG, 85, o);
                    sw.recycle();
                    BitmapFactory.Options opt = new BitmapFactory.Options();
                    opt.inPreferredConfig = "dechw".equals(mode) ? Bitmap.Config.HARDWARE
                        : "dec565".equals(mode) ? Bitmap.Config.RGB_565 : Bitmap.Config.ARGB_8888;
                    byte[] d = o.toByteArray();
                    b = BitmapFactory.decodeByteArray(d, 0, d.length, opt);
                } else {
                    b = sw;
                }
                if (b == null) { fail++; continue; }
                ok++;
                bytes += b.getAllocationByteCount();
                cfg = String.valueOf(b.getConfig());
                KEEP.add(b);
            }
        } catch (Throwable t) {
            err = t.toString();
        }
        String msg = "PCHW mode=" + mode + " n=" + n + " size=" + size + " ok=" + ok + " fail=" + fail
            + " config=" + cfg + " allocBytes=" + bytes + " kept=" + KEEP.size()
            + " ms=" + (SystemClock.uptimeMillis() - t0) + (err.isEmpty() ? "" : " error=" + err);
        Log.i("PCHW", msg);
        ImageView v = new ImageView(this);
        if (!KEEP.isEmpty()) v.setImageBitmap(KEEP.get(KEEP.size() - 1));
        setContentView(v);
    }
}
