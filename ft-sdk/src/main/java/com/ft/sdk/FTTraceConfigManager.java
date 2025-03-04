package com.ft.sdk;

public class FTTraceConfigManager {


    private static class SingletonHolder {
        private static final FTTraceConfigManager INSTANCE = new FTTraceConfigManager();
    }

    public static FTTraceConfigManager get() {
        return FTTraceConfigManager.SingletonHolder.INSTANCE;
    }

    private FTTraceConfig config;


    void initWithConfig(FTTraceConfig config) {
        if (config.isEnableLinkRUMData() && config.getTraceType() != TraceType.DDTRACE) {
            throw new InitSDKProcessException("FTTraceConfig.isEnableLinkRUMData,仅支持 TraceType.DDTRACE");
        }
        this.config = config;
    }

    public FTTraceConfig getConfig() {
        return config;
    }

    public boolean isEnableAutoTrace() {
        return config != null && config.isEnableAutoTrace();
    }

    public boolean isEnableLinkRUMData() {
        return config != null && config.isEnableLinkRUMData();
    }

    public boolean isEnableWebTrace() {
        return config != null && config.isEnableWebTrace();
    }

    void release() {
        config = null;
    }

}
