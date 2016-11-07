package ru.falseteam.schedule.socket;

import android.content.Context;

import com.vk.sdk.VKAccessToken;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import ru.falseteam.schedule.Data;
import ru.falseteam.schedule.R;
import ru.falseteam.schedule.serializable.Groups;
import ru.falseteam.schedule.socket.commands.AccessDenied;
import ru.falseteam.schedule.socket.commands.Auth;
import ru.falseteam.schedule.socket.commands.GetLessonNumbers;
import ru.falseteam.schedule.socket.commands.GetTemplates;
import ru.falseteam.schedule.socket.commands.GetUsers;
import ru.falseteam.schedule.socket.commands.GetWeekDays;
import ru.falseteam.schedule.socket.commands.Ping;
import ru.falseteam.schedule.socket.commands.ToastShort;
import ru.falseteam.schedule.socket.commands.GetLessons;

public class Worker implements Runnable {

    private SSLSocket socket;
    private ObjectOutputStream out;
    private Context context;

    private boolean interrupt = false;

    private static Map<String, CommandInterface> protocols;

    static {
        protocols = new HashMap<>();

        addCommand(new AccessDenied());
        addCommand(new Ping());
        addCommand(new Auth());
        addCommand(new GetLessons());
        addCommand(new GetUsers());
        addCommand(new ToastShort());
        addCommand(new GetTemplates());
        addCommand(new GetWeekDays());
        addCommand(new GetLessonNumbers());
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

    public static boolean sendFromMainThread(final Map<String, Object> map) {
        final boolean[] output = new boolean[1];
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Эта строчка появилась здесь после двух часов мучений
                    worker.out.reset();
                    // ----------------------------------------------------
                    worker.out.writeObject(map);
                    worker.out.flush();
                    output[0] = true;
                } catch (Exception ignore) {
                    output[0] = false;
                }
            }
        }).start();
        return output[0];
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

    private void initSSL() {
        try {
            String algorithm = KeyManagerFactory.getDefaultAlgorithm();

            KeyStore ks = KeyStore.getInstance("BKS");
            ks.load(context.getResources().openRawResource(R.raw.keystore),
                    Data.getPublicPass().toCharArray());

            TrustManagerFactory tmf = TrustManagerFactory.getInstance(algorithm);
            tmf.init(ks);

            SSLContext sc = SSLContext.getInstance("TLS");
            TrustManager[] trustManagers = tmf.getTrustManagers();
            sc.init(null, trustManagers, null);

            SSLSocketFactory ssf = sc.getSocketFactory();
            socket = (SSLSocket) ssf.createSocket(Data.getHostname(), Data.getPortSchedule());

            socket.setEnabledCipherSuites(new String[]{"TLS_RSA_WITH_AES_256_CBC_SHA"});
            socket.setEnabledProtocols(new String[]{"TLSv1.2"});
            socket.setEnableSessionCreation(true);
            socket.setUseClientMode(true);
            socket.startHandshake();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings({"InfiniteLoopStatement", "unchecked"})
    @Override
    public void run() {
        while (!interrupt) {
            try {
                initSSL();
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
