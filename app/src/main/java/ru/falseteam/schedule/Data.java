package ru.falseteam.schedule;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.net.URL;

import ru.falseteam.schedule.listeners.OnChangeGroup;
import ru.falseteam.schedule.listeners.Redrawer;
import ru.falseteam.schedule.serializable.Groups;
import ru.falseteam.schedule.socket.commands.GetTemplates;
import ru.falseteam.schedule.utils.BitmapUtils;

import static com.vk.sdk.api.VKApiConst.FIELDS;

public class Data {
    private static String clientVersion;
    private static String hostname;
    private static String publicPass;
    private static int portSchedule;
    private static int portUpdate;

    private static Groups currentGroup = Groups.disconnected;
    private static String name;
    private static Bitmap userIcon;
    private static String userIconUrl;
    private static String userIconPath;

    private static SharedPreferences preferences;

    private static WeakReference<Context> context;

    static void init(Context context) {
        Data.context = new WeakReference<Context>(context);
        try {
            clientVersion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception ignore) {
        }
        hostname = context.getString(R.string.hostname);
        publicPass = context.getString(R.string.public_pass);
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
        GetTemplates.load();
    }

    static void vkUpdate() {
        VKApi.users().get(VKParameters.from(FIELDS, "photo_100")).executeWithListener(new VKRequest.VKRequestListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void onComplete(VKResponse response) {
                VKList<VKApiUserFull> full = (VKList<VKApiUserFull>) response.parsedModel;
                for (final VKApiUserFull user : full) {
                    Data.setName(user.last_name + " " + user.first_name);
                    Data.setUserIconUrl(user.photo_100);
                }
                super.onComplete(response);
            }
        });
    }

    public static Context getContext() {
        return context.get();
    }

    public static int getPortSchedule() {
        return portSchedule;
    }

    public static int getPortUpdate() {
        return portUpdate;
    }

    public static String getPublicPass() {
        return publicPass;
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
        OnChangeGroup.change();
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
