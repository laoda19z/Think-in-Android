package com.example.uidemo.dynamic;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import androidx.core.app.ActivityCompat;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GPS {
    private Context context;
    private LocationManager locationManager;
    private static final String TAG = "GPS-Info";
    private String mylocation;
    EventBus eventBus;
    public GPS(Context context) {
        this.context = context;
        initLocationManager();
    }

    public String getMylocation() {
        return mylocation;
    }

    public void setMylocation(String mylocation) {
        this.mylocation = mylocation;
    }

    /**
     * 获取权限，并检查有无开户GPS
     */
    private void initLocationManager() {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            // 转到手机设置界面，用户设置GPS
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
        getProviders();
    }

    /**
     * 获取可定位的方式
     */
    private MyLocationListener myLocationListener;
    private String bestProvider;
    private void getProviders() {
        //获取定位方式
        List<String> providers = locationManager.getProviders(true);
        for(String s:providers){
            Log.e(TAG,s);
        }

        Criteria criteria = new Criteria();
        // 查询精度：高，Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精确
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        // 是否查询海拨：否
        criteria.setAltitudeRequired(true);
        // 是否查询方位角 : 否
        criteria.setBearingRequired(false);
        // 设置是否要求速度
        criteria.setSpeedRequired(false);
        // 电量要求：低
//        criteria.setPowerRequirement(Criteria.ACCURACY_LOW);
        bestProvider = locationManager.getBestProvider(criteria, false);  //获取最佳定位
        myLocationListener = new MyLocationListener();

    }

    public void startLocation(){
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Log.e(TAG, "startLocation: ");

        locationManager.requestLocationUpdates(bestProvider, 0, 0, myLocationListener);

    }

    public void stopLocation(){
        Log.e(TAG, "stopLocation: ");
        locationManager.removeUpdates(myLocationListener);

    }

    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            //定位时调用
            Log.e(TAG,"onLocationChanged");

            List<Address> addresses = new ArrayList<>();
            //经纬度转城市
            Geocoder geocoder = new Geocoder(context);
            try {
                addresses =geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),10);
            } catch (IOException e) {
                e.printStackTrace();
            }
            for(Address address:addresses){
                //国家  CN
                Log.e(TAG,address.getCountryCode());
                //国家
                Log.e(TAG,address.getCountryName());
                //省，市，地址
                Log.e(TAG,address.getAdminArea());
                Log.e(TAG,address.getLocality());
                Log.e(TAG,address.getFeatureName());
                mylocation = address.getCountryName()+address.getAdminArea()+address.getLocality()+address.getFeatureName()+"";
                //经纬度
                Log.e(TAG, String.valueOf(address.getLatitude()));
                Log.e(TAG, String.valueOf(address.getLongitude()));
                eventBus.getDefault().postSticky(GPS.this);

                stopLocation();
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            //定位状态改变
            Log.e(TAG,"onStatusChanged");

        }

        @Override
        public void onProviderEnabled(String provider) {
            //定位开启
            Log.e(TAG,"onProviderEnabled");
        }
        @Override
        public void onProviderDisabled(String provider) {
            //定位关闭
            Log.e(TAG,"onProviderDisabled");
        }
    }
}

