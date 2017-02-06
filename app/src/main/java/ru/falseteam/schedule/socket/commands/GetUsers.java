package ru.falseteam.schedule.socket.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ru.falseteam.schedule.data.MainData;
import ru.falseteam.schedule.serializable.User;
import ru.falseteam.vframe.redraw.Redrawer;
import ru.falseteam.vframe.socket.Container;
import ru.falseteam.vframe.socket.ProtocolAbstract;
import ru.falseteam.vframe.socket.SocketWorker;

public class GetUsers extends ProtocolAbstract {
    @SuppressWarnings("unchecked")
    @Override
    public void exec(Map<String, Object> map, SocketWorker worker) {
        MainData.setUsers((ArrayList<User>) map.get("users"));
        Redrawer.redraw();
    }

    public static Container getRequest() {
        Map<String, Object> map = new HashMap<>();
        map.put("command", "get_users");
        return new Container(GetUsers.class.getSimpleName(), map);
    }
}
