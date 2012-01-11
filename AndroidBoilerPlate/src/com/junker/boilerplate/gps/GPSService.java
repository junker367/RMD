package com.junker.boilerplate.gps;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

public class GPSService extends Service implements LocationListener {

	private SharedPreferences prefs;
	private Handler handler = null;
	private LocationManager locationManager;
	private Location puntoActual;
	private Location puntoAnterior;
	private GPSController controllerHandler;
	private ConfigGPS configuracionGps = null;

  private final static String TAG = "GPSService";

	@Override
	public void onCreate() {
		super.onCreate();

		prefs = PreferenceManager.getDefaultSharedPreferences(this);

		prefs.registerOnSharedPreferenceChangeListener(new OnSharedPreferenceChangeListener() {
			public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
				Log.d(TAG, "Preferences Changed!");
			}
		});

		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		Log.d(TAG, "onCreate'd");
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

		try {
			checkConfig();
			inicioDeteccion();
		} catch (Exception ex) {
			Log.e(TAG, "Error al iniciar el servicio en onStart()");
		}

		if (handler == null) {
			handler = new Handler();
			controllerHandler = new GPSController();
			handler.post(controllerHandler);
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		handler.removeCallbacks(controllerHandler);
		handler = null;
		stopDeteccion();
		Log.d(TAG, "Deteniendo grabacion de posiciones...");
	}

	public void onLocationChanged(Location location) {
		Log.d(TAG, "onLocationChanged with Location: " + location);

		Log.d(TAG, "Recibida posicion. Procesando...");

		if ((getPuntoActual() != null) && (location != null) && (isCorrectPosicion(location, getPuntoActual()))) {
			setPuntoActual(location);
		} else {
			Log.d(TAG, "Es primera posicion recibida. Guardando...");
			setPuntoActual(location);
		}
	}

	private boolean isCorrectPosicion(Location location, Location currentBestLocation) {
		Log.d(TAG, "Procesando precision de la posicion recibida...");
		if (currentBestLocation == null) {
			Log.d(TAG, "currentBestLocation == null. Posicion correcta. Esto no debería ocurrir!!!");
			return true;
		}

		if (intervalTimeLocationIsCorrect(location, currentBestLocation)) {
			return true;
		}
		return false;
	}

	private boolean intervalTimeLocationIsCorrect(Location location, Location currentBestLocation) {

		long intervaloConfiguracion = configuracionGps.intervaloAplicacionCheckeaGPSConfig;
		long timeDelta = location.getTime() - currentBestLocation.getTime();
		boolean entraEnIntervalo = timeDelta >= intervaloConfiguracion;
		boolean noEntraEnIntervalo = timeDelta < intervaloConfiguracion * -1;

		Log.d(TAG, "Ultima posicion recibida en: " + currentBestLocation.getTime());
		Log.d(TAG, "Nueva posicion recibida en: " + location.getTime());
		Log.d(TAG, "Diferencia de tiempo entre posiciones: " + timeDelta);

		if (entraEnIntervalo) {
			Log.d(TAG, "Diferencia de tiempo(" + timeDelta + ") mayor/igual que intervalo definido(" + intervaloConfiguracion + "). Posicion correcta.");
			return true;
		} else if (noEntraEnIntervalo) {
			Log.d(TAG, "Diferencia de tiempo(" + timeDelta + ") menor que intervalo definido(" + intervaloConfiguracion + "). Posicion correcta.");
			return false;
		}
		return false;
	}

	private void stopDeteccion() {
		locationManager.removeUpdates(this);
	}

	private void inicioDeteccion() {
		if (configuracionGps.intervaloRecepcionSignal != null) {
			String provider = getServiceProvider();
			//String provider = locationManager.PASSIVE_PROVIDER;
			locationManager.requestLocationUpdates(provider, configuracionGps.intervaloRecepcionSignal, configuracionGps.intervaloMetersBusquedaGPSSignal, this);
			Log.d(TAG, "Iniciando grabacion de posiciones desde " + provider);
		}
	}

	private String getServiceProvider() {
		String provider=null;
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(false);
		if(locationManager!=null){
			provider = locationManager.getBestProvider(criteria, true);	
		}
		return provider;
	}

	private void checkConfig() throws Exception {
		if (locationManager == null) {
			Exception ex = new Exception("Excepcion: LocationManager is null!");
			throw ex;
		}
		if (configuracionGps == null) {
			configuracionGps = new ConfigGPS();
			configuracionGps.setDefaultValues();
		}
	}

	class GPSController implements Runnable {
		String msg;
		ContentValues values = new ContentValues();

		public void run() {
			if (getPuntoActual() != null) {
				if (posicionesSonDistintas()) {
					setPuntoAnterior(getPuntoActual());
					msg = String.format("Recibido punto con (%.5f,%.5f)", getPuntoActual().getLatitude(), getPuntoActual().getLongitude());
					Log.d(TAG, "Punto recibido en el gps. Enviada notificacion.");
					Intent i = new Intent(Intent.ACTION_VIEW);

					i.putExtra("accuracy", getPuntoActual().getAccuracy());
					i.putExtra("altitude", getPuntoActual().getAltitude());
					i.putExtra("bearing", getPuntoActual().getBearing());
					i.putExtra("latitude", getPuntoActual().getLatitude());
					i.putExtra("longitude", getPuntoActual().getLongitude());
					i.putExtra("provider", getPuntoActual().getProvider());
					i.putExtra("speed", getPuntoActual().getSpeed());
					i.putExtra("time", getPuntoActual().getTime());
					i.putExtra("extras", getPuntoActual().getExtras());

					sendOrderedBroadcast(i, null);
					Log.d(TAG, "Enviada notificacion OK.");
				}
			} else {
				String provider=getServiceProvider();
				if(provider!=null){
					Location loc = locationManager.getLastKnownLocation(provider);
					setPuntoActual(loc);	
				}
			}
			handler.postDelayed(this, configuracionGps.intervaloAplicacionCheckeaGPSConfig);
		}
	}

	public Location getPuntoActual() {
		return puntoActual;
	}

	public boolean posicionesSonDistintas() {
		if (getPuntoActual() != null) {
			if (getPuntoAnterior() != null) {
				if (getPuntoActual().getLatitude() != getPuntoAnterior().getLatitude()) {
					return true;
				} else {
					if (getPuntoActual().getLongitude() != getPuntoAnterior().getLongitude()) {
						return true;
					} else {
						if (getPuntoActual().getAltitude() != getPuntoAnterior().getAltitude()) {
							return true;
						}
					}
				}
			} else {
				return true;
			}
		}
		return false;
	}

	public void setPuntoActual(Location puntoActual) {
		this.puntoActual = puntoActual;
	}

	public Location getPuntoAnterior() {
		return puntoAnterior;
	}

	public void setPuntoAnterior(Location puntoAnterior) {
		this.puntoAnterior = puntoAnterior;
	}

	public void onProviderDisabled(String provider) {
	}

	public void onProviderEnabled(String provider) {
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
}
