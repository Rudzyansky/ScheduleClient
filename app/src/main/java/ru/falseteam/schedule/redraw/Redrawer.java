package ru.falseteam.schedule.redraw;

import java.util.ArrayList;

public class Redrawer {
    private static ArrayList<Redrawable> redrawables = new ArrayList<>();

    public static void add(Redrawable r) {
        redrawables.add(r);
    }

    public static void remove(Redrawable r) {
        redrawables.remove(r);
    }

    public static void redraw() {
        for (Redrawable r : redrawables) r.redraw();
    }
}
