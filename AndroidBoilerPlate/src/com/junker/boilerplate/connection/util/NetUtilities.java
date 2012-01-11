package com.junker.boilerplate.connection.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
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
	
	private static List<NameValuePair> generateParams(TreeMap<String, String> params){
	  if (params == null) {
      params = new TreeMap<String, String>();
    }
    
    List<NameValuePair> listparams = new ArrayList<NameValuePair>();
    for (String key : params.keySet()) {
      String value = params.get(key);
      listparams.add(new BasicNameValuePair(key, value));
    }
    
    return listparams;
	}
	
	public static void setPutParams(TreeMap<String, String> params, HttpPut put) {
	  List<NameValuePair> putParams=generateParams(params);
    try {
      put.setEntity(new UrlEncodedFormEntity(putParams, HTTP.UTF_8));
    } catch (UnsupportedEncodingException e2) {
      e2.printStackTrace();
    }

  }
	
	public static void setPostParams(TreeMap<String, String> params, HttpPost post) {
	  List<NameValuePair> postParams=generateParams(params);
		try {
			post.setEntity(new UrlEncodedFormEntity(postParams, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
		}
	}
}
