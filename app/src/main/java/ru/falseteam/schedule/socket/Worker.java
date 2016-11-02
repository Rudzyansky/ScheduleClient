package ru.falseteam.schedule.socket;

import android.content.Context;

import com.vk.sdk.VKAccessToken;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import ru.falseteam.schedule.Data;
import ru.falseteam.schedule.serializable.Groups;
import ru.falseteam.schedule.socket.commands.AccessDenied;
import ru.falseteam.schedule.socket.commands.Auth;
import ru.falseteam.schedule.socket.commands.GetUsers;
import ru.falseteam.schedule.socket.commands.ToastShort;
import ru.falseteam.schedule.socket.commands.GetPairs;

public class Worker implements Runnable {

    private Socket socket;
    private ObjectOutputStream out;
    private Context context;

    private boolean interrupt = false;

    private static Map<String, CommandInterface> protocols;

    static {
        protocols = new HashMap<>();

        addCommand(new AccessDenied());
        addCommand(new Auth());
        addCommand(new GetPairs());
        addCommand(new GetUsers());
        addCommand(new ToastShort());
    }

    public Context getContext() {
        return context;
    }

    private static void addCommand(CommandInterface c) {
        protocols.put(c.getName(), c);
    }

    private static Worker worker;

    private Worker(Context context) {
        this.context = context;
        new Thread(this, "Socket worker thread").start();
    }

    public static void init(Context context) {
        worker = new Worker(context);
    }

    public static void stop() {
        worker.interrupt = true;
        worker.disconnect();
        worker = null;
    }

    public static Worker get() {
        return worker;
    }

    public boolean send(Map<String, Object> map) {
        try {
            // Эта строчка появилась здесь после двух часов мучений
            out.reset();
            // ----------------------------------------------------
            out.writeObject(map);
            out.flush();
        } catch (Exception ignore) {
            return false;
        }
        return true;
    }

    private void disconnect() {
        Data.setCurrentGroup(Groups.disconnected);
        try {
            socket.close();
        } catch (Exception ignore) {
        }
    }

    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignore) {
        }
    }

    @SuppressWarnings({"InfiniteLoopStatement", "unchecked"})
    @Override
    public void run() {
        while (!interrupt) {
            try {
                socket = new Socket(Data.getHostname(), Data.getPortSchedule());
                out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                onConnect();
                while (true) {
                    Object o = in.readObject();
                    if (!(o instanceof Map)) throw new Exception("not Map");
                    Map<String, Object> map = (Map<String, Object>) o;
                    if (!map.containsKey("command")) continue;
                    protocols.get(map.get("command").toString()).exec(map);
                }
            } catch (Exception ignore) {
            }
            disconnect();
            sleep(2000);
        }
    }

    private void onConnect() {
        Data.setCurrentGroup(Groups.guest);
        // авторизуемся на сервере с косты
        send(Auth.getRequest(VKAccessToken.currentToken().accessToken));
    }
}
