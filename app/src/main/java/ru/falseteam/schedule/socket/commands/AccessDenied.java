package ru.falseteam.schedule.socket.commands;

import android.widget.Toast;

import java.util.Map;

import ru.falseteam.schedule.socket.CommandAbstract;
import ru.falseteam.schedule.socket.Worker;

public class AccessDenied extends CommandAbstract {
    public AccessDenied() {
        super("forbidden");
    }

    @Override
    public void exec(Map<String, Object> map) {
        Toast.makeText(Worker.get().context, "Access Denied: \n" + map.get("command"), Toast.LENGTH_SHORT).show();
    }
}
