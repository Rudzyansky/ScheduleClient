package ru.falseteam.schedule;


import com.vk.sdk.VKSdk;

import ru.falseteam.schedule.data.DataLoader;
import ru.falseteam.schedule.socket.Worker;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
//        String[] t = VKUtil.getCertificateFingerprint(this, this.getPackageName());
        VKSdk.initialize(this);

        DataLoader.init(getApplicationContext());
        Worker.init(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        Worker.stop();
        super.onTerminate();
    }
}
