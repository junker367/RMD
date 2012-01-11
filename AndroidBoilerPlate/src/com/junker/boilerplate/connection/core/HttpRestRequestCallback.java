package com.junker.boilerplate.connection.core;

import android.graphics.Bitmap;

public interface HttpRestRequestCallback {

  public void onError(Throwable exception);

  public void onStart();

  public void onResponseString(String response);

  public void onResponseBitmap(Bitmap bm);

}