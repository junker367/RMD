package com.junker.boilerplate.connection.sample;

import java.util.TreeMap;

import com.junker.boilerplate.connection.core.ConnectionManager;
import com.junker.boilerplate.connection.core.HttpRestRequestCallback;
import com.junker.boilerplate.connection.core.HttpRestConnection;

import android.app.Activity;
import android.graphics.Bitmap;

/*
 //TODO 
 * Clase de ejemplo y de test del manager connection.
 * Poner los siguientes ejemplos:
 * una conection unica
 * una conection bitmap unica
 * encolar 10 conections
 * encolar 10 conection bitmaps
 */

public final class SampleConnectionHttp extends Activity {

  public void loginHttp() {
    String url = "http://192.168.1.117:8081/usuario/login";
    TreeMap<String, String> params = new TreeMap<String, String>();
    params.put("restfulusername", "restfulusername");
    params.put("restfulpassword", "restfulusername");
    params.put("username", "restfulusername");
    params.put("password", "restfulusername");
    params.put("iddispositivo", "restfulusername");
    ConnectionManager.getInstance().enqueueHttpConnection(url, params, HttpRestConnection.POST, responseLogin, this);
  }

  HttpRestRequestCallback responseLogin = new HttpRestRequestCallback() {

    public void onStart() {
    }

    public void onResponseString(String response) {
    }

    public void onResponseBitmap(Bitmap bm) {
    }

    public void onError(Throwable exception) {
    }
  };
}