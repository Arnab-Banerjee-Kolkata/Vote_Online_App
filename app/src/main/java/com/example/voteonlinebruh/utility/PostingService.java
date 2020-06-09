package com.example.voteonlinebruh.utility;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.voteonlinebruh.R;
import com.example.voteonlinebruh.api.PublicAPICall;

import static com.example.voteonlinebruh.utility.NotificationChannelCreator.CHANNEL_ID;

public class PostingService extends Service {

  public static final String ACTION_START_SERVICE = "START_SERVICE";
  public static final String ACTION_STOP_SERVICE = "STOP_SERVICE";

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    String action = intent.getStringExtra("action");
    Log.d("action", action);
    if (ACTION_START_SERVICE.equalsIgnoreCase(action)) {
      Notification notification =
          new NotificationCompat.Builder(this, CHANNEL_ID)
              .setContentTitle("Submitting Vote")
              .setContentText("Your vote is being submitted as 'NOTA'")
              .setSmallIcon(R.drawable.ic_info_outline_black_24dp)
              .build();
      startForeground(1, notification);
      new PublicAPICall().storeVote(intent.getStringExtra("boothId"), "NOTA", this, true);
    } else {
      Notification notification1 =
          new NotificationCompat.Builder(this, CHANNEL_ID)
              .setContentTitle("Vote Submitted")
              .setContentText(
                  intent.getBooleanExtra("status", false)
                      ? "Your vote was posted as NOTA"
                      : "Could not post vote!")
              .setSmallIcon(R.drawable.ic_info_outline_black_24dp)
              .build();
      NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
      notificationManager.notify(2, notification1);
      stopSelf();
    }
    return START_NOT_STICKY;
  }
}
