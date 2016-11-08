package ru.falseteam.schedule.socket.commands;

import android.content.Intent;

import java.util.HashMap;
import java.util.Map;

import ru.falseteam.schedule.Application;
import ru.falseteam.schedule.Data;
import ru.falseteam.schedule.UpdateActivity;
import ru.falseteam.schedule.listeners.OnChangeGroup;
import ru.falseteam.schedule.serializable.Groups;
import ru.falseteam.schedule.socket.CommandAbstract;
import ru.falseteam.schedule.socket.Worker;

public class Auth extends CommandAbstract {
    public Auth() {
        super("auth");
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public void exec(Map<String, Object> map) {
        Data.setCurrentGroup(Groups.valueOf(map.get("group").toString()));
        if (!map.get("version").toString().equals(Data.getClientVersion())) {
            Intent intent = new Intent(Worker.get().getContext(), UpdateActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("version", map.get("version").toString());
            Worker.get().getContext().startActivity(intent);
        }
    }

    public static Map<String, Object> getRequest(String token) {
        Map<String, Object> map = new HashMap<>();
        map.put("command", "auth");
        map.put("token", token);
        return map;
    }
}
