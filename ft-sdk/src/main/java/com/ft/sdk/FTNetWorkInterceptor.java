package com.ft.sdk;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ft.sdk.garble.bean.NetStatusBean;
import com.ft.sdk.garble.bean.ResourceParams;
import com.ft.sdk.garble.http.HttpUrl;
import com.ft.sdk.garble.http.NetStatusMonitor;
import com.ft.sdk.garble.utils.LogUtils;
import com.ft.sdk.garble.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;

/**
 * create: by huangDianHua
 * time: 2020/5/18 10:06:37
 * description:
 */
public class FTNetWorkInterceptor extends NetStatusMonitor implements Interceptor {

    private static final String TAG = "FTNetWorkTracerInterceptor";

    private final boolean webViewTrace;


//    private final FTNetworkRumHandler mNetworkRUMHandler = new FTNetworkRumHandler();
//    private final ArrayList<FTNetworkRumHandler> mArr = new ArrayList<>();

    public FTNetWorkInterceptor() {
        this(false);
    }


    public FTNetWorkInterceptor(boolean webViewTrace) {
        this.webViewTrace = webViewTrace;
    }


    private void uploadNetTrace(FTTraceHandler handler,
                                String operationName, Request request,
                                @Nullable Response response, String responseBody, String error) {
        try {

            JSONObject requestContent = buildRequestJsonContent(request);
            JSONObject responseContent = buildResponseJsonContent(response, responseBody, error);
            boolean isError = response == null || response.code() >= 400;

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("requestContent", requestContent);
            jsonObject.put("responseContent", responseContent);

            handler.traceDataUpload(jsonObject, operationName, isError);

        } catch (Exception e) {
            LogUtils.e(TAG, e.getMessage());
        }
    }


    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        Response response = null;
        Request.Builder requestBuilder = request.newBuilder();
        Exception exception = null;
        Request newRequest = null;
        String operationName = "";
        FTTraceHandler traceHandler = new FTTraceHandler();
        try {
            okhttp3.HttpUrl url = request.url();
            HttpUrl httpUrl = new HttpUrl(url.host(), url.encodedPath(), url.port(), url.toString());
            operationName = request.method() + " " + httpUrl.getPath();
            HashMap<String, String> headers = traceHandler.getTraceHeader(httpUrl);
            traceHandler.setIsWebViewTrace(webViewTrace);
            for (String key : headers.keySet()) {
                requestBuilder.header(key, headers.get(key));//避免重试出现重复头
            }

            newRequest = requestBuilder.build();
            response = chain.proceed(requestBuilder.build());

        } catch (IOException e) {
            exception = e;
        }
        String resourceId = Utils.identifyRequest(newRequest);
        FTRUMGlobalManager manager = FTRUMGlobalManager.get();

        manager.startResource(resourceId);

        ResourceParams params = new ResourceParams();
        params.url = newRequest.url().toString();
        params.requestHeader = newRequest.headers().toString();
        params.resourceMethod = newRequest.method();

        if (exception != null) {
            uploadNetTrace(traceHandler, operationName, newRequest, null, "", exception.getMessage());
            manager.setTransformContent(resourceId, params,
                    traceHandler.getTraceID(), traceHandler.getSpanID());

            throw new IOException(exception);
        } else {
            String responseBody = "";
            Response.Builder responseBuilder = response.newBuilder();
            Response clone = responseBuilder.build();
            ResponseBody responseBody1 = clone.body();
            if (HttpHeaders.hasBody(clone)) {
                if (responseBody1 != null) {
                    if (isSupportFormat(responseBody1.contentType())) {
                        byte[] bytes = toByteArray(responseBody1.byteStream());
                        MediaType contentType = responseBody1.contentType();
                        responseBody = new String(bytes, getCharset(contentType));
                        responseBody1 = ResponseBody.create(responseBody1.contentType(), bytes);
                        response = response.newBuilder().body(responseBody1).build();
                        uploadNetTrace(traceHandler, operationName, newRequest, response, responseBody, "");

                        params.responseHeader = response.headers().toString();
                        params.responseBody = responseBody;
                        params.responseContentType = response.header("Content-Type");
                        params.responseConnection = response.header("Connection");
                        params.responseContentEncoding = response.header("Content-Encoding");
                        params.resourceStatus = response.code();
                        manager.setTransformContent(resourceId, params,
                                traceHandler.getTraceID(), traceHandler.getSpanID());

                    }
                }
            }
        }
        manager.stopResource(resourceId);


        return response;
    }

    /**
     * @param request
     * @return
     * @throws IOException
     */
    private JSONObject buildRequestJsonContent(Request request) throws IOException {
        JSONObject json = new JSONObject();
        JSONObject headers = new JSONObject(request.headers().toMultimap());

        try {
            json.put("method", request.method());
            json.put("url", request.url());
            json.put("headers", headers);
//            if (request.body() != null) {
//                Buffer sink = new Buffer();
//                request.body().writeTo(sink);
//                String body = sink.readString(StandardCharsets.UTF_8);
//                json.put("body", body);
//            }
        } catch (JSONException e) {
            LogUtils.e(TAG, e.getMessage());
        }
        return json;
    }

    /**
     * @param response
     * @return
     */
    private JSONObject buildResponseJsonContent(@Nullable Response response, String body, String error) {
        JSONObject json = new JSONObject();
        JSONObject headers = response != null ? new JSONObject(response.headers().toMultimap()) : new JSONObject();

        try {
            int code = response != null ? response.code() : 0;
            json.put("code", code);
            json.put("headers", headers);
//            if (code > HttpURLConnection.HTTP_OK) {
//                JSONObject jbBody = null;
//                JSONArray jaBody = null;
//                try {
//                    jbBody = new JSONObject(body);
//                    json.put("body", jbBody);
//                } catch (JSONException e) {
//                }
//                if (jbBody == null) {
//                    try {
//                        jaBody = new JSONArray(body);
//                        json.put("body", jaBody);
//                    } catch (JSONException e) {
//                    }
//                }
//                if (jaBody == null && jbBody == null) {
//                    json.put("body", body);
//                }
//            }
            if (error != null && !error.isEmpty()) {
                json.put("error", error);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }


    private static Charset getCharset(MediaType contentType) {
        Charset charset = contentType != null ? contentType.charset(StandardCharsets.UTF_8) : StandardCharsets.UTF_8;
        if (charset == null) charset = StandardCharsets.UTF_8;
        return charset;
    }

    private static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        write(input, output);
        output.close();
        return output.toByteArray();
    }

    private static void write(InputStream inputStream, OutputStream outputStream) throws IOException {
        int len;
        byte[] buffer = new byte[4096];
        while ((len = inputStream.read(buffer)) != -1) outputStream.write(buffer, 0, len);
    }

    /**
     * 支持内容抓取的
     *
     * @param mediaType
     * @return
     */
    private static boolean isSupportFormat(MediaType mediaType) {
        if (mediaType == null) return false;
        String contentType = mediaType.type() + "/" + mediaType.subtype();
        FTTraceConfig config = FTTraceConfigManager.get().getConfig();
        if (config == null) {
            return false;
        }
        List<String> supportContentType = config.getTraceContentType();
        if (supportContentType == null) {
            return false;
        }
        if (supportContentType.contains(contentType)) {
            return true;
        }
        return false;
    }

    @Override
    protected void getNetStatusInfoWhenCallEnd(String requestId, NetStatusBean bean) {
        FTRUMGlobalManager.get().putRUMResourcePerformance(requestId, bean);
    }
}
