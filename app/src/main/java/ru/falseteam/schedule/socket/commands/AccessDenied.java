package ru.falseteam.schedule.socket.commands;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import java.util.Map;

import ru.falseteam.schedule.socket.Worker;
import ru.falseteam.vframe.socket.ClientProtocolAbstract;
import ru.falseteam.vframe.socket.ClientSocketWorker;

public class AccessDenied extends ClientProtocolAbstract {
    @Override
    public void exec(final Map<String, Object> map, ClientSocketWorker worker) {
        new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message inputMessage) {
                Toast.makeText(Worker.get().getContext(), "Access Denied: \n" + map.get("forbidden"), Toast.LENGTH_LONG).show();
            }
        }.obtainMessage().sendToTarget();
    }
}
