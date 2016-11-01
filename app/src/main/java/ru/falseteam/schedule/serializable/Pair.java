package ru.falseteam.schedule.serializable;

import java.io.Serializable;

/**
 * Created by Prog on 01.11.16.
 */
public class Pair implements Serializable {
    public boolean exists;
    public int id;
    public String name;
    public String audience;
    public String teacher;
    public String lastTask;

    private Pair() {
    }

    public static class Factory {
        public static Pair getDefault() {
            Pair pair = new Pair();
            pair.exists = false;
            return pair;
        }
    }
}
