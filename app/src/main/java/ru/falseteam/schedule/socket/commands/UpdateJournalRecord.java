package ru.falseteam.schedule.socket.commands;

import ru.falseteam.schedule.serializable.JournalRecord;
import ru.falseteam.vframe.socket.Container;

public class UpdateJournalRecord {
    public static Container getRequest(JournalRecord record) {
        Container c = new Container(UpdateJournalRecord.class.getSimpleName(), true);
        c.data.put("record", record);
        return c;
    }
}
