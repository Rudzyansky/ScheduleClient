package ru.falseteam.schedule.data;

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
import java.io.IOException;
import java.net.URL;

import ru.falseteam.schedule.listeners.Redrawer;
import ru.falseteam.schedule.utils.BitmapUtils;

import static com.vk.sdk.api.VKApiConst.FIELDS;

public class VkData {
    private static String name;
    private static Bitmap userIcon;
    private static String userIconUrl;
    private static String userIconPath;

    private static SharedPreferences preferences;

    static void init(Context context) {
        preferences = context.getSharedPreferences("VkData", Context.MODE_PRIVATE);

        name = preferences.getString("name", "guest");
        userIconUrl = preferences.getString("userIconUrl", "");
        userIconPath = context.getApplicationInfo().dataDir + "/usericon.png";

        loadUserIcon();
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
        if (!VkData.name.equals(name)) {
            VkData.name = name;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("name", name).apply();

            Redrawer.redraw();
        }
    }

    private static void setUserIconUrl(final String userIconUrl) {
        if (!VkData.userIconUrl.equals(userIconUrl)) {
            VkData.userIconUrl = userIconUrl;
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


    public static String getName() {
        return name;
    }

    public static Bitmap getUserIcon() {
        return userIcon;
    }
}
