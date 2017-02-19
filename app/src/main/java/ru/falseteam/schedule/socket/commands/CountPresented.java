package ru.falseteam.schedule.socket.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ru.falseteam.schedule.serializable.UserPresented;
import ru.falseteam.vframe.redraw.Redrawer;
import ru.falseteam.vframe.socket.Container;
import ru.falseteam.vframe.socket.ProtocolAbstract;
import ru.falseteam.vframe.socket.SocketWorker;

public class CountPresented extends ProtocolAbstract {

    public static List<UserPresented> ups = new ArrayList<>();
    public static int count = 0;

    @Override
    public void exec(final Map<String, Object> map, SocketWorker worker) {
        ups = (List<UserPresented>) map.get("users");
        count = (int) map.get("count");
        Redrawer.redraw();
    }

    public static Container getRequest() {
        return new Container(CountPresented.class.getSimpleName(), true);
    }
}
