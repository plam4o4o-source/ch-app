package org.pc.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.LocaleList;
import android.util.Log;

/**
 * Смяна на езика на устройството без рестарт (както от Настройки › Език):
 * am broadcast -n org.pc.helper/.Cmd -a org.pc.helper.LOCALE --es tags bg-BG
 * Нужни са CHANGE_CONFIGURATION (pm grant) и достъп до скритото API (hidden_api_policy 1).
 */
public class Cmd extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String tags = intent.getStringExtra("tags");
        try {
            Class<?> c = Class.forName("com.android.internal.app.LocalePicker");
            c.getMethod("updateLocales", LocaleList.class).invoke(null, LocaleList.forLanguageTags(tags));
            Log.i("PCHELPER", "LOCALE ok " + tags);
            setResultData("LOCALE ok " + tags);
        } catch (Throwable t) {
            Throwable c = t.getCause() != null ? t.getCause() : t;
            Log.i("PCHELPER", "LOCALE failed " + tags + ": " + c);
            setResultData("LOCALE failed: " + c);
        }
    }
}
