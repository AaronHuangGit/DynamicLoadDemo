package com.aaron.android.codelibrary.utils;

import android.content.Context;
import android.text.TextUtils;

/**
 * Created on 15/7/23.
 *
 * @author ran.huang
 * @version 1.0.0
 */
public class DeviceUtils {

    /**
     * 获取Android设备唯一标识
     * @param context 上下文资源
     * @return Android设备唯一标识
     */
    public static String getDeviceInfo(Context context) {
        try{
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);

            String device_id = tm.getDeviceId();

            android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context.getSystemService(Context.WIFI_SERVICE);

            String mac = wifi.getConnectionInfo().getMacAddress();
            json.put("mac", mac);

            if( TextUtils.isEmpty(device_id) ){
                device_id = mac;
            }

            if( TextUtils.isEmpty(device_id) ){
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),android.provider.Settings.Secure.ANDROID_ID);
            }

            json.put("device_id", device_id);
            return json.toString();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
