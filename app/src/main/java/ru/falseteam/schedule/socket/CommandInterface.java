package ru.falseteam.schedule.socket;

import java.util.Map;

interface CommandInterface {
    String getName();

    void exec(Worker worker, Map<String, Object> map);
}
