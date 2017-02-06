package ru.falseteam.schedule.socket.commands;

import android.content.Intent;
import android.os.Build;

import java.util.HashMap;
import java.util.Map;

import ru.falseteam.schedule.UpdateActivity;
import ru.falseteam.schedule.data.MainData;
import ru.falseteam.schedule.data.StaticData;
import ru.falseteam.schedule.serializable.Groups;
import ru.falseteam.schedule.socket.Worker;
import ru.falseteam.vframe.socket.Container;
import ru.falseteam.vframe.socket.ProtocolAbstract;
import ru.falseteam.vframe.socket.SocketWorker;

public class Auth extends ProtocolAbstract {
    @Override
    public void exec(Map<String, Object> map, SocketWorker worker) {
        MainData.setCurrentGroup(Groups.valueOf(map.get("permissions").toString()));
        if (!map.get("version").toString().equals(StaticData.getClientVersion())) {
            Intent intent = new Intent(Worker.get().getContext(), UpdateActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("version", map.get("version").toString());
            Worker.get().getContext().startActivity(intent);
        }
    }

    public static Container getRequest(String token) {
        Map<String, Object> map = new HashMap<>();
        map.put("command", "auth");
        map.put("token", token);
        map.put("app_version", StaticData.getClientVersion());
        map.put("sdk_version", Build.VERSION.SDK_INT);
        return new Container(Auth.class.getSimpleName(), map);
    }
}
