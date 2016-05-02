package com.iocom.andweb;

import android.Manifest;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity implements PluginListener {
    private static final int CAMERA_CODE = 11;
    private WebView wv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wv = new WebView(this);
        setContentView(wv);

        wv.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                wv.loadUrl(url);
                return false;
            }
        });
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,Manifest.permission.MODIFY_AUDIO_SETTINGS,Manifest.permission.RECORD_AUDIO},CAMERA_CODE);

        wv.setWebChromeClient(new WebChromeClient() {

                                  @Override
                                  public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                                      Log.d("andweb", "onCreateWindow");
                                      return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
                                  }

                                  @Override
                                  public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                                      Log.d("andweb", "JS: " + consoleMessage.message());
                                      //return super.onConsoleMessage(consoleMessage);
                                      return true;
                                  }

                                  @Override
                                  public void onPermissionRequest(final PermissionRequest request) {
                                      Log.d("andweb", "onPermissionRequest");
                                      for (String s: request.getResources())
                                      {
                                          Log.d("andweb", "...onPermissionRequest:  "+s);
                                      }
                                      MainActivity.this.runOnUiThread(new Runnable() {
                                          @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                                          @Override
                                          public void run() {
                                              Log.d("andweb", "request granted");
                                              request.grant(request.getResources());
                                          }
                                      });

                                  }

                              }
        );
        wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wv.getSettings().setSupportMultipleWindows(true);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setAppCacheEnabled(true);
        wv.getSettings().setDatabaseEnabled(true);
        wv.getSettings().setDomStorageEnabled(true);
        wv.getSettings().setGeolocationEnabled(true);
        wv.getSettings().setLoadsImagesAutomatically(true);
        wv.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        wv.addJavascriptInterface(this,"plugin");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        wv.loadUrl("https://visitest1.iocom.com/manage/api/webrtc");
    }

    @Override
    @JavascriptInterface
    public String GetUsername() {
        return "testing@example.com";
    }

    @Override
    @JavascriptInterface
    public String GetName() {
        return "Test User";
    }

    @Override
    @JavascriptInterface
    public String GetHostname() {
        return "visitest1.iocom.com";
    }

    @Override
    @JavascriptInterface
    public String GetMeetingId() {
        return "1234567";
    }
}
