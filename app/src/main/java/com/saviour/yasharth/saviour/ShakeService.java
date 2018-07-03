package com.saviour.yasharth.saviour;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;



public class ShakeService extends Service implements ShakeListener.OnShakeListener {
    private ShakeListener mShaker;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    public int check;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public void onCreate() {

        super.onCreate();
        this.mSensorManager = ((SensorManager)getSystemService(Context.SENSOR_SERVICE));
        this.mAccelerometer = this.mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShaker = new ShakeListener(this);
        mShaker.setOnShakeListener(this);
        Toast.makeText(ShakeService.this, "Automatic Tracking Active!",Toast.LENGTH_LONG).show();
        Log.d(getPackageName(), "Created the Service!");
        check=1;

    }
    @Override
    public void onShake() {
        if(check==1) {
            Toast.makeText(ShakeService.this, "SHAKEN!", Toast.LENGTH_LONG).show();
            final Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vib.vibrate(500);

            MainActivity activity=MainActivity.instance;
               if(activity==null){
               Intent intent = new Intent(ShakeService.this, LoginActivity.class);
               intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP); // You need this if starting
               //  the activity from a service
               intent.setAction(Intent.ACTION_MAIN);
               intent.addCategory(Intent.CATEGORY_LAUNCHER);
               startActivity(intent);
                   new Handler().postDelayed(new Runnable() {

                       public void run() {
                           MainActivity.instance.alertD();
                       }
                   }, 3000);

           }
           else{
                   Intent intent = new Intent(ShakeService.this, MainActivity.class);
                   intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                   startActivity(intent);
                   MainActivity.instance.alertD();
               }
        }

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return  START_STICKY;
      //return super.onStartCommand(intent, flags, startId);

    }
    public void onDestroy(){
        super.onDestroy();
        check=0;
        Log.d(getPackageName(),"Service Destroyed.");
    }
}
