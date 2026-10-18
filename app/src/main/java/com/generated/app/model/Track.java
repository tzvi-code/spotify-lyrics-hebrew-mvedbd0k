package com.generated.app.model;

import java.util.List;

public class Track {
    private final String title;
    private final String artist;
    private final long durationMs;
    private final List<LyricLine> lyrics;

    public Track(String title, String artist, long durationMs, List<LyricLine> lyrics) {
        this.title = title;
        this.artist = artist;
        this.durationMs = durationMs;
        this.lyrics = lyrics;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public long getDurationMs() {
        return durationMs;
    }

    public List<LyricLine> getLyrics() {
        return lyrics;
    }
}
