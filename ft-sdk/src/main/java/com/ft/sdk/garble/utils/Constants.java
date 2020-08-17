package com.ft.sdk.garble.utils;

/**
 * BY huangDianHua
 * DATE:2019-12-10 10:41
 * Description:
 */
public class Constants {
    public static final String FT_SDK_INIT_UUID = "ft.sdk.init.uuid";
    public static final String FT_USER_SESSION_ID = "ft.user.session.id";
    public static final String FT_SHARE_PER_FILE = "ftSDKShareFile";
    public static final String FT_KEY_VALUE_NULL = "null";
    public static final String FT_DEFAULT_MEASUREMENT = "mobile_tracker";
    public static final String FT_MONITOR_MEASUREMENT = "mobile_monitor";
    public static final String USER_AGENT= "ft_mobile_sdk_android";
    public static final String UNKNOWN = "N/A";
    public static final String FLOW_ROOT = "root";
    public static final String MEASUREMENT= "measurement";
    public static final String FIELDS= "fields";
    public static final String TAGS= "tags";
    public static final String SEPARATION_PRINT = "--temp_separation--";
    public static final String SEPARATION = " ";
    public static final String SEPARATION_LINE_BREAK = "--line_break_temp--";//换行标志符，用于日志显示
    public static final String SEPARATION_REALLY_LINE_BREAK = "\n";//换行标志符，用于日志显示
    public static final String PERFIX = "ft_parent_not_fragment";
    public static final String MOCK_SON_PAGE_DATA = "mock_son_page_data";
    public static final String SHARE_PRE_STEP_DATE = "share_pre_step_date";
    public static final String SHARE_PRE_STEP_HISTORY = "share_pre_step_history";

    public static final String URL_MODEL_TRACK = "v1/write/metrics";//指标数据上传路径
    public static final String URL_MODEL_LOG = "v1/write/logging";//日志数据上传路径
    public static final String URL_MODEL_KEY_EVENT = "v1/write/keyevent";//事件数据上传路径
    public static final String URL_MODEL_OBJECT = "v1/write/object";//对象数据上传路径
    public static final String URL_MODEL_TOKEN_CHECK = "v1/check/token";//验证token是否合法

    public static final String DEFAULT_OBJECT_CLASS = "Dataflux Android SDK";//默认的对象名
    public static final String DEFAULT_LOG_SERVICE_NAME = "dataflux sdk";
    public static final int MAX_DB_CACHE_NUM = 5000;//数据库最大缓存容量

    public static final String KEEP_MEASUREMENT_FLOW = "$flow_mobile_activity";
    public static final String KEEP_KEY_TRACE_ID = "$traceId";
    public static final String KEEP_KEY_NAME = "$name";
    public static final String KEEP_KEY_PARENT = "$parent";
    public static final String KEEP_KEY_DURATION = "$duration";

    public static final String KEY_CURRENT_PAGE_NAME ="current_page_name";
    public static final String KEY_ROOT_PAGE_NAME ="root_page_name";
    public static final String KEY_EVENT_ID ="event_id";
    public static final String KEY_EVENT ="event";
    public static final String KEY_PAGE_DESC ="page_desc";
    public static final String KEY_USER_NAME ="ud_name";
    public static final String KEY_USER_ID ="ud_id";

    public static final String KEY_BATTERY_TOTAL = "battery_total";
    public static final String KEY_BATTERY_CHARGE_TYPE = "battery_charge_type";
    public static final String KEY_BATTERY_STATUS = "battery_status";
    public static final String KEY_BATTERY_USE = "battery_use";

    public static final String KEY_MEMORY_TOTAL = "memory_total";
    public static final String KEY_MEMORY_USE = "memory_use";
    public static final String KEY_CPU_NO = "cpu_no";
    public static final String KEY_CPU_USE = "cpu_use";
    public static final String KEY_CPU_TEMPERATURE = "cpu_temperature";
    public static final String KEY_CPU_HZ = "cpu_hz";

    public static final String KEY_GPU_MODEL = "gpu_model";
    public static final String KEY_GPU_HZ = "gpu_hz";
    public static final String KEY_GPU_RATE = "gpu_rate";

