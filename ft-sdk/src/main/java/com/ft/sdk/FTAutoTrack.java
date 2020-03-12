package com.ft.sdk;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ft.sdk.garble.FTAutoTrackConfig;
import com.ft.sdk.garble.FTFlowChartConfig;
import com.ft.sdk.garble.FTFragmentManager;
import com.ft.sdk.garble.FTUserConfig;
import com.ft.sdk.garble.bean.OP;
import com.ft.sdk.garble.bean.RecordData;
import com.ft.sdk.garble.manager.FTActivityManager;
import com.ft.sdk.garble.manager.FTManager;
import com.ft.sdk.garble.manager.SyncDataManager;
import com.ft.sdk.garble.utils.AopUtils;
import com.ft.sdk.garble.utils.Constants;
import com.ft.sdk.garble.utils.LogUtils;
import com.ft.sdk.garble.utils.ThreadPoolUtils;
import com.ft.sdk.garble.utils.Utils;

import org.json.JSONObject;

/**
 * BY huangDianHua
 * DATE:2019-12-02 16:43
 * Description
 */
public class FTAutoTrack {

    /**
     * Activity 打开的方式（标记是从 Fragment 打开还是 Activity）
     * @param fromFragment
     * @param intent
     */
    public static void startActivityByWay(Boolean fromFragment, Intent intent){
        LogUtils.d("activityFromWay="+fromFragment+",,,intent="+intent);
        if(intent != null && intent.getComponent() != null) {
            FTActivityManager.get().putActivityStatus(intent.getComponent().getClassName(),fromFragment);
        }
    }

