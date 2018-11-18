package com.thinkman.hookimei;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.telephony.TelephonyManager;
import android.hardware.SensorManager;
import android.util.Log;
import android.util.SparseArray;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findClass;

public class Main implements IXposedHookLoadPackage {

    private static int stepCount = 1;//must lower than 98800

    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
//        if( !(lpparam.packageName.equals("com.thinkman.getdeviceinfo")
//                || lpparam.packageName.contains("com.imohoo.shanpao")) ) {
//            return;
//        }
        Log.d("THINKMAN", lpparam.processName);

        XposedHelpers.findAndHookMethod(TelephonyManager.class, "getDeviceId", new XC_MethodReplacement() {
            @Override
            protected Object replaceHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
                return "Hook 成功了 哈哈！！！";
            }
        });

        XposedHelpers.findAndHookMethod(TelephonyManager.class, "getSubscriberId", new XC_MethodReplacement() {
            @Override
            protected Object replaceHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
                return "Hook 又成功了 哈哈！！！";
            }
        });

        final Class<?> sensorEL = findClass("android.hardware.SystemSensorManager", lpparam.classLoader);
        XposedBridge.hookAllMethods(sensorEL, "registerListenerImpl", new XC_MethodHook() {

            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Log.d("THINKMAN", "registerListenerImpl " + lpparam.packageName + " " + param.args[1].toString());
            }
        });


        XposedHelpers.findAndHookMethod(ContextWrapper.class, "getSystemService", new XC_MethodReplacement() {
            @Override
            protected Object replaceHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
                Log.d("THINKMAN", "FXXK1");
                if (methodHookParam.args != null && methodHookParam.args.length > 0) {
                    for (Object obj : methodHookParam.args) {
                        Log.d("THINKMAN", obj.toString());
                    }
                } else {
                    Log.d("THINKMAN",  lpparam.packageName + " methodHookParam.args is null");
                }

                return methodHookParam.getResult();
            }
        });

        XposedHelpers.findAndHookMethod(Context.class, "getSystemService", new XC_MethodReplacement() {
            @Override
            protected Object replaceHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
                Log.d("THINKMAN", "FXXK1");
                if (methodHookParam.args != null && methodHookParam.args.length > 0) {
                    for (Object obj : methodHookParam.args) {
                        Log.d("THINKMAN", obj.toString());
                    }
                } else {
                    Log.d("THINKMAN", lpparam.packageName + " methodHookParam.args is null");
                }

                return methodHookParam.getResult();
            }
        });

        XposedHelpers.findAndHookMethod(Activity.class, "getSystemService", new XC_MethodReplacement() {
            @Override
            protected Object replaceHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
                Log.d("THINKMAN", "FXXK1");
                if (methodHookParam.args != null && methodHookParam.args.length > 0) {
                    for (Object obj : methodHookParam.args) {
                        Log.d("THINKMAN", obj.toString());
                    }
                } else {
                    Log.d("THINKMAN", lpparam.packageName + " methodHookParam.args is null");
                }

                return methodHookParam.getResult();
            }
        });


        final Class<?> sensorEL1 = findClass("android.hardware.SystemSensorManager$SensorEventQueue", lpparam.classLoader);

        XposedBridge.hookAllMethods(sensorEL1, "dispatchSensorEvent", new XC_MethodHook() {

            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Log.d("THINKMAN", lpparam.packageName + " dispatchSensorEvent");


                ((float[]) param.args[1])[0] = ((float[]) param.args[1])[0] + 1168 * stepCount;
                stepCount++;

                Field field = param.thisObject.getClass().getEnclosingClass().getDeclaredField("sHandleToSensor");
                field.setAccessible(true);

                int handle = (Integer) param.args[0];
                Sensor sensor = ((SparseArray<Sensor>) field.get(0)).get(handle);
                Log.d("THINKMAN", lpparam.packageName + " sensor = " + sensor);
                XposedBridge.log("sensor = " + sensor);
            }
        });

    }
}
