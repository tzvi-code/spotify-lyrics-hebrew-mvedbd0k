package com.generated.app;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.generated.app.engine.LyricsSyncEngine;
import com.generated.app.model.LyricLine;
import com.generated.app.model.Track;

import java.util.List;

public class FloatingLyricsService extends Service implements LyricsSyncEngine.OnSyncListener {

    private WindowManager windowManager;
    private View floatingView;
    private WindowManager.LayoutParams params;

    private TextView tvTrack;
    private TextView tvHebrew;
    private TextView tvOriginal;
    private Button btnToggleOrig;
    private Button btnClose;

    private boolean showOriginal = false;
    private static boolean isRunning = false;

    public static boolean isServiceRunning() {
        return isRunning;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isRunning = true;
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        floatingView = LayoutInflater.from(this).inflate(R.layout.floating_lyrics_layout, null);

        int layoutType;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutType = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutType = WindowManager.LayoutParams.TYPE_PHONE;
        }

        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                layoutType,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 60;
        params.y = 120;

        tvTrack = floatingView.findViewById(R.id.tv_floating_track);
        tvHebrew = floatingView.findViewById(R.id.tv_floating_hebrew);
        tvOriginal = floatingView.findViewById(R.id.tv_floating_original);
        btnToggleOrig = floatingView.findViewById(R.id.btn_floating_toggle_orig);
        btnClose = floatingView.findViewById(R.id.btn_floating_close);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSelf();
            }
        });

        btnToggleOrig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOriginal = !showOriginal;
                tvOriginal.setVisibility(showOriginal ? View.VISIBLE : View.GONE);
                btnToggleOrig.setText(showOriginal ? "הסתר" : "מקורי");
            }
        });

        // Touch listener for dragging the floating window
        floatingView.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        if (floatingView.isAttachedToWindow()) {
                            windowManager.updateViewLayout(floatingView, params);
                        }
                        return true;
                }
                return false;
            }
        });

        try {
            windowManager.addView(floatingView, params);
        } catch (Exception e) {
            stopSelf();
            return;
        }

        LyricsSyncEngine.getInstance().addListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
        LyricsSyncEngine.getInstance().removeListener(this);
        if (floatingView != null && floatingView.isAttachedToWindow()) {
            windowManager.removeView(floatingView);
        }
    }

    @Override
    public void onTrackChanged(Track track) {
        if (tvTrack != null && track != null) {
            tvTrack.setText(track.getTitle() + " • " + track.getArtist());
        }
    }

    @Override
    public void onPositionUpdated(long currentPositionMs, int activeLineIndex) {
        Track track = LyricsSyncEngine.getInstance().getCurrentTrack();
        if (track == null || activeLineIndex < 0 || activeLineIndex >= track.getLyrics().size()) {
            return;
        }
        LyricLine line = track.getLyrics().get(activeLineIndex);
        if (tvHebrew != null) {
            tvHebrew.setText(line.getHebrewText());
        }
        if (tvOriginal != null) {
            tvOriginal.setText(line.getOriginalText());
        }
    }

    @Override
    public void onPlaybackStateChanged(boolean isPlaying) {
        // Active indicator could reflect playback state
    }
}
