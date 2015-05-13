package scu.edu.moodlogger;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

    public static final int NOTIFICATION_ID = 1;
    private Intent mainActivityIntent;
    private PendingIntent pendingIntent;
    private PendingIntent dismissPIntent;
    private PendingIntent snoozePIntent;
    private NotificationManager notificationManager;


    @Override
    public void onReceive(Context context, Intent intent) {

        notificationManager = (NotificationManager)
                context.getSystemService(context.NOTIFICATION_SERVICE);

        if (intent.getAction() != null) {
            // when user choose to dismiss the notification from notification drawer or android wear
            if (intent.getAction().equals("ACTION_DISMISS")) {
                Log.d("action", intent.getAction());
                notificationManager.cancel(NOTIFICATION_ID);
                Log.d("NOTIFICATION_ID", (String.valueOf(NOTIFICATION_ID)));

                // when user choose to snooze the notification from notification drawer or android wear
            } else if (intent.getAction().equals("ACTION_SNOOZE")) {
                Log.d("action", intent.getAction());
                notificationManager.cancel(NOTIFICATION_ID);

                Intent notificationReceiver = new Intent(context, AlarmBroadcastReceiver.class);
                PendingIntent pendingNotification = PendingIntent.getBroadcast(context,
                        2, notificationReceiver, 0);

                //If user chooses to snooze then set another alarm in 1 hour to notify again
                //For demo set to 20 seconds= 20*1000
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        SystemClock.elapsedRealtime() +
                                60 * 60 * 1000, pendingNotification); //20 sec for demo
                // 60 * 60 * 1000, pendingNotification);

            }
        } else {
            createNotification(context);
        }

    }

    //creates a notification for handheld and non-handheld devices
    public void createNotification(Context context) {

        int notificationId = 1;

        String tickerText = context.getResources().getString(
                R.string.ticker_text);
        String title = context.getResources().getString(R.string.app_name);

        mainActivityIntent = new Intent(context, MainActivity.class);
        mainActivityIntent.setAction("ACTION_MAIN");
        pendingIntent = PendingIntent.getActivity(context, 0,
                mainActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Intent for the dismiss action
        Intent dismissIntent = new Intent();
        dismissIntent.setAction("ACTION_DISMISS");
        dismissPIntent = PendingIntent.getBroadcast(context, 1,
                dismissIntent, 0);

        // Intent for the snooze action
        Intent snoozeIntent = new Intent();
        snoozeIntent.setAction("ACTION_SNOOZE");
        snoozePIntent = PendingIntent.getBroadcast(context, 1,
                snoozeIntent, 0);


        // Build notification
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
                context).setTicker(tickerText)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_action_camera)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(tickerText)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.ic_action_cancel, "Dismiss", dismissPIntent)
                .addAction(R.drawable.ic_action_alarms, "Snooze", snoozePIntent);

        notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager
                .notify(notificationId, notificationBuilder.build());
    }
}
