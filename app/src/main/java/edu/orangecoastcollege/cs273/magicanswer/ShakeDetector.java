package edu.orangecoastcollege.cs273.magicanswer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by phuynh101 on 10/31/2017.
 */

public class ShakeDetector implements SensorEventListener {

    private static final long ELAPSED_TIME = 1000L;
    //accelerometer data uses float
    private static final float THRESHOLD = 20;
    private long previousShake;

    private OnShakeListener mListener;

    public ShakeDetector(OnShakeListener listener)
    {
        mListener = listener;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        //ignore all other events, except ACCELEROMETER
        if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            //neutralize the effect of gravity by subtracting it out from each value
            float gForceX = x - SensorManager.GRAVITY_EARTH;
            float gForceY = y - SensorManager.GRAVITY_EARTH;
            float gForceZ = z - SensorManager.GRAVITY_EARTH;

            float netForce = (float) Math.sqrt(Math.pow(gForceX,2) + Math.pow(gForceY,2) + Math.pow(gForceZ,2));
            if(netForce >= THRESHOLD)
            {
                //get current time
                long currentTime = System.currentTimeMillis();
                if(currentTime > previousShake + ELAPSED_TIME)
                {
                    //reset the previous shake to the current time
                    previousShake = currentTime;

                    //Register a shake event (it qualifies)
                    mListener.onShake();
                }

            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        //Do nothing, not being used
    }

    //define an interface for others to implement whenever a true shake occurs.
    // interface = contract (method declarations without implementation)
    // some other class has to implement the method

    public interface OnShakeListener
    {
        void onShake();
    }
}
