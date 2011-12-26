package util.connection.task;

import java.util.TreeMap;

import android.app.Activity;
import android.os.AsyncTask;

import com.qewei.jmj.src.communication.ManagerConnection;
import com.qewei.jmj.src.communication.ManagerConnection.RequestCallback;
import com.qewei.jmj.src.controller.PersistController;
import com.qewei.jmj.src.exceptions.UserNotFoundException;
import com.qewei.jmj.src.tasks.INotifyEventsTask;
import com.qewei.jmj.src.ui.v2.ApplicationController;
import com.qewei.jmj.src.util.ConstantesSMS;
import com.qewei.jmj.src.util.NetUtilities;

public final class TaskGetEvents extends AsyncTask<Void, Void, String[]> {

	protected String url, lat, lon, cat, lang;
	INotifyEventsTask listener;

	public TaskGetEvents(INotifyEventsTask listener, String url, String lat, String lon, String cat, String lang) {
		this.listener=listener;
		this.url = url;
		this.lat=lat;
		this.lon=lon;
		this.cat=cat;
		this.lang=lang;
	}

	protected String[] doInBackground(Void... params) {
		try{
			TreeMap<String, String> paramsLogin = new TreeMap<String, String>();
			paramsLogin.put("auth", PersistController.getU().getUser().getAuth());
			if(lat!=null) paramsLogin.put("lat", lat);
			if(lon!=null) paramsLogin.put("lon", lon);
			
			String queryString="";
			if((cat!=null)&&((!cat.equals("")))&&(!cat.equals(ConstantesSMS.eventFilters.ALL_CAT))) queryString=NetUtilities.addParameterEncoded(queryString, "cat", cat);
			if((lang!=null)&&((!lang.equals("")))&&(!lang.equals(ConstantesSMS.eventFilters.ALL_LANG))) queryString=NetUtilities.addParameterEncoded(queryString, "lang", lang);
			if (!queryString.equals("")) paramsLogin.put("filter", queryString);
			
			ManagerConnection.doRequest(url, paramsLogin, ConstantesSMS.codesServer.PETICION_GET, requestCallbackListarEventos);
		} catch (UserNotFoundException e) {
			Activity act=(Activity)listener;
			((ApplicationController)act.getApplicationContext()).processReconnect(act);
		}
		return null;
	}

	public RequestCallback requestCallbackListarEventos = new RequestCallback() {
		public void onError(Throwable exception) {
			exception.printStackTrace();
		}

		public void onResponseReceived(String JSonStrResponse) {
			listener.finishEventsTask(JSonStrResponse, url);
		}
	};

	

}
