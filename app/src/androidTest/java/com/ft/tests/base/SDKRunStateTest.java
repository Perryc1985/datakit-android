package com.ft.tests.base;

import android.content.Context;
import android.os.Looper;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.ft.AccountUtils;
import com.ft.BaseTest;
import com.ft.application.MockApplication;
import com.ft.sdk.EnvType;
import com.ft.sdk.FTAutoTrackType;
import com.ft.sdk.FTSDKConfig;
import com.ft.sdk.FTSdk;
import com.ft.sdk.MonitorType;
import com.ft.sdk.TraceType;
import com.ft.sdk.garble.FTAutoTrackConfig;
import com.ft.sdk.garble.FTUserActionConfig;
import com.ft.sdk.garble.FTHttpConfig;
import com.ft.sdk.garble.FTMonitorConfig;
import com.ft.sdk.FTExceptionHandler;
import com.ft.sdk.garble.utils.NetUtils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.reflect.Whitebox;

import static com.ft.AllTests.hasPrepare;

/**
 * author: huangDianHua
 * time: 2020/8/25 13:42:48
 * description:
 */
@RunWith(AndroidJUnit4.class)
public class SDKRunStateTest extends BaseTest {
    Context context;

    @Before
    public void setUp() {
        if (!hasPrepare) {
            Looper.prepare();
            hasPrepare = true;
        }
        context = MockApplication.getContext();
        FTSDKConfig ftSDKConfig = FTSDKConfig.builder(AccountUtils.getProperty(context, AccountUtils.ACCESS_SERVER_URL))
                .setXDataKitUUID("ft-dataKit-uuid-001")
                .setUseOAID(true)//设置 OAID 是否可用
                .setDebug(true)//设置是否是 debug
                .setMonitorType(MonitorType.ALL)//设置监控项
                .setEnableTrackAppCrash(true)
                .setEnableTrackAppANR(true)
                .setEnableTrackAppUIBlock(true)
                .setEnv(EnvType.GRAY)
                .setNetworkTrace(true)
                .setTraceConsoleLog(true)
                .setEnableTraceUserAction(true)
                .setTraceType(TraceType.ZIPKIN);
        FTSdk.install(ftSDKConfig);
    }

    @Test
    public void enableAutoTrackTest() {
        Assert.assertTrue(FTAutoTrackConfig.get().isAutoTrack());
    }

    @Test
    public void enableAutoTrackTypeTest() {
        Assert.assertTrue(FTAutoTrackConfig.get().enableAutoTrackType(FTAutoTrackType.APP_CLICK));
    }

    @Test
    public void monitorTypeTest() {
        Assert.assertTrue(FTMonitorConfig.get().isMonitorType(MonitorType.ALL));
    }

    @Test
    public void networkTrackTest() {
        Assert.assertTrue(FTHttpConfig.get().networkTrace);
    }

    @Test
    public void trackConsoleLogTest() {
        Assert.assertTrue(FTExceptionHandler.get().isTrackConsoleLog());
    }

    @Test
    public void traceUserActionTest() {
        Assert.assertTrue(FTUserActionConfig.get().isEnableTraceUserAction());
    }

    @Test
    public void showdownEnableAutoTrackTest() {
        FTSdk.get().shutDown();
        Assert.assertFalse(FTAutoTrackConfig.get().isAutoTrack());
    }

    @Test
    public void showdownEnableAutoTrackTypeTest() {
        FTSdk.get().shutDown();
        Assert.assertTrue(FTAutoTrackConfig.get().enableAutoTrackType(FTAutoTrackType.APP_CLICK));
    }

    @Test
    public void showdownMonitorTypeTest() {
        FTSdk.get().shutDown();
        Assert.assertFalse(FTMonitorConfig.get().isMonitorType(MonitorType.ALL));
    }

    @Test
    public void showdownNetworkTrackTest() {
        FTSdk.get().shutDown();
        Assert.assertFalse(FTHttpConfig.get().networkTrace);
    }

    @Test
    public void showdownTrackConsoleLogTest() {
        FTSdk.get().shutDown();
        Assert.assertFalse(FTExceptionHandler.get().isTrackConsoleLog());
    }

    @Test
    public void showdownEventFlowLogTest() {
        FTSdk.get().shutDown();
        Assert.assertFalse(FTUserActionConfig.get().isEnableTraceUserAction());
    }
}
