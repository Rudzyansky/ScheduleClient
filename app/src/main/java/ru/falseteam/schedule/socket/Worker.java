package ru.falseteam.schedule.socket;

import android.content.Context;

import com.vk.sdk.VKAccessToken;

import ru.falseteam.schedule.R;
import ru.falseteam.schedule.data.StaticData;
import ru.falseteam.schedule.serializable.Groups;
import ru.falseteam.schedule.socket.commands.AccessDenied;
import ru.falseteam.schedule.socket.commands.Auth;
import ru.falseteam.schedule.socket.commands.CountPresented;
import ru.falseteam.schedule.socket.commands.ToastShort;
import ru.falseteam.vframe.socket.SocketWorker;
import ru.falseteam.vframe.socket.VFKeystore;

public class Worker extends SocketWorker<Groups> implements SocketWorker.OnChangePermissionListener<Groups> {

    private Context context;

    public Context getContext() {
        return context;
    }

    private static Worker worker;

    private Worker(Context context) {
        super(StaticData.getHostname(), StaticData.getPortSchedule(),
                new VFKeystore(context.getResources().openRawResource(R.raw.keystore),
                        StaticData.getPublicPass()), Groups.class, Groups.disconnected, Groups.guest);
        this.context = context;
        addProtocol(new AccessDenied());
        addProtocol(new Auth());
        addProtocol(new ToastShort());
        addProtocol(new CountPresented());
        worker = this;
        worker.start();
        worker.addOnConnectionChangeStateListener(this);
    }

    // TODO: 05.02.17 fix something ..
    public static void init(Context context) {
        new Worker(context);
    }

    public static Worker get() {
        return worker;
    }

    @Override
    public void onChangePermission(Groups permission) {
        if (permission.equals(Groups.guest)) {
            // авторизуемся на сервере с косты
            while (VKAccessToken.currentToken() == null) try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            send(Auth.getRequest(VKAccessToken.currentToken().accessToken));
        }
    }
}
