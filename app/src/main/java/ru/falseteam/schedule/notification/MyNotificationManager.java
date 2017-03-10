package ru.falseteam.schedule.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;

import java.util.Calendar;
import java.util.List;

import ru.falseteam.schedule.R;
import ru.falseteam.schedule.serializable.Template;
import ru.falseteam.schedule.socket.Worker;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Prog on 21.02.17.
 */

public class MyNotificationManager {

    private static Notification notification;
    private static NotificationManager nm;
    private static Context context;

    public static void init(Context context) {
        MyNotificationManager.context = context;

        Worker.get().getSubscriptionManager().subscribeWithCache("GetTemplates");

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    while (Worker.get().getSubscriptionManager().getData("GetTemplates") == null) {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    Calendar c = Calendar.getInstance();
                    int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 2;
                    if (dayOfWeek == -1) dayOfWeek = 7;

//                    BitSet flag = new BitSet(3);
                    int week = (Calendar.getInstance().get(Calendar.WEEK_OF_YEAR) - 6);
                    for (Template t : ((List<Template>) Worker.get().getSubscriptionManager().getData("GetTemplates").get("templates")))
                        if (t.weekDay.id == dayOfWeek + 1 && (
                                (t.weeks.get(31) &&
                                        (t.weeks.get(30) || (t.weeks.get(29) && week % 2 == 1) || (!t.weeks.get(29) && week % 2 == 0))
                                ) || t.weeks.get(week - 1)
                        )) {
                            long curr = System.currentTimeMillis();
                            if (curr > t.lessonNumber.begin.getTime() && curr < t.lessonNumber.end.getTime())// {
                                clingNotify(t.lesson.name + " (" + t.lesson.audience + ")", t.lessonNumber.begin.toString() + " :: " + t.lessonNumber.end.toString());
//                                flag.set(0, true);
//                            } else if (curr < t.lessonNumber.end.getTime()) flag.set(1, true);
                        }
//                    if (!flag.get(1)) clingNotify("");
                }
            }
        }).start();
    }

    private static Notification.Builder builder() {
        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(R.drawable.ic_launcher);
        return builder;
    }

    public static void clingNotify(String title, String body) {
        notification = builder().setContentTitle(title).setContentText(body).build();
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        nm = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        nm.notify(1, notification);
    }

    public static void notify(String title, String body) {
        notification = builder().setContentTitle(title).setContentText(body).build();
        nm = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        nm.notify(1, notification);
    }
}
