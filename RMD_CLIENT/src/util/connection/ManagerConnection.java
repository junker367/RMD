package util.connection;

import java.util.TreeMap;

import src.rmd.RestClient;
import src.rmd.R.string;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.qewei.jmj.src.R;
import com.qewei.jmj.src.exceptions.CommunicationTimeOutException;
import com.qewei.jmj.src.ui.v2.ApplicationController;
import com.qewei.jmj.src.util.ConstantesSMS.tagsLogger;

public class ManagerConnection {

	public interface RequestCallback {
		public void onError(Throwable exception);
		public void onResponseReceived(String JsonResponse) throws Exception;
	}
	
	private static RequestCallback listener;
	private static int iteraciones = 0;
	private static String uri;
	private static TreeMap<String, String> parameters;
	private static int type;

	private static int REQUEST_MAXIMUM_COUNT = 5;

	public static boolean checkConex(Context ctx) {
		boolean bTieneConexion = false;
		ConnectivityManager connec = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

		// Con esto recogemos todas las redes que tiene el m—vil (wifi, gprs...)
		NetworkInfo[] redes = connec.getAllNetworkInfo();

		for (int i = 0; i < 2; i++) {
			// Si alguna tiene conexi—n ponemos el boolean a true
			if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
				bTieneConexion = true;
			}
		}

		// Si el boolean sigue a false significa que no hay red disponible
		if (!bTieneConexion) {
			// Mostramos un error indicando al usuario que no tiene conexi—n a
			// Internet
			ApplicationController.mostrarMensaje(R.string.errorSinConexion, ctx);
		}

		return bTieneConexion;
	}

	
	public static void doRequest(final String url, final TreeMap<String, String> params, final int tipoPeticion, final RequestCallback callback) {
		Log.i(tagsLogger.COMMUNICATION_SYSTEM, "doRequest a url: " + url + " with - params: " + params.toString());

		listener = callback;
		uri = url;
		parameters = params;
		type = tipoPeticion;

		doHttpRequest(url, params, tipoPeticion, requestCallbackRestClient);
	}

	public static void doHttpRequest(final String url, final TreeMap<String, String> params, final int tipoPeticion, final RequestCallback callback){
		RestClient.doRequest(url, params, tipoPeticion, callback);
	}
	
	public static RequestCallback requestCallbackRestClient = new RequestCallback() {
		public void onError(Throwable exception) {
			processResultError(exception);
		}

		public void onResponseReceived(String JSonStrResponse) throws Exception {
			processResultOk(JSonStrResponse);
		}
	};

	protected static void processResultOk(String response) {
		try {
			listener.onResponseReceived(response);
		} catch (Exception e) {
			e.printStackTrace();
			listener.onError(e);
		}
	}
	
	protected static void processResultError(Throwable exception) {
		if (exception instanceof CommunicationTimeOutException) {
			Log.d(tagsLogger.COMMUNICATION_SYSTEM, "Timeout detected. URL: " + uri + " with - params: " + parameters.toString());
			
			iteraciones++;
			if (iteraciones < REQUEST_MAXIMUM_COUNT) {
				Log.d(tagsLogger.COMMUNICATION_SYSTEM, "Reintent with id #"+iteraciones);
				doHttpRequest(uri, parameters, type, requestCallbackRestClient);
			} else {
				listener.onError(exception);
			}
		} else {
			listener.onError(exception);
		}
	}

}