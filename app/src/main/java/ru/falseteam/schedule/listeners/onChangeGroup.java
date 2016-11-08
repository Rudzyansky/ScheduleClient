package ru.falseteam.schedule.listeners;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.falseteam.schedule.Data;
import ru.falseteam.schedule.serializable.Groups;

public class OnChangeGroup {
    private static Map<OnChangeGroupListener, List<Groups>> listeners = new HashMap<>();

    public static void add(OnChangeGroupListener r, Groups... groups) {
        listeners.put(r, Arrays.asList(groups));
    }

    public static void remove(OnChangeGroupListener r) {
        listeners.remove(r);
    }

    public static void change() {
        Groups g = Data.getCurrentGroup();
        for (OnChangeGroupListener onChangeGroupListener : listeners.keySet()) {
            if (listeners.get(onChangeGroupListener).contains(g))
                onChangeGroupListener.onChangeGroup();
        }
    }
}
