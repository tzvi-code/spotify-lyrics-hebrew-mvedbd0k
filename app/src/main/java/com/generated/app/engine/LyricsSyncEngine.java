package com.generated.app.engine;

import android.os.Handler;
import android.os.Looper;

import com.generated.app.model.LyricLine;
import com.generated.app.model.Track;

import java.util.ArrayList;
import java.util.List;

public class LyricsSyncEngine {

    public interface OnSyncListener {
        void onTrackChanged(Track track);
        void onPositionUpdated(long currentPositionMs, int activeLineIndex);
        void onPlaybackStateChanged(boolean isPlaying);
    }

    private static LyricsSyncEngine instance;

    public static synchronized LyricsSyncEngine getInstance() {
        if (instance == null) {
            instance = new LyricsSyncEngine();
        }
        return instance;
    }

    private final List<OnSyncListener> listeners = new ArrayList<>();
    private final Handler handler = new Handler(Looper.getMainLooper());

    private Track currentTrack;
    private long currentPositionMs = 0;
    private boolean isPlaying = false;
    private int activeLineIndex = -1;

    private final Runnable tickRunnable = new Runnable() {
        @Override
        public void run() {
            if (isPlaying && currentTrack != null) {
                currentPositionMs += 250;
                if (currentPositionMs > currentTrack.getDurationMs()) {
                    currentPositionMs = 0; // loop or reset
                }
                updateActiveLine();
                notifyPosition();
                handler.postDelayed(this, 250);
            }
        }
    };

    private LyricsSyncEngine() {}

    public synchronized void addListener(OnSyncListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
            if (currentTrack != null) {
                listener.onTrackChanged(currentTrack);
                listener.onPositionUpdated(currentPositionMs, activeLineIndex);
                listener.onPlaybackStateChanged(isPlaying);
            }
        }
    }

    public synchronized void removeListener(OnSyncListener listener) {
        listeners.remove(listener);
    }

    public void setTrack(Track track) {
        this.currentTrack = track;
        this.currentPositionMs = 0;
        this.activeLineIndex = -1;
        updateActiveLine();
        for (OnSyncListener l : new ArrayList<>(listeners)) {
            l.onTrackChanged(track);
            l.onPositionUpdated(currentPositionMs, activeLineIndex);
        }
    }

    public void setPlaying(boolean playing) {
        if (this.isPlaying != playing) {
            this.isPlaying = playing;
            if (playing) {
                handler.removeCallbacks(tickRunnable);
                handler.post(tickRunnable);
            } else {
                handler.removeCallbacks(tickRunnable);
            }
            for (OnSyncListener l : new ArrayList<>(listeners)) {
                l.onPlaybackStateChanged(playing);
            }
        }
    }

    public void seekTo(long positionMs) {
        this.currentPositionMs = positionMs;
        updateActiveLine();
        notifyPosition();
    }

    private void updateActiveLine() {
        if (currentTrack == null || currentTrack.getLyrics().isEmpty()) {
            activeLineIndex = -1;
            return;
        }
        List<LyricLine> lines = currentTrack.getLyrics();
        int newIndex = -1;
        for (int i = 0; i < lines.size(); i++) {
            if (currentPositionMs >= lines.get(i).getTimeMs()) {
                newIndex = i;
            } else {
                break;
            }
        }
        activeLineIndex = newIndex;
    }

    private void notifyPosition() {
        for (OnSyncListener l : new ArrayList<>(listeners)) {
            l.onPositionUpdated(currentPositionMs, activeLineIndex);
        }
    }

    public Track getCurrentTrack() {
        return currentTrack;
    }

    public long getCurrentPositionMs() {
        return currentPositionMs;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public int getActiveLineIndex() {
        return activeLineIndex;
    }
}
