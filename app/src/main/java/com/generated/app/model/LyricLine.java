package com.generated.app.model;

public class LyricLine {
    private final int id;
    private final long timeMs;
    private final String originalText;
    private final String hebrewText;

    public LyricLine(int id, long timeMs, String originalText, String hebrewText) {
        this.id = id;
        this.timeMs = timeMs;
        this.originalText = originalText;
        this.hebrewText = hebrewText;
    }

    public int getId() {
        return id;
    }

    public long getTimeMs() {
        return timeMs;
    }

    public String getOriginalText() {
        return originalText;
    }

    public String getHebrewText() {
        return hebrewText;
    }
}
