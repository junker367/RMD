package src.rmd;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class CustomAlarm extends Activity {

  AlarmManager am;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
    setOneTimeAlarm();
  }

  public void setOneTimeAlarm() {
    Intent intent = new Intent(this, RMD_WakeUP_Alarms.class);
    PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
    am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (5 * 1000), pendingIntent);
  }

  public void setRepeatingAlarm() {
    Intent intent = new Intent(this, RMD_WakeUP_Alarms.class);
    PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), (5 * 1000), pendingIntent);
  }

}