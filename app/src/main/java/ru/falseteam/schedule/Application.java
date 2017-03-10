package ru.falseteam.schedule;


import android.content.Intent;

import com.vk.sdk.VKSdk;

import ru.falseteam.schedule.data.DataLoader;
import ru.falseteam.schedule.service.ScheduleService;
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
        Worker.get().getSubscriptionManager().setCacheDir(getApplicationInfo().dataDir + "/cache");
        Intent service = new Intent();
        service.setAction(ScheduleService.INTENT_NAME);
        this.sendBroadcast(service);
//        MyNotificationManager.init(this);
    }

    @Override
    public void onTerminate() {
        Worker.get().stop();
        VFrame.stop();
        super.onTerminate();
    }
}
