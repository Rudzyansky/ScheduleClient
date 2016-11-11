package ru.falseteam.schedule.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;

import ru.falseteam.schedule.R;
import ru.falseteam.schedule.listeners.Redrawer;
import ru.falseteam.schedule.serializable.Groups;
import ru.falseteam.schedule.socket.commands.GetTemplates;
import ru.falseteam.schedule.utils.BitmapUtils;

import static com.vk.sdk.api.VKApiConst.FIELDS;
import static ru.falseteam.schedule.data.Data.*;

public class DataLoader {
    public static void load(Context context) {
        Data.context = new WeakReference<>(context);

        // Static Data
        try {
            StaticData.clientVersion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        StaticData.hostname = context.getString(R.string.hostname);
        StaticData.publicPass = context.getString(R.string.public_pass);
        StaticData.portSchedule = context.getResources().getInteger(R.integer.port_schedule);
        StaticData.portUpdate = context.getResources().getInteger(R.integer.port_update);

        currentGroup = Groups.disconnected;
        preferences = context.getSharedPreferences("schedule", Context.MODE_PRIVATE);

        name = preferences.getString("name", "guest");
        userIconUrl = preferences.getString("userIconUrl", "");
        userIconPath = context.getApplicationInfo().dataDir + "/usericon.png";

        loadUserIcon();
        GetTemplates.load();

        Redrawer.redraw();
    }

    private static void loadUserIcon() {
        File file = new File(userIconPath);
        if (file.exists()) {
            try {
                FileInputStream fin = new FileInputStream(file);
                userIcon = BitmapFactory.decodeStream(fin);
                fin.close();
            } catch (IOException ignore) {
            }
        }
    }

    public static void vkUpdate() {
        VKApi.users().get(VKParameters.from(FIELDS, "photo_100")).executeWithListener(new VKRequest.VKRequestListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void onComplete(VKResponse response) {
                VKList<VKApiUserFull> full = (VKList<VKApiUserFull>) response.parsedModel;
                for (final VKApiUserFull user : full) {
                    setUsername(user.last_name + " " + user.first_name);
                    setUserIconUrl(user.photo_100);
                }
                super.onComplete(response);
            }
        });
    }

    private static void setUsername(String name) {
        if (!Data.name.equals(name)) {
            Data.name = name;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("name", name).apply();

            Redrawer.redraw();
        }
    }

    private static void setUserIconUrl(final String userIconUrl) {
        if (!Data.userIconUrl.equals(userIconUrl)) {
            Data.userIconUrl = userIconUrl;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("userIconUrl", userIconUrl).apply();
            setUserIcon();
        }
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
}
