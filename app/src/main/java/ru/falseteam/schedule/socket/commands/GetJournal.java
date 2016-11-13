package ru.falseteam.schedule.socket.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.falseteam.schedule.data.MainData;
import ru.falseteam.schedule.serializable.JournalRecord;
import ru.falseteam.schedule.socket.CommandAbstract;

public class GetJournal extends CommandAbstract {

    public GetJournal() {
        super("get_journal");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void exec(Map<String, Object> map) {
        MainData.setJournal((List<JournalRecord>) map.get("journal"));
    }

    public static Map<String, Object> getRequest() {
        Map<String, Object> map = new HashMap<>();
        map.put("command", "get_journal");
        return map;
    }
}