    public static final String KEY_NETWORK_TYPE = "network_type";
    public static final String KEY_NETWORK_STRENGTH = "network_strength";
    public static final String KEY_NETWORK_IN_RATE = "network_in_rate";
    public static final String KEY_NETWORK_OUT_RATE = "network_out_rate";
    public static final String KEY_NETWORK_PROXY = "network_proxy";
    public static final String KEY_NETWORK_DNS = "dns";
    public static final String KEY_NETWORK_ROAM = "roam";
    public static final String KEY_NETWORK_WIFI_SSID = "wifi_ssid";
    public static final String KEY_NETWORK_WIFI_IP = "wifi_ip";
    public static final String KEY__NETWORK_TCP_TIME = "_network_tcp_time";
    public static final String KEY__NETWORK_DNS_TIME = "_network_dns_time";
    public static final String KEY__NETWORK_RESPONSE_TIME = "_network_response_time";
    public static final String KEY_NETWORK_TCP_TIME = "network_tcp_time";
    public static final String KEY_NETWORK_DNS_TIME = "network_dns_time";
    public static final String KEY_NETWORK_RESPONSE_TIME = "network_response_time";
    public static final String KEY_NETWORK_ERROR_RATE = "network_error_rate";

    public static final String KEY_LOCATION_PROVINCE = "province";
    public static final String KEY_LOCATION_CITY = "city";
    public static final String KEY_LOCATION_COUNTRY = "country";
    public static final String KEY_LOCATION_LATITUDE = "latitude";
    public static final String KEY_LOCATION_LONGITUDE = "longitude";
    public static final String KEY_LOCATION_GPS_OPEN = "gps_open";

    public static final String KEY_DEVICE_OPEN_TIME = "device_open_time";
    public static final String KEY_DEVICE_NAME = "device_name";

    public static final String KEY_BT_DEVICE = "bt_device";
    public static final String KEY_BT_OPEN = "bt_open";

    public static final String KEY_SENSOR_BRIGHTNESS = "screen_brightness";
    public static final String KEY_SENSOR_LIGHT = "light";
    public static final String KEY_SENSOR_PROXIMITY = "proximity";
    public static final String KEY_SENSOR_STEPS = "steps";
    public static final String KEY_SENSOR_ROTATION_X= "rotation_x";
    public static final String KEY_SENSOR_ROTATION_Y= "rotation_y";
    public static final String KEY_SENSOR_ROTATION_Z= "rotation_z";
    public static final String KEY_SENSOR_ACCELERATION_X= "acceleration_x";
    public static final String KEY_SENSOR_ACCELERATION_Y= "acceleration_y";
    public static final String KEY_SENSOR_ACCELERATION_Z= "acceleration_z";
    public static final String KEY_SENSOR_MAGNETIC_X= "magnetic_x";
    public static final String KEY_SENSOR_MAGNETIC_Y= "magnetic_y";
    public static final String KEY_SENSOR_MAGNETIC_Z= "magnetic_z";

    public static final String KEY_FPS= "fps";
    public static final String KEY_TORCH= "torch";

    public static final String KEY_DEVICE_UUID = "device_uuid";
    public static final String KEY_DEVICE_APPLICATION_ID = "application_identifier";
    public static final String KEY_DEVICE_APPLICATION_NAME = "application_name";
    public static final String KEY_DEVICE_AGENT = "agent";
    public static final String KEY_DEVICE_AUTO_TRACK = "autoTrack";
    public static final String KEY_DEVICE_IMEI = "imei";
    public static final String KEY_DEVICE_OS = "os";
    public static final String KEY_DEVICE_OS_VERSION = "os_version";
    public static final String KEY_DEVICE_DEVICE_BAND = "device_band";
    public static final String KEY_DEVICE_DEVICE_MODEL = "device_model";
    public static final String KEY_DEVICE_DISPLAY = "display";
    public static final String KEY_DEVICE_CARRIER = "carrier";
    public static final String KEY_DEVICE_LOCALE = "locale";
    public static final String KEY_DEVICE_OAID = "oaid";
    public static final String KEY_APP_VERSION_NAME = "app_version_name";

    public static final String EVENT_NAME_LAUNCH = "launch";
    public static final String EVENT_NAME_CLICK = "click";
    public static final String EVENT_NAME_LEAVE = "leave";
    public static final String EVENT_NAME_ENTER = "enter";


}
