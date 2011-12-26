package src.rmd;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class RMD_Ip_Service extends Service {

  public static final String BBDDUri = "http://rmdApplication.com/PUT";
  
  private Timer timer = new Timer();
  private static final long UPDATE_INTERVAL = 5000;
  private static String ipBefore="";
  
  
  public IBinder onBind(Intent intent) {
    return null;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    _startService();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    _shutdownService();
  }

  private void _startService() {
    timer.scheduleAtFixedRate(new TimerTask() {
      public void run() {
        _checkIpAddress();
      }
    }, 0, UPDATE_INTERVAL);
    Log.i(getClass().getSimpleName(), "Timer started!!!");
  }

  
  private void _checkIpAddress() {

    Log.i(getClass().getSimpleName(), "background task - start");

    String ipNow = getipAddress();

    if (ipNow == null) ipNow = "";

    if (ipBefore == null) ipBefore = "";

    if ( !(ipNow.equals(ipBefore))) {
      ipBefore=ipNow;
      updateIpRemote();
    }
    
    Log.i(getClass().getSimpleName(), "background task - end");
  }

  private void updateIpRemote() {
    // TODO se conecta aqui a un webservice de mi servidor para actualizar su ip.  Comprobar y test.
  }

  public static String getipAddress() {
    try {
      for (Enumeration en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
        NetworkInterface intf = (NetworkInterface) en.nextElement();
        for (Enumeration enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
          InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
          if ( !inetAddress.isLoopbackAddress()) {
            String ipaddress = inetAddress.getHostAddress().toString();
            Log.e("ip address", "" + ipaddress);
            return ipaddress;
          }
        }
      }
    } catch (SocketException ex) {
      Log.e("Socket exception in GetIP Address of Utilities", ex.toString());
    }
    return "";
  }

  private void _shutdownService() {
    if (timer != null)
      timer.cancel();
    Log.i(getClass().getSimpleName(), "Timer stopped!!!");
  }

}