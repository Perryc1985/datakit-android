package com.ft.sdk.garble.bean;

import com.ft.sdk.FTApplication;
import com.ft.sdk.FTSdk;
import com.ft.sdk.garble.utils.Constants;
import com.ft.sdk.garble.utils.DeviceUtils;
import com.ft.sdk.garble.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * create: by huangDianHua
 * time: 2020/6/5 15:08:45
 * description:日志对象(SDK内部使用)
 */
public class LogBean {
    private static final int LIMIT_SIZE = 30720;
    //指定当前日志的来源，比如如果来源于 Ngnix，可指定为 Nginx，
    // 同一应用产生的日志 source 应该一样，这样在 DataFlux 中方便针对该来源的日志配置同一的提取规则
    String measurement;
    //日志的子分类，目前仅支持：tracing：表示该日志是链路追踪日志
    String clazz;
    //日志来源，日志上报后，会自动将指定的指标集名作为该标签附加到该条日志上
    String source;
    //日志所属业务或服务的名称，建议用户通过该标签指定产生该日志业务系统的名称
    String serviceName;
    //日志所属环境，比如可用 dev 表示开发环境，prod 表示生产环境，用户可自定义
    String env;
    //日志等级
    Status status = Status.INFO;
    //用于链路日志，表示当前 span 的上一个 span的 ID
    String parentID;
    //用于链路日志，表示当前 span 操作名，也可理解为 span 名称
    String operationName;
    //用于链路日志，表示当前 span 的 ID
    String spanID;
    //用于链路日志，表示当前链路的 ID
    String traceID;
    //字符串类型，true 表示该 span 的请求响应是错误,false 或者无该标签，表示该 span 的响应是正常的请求
    String isError;
    //日志内容，纯文本或 JSONString 都可以
    String content;
    //span 的类型，目前支持 2 个值：entry 和 local，entry span 表示该 span 的调用的是服务的入口，
    // 即该服务的对其他服务提供调用请求的端点，几乎所有服务和消息队列消费者都是 entry span，
    // 因此只有 span 是 entry 类型的调用才是一个独立的请求。 local span 表示该 span 和远程调用没有任何关系，
    // 只是程序内部的函数调用，例如一个普通的 Java 方法，默认值 entry
    String spanType;
    //请求的目标地址，客户端用于访问目标服务的网络地址(但不一定是 IP + 端口)，例如 127.0.0.1:8080 ,默认：null
    String endpoint;
    //用于链路日志，当前链路的请求响应时间，微秒为单位
    long duration;
    long time;
    JSONObject tags;
    JSONObject fields;


    /**
     * 是否超过 30KB
     *
     * @param content
     * @return
     */
    private static boolean isOverMaxLength(String content) {
        if (content == null) return false;
        return content.length() > LIMIT_SIZE;
    }

    public LogBean(String measurement, String content, long time) {
        this.measurement = measurement;

        if (isOverMaxLength(content)) {
            this.content = content.substring(0, LIMIT_SIZE);
        } else {
            this.content = content;
        }
        this.time = time;
    }

    public LogBean(String content, long time) {
        this(Constants.FT_LOG_DEFAULT_MEASUREMENT, content, time);
    }

    public LogBean(String measurement, JSONObject json, long time) {
        this(measurement, json.toString(), time);
    }

    public LogBean(JSONObject json, long time) {
        this(Constants.FT_LOG_DEFAULT_MEASUREMENT, json.toString(), time);
    }

    public JSONObject getAllFields() {
        if (fields == null) {
            fields = new JSONObject();
        }
        try {
            fields.put("__content", content);
            if (duration > 0) {
                fields.put("__duration", duration);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return fields;
    }

    public JSONObject getAllTags() {
        if (tags == null) {
            tags = new JSONObject();
        }
        try {
            if (!Utils.isNullOrEmpty(clazz)) {//目前只支持tracing
                tags.put("__class", "tracing");
            }
            if (!Utils.isNullOrEmpty(source)) {
                tags.put("__source", source);
            }
            if (!Utils.isNullOrEmpty(serviceName)) {
                tags.put("__serviceName", serviceName);
            }
            if (!Utils.isNullOrEmpty(env)) {
                tags.put("__env", env);
            }
            tags.put("__status", status.name);

            if (!Utils.isNullOrEmpty(parentID)) {
                tags.put("__parentID", parentID);
            }
            if (!Utils.isNullOrEmpty(operationName)) {
                tags.put("__operationName", operationName);
            }
            if (!Utils.isNullOrEmpty(spanID)) {
                tags.put("__spanID", spanID);
            }
            if (!Utils.isNullOrEmpty(traceID)) {
                tags.put("__traceID", traceID);
            }
            if (!Utils.isNullOrEmpty(isError)) {
                tags.put("__isError", isError);
            }
            if (!Utils.isNullOrEmpty(spanType)) {
                tags.put("__spanType", spanType);
            }
            if (!Utils.isNullOrEmpty(endpoint)) {
                tags.put("__endpoint", endpoint);
            }
            if (!tags.has("device_uuid")) {
                tags.put("device_uuid", DeviceUtils.getUuid(FTApplication.getApplication()));
            }

            if (!tags.has("application_uuid")) {
                tags.put("application_uuid", FTSdk.PACKAGE_UUID);
            }

            if (!tags.has("application_identifier")) {
                tags.put("application_identifier", DeviceUtils.getApplicationId(FTApplication.getApplication()));
            }
            tags.put("app_version_name", Utils.getAppVersionName());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return tags;
    }

    public String getMeasurement() {
        return measurement;
    }

    public String getContent() {
        return content;
    }

    public long getTime() {
        return time;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getSpanID() {
        return spanID;
    }

    public void setSpanID(String spanID) {
        this.spanID = spanID;
    }

    public String getTraceID() {
        return traceID;
    }

    public void setTraceID(String traceID) {
        this.traceID = traceID;
    }

    public String getIsError() {
        return isError;
    }

    /**
     * @param isError 字符串类型，true 表示该 span 的请求响应是错误,false 或者无该标签，表示该 span 的响应是正常的请求
     */
    public void setIsError(String isError) {
        this.isError = isError;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public JSONObject getTags() {
        return tags;
    }

    public void setTags(JSONObject tags) {
        this.tags = tags;
    }

    public JSONObject getFields() {
        return fields;
    }

    public void setFields(JSONObject fields) {
        this.fields = fields;
    }

    public String getSpanType() {
        return spanType;
    }

    public void setSpanType(String spanType) {
        this.spanType = spanType;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }
}
