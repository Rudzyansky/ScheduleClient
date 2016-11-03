package ru.falseteam.schedule.socket.commands;

import java.util.Map;

import ru.falseteam.schedule.socket.CommandAbstract;
import ru.falseteam.schedule.socket.Worker;

/**
 * @author Vladislav Sumin
 * @version 1.0
 */
public class Ping extends CommandAbstract {
    public Ping() {
        super("ping");
    }

    @Override
    public void exec(Map<String, Object> map) {
        Worker.get().send(map);
    }
}
