/*
  Funcionalidades:
    servicio que actualice su ip en bbdd remota
    servicio que detecta si la aplicacion está apagada y la activa
    donde está!!! enciende timbre y 
    activar o desactivar el servicio desde la web
    seguridad whereis. Si está en movimento, síguelo. saca foto, consigue sacar foto (vibra
    borrame todo: backup de contactos y sms.
    mandar sms desde el movil
    leer sms desde la web
    interaccion tipo chat para mandar mensajicos
    servicio que dete
*/

/*
 Arquitectura:
 Cuando se instala la primera vez y se ejecuta, se programa el gestor de alarmas con CustomAlarm. Cuando al gestor le toca, llamará a TimeAlarm. Esto lo hace siempre!!!!
 Time alarm se encarga de chequear que el service y la app están up. Si están down, pues para up.
 Service chequea la ip del device. Si no coincide con la anterior que tenía, actualiza en servidor remoto mediante invocacion del servicio web remoto.
 App recibe todas las peticiones y las procesa, si las hay, gracias a su server integrado.
 
 El móvil siempre hace lo que le pidan desde la llamada. Deberá recibir un parámetro auth que será encargado de si la petición es o no correcta. Ese parametro se lo da el servidor.
 
 ¿si la app se desinstala, que pasa con el am?
 ¿si la app se para, que pasa con el am?
  
*/

package src.rmd;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;

public class RMDActivity extends Activity {

  public int batteryLevel;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    batteryLevel();
    Server server = new Server(SERVERPORT);
    server.setHandler(handler);
    try {
      server.start();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static final int SERVERPORT = 1234;

  Handler handler = new AbstractHandler() {
    public void handle(String target, Request request, HttpServletRequest MainRequestObject, HttpServletResponse response)
        throws IOException, ServletException {
      try {
        Log.i("Query String", target);

        if (target.contains("finder/where")) {
          whereIsMobile(request, response);
        }else if (target.contains("finder/ring")) {
          playRing(request, response);
        }else if (target.contains("caller")) {
          callPhoneNumber(request, response);
        } else if (target.contains("messages/show")) {
          showMessages(request, response);
        } else if (target.contains("messages/send")) {
          showMessages(request, response);
        } else if (target.contains("finder/where")) {
          whereIsMobile(request, response);
        }else if (target.contains("finder/ring")) {
          playRing(request, response);
        }

        ((Request) MainRequestObject).setHandled(true);

      } catch (Exception ex) {
        Log.i("Error", ex.getMessage());
      }
    }
  };

  

  protected void showMessages(Request request, HttpServletResponse response) {
    // TODO Auto-generated method stub

  }

  protected void callPhoneNumber(Request request, HttpServletResponse response) {
    String telefono = target.substring(9);
    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + telefono));
    startActivity(intent);

    response.setContentType("text/html");
    response.setStatus(HttpServletResponse.SC_OK);
    response.getWriter().println("<h1>Hola hola. Soy el movil Android contestando a la peticion del navegador.</h1>");

  }

  private void batteryLevel() {
    BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            context.unregisterReceiver(this);
            int rawlevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            int level = -1;
            if (rawlevel >= 0 && scale > 0) {
                level = (rawlevel * 100) / scale;
            }
            batteryLevel=level;
        }
    };
    IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
    registerReceiver(batteryLevelReceiver, batteryLevelFilter);
}
  
}

//
// // URI format
// // http://127.0.0.1:1234/Function/para1/para2
//
// // Http Request Type: GET/POST/PUT/DELETE
// Log.i("HTTP Verb", MainRequestObject.getMethod());
//
// BufferedReader in = new BufferedReader(new InputStreamReader(MainRequestObject.getInputStream()));
// String line = null;
//
// StringBuilder PostedData = new StringBuilder();
//
//
// while ((line = in.readLine()) != null) {
// Log.i("Received Message Line by Line", line);
// PostedData.append(line);
// }
//
// // Http Request Data Type
// try {
// Log.i("Posted Data Type", MainRequestObject.getContentType());
// } catch (Exception e) {
// // TODO: handle exception
// }
// try {
// // Http Request Type: GET/POST/PUT/DELETE
// Log.i("Posted Data", PostedData.toString());
// } catch (Exception e) {
// // TODO: handle exception
// }
// How To Send Responce Back