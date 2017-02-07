package ru.falseteam.schedule.socket.commands;

import android.content.Intent;
import android.os.Build;

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
        Container c = new Container(Auth.class.getSimpleName(), true);
        c.data.put("token", token);
        c.data.put("app_version", StaticData.getClientVersion());
        c.data.put("sdk_version", Build.VERSION.SDK_INT);
        return c;
    }
}
