package ru.falseteam.schedule.socket.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ru.falseteam.schedule.listeners.Redrawer;
import ru.falseteam.schedule.serializable.User;
import ru.falseteam.schedule.socket.CommandAbstract;

public class GetUsers extends CommandAbstract {
    public GetUsers() {
        super("get_users");
    }

    public static ArrayList<User> users;

    @SuppressWarnings("unchecked")
    @Override
    public void exec(Map<String, Object> map) {
        users = (ArrayList<User>) map.get("users");
        Redrawer.redraw();
    }

    public static Map<String, Object> getRequest() {
        Map<String, Object> map = new HashMap<>();
        map.put("command", "get_users");
        return map;
    }
}
