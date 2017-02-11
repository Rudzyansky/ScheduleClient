package ru.falseteam.schedule;


import com.vk.sdk.VKSdk;

import ru.falseteam.schedule.data.DataLoader;
import ru.falseteam.schedule.socket.Worker;
import ru.falseteam.vframe.VFrame;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        VFrame.init();
//        String[] t = VKUtil.getCertificateFingerprint(this, this.getPackageName());
        VKSdk.initialize(this);

        DataLoader.init(getApplicationContext());
        Worker.init(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        Worker.get().stop();
        VFrame.stop();
        super.onTerminate();
    }
}
