package ru.falseteam.schedule.listeners;

import java.util.ArrayList;

public class Redrawer {
    private static final ArrayList<Redrawable> redrawables = new ArrayList<>();
    private static int redrawCounter = 0;

    public static void add(Redrawable r) {
        synchronized (redrawables) {
            redrawables.add(r);
        }
    }

    public static void remove(Redrawable r) {
        synchronized (redrawables) {
            redrawables.remove(r);
        }
    }

    public static void redraw() {
        synchronized (redrawables) {
            ++redrawCounter;
            for (Redrawable r : redrawables) r.redraw();
        }
    }

    public static int getRedrawCounter() {
        return redrawCounter;
    }
}
