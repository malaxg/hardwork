package com.malaxg.dto;

import org.opencv.core.Point;
import org.opencv.core.Size;

public class MatchResult {
    private final Point matchLocation;
    private final double confidence;
    private final boolean matched;

    public MatchResult(Point matchLocation, double confidence, boolean matched) {
        this.matchLocation = matchLocation;
        this.confidence = confidence;
        this.matched = matched;
    }

    public Point getMatchLocation() {
        return matchLocation;
    }

    public double getConfidence() {
        return confidence;
    }

    public boolean isMatched() {
        return matched;
    }
}