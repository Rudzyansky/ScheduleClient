package ru.falseteam.schedule;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;

import java.net.URL;

import ru.falseteam.schedule.socket.Worker;

import static com.vk.sdk.api.VKApiConst.FIELDS;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
//        String[] t = VKUtil.getCertificateFingerprint(this, this.getPackageName());
        // Логинимся в вк.
        VKSdk.initialize(this);
        if (!VKSdk.isLoggedIn()) {
            Intent intent = new Intent(Application.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        Worker.init(getApplicationContext());
        updateInfo();
    }

    @Override
    public void onTerminate() {
        Worker.stop();
        super.onTerminate();
    }

    public void updateInfo() {
        Data.init(getApplicationContext());
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
}
