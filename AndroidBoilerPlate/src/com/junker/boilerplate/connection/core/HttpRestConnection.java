package com.junker.boilerplate.connection.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.TreeMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.junker.boilerplate.connection.exceptions.CommunicationResponseException;
import com.junker.boilerplate.connection.exceptions.CommunicationTimeOutException;
import com.junker.boilerplate.connection.exceptions.InternalServerErrorException;
import com.junker.boilerplate.connection.exceptions.NotCommunicationOnLineException;
import com.junker.boilerplate.connection.exceptions.ServerNotFoundException;
import com.junker.boilerplate.connection.util.NetUtilities;

public class HttpRestConnection extends AsyncTask<Void, Void, String[]> {

  private final static String TAG = "RestClient";

  private int timeOut;
  private String url;
  private int tipoPeticion;
  private TreeMap<String, String> params;
  private HttpRestRequestCallback callback;

  private static final int RESPUESTA_HTTP_NOT_FOUND = 404;
  private static final int RESPUESTA_HTTP_INTERNAL_SERVER_ERROR = 500;

  public static final int GET = 0;
  public static final int POST = 1;
  public static final int PUT = 2;
  public static final int DELETE = 3;
  public static final int BITMAP = 4;

  private HttpEntity entity = null;
  private Context ctx;

  HttpRestConnection(String url, int tipoPeticion, TreeMap<String, String> params, HttpRestRequestCallback callback, int timeOut,
      Context ctx) {
    super();
    this.timeOut = timeOut;
    this.url = url;
    this.tipoPeticion = tipoPeticion;
    this.params = params;
    this.callback = callback;
    this.ctx = ctx;
  }

  private void processBitmapEntity(HttpEntity entity) throws IOException {
    BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);
    Bitmap bm = BitmapFactory.decodeStream(bufHttpEntity.getContent());
    callback.onResponseBitmap(bm);
  }

  private void processEntity(HttpEntity entity) throws IllegalStateException, IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
    String line, result = "";
    while ((line = br.readLine()) != null) {
      result += line;
    }

    Log.i(TAG, "response:" + result);
    callback.onResponseString(result);
  }

  private static void checkConex(Context ctx) throws NotCommunicationOnLineException {
    try {
      boolean bTieneConexion = false;
      ConnectivityManager connec = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

      NetworkInfo[] redes = connec.getAllNetworkInfo();

      for (int i = 0; i < 2; i++) {
        if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
          bTieneConexion = true;
        }
      }

      if ( !bTieneConexion) {
        throw new NotCommunicationOnLineException();
      }
    } catch (Exception ex) {
      throw new NotCommunicationOnLineException();
    }
  }

  private HttpEntity executeConnection(HttpClient httpClient, HttpRequestBase httpRequest) throws NotCommunicationOnLineException,
      CommunicationTimeOutException, CommunicationResponseException, ServerNotFoundException, InternalServerErrorException {
    HttpResponse response = null;
    try {
      checkConex(ctx);
      response = httpClient.execute(httpRequest);

      int statusCode = ((StatusLine) response.getStatusLine()).getStatusCode();

      switch (statusCode) {
        case RESPUESTA_HTTP_NOT_FOUND:
          Log.e(TAG, "response Error 404 NOT_FOUND");
          throw new ServerNotFoundException();
        case RESPUESTA_HTTP_INTERNAL_SERVER_ERROR:
          Log.e(TAG, "response error 500 INTERNAL_SERVER_ERROR");
          throw new InternalServerErrorException();
      }
    } catch (ServerNotFoundException ex) {
      throw ex;
    } catch (InternalServerErrorException ex) {
      throw ex;
    } catch (NotCommunicationOnLineException ex) {
      Log.e(TAG, "Error. Not Connection online" + ex);
      ex.printStackTrace();
      throw new NotCommunicationOnLineException();
    } catch (org.apache.http.conn.ConnectTimeoutException ex) {
      Log.e(TAG, "Error. TimeOut detected>" + ex);
      ex.printStackTrace();
      throw new CommunicationTimeOutException();
    } catch (Exception ex) {
      Log.e(TAG, "Error from server." + ex);
      ex.printStackTrace();
      throw new CommunicationTimeOutException();
    }

    return response.getEntity();
  }

  protected String[] doInBackground(Void... parametros) {
    try {
      callback.onStart();
      Log.i(TAG, "doRequest a url: " + url + " with - params: " + params.toString());
      final HttpClient httpClient = new DefaultHttpClient();
      HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), timeOut);

      switch (tipoPeticion) {
        case GET:
          HttpGet httpget = new HttpGet(NetUtilities.createSignedURL(url, params));
          Log.d(TAG, "doRequest GET with params: " + params.toString());
          entity = executeConnection(httpClient, httpget);
          break;
        case POST:
          HttpPost httpPost = new HttpPost(url);
          NetUtilities.setPostParams(params, httpPost);
          Log.i(TAG, "doRequest POST with params: " + params.toString());
          entity = executeConnection(httpClient, httpPost);
          break;

        case PUT:
          HttpPut httpPut = new HttpPut(url);
          NetUtilities.setPutParams(params, httpPut);
          Log.i(TAG, "doRequest PUT with params: " + params.toString());
          entity = executeConnection(httpClient, httpPut);
          break;

        case DELETE:
          HttpDelete httpDelete = new HttpDelete(url);
          Log.i(TAG, "doRequest DELETE with params: " + params.toString());
          entity = executeConnection(httpClient, httpDelete);
          break;

        case BITMAP:
          HttpGet httpGet = new HttpGet(url);
          Log.i(TAG, "doRequest BITMAP with params: " + params.toString());
          entity = executeConnection(httpClient, httpGet);
          break;
      }

      if (tipoPeticion < BITMAP) {
        processEntity(entity);
      } else {
        processBitmapEntity(entity);
      }

    } catch (Exception ex) {
      callback.onError(ex);
    }
    return null;
  }

  protected void onPostExecute(String[] result) {
    super.onPostExecute(result);
    ConnectionManager.getInstance().didComplete(this);
  }

}