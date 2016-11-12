package ru.falseteam.schedule.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

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
import static ru.falseteam.schedule.data.StaticData.TAG;

/**
 * Содержит данные полученные от vk.com
 *
 * @author Sumin Vladislav
 * @version 1.0
 */
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
        userIconPath = context.getApplicationInfo().dataDir + "/cache/usericon.png";

        loadUserIcon();
    }

    public static void vkUpdate() {
        VKApi.users().get(VKParameters.from(FIELDS, "photo_100")).
                executeWithListener(new VKRequest.VKRequestListener() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public void onComplete(VKResponse response) {
                        VKList<VKApiUserFull> users = (VKList<VKApiUserFull>) response.parsedModel;
                        vkResponseParse(users);
                        super.onComplete(response);
                    }
                });
    }

    private static void vkResponseParse(final VKList<VKApiUserFull> users) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "Start parsing vk response");
                for (final VKApiUserFull user : users) {
                    updateUsername(user.last_name + " " + user.first_name);
                    updateUserIconUrl(user.photo_100);
                }
                Log.d(TAG, "Finish parsing vk response");
            }
        }, "Vk update thread").start();

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

    private static void updateUsername(String name) {
        if (!VkData.name.equals(name)) {
            VkData.name = name;

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("name", name).apply();

            Log.d(TAG, "Update vk username");

            Redrawer.redraw();
        }
    }

    private static void updateUserIconUrl(final String userIconUrl) {
        if (!VkData.userIconUrl.equals(userIconUrl)) {
            VkData.userIconUrl = userIconUrl;

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("userIconUrl", userIconUrl).apply();

            Log.d(TAG, "Update user icon URL");

            setUserIcon();
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void setUserIcon() {
        try {
            final URL url = new URL(userIconUrl);
            userIcon = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            userIcon = BitmapUtils.getCircleBitmap(userIcon);
            Redrawer.redraw();

            Log.d(TAG, "New vk user icon loaded.");

            File file = new File(userIconPath);
            file.createNewFile();
            FileOutputStream fout = new FileOutputStream(userIconPath);
            userIcon.compress(Bitmap.CompressFormat.PNG, 100, fout);
            fout.flush();
            fout.close();
            Log.d(TAG, "New vk user icon saved to cache");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    // Гетеры.
    public static String getName() {
        return name;
    }

    public static Bitmap getUserIcon() {
        return userIcon;
    }
}
