package ru.falseteam.schedule;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;

import ru.falseteam.schedule.redraw.Redrawer;
import ru.falseteam.schedule.serializable.Groups;
import ru.falseteam.schedule.utils.BitmapUtils;

public class Data {
    private static String clientVersion;
    private static String hostname;
    private static int portSchedule;
    private static int portUpdate;

    private static Groups currentGroup = Groups.disconnected;
    private static String name;
    private static Bitmap userIcon;
    private static String userIconUrl;
    private static String userIconPath;

    private static SharedPreferences preferences;

    static void init(Context context) {
        try {
            clientVersion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception ignore) {
        }
        hostname = context.getString(R.string.hostname);
        portSchedule = context.getResources().getInteger(R.integer.port_schedule);
        portUpdate = context.getResources().getInteger(R.integer.port_update);

        preferences = context.getSharedPreferences("schedule", Context.MODE_PRIVATE);
        name = preferences.getString("name", "guest");
        userIconUrl = preferences.getString("userIconUrl", "");
        Redrawer.redraw();

        userIconPath = context.getApplicationInfo().dataDir + "/usericon.png";
        File file = new File(userIconPath);
        if (file.exists()) {
            try {
                FileInputStream fin = new FileInputStream(file);
                userIcon = BitmapFactory.decodeStream(fin);
                fin.close();
            } catch (Exception ignore) {
            }
        }
    }

    public static int getPortSchedule() {
        return portSchedule;
    }

    public static int getPortUpdate() {
        return portUpdate;
    }

    public static String getHostname() {
        return hostname;
    }

    public static String getClientVersion() {
        return clientVersion;
    }

    public static Groups getCurrentGroup() {
        return currentGroup;
    }

    public static void setCurrentGroup(Groups currentGroup) {
        Data.currentGroup = currentGroup;
        Redrawer.redraw();
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        if (!Data.name.equals(name)) {
            Data.name = name;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("name", name).apply();
            Redrawer.redraw();
        }
    }

    static Bitmap getUserIcon() {
        return userIcon;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void setUserIcon() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final URL url = new URL(userIconUrl);
                    userIcon = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    userIcon = BitmapUtils.getCircleBitmap(userIcon);
                    Redrawer.redraw();
                    try {
                        File file = new File(userIconPath);
                        file.createNewFile();
                        FileOutputStream fout = new FileOutputStream(userIconPath);
                        userIcon.compress(Bitmap.CompressFormat.PNG, 100, fout);
                        fout.flush();
                        fout.close();
                    } catch (Exception ignore) {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void setUserIconUrl(final String userIconUrl) {
        if (!Data.userIconUrl.equals(userIconUrl)) {
            Data.userIconUrl = userIconUrl;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("userIconUrl", name).apply();
            setUserIcon();
        }
    }
}
