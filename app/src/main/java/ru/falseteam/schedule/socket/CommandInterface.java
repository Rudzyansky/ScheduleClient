package ru.falseteam.schedule.socket;

import java.util.Map;

interface CommandInterface {
    String getName();

    void exec(Map<String, Object> map);
}
