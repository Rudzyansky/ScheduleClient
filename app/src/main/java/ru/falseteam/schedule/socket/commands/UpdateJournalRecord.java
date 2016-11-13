package ru.falseteam.schedule.socket.commands;

import java.util.HashMap;
import java.util.Map;

import ru.falseteam.schedule.serializable.JournalRecord;

public class UpdateJournalRecord {
    public static Map<String, Object> getRequest(JournalRecord record) {
        Map<String, Object> request = new HashMap<>();
        request.put("command", "update_journal_record");
        request.put("record", record);
        return request;
    }
}
