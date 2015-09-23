package com.paulsonnentag.balanceboard;

import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaInterface;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.provider.Settings;

public class BalanceBoardPlugin extends CordovaPlugin {
    public static final String TAG = "MyPlugin";

    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

	callbackContext.success("native speaking");

        return false;
    }
}
