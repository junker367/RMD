package util.connection;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;


public class NetUtilities {

	public static String addParameterEncoded(String querystring, String key, String value){
		if (querystring.equals("")) {
			querystring += key + "=" + StringHelper.urlEncode(value);
		} else {
			querystring += "&" + key + "=" + StringHelper.urlEncode(value);
		}
		return querystring;
	}
	
	public static String createSignedURLBase(TreeMap<String, String> params) {

		if (params == null) {
			params = new TreeMap<String, String>();
		}
		params.put(ConstantesSMS.configRed.VALOR_RED, ConstantesSMS.configRed.PARMETRO_RED);
		params.put(ConstantesSMS.configRed.VALOR_VERSION, ConstantesSMS.configRed.PARMETRO_VERSION);

		String querystring = "";

		for (String key : params.keySet()) {
			String value = params.get(key);
			if (querystring.equals("")) {
				querystring += key + "=" + StringHelper.urlEncode(value);
			} else {
				querystring += "&" + key + "=" + StringHelper.urlEncode(value);
			}
		}
		System.out.println("Base URL " + querystring);
		return querystring;
	}
	
	
	public static String createSignedURL(String baseURL, TreeMap<String, String> params) {
		String s = baseURL + "?" + createSignedURLBase(params);
		System.out.println("final URL " + s);
		return s;
	}
	
	public static void setPostParams(TreeMap<String, String> params, HttpPost post) {
		// String publicKey = "123456";
		// String privateKey = "ABCDEF";

		if (params == null) {
			params = new TreeMap<String, String>();
		}
		
		params.put(ConstantesSMS.configRed.VALOR_RED, ConstantesSMS.configRed.PARMETRO_RED);
		params.put(ConstantesSMS.configRed.VALOR_VERSION, ConstantesSMS.configRed.PARMETRO_VERSION);
		// params.put("public", publicKey);

		// String signature = "private=" + privateKey;
		List<NameValuePair> postparams = new ArrayList<NameValuePair>();

		// HttpParams httpparams = post.getParams();
		for (String key : params.keySet()) {
			String value = params.get(key);
			// signature += "&" + key + "=" + value;
			postparams.add(new BasicNameValuePair(key, value));
		}
		// postparams.add(new BasicNameValuePair("signature",
		// StringHelper.sha1(signature)));
		try {
			post.setEntity(new UrlEncodedFormEntity(postparams, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
		}

	}
}
