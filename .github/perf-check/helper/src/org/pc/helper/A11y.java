package org.pc.helper;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;
import java.util.List;

/**
 * Тестова услуга за достъпност (вместо TalkBack, който няма в образите на емулатора):
 * - записва в logcat (PCA11Y) смяната на прозорци/панели (заглавията им) и обявяванията;
 * - org.pc.helper.DUMP --es id X — дървото, което вижда екранен четец (PCDUMP), с действията;
 * - org.pc.helper.TOUCH --ez on true|false — режим „изследване чрез докосване“ (като TalkBack);
 * - org.pc.helper.ACT --es label L — изпълнява действието с етикет L (напр. персонализирано);
 * - org.pc.helper.CLICK --es text T — ACTION_CLICK на възела с този текст/описание (или родителя му).
 */
public class A11y extends AccessibilityService {
    private static final String T = "PCA11Y";

    private final BroadcastReceiver cmd = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String a = intent.getAction();
                if ("org.pc.helper.DUMP".equals(a)) dump(intent.getStringExtra("id"));
                else if ("org.pc.helper.TOUCH".equals(a)) touch(intent.getBooleanExtra("on", false));
                else if ("org.pc.helper.ACT".equals(a)) act(intent.getStringExtra("label"));
                else if ("org.pc.helper.CLICK".equals(a)) click(intent.getStringExtra("text"));
            } catch (Throwable t) {
                Log.i(T, "command failed: " + t);
            }
        }
    };

    @Override
    protected void onServiceConnected() {
        IntentFilter f = new IntentFilter();
        f.addAction("org.pc.helper.DUMP");
        f.addAction("org.pc.helper.TOUCH");
        f.addAction("org.pc.helper.ACT");
        f.addAction("org.pc.helper.CLICK");
        registerReceiver(cmd, f);
        Log.i(T, "connected flags=" + getServiceInfo().flags);
    }

    @Override
    public void onDestroy() {
        try { unregisterReceiver(cmd); } catch (Throwable ignored) { }
        Log.i(T, "destroyed");
        super.onDestroy();
    }

    @Override
    public void onInterrupt() { }

    private String lastLive = "";

    private static String s(CharSequence c) {
        if (c == null) return "";
        return c.toString().replace('\n', ' ');
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent e) {
        int type = e.getEventType();
        int ch = e.getContentChangeTypes();
        // Вълна 19: промените на описанието/състоянието (историята „Моята читателска година“) — PCLIVE, без повторения.
        if (type == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED && (ch == 0 || (ch & (2 | 4 | 64)) != 0)) {
            AccessibilityNodeInfo n = e.getSource();
            if (n != null) {
                String d = s(n.getContentDescription());
                String st = Build.VERSION.SDK_INT >= 30 ? s(n.getStateDescription()) : "";
                String line = "desc=" + d + " state=" + st;
                if ((d.length() > 10 || !st.isEmpty()) && !line.equals(lastLive)) {
                    lastLive = line;
                    Log.i("PCLIVE", line.length() > 400 ? line.substring(0, 400) : line);
                }
            }
        }
        boolean pane = (ch & (AccessibilityEvent.CONTENT_CHANGE_TYPE_PANE_TITLE
            | AccessibilityEvent.CONTENT_CHANGE_TYPE_PANE_APPEARED | AccessibilityEvent.CONTENT_CHANGE_TYPE_PANE_DISAPPEARED)) != 0;
        if (type != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED && type != AccessibilityEvent.TYPE_ANNOUNCEMENT
            && !(type == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED && pane)) return;
        String paneTitle = "";
        AccessibilityNodeInfo src = e.getSource();
        if (src != null && Build.VERSION.SDK_INT >= 28) paneTitle = s(src.getPaneTitle());
        Log.i(T, "ev=" + AccessibilityEvent.eventTypeToString(type) + " changes=" + ch + " pkg=" + e.getPackageName()
            + " cls=" + e.getClassName() + " text=" + e.getText() + " desc=" + s(e.getContentDescription()) + " pane=" + paneTitle);
    }

    private void touch(boolean on) {
        AccessibilityServiceInfo info = getServiceInfo();
        if (on) info.flags |= AccessibilityServiceInfo.FLAG_REQUEST_TOUCH_EXPLORATION_MODE;
        else info.flags &= ~AccessibilityServiceInfo.FLAG_REQUEST_TOUCH_EXPLORATION_MODE;
        setServiceInfo(info);
        Log.i(T, "touch exploration requested=" + on + " flags=" + getServiceInfo().flags);
    }

    private void dump(String id) {
        List<AccessibilityWindowInfo> ws = getWindows();
        int wi = 0;
        for (AccessibilityWindowInfo w : ws) {
            AccessibilityNodeInfo root = w.getRoot();
            Rect r = new Rect();
            w.getBoundsInScreen(r);
            String title = Build.VERSION.SDK_INT >= 24 ? s(w.getTitle()) : "";
            Log.i("PCDUMP", id + " W" + wi + " type=" + w.getType() + " active=" + w.isActive() + " focused=" + w.isFocused()
                + " title=" + title + " pkg=" + (root != null ? root.getPackageName() : "-") + " b=" + r.toShortString());
            if (root != null) node(id, wi, root, 0);
            wi++;
        }
        Log.i("PCDUMP", id + " END windows=" + ws.size());
    }

    private void node(String id, int wi, AccessibilityNodeInfo n, int depth) {
        if (depth > 60) return;
        if (n.isVisibleToUser()) {
            Rect r = new Rect();
            n.getBoundsInScreen(r);
            StringBuilder acts = new StringBuilder();
            for (AccessibilityNodeInfo.AccessibilityAction a : n.getActionList()) {
                if (a.getLabel() != null) acts.append(a.getId()).append(':').append(s(a.getLabel())).append(';');
            }
            String pane = Build.VERSION.SDK_INT >= 28 ? s(n.getPaneTitle()) : "";
            boolean imp = Build.VERSION.SDK_INT < 24 || n.isImportantForAccessibility();
            String line = id + " W" + wi + " d" + depth + " cls=" + s(n.getClassName()) + " text=" + s(n.getText())
                + " desc=" + s(n.getContentDescription()) + " pane=" + pane + " heading=" + (Build.VERSION.SDK_INT >= 28 && n.isHeading())
                + " clk=" + n.isClickable() + " lclk=" + n.isLongClickable() + " focusable=" + n.isFocusable()
                + " checkable=" + n.isCheckable() + " checked=" + n.isChecked() + " enabled=" + n.isEnabled() + " imp=" + imp
                + " sel=" + n.isSelected() + " state=" + (Build.VERSION.SDK_INT >= 30 ? s(n.getStateDescription()) : "")
                + " b=" + r.toShortString() + " acts=" + acts;
            Log.i("PCDUMP", line.length() > 3500 ? line.substring(0, 3500) : line);
        }
        for (int i = 0; i < n.getChildCount(); i++) {
            AccessibilityNodeInfo c = n.getChild(i);
            if (c != null) node(id, wi, c, depth + 1);
        }
    }

    private AccessibilityNodeInfo find(AccessibilityNodeInfo n, String label, boolean byAction, int depth) {
        if (n == null || depth > 60) return null;
        if (byAction) {
            for (AccessibilityNodeInfo.AccessibilityAction a : n.getActionList()) {
                if (a.getLabel() != null && label.equals(a.getLabel().toString())) return n;
            }
        } else if (s(n.getText()).contains(label) || s(n.getContentDescription()).contains(label)) {
            return n;
        }
        for (int i = 0; i < n.getChildCount(); i++) {
            AccessibilityNodeInfo r = find(n.getChild(i), label, byAction, depth + 1);
            if (r != null) return r;
        }
        return null;
    }

    private void act(String label) {
        AccessibilityNodeInfo n = find(getRootInActiveWindow(), label, true, 0);
        if (n == null) { Log.i(T, "ACT '" + label + "': no node with this action"); return; }
        for (AccessibilityNodeInfo.AccessibilityAction a : n.getActionList()) {
            if (a.getLabel() != null && label.equals(a.getLabel().toString())) {
                boolean ok = n.performAction(a.getId());
                Log.i(T, "ACT '" + label + "' id=" + a.getId() + " on " + s(n.getContentDescription()) + s(n.getText()) + " -> " + ok);
                return;
            }
        }
    }

    private void click(String text) {
        AccessibilityNodeInfo n = find(getRootInActiveWindow(), text, false, 0);
        while (n != null && !n.isClickable()) n = n.getParent();
        if (n == null) { Log.i(T, "CLICK '" + text + "': not found"); return; }
        Log.i(T, "CLICK '" + text + "' -> " + n.performAction(AccessibilityNodeInfo.ACTION_CLICK));
    }
}
