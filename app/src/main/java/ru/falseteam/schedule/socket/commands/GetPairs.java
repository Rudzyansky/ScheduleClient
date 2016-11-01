package ru.falseteam.schedule.socket.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ru.falseteam.schedule.redraw.Redrawer;
import ru.falseteam.schedule.socket.CommandAbstract;
import ru.falseteam.schedule.serializable.Pair;

public class GetPairs extends CommandAbstract {
    public GetPairs() {
        super("get_pairs");
    }

    public static ArrayList<Pair> pairs = new ArrayList<>();

    @SuppressWarnings("unchecked")
    @Override
    public void exec(Map<String, Object> map) {
        pairs = (ArrayList<Pair>) map.get("pairs");
        Redrawer.redraw();
    }

    public static Map<String, Object> getRequest() {
        Map<String, Object> map = new HashMap<>();
        map.put("command", "get_pairs");
        return map;
    }
}
