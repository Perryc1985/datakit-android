package com.ft.sdk.tests;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.ft.sdk.garble.bean.DataType;
import com.ft.sdk.garble.bean.OP;
import com.ft.sdk.garble.bean.ObjectBean;
import com.ft.sdk.garble.bean.SyncJsonData;
import com.ft.sdk.garble.bean.TrackBean;
import com.ft.sdk.garble.manager.SyncDataHelper;
import com.ft.sdk.garble.utils.Constants;
import com.ft.sdk.garble.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * BY huangDianHua
 * DATE:2019-12-16 18:06
 * Description:
 */
public class UtilsTest {

    private static final String TEST_MEASUREMENT_INFLUX_DB_LINE = "TestInfluxDBLine";
    private static final String KEY_TAGS = "tags1";
    private static final String KEY_FIELD = "field1";
    private static final String VALUE_TAGS = "tags1-value";
    private static final String VALUE_FIELD = "field1-value";
    public static final long VALUE_TIME = 1598512145640L;
    private static final String SINGLE_LINE_DATA = TEST_MEASUREMENT_INFLUX_DB_LINE + "," + KEY_TAGS + "=" + VALUE_TAGS + " " + KEY_FIELD + "=\"" + VALUE_FIELD + "\" " + VALUE_TIME * 1000000 + "\n";
    private static final String LOG_EXPECT_DATA = SINGLE_LINE_DATA + SINGLE_LINE_DATA + SINGLE_LINE_DATA;


    private Context getContext() {
        return InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    @Test
    public void isNetworkAvailable() {
        assertTrue(Utils.isNetworkAvailable(getContext()));
    }

    @Test
    public void contentMD5Encode() {
        assertEquals("M1QEWjl2Ic2SQG8fmM3ikg==", Utils.contentMD5EncodeWithBase64("1122334455"));
    }

    @Test
    public void getHMacSha1() {
        //String value = Utils.getHMacSha1("screct", "POST" + "\n" + "xrKOMvb4g+/lSVHoW8XcaA==" + "\n" + "application/json" + "\n" + "Wed, 02 Sep 2020 09:41:24 GMT");
        assertEquals("4me5NXJallTGFmZiO3csizbWI90=", Utils.getHMacSha1("screct", "123456"));
    }


    /**
     * 验证日志传输行协议格式
     *
     * @throws JSONException
     * @throws InterruptedException
     */
    @Test
    public void logLineProtocolFormatTest() throws JSONException, InterruptedException {

        SyncJsonData recordData = new SyncJsonData(DataType.LOG);
        recordData.setTime(VALUE_TIME);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constants.MEASUREMENT, TEST_MEASUREMENT_INFLUX_DB_LINE);
        JSONObject tags = new JSONObject();
        tags.put(KEY_TAGS, VALUE_TAGS);
        JSONObject fields = new JSONObject();
        fields.put(KEY_FIELD, VALUE_FIELD);
        jsonObject.put(Constants.TAGS, tags);
        jsonObject.put(Constants.FIELDS, fields);
        recordData.setDataString(jsonObject.toString());

        List<SyncJsonData> recordDataList = new ArrayList<>();
        recordDataList.add(recordData);
        recordDataList.add(recordData);
        recordDataList.add(recordData);
        String content = new SyncDataHelper().getBodyContent(DataType.LOG, recordDataList);
        Assert.assertEquals(LOG_EXPECT_DATA, content);
    }

    /**
     *
     * 事件数据内容校验
     *
     * @throws JSONException
     */
    @Test
    public void trackLineProtocolFormatTest() throws JSONException {
        JSONObject tags = new JSONObject();
        tags.put(KEY_TAGS, VALUE_TAGS);
        JSONObject fields = new JSONObject();
        fields.put(KEY_FIELD, VALUE_FIELD);
        TrackBean trackBean = new TrackBean(TEST_MEASUREMENT_INFLUX_DB_LINE, tags, fields, VALUE_TIME);
        SyncJsonData data = SyncJsonData.getFromTrackBean(trackBean, OP.HTTP_CLIENT);

        List<SyncJsonData> recordDataList = new ArrayList<>();
        recordDataList.add(data);
        String content = new SyncDataHelper().getBodyContent(DataType.TRACK, recordDataList);
        Assert.assertTrue(content.contains(TEST_MEASUREMENT_INFLUX_DB_LINE));
        Assert.assertTrue(content.contains(SINGLE_LINE_DATA.replace(TEST_MEASUREMENT_INFLUX_DB_LINE, "")));

    }

    /**
     *
     * 对象数据内容校验
     *
     * @throws JSONException
     *
     */

    @Test
    public void objectDataTest() throws JSONException {

        String objectName = "objectTest";
        ObjectBean objectBean = new ObjectBean(objectName);
        ArrayList<SyncJsonData> list = new ArrayList<>();

        list.add(SyncJsonData.getFromObjectData(objectBean));
        String content = new SyncDataHelper().getBodyContent(DataType.OBJECT, list);

        JSONArray array = new JSONArray(content);
        Assert.assertTrue(array.length() > 0);
        JSONObject jsonObject = array.optJSONObject(0);
        String name = jsonObject.optString(ObjectBean.INNER_NAME);
        Assert.assertEquals(name, objectName);
        JSONObject tags = jsonObject.optJSONObject(ObjectBean.INNER_TAGS);
        String clazz = tags.optString(ObjectBean.INNER_CLASS);
        Assert.assertEquals(clazz, Constants.DEFAULT_OBJECT_CLASS);


    }
}
