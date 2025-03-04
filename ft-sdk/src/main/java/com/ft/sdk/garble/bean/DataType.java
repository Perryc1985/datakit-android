package com.ft.sdk.garble.bean;

/**
 * create: by huangDianHua
 * time: 2020/6/5 13:10:45
 * description:上传的数据类型
 * <p>
 * {@link DataType#TRACK  } 对应以下属性
 * {@link OP#LANC}
 * {@link OP#CLK}
 * {@link OP#CSTM}
 * {@link OP#OPEN}
 * {@link OP#OPEN_ACT}
 * {@link OP#OPEN_FRA}
 * {@link OP#CLS_ACT}
 * {@link OP#CLS_FRA}
 */
public enum DataType {
    TRACK,
    RUM_APP,
    RUM_WEBVIEW,
    LOG,
    OBJECT,
    TRACE;

    public String getValue() {
        return toString().toLowerCase();
    }

}
