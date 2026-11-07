package org.pc.helper;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Вълна 19: хваща ACTION_SEND (от системния избор „Сподели“) и записва в logcat (PCSHARE) типа,
 * текста, адреса на картинката (EXTRA_STREAM / ClipData), байтовете, подписа PNG и размерите ѝ —
 * чете я с временното право, което дава изпращащото приложение.
 */
public class Share extends Activity {
    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        Intent i = getIntent();
        StringBuilder sb = new StringBuilder();
        sb.append("action=").append(i.getAction()).append(" type=").append(i.getType());
        sb.append(" flags=0x").append(Integer.toHexString(i.getFlags()));
        CharSequence text = i.getCharSequenceExtra(Intent.EXTRA_TEXT);
        sb.append(" subject=").append(i.getStringExtra(Intent.EXTRA_SUBJECT));
        sb.append(" text=").append(text == null ? null : text.toString().replace('\n', ' '));
        Uri u = null;
        try {
            u = i.getParcelableExtra(Intent.EXTRA_STREAM);
        } catch (Throwable ignored) {
        }
        ClipData clip = i.getClipData();
        sb.append(" clipItems=").append(clip == null ? 0 : clip.getItemCount());
        if (u == null && clip != null && clip.getItemCount() > 0) u = clip.getItemAt(0).getUri();
        sb.append(" stream=").append(u);
        if (u != null) {
            try (InputStream in = getContentResolver().openInputStream(u)) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] buf = new byte[65536];
                int n;
                while ((n = in.read(buf)) > 0) out.write(buf, 0, n);
                byte[] all = out.toByteArray();
                BitmapFactory.Options o = new BitmapFactory.Options();
                o.inJustDecodeBounds = true;
                BitmapFactory.decodeByteArray(all, 0, all.length, o);
                boolean png = all.length > 8 && (all[0] & 0xff) == 0x89 && all[1] == 'P' && all[2] == 'N' && all[3] == 'G';
                sb.append(" mime=").append(getContentResolver().getType(u));
                sb.append(" bytes=").append(all.length).append(" png=").append(png);
                sb.append(" dims=").append(o.outWidth).append('x').append(o.outHeight);
            } catch (Throwable t) {
                sb.append(" READ FAILED: ").append(t);
            }
        }
        Log.i("PCSHARE", sb.toString());
        TextView t = new TextView(this);
        t.setText("PCSHARE " + sb);
        t.setTextSize(14);
        setContentView(t);
    }
}
