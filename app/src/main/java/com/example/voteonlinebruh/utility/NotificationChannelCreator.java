package com.example.voteonlinebruh.utility;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

@SuppressWarnings("WeakerAccess")
public class NotificationChannelCreator extends Application {

  public static final String CHANNEL_ID = "Vote.Online";

  @Override
  public void onCreate() {
    super.onCreate();
    crateNotificationChannel();
  }

  private void crateNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      NotificationChannel serviceChannel =
          new NotificationChannel(
              CHANNEL_ID, "Submitting Vote", NotificationManager.IMPORTANCE_HIGH);
      NotificationManager manager = getSystemService(NotificationManager.class);
      manager.createNotificationChannel(serviceChannel);
    }
  }
}
