package ru.falseteam.schedule.socket.commands;

import android.widget.Toast;

import java.util.Map;

import ru.falseteam.schedule.socket.CommandAbstract;
import ru.falseteam.schedule.socket.Worker;

public class ChangePair extends CommandAbstract {
    public ChangePair() {
        super("change_pair");
    }

    @Override
    public void exec(Map<String, Object> map) {
        Toast.makeText(Worker.get().context, String.valueOf(map.get("result")), Toast.LENGTH_SHORT).show();
    }
}
