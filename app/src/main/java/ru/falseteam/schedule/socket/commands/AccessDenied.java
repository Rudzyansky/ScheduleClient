package ru.falseteam.schedule.socket.commands;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import java.util.Map;

import ru.falseteam.schedule.socket.CommandAbstract;
import ru.falseteam.schedule.socket.Worker;

public class AccessDenied extends CommandAbstract {
    public AccessDenied() {
        super("forbidden");
    }

    @Override
    public void exec(final Map<String, Object> map) {
        new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message inputMessage) {
                Toast.makeText(Worker.get().getContext(), "Access Denied: \n" + map.get("forbidden"), Toast.LENGTH_LONG).show();
            }
        }.obtainMessage().sendToTarget();
    }
}
