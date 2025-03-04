package com.ft.sdk;

import com.ft.sdk.garble.http.FTResponseData;
import com.ft.sdk.garble.http.HttpBuilder;
import com.ft.sdk.garble.http.NetCodeStatus;
import com.ft.sdk.garble.http.RequestMethod;
import com.google.mockwebserver.MockResponse;
import com.google.mockwebserver.MockWebServer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.net.HttpURLConnection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * BY huangDianHua
 * DATE:2019-12-16 13:29
 * Description:模拟网络请求测试
 */

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = 21)
public class HttpTest {
    private String successString1 = "{\"code\":200,\"errorCode\":\"\",\"message\":\"\"}";
    private String successString2 = "";
    private String failString = "";
    private MockWebServer mMockWebServer;

    @Before
    public void setUp() {
        mMockWebServer = new MockWebServer();
    }

    @After
    public void tearDown() throws Exception {
        mMockWebServer.shutdown();
    }

    /**
     * 模拟网络 200 情况
     * @throws Exception
     */
    @Test
    public void mockedRequest200() throws Exception {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setBody(failString);
        mockResponse.setResponseCode(HttpURLConnection.HTTP_OK);
        mMockWebServer.enqueue(mockResponse);
        mMockWebServer.play();

        FTResponseData result = HttpBuilder.Builder()
                .setUrl(mMockWebServer.getUrl("/").toString())
                .setMethod(RequestMethod.POST)
                .setBodyString("")
                .executeSync(FTResponseData.class);
        assertEquals(HttpURLConnection.HTTP_OK,result.getHttpCode());
    }

    /**
     * 模拟网络 400 情况
     * @throws Exception
     */
    @Test
    public void mockedRequest400() throws Exception {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setBody(failString);
        mockResponse.setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST);
        mMockWebServer.enqueue(mockResponse);
        mMockWebServer.play();

        FTResponseData result = HttpBuilder.Builder()
                .setUrl(mMockWebServer.getUrl("/").toString())
                .setMethod(RequestMethod.POST)
                .setBodyString("")
                .executeSync(FTResponseData.class);
        assertEquals(HttpURLConnection.HTTP_BAD_REQUEST,result.getHttpCode());
    }

    /**
     * 模拟网络 200 且返回数据为正确的JSON格式
     * @throws Exception
     */
    @Test
    public void mockedRequestJSON() throws Exception {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setBody(successString1);
        mockResponse.setResponseCode(HttpURLConnection.HTTP_OK);
        mMockWebServer.enqueue(mockResponse);
        mMockWebServer.play();

        FTResponseData result = HttpBuilder.Builder()
                .setUrl(mMockWebServer.getUrl("/").toString())
                .setMethod(RequestMethod.POST)
                .setBodyString("")
                .executeSync(FTResponseData.class);
        assertEquals( HttpURLConnection.HTTP_OK,result.getCode());
    }

    /**
     * 模拟网络 200 且返回数据不是JSON格式
     * @throws Exception
     */
    @Test
    public void mockedRequest400NotJson() throws Exception {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setBody(successString2);
        mockResponse.setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST);
        mMockWebServer.enqueue(mockResponse);
        mMockWebServer.play();

        FTResponseData result = HttpBuilder.Builder()
                .setUrl(mMockWebServer.getUrl("/").toString())
                .setMethod(RequestMethod.POST)
                .setBodyString("")
                .executeSync(FTResponseData.class);
        assertEquals(NetCodeStatus.NET_STATUS_RESPONSE_NOT_JSON,result.getCode());
    }
}
