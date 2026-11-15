package org.pc.helper;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import java.io.InputStream;

/**
 * debug/w26-check: „Сподели“ на снимка от MediaStore към приложението — както галерия: ACTION_SEND с
 * EXTRA_STREAM, ClipData и FLAG_GRANT_READ_URI_PERMISSION (помощното приложение чете медиите с
 * READ_EXTERNAL_STORAGE). Extras: uri — content://media/…, pkg — пакетът на приложението.
 */
public class Send extends Activity {
    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        String u = getIntent().getStringExtra("uri");
        String pkg = getIntent().getStringExtra("pkg");
        StringBuilder sb = new StringBuilder("uri=").append(u).append(" pkg=").append(pkg);
        try {
            Uri uri = Uri.parse(u);
            long n = 0;
            try (InputStream in = getContentResolver().openInputStream(uri)) {
                byte[] buf = new byte[65536];
                int k;
                while ((k = in.read(buf)) > 0) n += k;
                sb.append(" helper-read=").append(n);
            } catch (Throwable t) {
                sb.append(" helper-read FAILED: ").append(t);
            }
            Intent s = new Intent(Intent.ACTION_SEND);
            s.setType("image/jpeg");
            s.putExtra(Intent.EXTRA_STREAM, uri);
            s.setClipData(ClipData.newRawUri("", uri));
            s.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            s.setClassName(pkg, "org.chyavorec.app.MainActivity");
            startActivity(s);
            sb.append(" started");
        } catch (Throwable t) {
            sb.append(" start FAILED: ").append(t);
        }
        Log.i("PCSEND", sb.toString());
        finish();
    }
}
