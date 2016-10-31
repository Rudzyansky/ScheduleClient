package ru.falseteam.schedule.socket.commands;

import android.content.Intent;

import java.util.Map;

import ru.falseteam.schedule.Application;
import ru.falseteam.schedule.Data;
import ru.falseteam.schedule.UpdateActivity;
import ru.falseteam.schedule.socket.CommandAbstract;
import ru.falseteam.schedule.socket.Worker;

public class Auth extends CommandAbstract {
    public Auth() {
        super("auth");
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public void exec(Worker worker, Map<String, Object> map) {
        Data.setCurrentGroup(Data.Groups.valueOf(map.get("group").toString()));
        if (!map.get("version").toString().equals(Data.getClientVersion())) {
            Intent intent = new Intent(worker.context, UpdateActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("version", map.get("version").toString());
            worker.context.startActivity(intent);
        }
    }
}
