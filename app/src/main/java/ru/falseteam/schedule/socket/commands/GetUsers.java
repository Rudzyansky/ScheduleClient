package ru.falseteam.schedule.socket.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ru.falseteam.schedule.data.MainData;
import ru.falseteam.schedule.listeners.Redrawer;
import ru.falseteam.schedule.serializable.User;
import ru.falseteam.vframe.socket.ClientProtocolAbstract;
import ru.falseteam.vframe.socket.ClientSocketWorker;
import ru.falseteam.vframe.socket.Container;

public class GetUsers extends ClientProtocolAbstract {
    @SuppressWarnings("unchecked")
    @Override
    public void exec(Map<String, Object> map, ClientSocketWorker worker) {
        MainData.setUsers((ArrayList<User>) map.get("users"));
        Redrawer.redraw();
    }

    public static Container getRequest() {
        Map<String, Object> map = new HashMap<>();
        map.put("command", "get_users");
        return new Container(GetUsers.class.getSimpleName(), map);
    }
}
