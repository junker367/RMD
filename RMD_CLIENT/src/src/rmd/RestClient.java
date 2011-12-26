package src.rmd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.TreeMap;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

import android.util.Log;

import com.qewei.jmj.src.communication.ManagerConnection.RequestCallback;
import com.qewei.jmj.src.exceptions.CommunicationResponseException;
import com.qewei.jmj.src.exceptions.CommunicationTimeOutException;
import com.qewei.jmj.src.exceptions.InternalServerErrorException;
import com.qewei.jmj.src.exceptions.InvalidAuthDetectedException;
import com.qewei.jmj.src.exceptions.ParserResponseException;
import com.qewei.jmj.src.exceptions.ServerNotFoundException;
import com.qewei.jmj.src.exceptions.UndefinedResponseCodeException;
import com.qewei.jmj.src.util.ConstantesSMS;
import com.qewei.jmj.src.util.ConstantesSMS.tagsLogger;
import com.qewei.jmj.src.util.NetUtilities;

public class RestClient {
	
	public static void doRequest(final String url, final TreeMap<String, String> params, final int tipoPeticion, final RequestCallback callback) {
		Log.i(tagsLogger.COMMUNICATION_SYSTEM, "doRequest a url: " + url + " with - params: " + params.toString());
		final HttpClient httpClient = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 10000);
		Thread thread = new Thread() {
			public void run() {
				HttpResponse response = null;
				String JSonStrResponse = null;
				if (tipoPeticion == ConstantesSMS.codesServer.PETICION_GET) {
					HttpGet httpget = new HttpGet(NetUtilities.createSignedURL(url, params));
					try {
						Log.i(tagsLogger.COMMUNICATION_SYSTEM, "doRequest GET with params: " + params.toString());
						response = httpClient.execute(httpget);
					} catch (org.apache.http.conn.ConnectTimeoutException ex) {
						Log.e(tagsLogger.COMMUNICATION_SYSTEM, "Error de timeout controlado!!!->" + ex);
						ex.printStackTrace();
						callback.onError(new CommunicationTimeOutException());
					} catch (Exception ex) {
						Log.e(tagsLogger.COMMUNICATION_SYSTEM, "Error6->" + ex);
						ex.printStackTrace();
						callback.onError(new CommunicationResponseException());
					}
				} else if (tipoPeticion == ConstantesSMS.codesServer.PETICION_POST) {
					HttpPost httpPost = new HttpPost(url);
					httpPost.addHeader("Accept", "application/json");
					httpPost.addHeader("Content-Type", "application/json");
					NetUtilities.setPostParams(params, httpPost);
					try {
						Log.i(tagsLogger.COMMUNICATION_SYSTEM, "doRequest POST with params: " + params.toString());
							response = httpClient.execute(httpPost);
					} catch (org.apache.http.conn.ConnectTimeoutException ex) {
						Log.e(tagsLogger.COMMUNICATION_SYSTEM, "Error de timeout controlado!!!->" + ex);
						ex.printStackTrace();
						callback.onError(new CommunicationTimeOutException());
					} catch (Exception ex) {
						Log.e(tagsLogger.COMMUNICATION_SYSTEM, "Error5->" + ex);
						ex.printStackTrace();
						callback.onError(new CommunicationResponseException());
					}
				}

				BufferedReader reader;
				Log.i(tagsLogger.COMMUNICATION_SYSTEM, "Analyzing response...");
				try {
					reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
					StringBuilder builder = new StringBuilder();
					for (String line = null; (line = reader.readLine()) != null;) {
						builder.append(line).append("\n");
					}
					JSonStrResponse = builder.toString();
					Log.i(tagsLogger.COMMUNICATION_SYSTEM, "response:" + JSonStrResponse);
					int statusCode = ((StatusLine) response.getStatusLine()).getStatusCode();
					if ((statusCode == ConstantesSMS.codesServer.RESPUESTA_HTTP_OK) || (statusCode == ConstantesSMS.codesServer.RESPUESTA_HTTP_CREATED)) {
						Log.i(tagsLogger.COMMUNICATION_SYSTEM, "All Ok. Redirecting...");
						callback.onResponseReceived(JSonStrResponse);
					} else {
						Log.e(tagsLogger.COMMUNICATION_SYSTEM, "StatusCode Invalid=" + statusCode);
						if (statusCode == ConstantesSMS.codesServer.AUTH_INVALIDO) {
							Log.e(tagsLogger.COMMUNICATION_SYSTEM, "StatusCode is AUTH_INVALIDO");
							callback.onError(new InvalidAuthDetectedException(JSonStrResponse));
						} else if (statusCode == ConstantesSMS.codesServer.INTERNAL_SERVER_ERROR) {
							Log.e(tagsLogger.COMMUNICATION_SYSTEM, "StatusCode is INTERNAL_SERVER_ERROR");
							callback.onError(new InternalServerErrorException());
						} else if (statusCode == ConstantesSMS.codesServer.NOT_FOUND) {
							Log.e(tagsLogger.COMMUNICATION_SYSTEM, "StatusCode is NOT_FOUND");
							callback.onError(new ServerNotFoundException());
						} else {
							Log.e(tagsLogger.COMMUNICATION_SYSTEM, "StatusCode undefined");
							callback.onError(new UndefinedResponseCodeException());
						}
					}
				} catch (UnsupportedEncodingException ex) {
					Log.e(tagsLogger.COMMUNICATION_SYSTEM, "Error1->" + ex);
					ex.printStackTrace();
					callback.onError(new ParserResponseException());
				} catch (IllegalStateException ex) {
					Log.e(tagsLogger.COMMUNICATION_SYSTEM, "Error2->" + ex);
					ex.printStackTrace();
					callback.onError(new ParserResponseException());
				} catch (IOException ex) {
					Log.e(tagsLogger.COMMUNICATION_SYSTEM, "Error3->" + ex);
					ex.printStackTrace();
					callback.onError(new ParserResponseException());
				} catch (Exception ex) {
					Log.e(tagsLogger.COMMUNICATION_SYSTEM, "Error4->" + ex);
					ex.printStackTrace();
					callback.onError(new ParserResponseException());
				}
			}

		};
		thread.start();
	}
	
}
