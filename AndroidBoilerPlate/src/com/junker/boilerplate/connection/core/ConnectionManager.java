package com.junker.boilerplate.connection.core;

import java.util.ArrayList;
import java.util.TreeMap;

import android.content.Context;

public class ConnectionManager {

  public static final int MAX_CONNECTIONS = 5;

  private ArrayList<HttpRestConnection> active = new ArrayList<HttpRestConnection>();
  private ArrayList<HttpRestConnection> queue = new ArrayList<HttpRestConnection>();

  private int timeOut = 10000;

  private static ConnectionManager instance;

  public static ConnectionManager getInstance() {
    if (instance == null)
      instance = new ConnectionManager();
    return instance;
  }

  public void enqueueHttpConnection(String url, TreeMap<String, String> params, int tipoPeticion, HttpRestRequestCallback callback,
      Context ctx) {
    HttpRestConnection httpRest = new HttpRestConnection(url, tipoPeticion, params, callback, timeOut, ctx);
    push(httpRest);
  }

  private void push(HttpRestConnection httpPendingConnection) {
    queue.add(httpPendingConnection);
    if (active.size() < MAX_CONNECTIONS) {
      startNext();
    }
  }

  private void startNext() {
    if ( !queue.isEmpty()) {
      HttpRestConnection next = queue.get(0);
      queue.remove(0);
      active.add(next);
      next.execute();
    }
  }

  void didComplete(HttpRestConnection httpFinishConnection) {
    active.remove(httpFinishConnection);
    startNext();
  }

}