package com.ft.sdk;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.webkit.WebView;

import com.ft.sdk.garble.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

final class FTWebViewHandler implements WebAppInterface.JsReceiver {

    private static final String TAG = "FTWebViewHandler";

    private final Handler mHandler = new Handler(Looper.getMainLooper());

    private WebView mWebView;

    public void setWebView(WebView webview) {
        mWebView = webview;
        webview.getSettings().setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new WebAppInterface(webview.getContext(), this), "FTWebViewJavascriptBridge");

    }


    public FTWebViewHandler() {
    }

    @Override
    public void sendEvent(String s) {
        sendEvent(s, null);
    }

    @Override
    public void sendEvent(String s, String callbackMethod) {
        try {
            LogUtils.d(TAG, "sendEvent：" + s);
            JSONObject json = new JSONObject(s);
            String name = json.optString("name");
            JSONObject data = json.optJSONObject("data");
            String tag = json.optString("_tag");
            if (name.equals("rum")) {
                if (data != null) {
                    JSONObject tags = data.optJSONObject("tags");
                    JSONObject fields = data.optJSONObject("fields");

                    JSONObject publicTags = FTAutoTrack.getRUMPublicTags();
                    Iterator<String> keys = publicTags.keys();
                    if (tags == null) {
                        tags = new JSONObject();
                    }

                    while (keys.hasNext()) {
                        String key = keys.next();
                        tags.put(key, publicTags.opt(key));
                    }

                    String sessionId = FTRUMGlobalManager.get().getSessionId();
                    tags.put("session_id", sessionId);
                    tags.put("is_active", false);
                    tags.put("is_web_view", true);

                    long time = data.optLong("time");
                    String measurement = data.optString("measurement");
                    FTTrackInner.getInstance().rumWebView(time, measurement, tags, fields);
                }

            } else if (name.equals("track")) {

            } else if (name.equals("log")) {

            } else if (name.equals("urlVerify")) {

            }
            if (callbackMethod != null && callbackMethod.isEmpty()) {
                JSONObject retJson = new JSONObject();
                retJson.put("status", true);
                String ret = retJson.toString();
                String err = "{}";
                callbackFromNative(tag, callbackMethod, ret, err);
            }

        } catch (JSONException e) {
            LogUtils.e(TAG, e.getMessage());
        }


    }

    @Override
    public void addEventListener(String s, String callBackMethod) {

        try {
            JSONObject json = new JSONObject(s);
            String name = json.optString("name");
            String tag = json.optString("_tag");

            // NO implement
            String ret = "{}";
            String err = "{}";
            callbackFromNative(tag, callBackMethod, ret, err);
        } catch (JSONException e) {
            LogUtils.e(TAG, e.getMessage());
        }

    }

    private void callJsMethod(String method) {
        callJsMethod(method, null);
    }

    private void callbackFromNative(String requestTag, String callBackMethod, String retJsonString, String errJsonString) {
        String separate = !retJsonString.isEmpty() && !errJsonString.isEmpty() ? "," : "";
        String jsStr = callBackMethod + "[" + requestTag + "](" + retJsonString + separate + errJsonString + ")";
        callJsMethod(jsStr);

    }

    private void callJsMethod(String method, CallbackFromJS callback) {
        mHandler.post(() -> {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                mWebView.evaluateJavascript(method, value -> {
                    if (callback != null) {
                        callback.callBack(value);
                    }
                });
            } else {
                LogUtils.e(TAG, "This Android Device may be low than 4.4, can't get js call Back ");
                mWebView.loadUrl("javascript:" + method);
            }

        });

    }


    interface CallbackFromJS {
        void callBack(String content);
    }


}
