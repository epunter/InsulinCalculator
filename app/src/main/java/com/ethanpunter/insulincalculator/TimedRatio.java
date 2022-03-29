package com.ethanpunter.insulincalculator;

public class TimedRatio {

    private int from;
    private int to;
    private float ratio;

    public TimedRatio(int from, int to, float ratio) {
        this.from = from;
        this.to = to;
        this.ratio = ratio;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public float getRatio() {
        return ratio;
    }
}