    public static void notifyUserVisibleHint(Object clazz, Object activity,boolean isVisible){

    }
    /**
     * 启动 APP
     */
    public static void startApp(Object object) {
        try {
            startApp();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Activity 开启
     */
    public static void activityOnCreate(Class clazz) {
        try {
            //startPage(clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Activity 关闭
     */
    public static void activityOnDestroy(Class clazz) {
        try {
            //destroyPage(clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Fragment 打开
     *
     * @param clazz
     * @param activity
     */
    public static void fragmentOnCreateView(Object clazz, Object activity) {
        try {
            //startPage(clazz, activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Fragment 关闭
     *
     * @param clazz
     * @param activity
     */
    public static void fragmentOnDestroyView(Object clazz, Object activity) {
        try {
            //destroyPage(clazz, activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 点击事件
     *
     * @param view
     */
    public static void trackViewOnClick(View view) {
        if (view == null) {
            return;
        }
        trackViewOnClick(null, view, view.isPressed());
    }

    /**
     * RadioGroup的点击选择事件
     *
     * @param group
     * @param checkedId
     */
    public static void trackRadioGroup(RadioGroup group, int checkedId) {
        if (group == null) {
            return;
        }

        trackViewOnClick(null, group, true);
    }

    /**
     * listView点击事件
     *
     * @param parent
     * @param v
     * @param position
     */
    public static void trackListView(AdapterView<?> parent, View v, int position) {
        trackViewOnClick(null, v, true);
    }

    /**
     * ExpandableList 父点击事件
     *
     * @param parent
     * @param v
     * @param position
     */
    public static void trackExpandableListViewOnGroupClick(ExpandableListView parent, View v, int position) {
        trackViewOnClick(null, v, true);
    }

    /**
     * TabHost切换
     *
     * @param tabName
     */
    public static void trackTabHost(String tabName) {
        //trackViewOnClick(null, v, true);
    }

    /**
     * ExpandableList 子点击事件
     *
     * @param parent
     * @param v
     * @param parentPosition
     * @param childPosition
     */
    public static void trackExpandableListViewOnChildClick(ExpandableListView parent, View v, int parentPosition, int childPosition) {
        trackViewOnClick(null, v, true);
    }

    /**
     * 点击事件
     *
     * @param object
     * @param view
     */
    public static void trackViewOnClick(Object object, View view) {
        try {
            if (view == null) {
                return;
            }

            trackViewOnClick(object, view, view.isPressed());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 点击事件
     *
     * @param object
     * @param view
     * @param isFromUser
     */
    public static void trackViewOnClick(Object object, View view, boolean isFromUser) {
        try {
            if (isFromUser) {
                if (object == null) {
                    object = AopUtils.getActivityFromContext(view.getContext());
                }
                clickView(view, object.getClass(), AopUtils.getClassName(object), AopUtils.getSupperClassName(object), AopUtils.getViewTree(view));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void trackMenuItem(MenuItem menuItem) {
        try {
            trackMenuItem(null, menuItem);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void trackMenuItem(Object object, MenuItem menuItem) {
        try {
            clickView((Class<?>) object, AopUtils.getClassName(object), AopUtils.getSupperClassName(object), "MenuItem/" + menuItem);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void trackDialog(DialogInterface dialogInterface, int whichButton) {
        try {
            Dialog dialog = null;
            if (dialogInterface instanceof Dialog) {
                dialog = (Dialog) dialogInterface;
            }

            if (dialog == null) {
                return;
            }
            Context context = dialog.getContext();
            Activity activity = AopUtils.getActivityFromContext(context);
            if (activity == null) {
                activity = dialog.getOwnerActivity();
            }

            clickView(activity.getClass(), AopUtils.getClassName(activity), AopUtils.getSupperClassName(activity), AopUtils.getDialogClickView(dialog, whichButton));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * APP 启动
     */
    public static void startApp() {
        if (!FTAutoTrackConfig.get().isAutoTrack()) {
            return;
        }
        if (!FTAutoTrackConfig.get().enableAutoTrackType(FTAutoTrackType.APP_START)) {
            return;
        }
        putRecordClick(OP.LANC, null, null, null);
    }

    /**
     * 打开某个Fragment页面
     *
     * @param clazz
     * @param activity
     */
    public static void startPage(Object clazz, Object activity,String parentPage) {
        /*没有开启自动埋点*/
        if (!FTAutoTrackConfig.get().isAutoTrack()) {
            return;
        }
        /*设置了白名单，但当前事件不在其中*/
        if (!FTAutoTrackConfig.get().enableAutoTrackType(FTAutoTrackType.APP_START)) {
            return;
        }

        /*设置了白名单，但当前页面不在其中*/
        if (!FTAutoTrackConfig.get().isOnlyAutoTrackActivity(activity.getClass())) {
            return;
        }
        /*设置了白名单，但当前页面不在其中*/
        if (!FTAutoTrackConfig.get().isOnlyAutoTrackActivity((Class<?>) clazz)) {
            return;
        }
        /*设置了黑名单，且事件在其中*/
        if (FTAutoTrackConfig.get().disableAutoTrackType(FTAutoTrackType.APP_START)) {
            return;
        }
        /*设置了黑名单，且页面在其中*/
        if (FTAutoTrackConfig.get().isIgnoreAutoTrackActivity(activity.getClass())) {
            return;
        }
        /*设置了黑名单，且页面在其中*/
        if (FTAutoTrackConfig.get().isIgnoreAutoTrackActivity((Class<?>) clazz)) {
            return;
        }
        putRecordFragment(OP.OPEN_FRA, AopUtils.getClassName(clazz), AopUtils.getActivityName(activity),parentPage);
    }

    /**
     * 关闭某个Fragment
     *
     * @param clazz
     * @param activity
     */
    public static void destroyPage(Object clazz, Object activity,String parentPage) {
        /*没有开启自动埋点*/
        if (!FTAutoTrackConfig.get().isAutoTrack()) {
            return;
        }
        /*设置了白名单，但当前事件不在其中*/
        if (!FTAutoTrackConfig.get().enableAutoTrackType(FTAutoTrackType.APP_END)) {
            return;
        }

        /*设置了白名单，但当前页面不在其中*/
        if (!FTAutoTrackConfig.get().isOnlyAutoTrackActivity(activity.getClass())) {
            return;
        }
        /*设置了白名单，但当前页面不在其中*/
        if (!FTAutoTrackConfig.get().isOnlyAutoTrackActivity((Class<?>) clazz)) {
            return;
        }
        /*设置了黑名单，且事件在其中*/
        if (FTAutoTrackConfig.get().disableAutoTrackType(FTAutoTrackType.APP_END)) {
            return;
        }
        /*设置了黑名单，且页面在其中*/
        if (FTAutoTrackConfig.get().isIgnoreAutoTrackActivity(activity.getClass())) {
            return;
        }
        /*设置了黑名单，且页面在其中*/
        if (FTAutoTrackConfig.get().isIgnoreAutoTrackActivity((Class<?>) clazz)) {
            return;
        }
        putRecordFragment(OP.CLS_FRA, AopUtils.getClassName(clazz), AopUtils.getActivityName(activity),parentPage);
    }

    /**
     * 打开某个Activity页面
     *
     * @param clazz
     */
    public static void startPage(Class<?> clazz) {
        /*没有开启自动埋点*/
        if (!FTAutoTrackConfig.get().isAutoTrack()) {
            return;
        }
        /*设置了白名单，但当前事件不在其中*/
        if (!FTAutoTrackConfig.get().enableAutoTrackType(FTAutoTrackType.APP_START)) {
            return;
        }
        /*设置了白名单，但当前页面不在其中*/
        if (!FTAutoTrackConfig.get().isOnlyAutoTrackActivity(clazz)) {
            return;
        }
        /*设置了黑名单，且事件在其中*/
        if (FTAutoTrackConfig.get().disableAutoTrackType(FTAutoTrackType.APP_START)) {
            return;
        }
        /*设置了黑名单，且页面在其中*/
        if (FTAutoTrackConfig.get().isIgnoreAutoTrackActivity(clazz)) {
            return;
        }
        putRecordActivity(OP.OPEN_ACT, clazz);
    }

    /**
     * 关闭某个Activity
     *
     * @param clazz
     */
    public static void destroyPage(Class<?> clazz) {
        /*没有开启自动埋点*/
        if (!FTAutoTrackConfig.get().isAutoTrack()) {
            return;
        }
        /*设置了白名单，但当前事件不在其中*/
        if (!FTAutoTrackConfig.get().enableAutoTrackType(FTAutoTrackType.APP_END)) {
            return;
        }
        /*设置了白名单，但当前页面不在其中*/
        if (!FTAutoTrackConfig.get().isOnlyAutoTrackActivity(clazz)) {
            return;
        }
        /*设置了黑名单，且事件在其中*/
        if (FTAutoTrackConfig.get().disableAutoTrackType(FTAutoTrackType.APP_END)) {
            return;
        }
        /*设置了黑名单，且页面在其中*/
        if (FTAutoTrackConfig.get().isIgnoreAutoTrackActivity(clazz)) {
            return;
        }
        putRecordActivity(OP.CLS_ACT, clazz);
    }

    /**
     * 点击事件
     *
     * @param clazz
     * @param currentPage
     * @param rootPage
     * @param vtp
     */
    public static void clickView(Class<?> clazz, String currentPage, String rootPage, String vtp) {
        if (!FTAutoTrackConfig.get().isAutoTrack()) {
            return;
        }
        if (!FTAutoTrackConfig.get().enableAutoTrackType(FTAutoTrackType.APP_CLICK)) {
            return;
        }

        if (!FTAutoTrackConfig.get().isOnlyAutoTrackActivity(clazz)) {
            return;
        }

        if (FTAutoTrackConfig.get().disableAutoTrackType(FTAutoTrackType.APP_CLICK)) {
            return;
        }
        if (FTAutoTrackConfig.get().isIgnoreAutoTrackActivity(clazz)) {
            return;
        }
        putRecordClick(OP.CLK, currentPage, rootPage, vtp);
    }

    /**
     * 点击事件
     *
     * @param view
     * @param clazz
     * @param currentPage
     * @param rootPage
     * @param vtp
     */
    public static void clickView(View view, Class<?> clazz, String currentPage, String rootPage, String vtp) {
        if (!FTAutoTrackConfig.get().isAutoTrack()) {
            return;
        }
        if (!FTAutoTrackConfig.get().enableAutoTrackType(FTAutoTrackType.APP_CLICK)) {
            return;
        }
        if (!FTAutoTrackConfig.get().isOnlyAutoTrackActivity(clazz)) {
            return;
        }
        if (!FTAutoTrackConfig.get().isOnlyView(view)) {
            return;
        }

        if (FTAutoTrackConfig.get().disableAutoTrackType(FTAutoTrackType.APP_CLICK)) {
            return;
        }
        if (FTAutoTrackConfig.get().isIgnoreAutoTrackActivity(clazz)) {
            return;
        }
        if (FTAutoTrackConfig.get().isIgnoreView(view)) {
            return;
        }
        putRecordClick(OP.CLK, currentPage, rootPage, vtp);
    }

    /**
     * 点击事件
     * @param op
     * @param currentPage
     * @param rootPage
     * @param vtp
     */
    public static void putRecordClick(@NonNull OP op, @Nullable String currentPage, @Nullable String rootPage, @Nullable String vtp){
        long time = System.currentTimeMillis();
        putRecord(time,op, currentPage, rootPage,null, vtp);
    }

    /**
     * Fragment 开关
     * @param op
     * @param currentPage
     * @param rootPage
     * @param parentPage
     */
    public static void putRecordFragment(@NonNull OP op, @Nullable String currentPage, @Nullable String rootPage,@Nullable String parentPage) {
        long time = System.currentTimeMillis();
        putRecord(time, op, currentPage, rootPage,parentPage, null);
    }

    /**
     * Activity 开关
     * @param op
     * @param classCurrent
     */
    public static void putRecordActivity(@NonNull OP op,Class classCurrent) {
        long time = System.currentTimeMillis();
        Class parentClass = FTActivityManager.get().getLastActivity();
        String parentPageName = Constants.FLOW_ROOT;
        if(parentClass != null) {
            boolean isFromFragment = FTActivityManager.get().getActivityStatus(classCurrent.getName());
            if(isFromFragment){
                Class c = FTFragmentManager.getInstance().getLastFragmentName(parentClass.getName());
                if(c != null){
                    parentPageName = parentClass.getSimpleName()+"."+c.getSimpleName();
                }
            }else{
                parentPageName = parentClass.getSimpleName();
            }

        }
        putRecord(time, op, classCurrent.getSimpleName(), classCurrent.getSimpleName(),parentPageName, null);
    }

    public static void putRecord(long time, @NonNull OP op, @Nullable String currentPage, @Nullable String rootPage,@Nullable String parentPage, @Nullable String vtp) {
        ThreadPoolUtils.get().execute(() -> {
            try {
                final RecordData recordData = new RecordData();
                recordData.setTime(time);
                recordData.setOp(op.value);
                recordData.setCpn(currentPage);
                recordData.setRpn(rootPage);
                JSONObject opData = new JSONObject();
                try {
                    opData.put("vtp", vtp);
                    JSONObject tags = new JSONObject();
                    SyncDataManager.addMonitorData(tags);
                    opData.put("tags", tags);
                    //开启流程图，获取流程图相关数据存入数据库中
                    if (FTFlowChartConfig.get().isOpenFlowChart()) {
                        if (op == OP.OPEN_ACT || op == OP.CLS_ACT || op == OP.OPEN_FRA || op == OP.CLS_FRA) {
                            opData.put(Constants.MEASUREMENT, FTFlowChartConfig.get().getFlowProduct());
                        }
                    }
                    recordData.setOpdata(opData.toString());
                } catch (Exception e) {
                }
                String sessionId = FTUserConfig.get().getSessionId();
                if (!Utils.isNullOrEmpty(sessionId)) {
                    recordData.setSessionid(sessionId);
                }
                //开启流程图，获取流程图相关数据存入数据库中
                if (FTFlowChartConfig.get().isOpenFlowChart()){
                    if (op == OP.OPEN_ACT || op == OP.CLS_ACT || op == OP.OPEN_FRA || op == OP.CLS_FRA){
                        recordData.setPpn(parentPage);
                        recordData.setTraceId(FTFlowChartConfig.get().getFlowUUID());
                        long currentTime = System.currentTimeMillis();
                        recordData.setDuration(currentTime - FTFlowChartConfig.get().lastOpTime);
                        FTFlowChartConfig.get().lastOpTime = currentTime;
                    }
                }

                LogUtils.d("FTAutoTrack数据进数据库：" + recordData.getJsonString());
                FTManager.getFTDBManager().insertFTOperation(recordData);
                FTManager.getSyncTaskManager().executeSyncPoll();
            } catch (Exception e) {
            }
        });
    }
}
