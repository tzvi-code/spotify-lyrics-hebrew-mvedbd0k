package com.generated.app;

import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;

import com.generated.app.data.LyricsRepository;
import com.generated.app.engine.LyricsSyncEngine;
import com.generated.app.model.Track;

public class SpotifyNotificationListenerService extends NotificationListenerService {

    private static final String SPOTIFY_PACKAGE = "com.spotify.music";
    private static final String SPOTIFY_LITE_PACKAGE = "com.spotify.lite";

    private static boolean isServiceConnected = false;
    private String lastTrackTitle = "";

    public static boolean isConnected() {
        return isServiceConnected;
    }

    public static boolean isNotificationAccessEnabled(Context context) {
        String pkgName = context.getPackageName();
        final String flat = Settings.Secure.getString(context.getContentResolver(), "enabled_notification_listeners");
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (String name : names) {
                final ComponentName cn = ComponentName.unflattenFromString(name);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
        isServiceConnected = true;
    }

    @Override
    public void onListenerDisconnected() {
        super.onListenerDisconnected();
        isServiceConnected = false;
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        if (sbn == null || sbn.getNotification() == null) return;

        String packageName = sbn.getPackageName();
        if (SPOTIFY_PACKAGE.equals(packageName) || SPOTIFY_LITE_PACKAGE.equals(packageName)) {
            Bundle extras = sbn.getNotification().extras;
            if (extras != null) {
                CharSequence titleSeq = extras.getCharSequence("android.title");
                CharSequence textSeq = extras.getCharSequence("android.text");

                String title = titleSeq != null ? titleSeq.toString() : "";
                String artist = textSeq != null ? textSeq.toString() : "";

                if (!TextUtils.isEmpty(title) && !title.equals(lastTrackTitle)) {
                    lastTrackTitle = title;
                    Track track = LyricsRepository.getInstance().findTrackOrGenerate(title, artist);
                    LyricsSyncEngine.getInstance().setTrack(track);
                    LyricsSyncEngine.getInstance().setPlaying(true);
                }
            }
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        if (sbn == null) return;
        String packageName = sbn.getPackageName();
        if (SPOTIFY_PACKAGE.equals(packageName) || SPOTIFY_LITE_PACKAGE.equals(packageName)) {
            LyricsSyncEngine.getInstance().setPlaying(false);
        }
    }
}
