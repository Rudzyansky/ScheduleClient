package ru.falseteam.schedule;


import com.vk.sdk.VKSdk;

import ru.falseteam.schedule.data.Data;
import ru.falseteam.schedule.data.DataLoader;
import ru.falseteam.schedule.socket.Worker;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
//        String[] t = VKUtil.getCertificateFingerprint(this, this.getPackageName());
        VKSdk.initialize(this);
//        if (!VKSdk.isLoggedIn()) {
//            Intent intent = new Intent(Application.this, LoginActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//        }

        DataLoader.load(getApplicationContext());
        Data.init(getApplicationContext());
        Worker.init(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        Worker.stop();
        super.onTerminate();
    }
}
