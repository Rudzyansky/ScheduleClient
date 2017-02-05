package ru.falseteam.schedule.socket.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.falseteam.schedule.data.MainData;
import ru.falseteam.schedule.listeners.Redrawer;
import ru.falseteam.schedule.serializable.JournalRecord;
import ru.falseteam.vframe.socket.ClientProtocolAbstract;
import ru.falseteam.vframe.socket.ClientSocketWorker;
import ru.falseteam.vframe.socket.Container;

public class GetJournal extends ClientProtocolAbstract {
    @SuppressWarnings("unchecked")
    @Override
    public void exec(Map<String, Object> map, ClientSocketWorker worker) {
        MainData.setJournal((List<JournalRecord>) map.get("journal"));
        Redrawer.redraw();
    }

    public static Container getRequest() {
        Map<String, Object> map = new HashMap<>();
        map.put("command", "get_journal");
        return new Container(GetJournal.class.getSimpleName(), map);
    }
}
