package com.generated.app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.generated.app.data.LyricsRepository;
import com.generated.app.engine.LyricsSyncEngine;
import com.generated.app.model.LyricLine;
import com.generated.app.model.Track;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity implements LyricsSyncEngine.OnSyncListener {

    private TextView tvTrackTitle;
    private TextView tvTrackArtist;
    private TextView tvPlaybackStatus;
    private TextView tvCurrentTime;
    private TextView tvTotalTime;
    private ProgressBar pbPlayback;
    private ScrollView scrollLyrics;
    private LinearLayout layoutLyricsContainer;
    private LinearLayout layoutPermissionBanner;

    private Button btnQuickFloating;
    private Button btnOpenSettings;
    private Button btnToggleDemo;
    private Button btnToggleOriginal;
    private Button btnToggleAutoscroll;
    private Button btnGrantNotification;

    private boolean showOriginalLyrics = false;
    private boolean autoScrollEnabled = true;
    private int demoTrackIndex = 0;
    private float lyricFontSize = 18f;

    private final List<View> lyricLineViews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindViews();
        loadPreferences();
        setupListeners();

        // Initialize engine with default demo track
        List<Track> demoList = LyricsRepository.getInstance().getDemoTracks();
        if (!demoList.isEmpty() && LyricsSyncEngine.getInstance().getCurrentTrack() == null) {
            LyricsSyncEngine.getInstance().setTrack(demoList.get(0));
            LyricsSyncEngine.getInstance().setPlaying(true);
        }

        LyricsSyncEngine.getInstance().addListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPreferences();
        checkPermissions();
        updateFloatingButtonState();
        refreshLyricsViews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LyricsSyncEngine.getInstance().removeListener(this);
    }

    private void bindViews() {
        tvTrackTitle = findViewById(R.id.tv_track_title);
        tvTrackArtist = findViewById(R.id.tv_track_artist);
        tvPlaybackStatus = findViewById(R.id.tv_playback_status);
        tvCurrentTime = findViewById(R.id.tv_current_time);
        tvTotalTime = findViewById(R.id.tv_total_time);
        pbPlayback = findViewById(R.id.pb_playback);
        scrollLyrics = findViewById(R.id.scroll_lyrics);
        layoutLyricsContainer = findViewById(R.id.layout_lyrics_container);
        layoutPermissionBanner = findViewById(R.id.layout_permission_banner);

        btnQuickFloating = findViewById(R.id.btn_quick_floating);
        btnOpenSettings = findViewById(R.id.btn_open_settings);
        btnToggleDemo = findViewById(R.id.btn_toggle_demo);
        btnToggleOriginal = findViewById(R.id.btn_toggle_original);
        btnToggleAutoscroll = findViewById(R.id.btn_toggle_autoscroll);
        btnGrantNotification = findViewById(R.id.btn_grant_notification);
    }

    private void setupListeners() {
        btnOpenSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        btnQuickFloating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFloatingWindow();
            }
        });

        btnToggleDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Track> demoList = LyricsRepository.getInstance().getDemoTracks();
                demoTrackIndex = (demoTrackIndex + 1) % demoList.size();
                Track nextTrack = demoList.get(demoTrackIndex);
                LyricsSyncEngine.getInstance().setTrack(nextTrack);
                LyricsSyncEngine.getInstance().setPlaying(true);
                Toast.makeText(MainActivity.this, "הועבר לשיר הדגמה: " + nextTrack.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

        btnToggleOriginal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOriginalLyrics = !showOriginalLyrics;
                btnToggleOriginal.setText(showOriginalLyrics ? R.string.btn_hide_original : R.string.btn_show_original);
                savePreference("show_original", showOriginalLyrics);
                refreshLyricsViews();
            }
        });

        btnToggleAutoscroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoScrollEnabled = !autoScrollEnabled;
                btnToggleAutoscroll.setText(autoScrollEnabled ? R.string.btn_autoscroll_on : R.string.btn_autoscroll_off);
                btnToggleAutoscroll.setTextColor(autoScrollEnabled ? Color.parseColor("#1DB954") : Color.parseColor("#888888"));
                savePreference("autoscroll", autoScrollEnabled);
            }
        });

        btnGrantNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                startActivity(intent);
            }
        });
    }

    private void toggleFloatingWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            Toast.makeText(this, R.string.overlay_perm_required, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivity(intent);
            return;
        }

        Intent serviceIntent = new Intent(this, FloatingLyricsService.class);
        if (FloatingLyricsService.isServiceRunning()) {
            stopService(serviceIntent);
            btnQuickFloating.setText("חלון צף");
            Toast.makeText(this, "החלון הצף נסגר", Toast.LENGTH_SHORT).show();
        } else {
            startService(serviceIntent);
            btnQuickFloating.setText("סגור צף");
            Toast.makeText(this, "החלון הצף נפתח מעל מסך הטלפון", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateFloatingButtonState() {
        if (FloatingLyricsService.isServiceRunning()) {
            btnQuickFloating.setText("סגור צף");
        } else {
            btnQuickFloating.setText("חלון צף");
        }
    }

    private void checkPermissions() {
        boolean hasNotif = SpotifyNotificationListenerService.isNotificationAccessEnabled(this);
        if (hasNotif) {
            layoutPermissionBanner.setVisibility(View.GONE);
            if (SpotifyNotificationListenerService.isConnected()) {
                tvPlaybackStatus.setText(R.string.status_connected);
            } else {
                tvPlaybackStatus.setText(R.string.status_listening);
            }
        } else {
            layoutPermissionBanner.setVisibility(View.VISIBLE);
            tvPlaybackStatus.setText(R.string.status_demo);
        }
    }

    private void loadPreferences() {
        SharedPreferences sp = getSharedPreferences("lyrics_prefs", MODE_PRIVATE);
        showOriginalLyrics = sp.getBoolean("show_original", false);
        autoScrollEnabled = sp.getBoolean("autoscroll", true);
        lyricFontSize = sp.getFloat("font_size", 18f);

        btnToggleOriginal.setText(showOriginalLyrics ? R.string.btn_hide_original : R.string.btn_show_original);
        btnToggleAutoscroll.setText(autoScrollEnabled ? R.string.btn_autoscroll_on : R.string.btn_autoscroll_off);
        btnToggleAutoscroll.setTextColor(autoScrollEnabled ? Color.parseColor("#1DB954") : Color.parseColor("#888888"));
    }

    private void savePreference(String key, boolean val) {
        SharedPreferences sp = getSharedPreferences("lyrics_prefs", MODE_PRIVATE);
        sp.edit().putBoolean(key, val).apply();
    }

    private void refreshLyricsViews() {
        Track track = LyricsSyncEngine.getInstance().getCurrentTrack();
        if (track == null) return;
        layoutLyricsContainer.removeAllViews();
        lyricLineViews.clear();

        List<LyricLine> lines = track.getLyrics();
        for (int i = 0; i < lines.size(); i++) {
            final int lineIndex = i;
            final LyricLine line = lines.get(i);

            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.VERTICAL);
            row.setPadding(24, 20, 24, 20);
            LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            rowParams.setMargins(0, 8, 0, 8);
            row.setLayoutParams(rowParams);
            row.setBackgroundResource(R.drawable.bg_card);

            // Hebrew Line (Prominent & Default)
            TextView tvHebrew = new TextView(this);
            tvHebrew.setText(line.getHebrewText());
            tvHebrew.setTextColor(Color.parseColor("#E0E0E0"));
            tvHebrew.setTextSize(lyricFontSize);
            tvHebrew.setTextDirection(View.TEXT_DIRECTION_RTL);
            tvHebrew.setGravity(Gravity.START);
            row.addView(tvHebrew);

            // Optional Original Line
            if (showOriginalLyrics && line.getOriginalText() != null && !line.getOriginalText().isEmpty()) {
                TextView tvOrig = new TextView(this);
                tvOrig.setText(line.getOriginalText());
                tvOrig.setTextColor(Color.parseColor("#888888"));
                tvOrig.setTextSize(lyricFontSize * 0.75f);
                tvOrig.setPadding(0, 6, 0, 0);
                tvOrig.setTextDirection(View.TEXT_DIRECTION_LTR);
                tvOrig.setGravity(Gravity.START);
                row.addView(tvOrig);
            }

            // Click on line to jump/seek playback
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LyricsSyncEngine.getInstance().seekTo(line.getTimeMs());
                }
            });

            layoutLyricsContainer.addView(row);
            lyricLineViews.add(row);
        }

        highlightActiveLine(LyricsSyncEngine.getInstance().getActiveLineIndex());
    }

    private void highlightActiveLine(int activeIndex) {
        for (int i = 0; i < lyricLineViews.size(); i++) {
            View view = lyricLineViews.get(i);
            if (i == activeIndex) {
                view.setBackgroundResource(R.drawable.bg_active_lyric);
                // Make active line text bright
                if (view instanceof LinearLayout) {
                    LinearLayout container = (LinearLayout) view;
                    if (container.getChildCount() > 0 && container.getChildAt(0) instanceof TextView) {
                        ((TextView) container.getChildAt(0)).setTextColor(Color.parseColor("#1DB954"));
                    }
                }
                if (autoScrollEnabled) {
                    smoothScrollToView(view);
                }
            } else {
                view.setBackgroundResource(R.drawable.bg_card);
                if (view instanceof LinearLayout) {
                    LinearLayout container = (LinearLayout) view;
                    if (container.getChildCount() > 0 && container.getChildAt(0) instanceof TextView) {
                        ((TextView) container.getChildAt(0)).setTextColor(Color.parseColor("#B3B3B3"));
                    }
                }
            }
        }
    }

    private void smoothScrollToView(final View view) {
        scrollLyrics.post(new Runnable() {
            @Override
            public void run() {
                int targetY = view.getTop() - (scrollLyrics.getHeight() / 3);
                if (targetY < 0) targetY = 0;
                scrollLyrics.smoothScrollTo(0, targetY);
            }
        });
    }

    @Override
    public void onTrackChanged(final Track track) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (track != null) {
                    tvTrackTitle.setText(track.getTitle());
                    tvTrackArtist.setText(track.getArtist());
                    tvTotalTime.setText(formatTime(track.getDurationMs()));
                    refreshLyricsViews();
                }
            }
        });
    }

    @Override
    public void onPositionUpdated(final long currentPositionMs, final int activeLineIndex) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvCurrentTime.setText(formatTime(currentPositionMs));
                Track track = LyricsSyncEngine.getInstance().getCurrentTrack();
                if (track != null && track.getDurationMs() > 0) {
                    int progress = (int) ((currentPositionMs * 1000) / track.getDurationMs());
                    pbPlayback.setProgress(progress);
                }
                highlightActiveLine(activeLineIndex);
            }
        });
    }

    @Override
    public void onPlaybackStateChanged(final boolean isPlaying) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!isPlaying) {
                    tvPlaybackStatus.setText(R.string.status_paused);
                } else {
                    if (SpotifyNotificationListenerService.isConnected()) {
                        tvPlaybackStatus.setText(R.string.status_connected);
                    } else {
                        tvPlaybackStatus.setText(R.string.status_demo);
                    }
                }
            }
        });
    }

    private String formatTime(long ms) {
        long totalSeconds = ms / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return String.format(Locale.US, "%02d:%02d", minutes, seconds);
    }
}
