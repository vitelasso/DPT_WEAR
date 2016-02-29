package com.example.tiagoferreira.androidwear_sensorfusion;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by Tiago on 29/02/2016.
 */
public class TestActivity extends Activity {
    static class Classification {
        String label;
    }

    List<Float> azimuthdata = new ArrayList<Float>();
    List<Float> pitchdata = new ArrayList<Float>();
    List<Float> rolldata = new ArrayList<Float>();
    List<Float> accelxdata = new ArrayList<Float>();
    List<Float> accelydata = new ArrayList<Float>();
    List<Float> accelzdata = new ArrayList<Float>();

    static final float ALPHA = 0.25f;

    private TextView mAzimuthText, mPitchText, mRollText, mWText,mAccelXText,mAccelYText,mAccelZText;

    private Sensor mRotationVectorSensor, mMagneticSensor,mAccelerometer;

    public  float a = 0.1f, mLowPassX,mLowPassY,mLowPassZ;

    private float mAzimuth = 0, mPitch=0,mRoll=0; // degree

    static class Data {
        List<Classification> classifications = new LinkedList<Classification>();
        long timestamp;
    }

    private Data data;
    private boolean active = false;

    private SensorManager mSensorManager = null;

    private static final String FORMAT = "%02d:%02d:%02d";


    public Button clickButton;

    // The following members are only for displaying the sensor output.
    public Handler mHandler;
    //private RadioGroup mRadioGroup;

    private TextView text,text4;

    private int radioSelection;
    public int count=0;

    DecimalFormat d = new DecimalFormat("#.##");

    ArrayList<String> myList = new ArrayList<String>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Keep the Wear screen always on (for testing only!)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mSensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);

        // final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        // stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
        //@Override
        // public void onLayoutInflated(WatchViewStub stub) {
        // get sensorManager and initialise sensor listeners


        clickButton = (Button) findViewById(R.id.getdata);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mRotationVectorSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        mMagneticSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        // mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


        // GUI stuff
        mHandler = new Handler();
        radioSelection = 0;
        d.setRoundingMode(RoundingMode.HALF_UP);
        d.setMaximumFractionDigits(3);
        d.setMinimumFractionDigits(3);
        //mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
        mAzimuthText= (TextView) findViewById(R.id.textView4);
        mPitchText = (TextView) findViewById(R.id.textView5);
        mRollText = (TextView) findViewById(R.id.textView6);
        text4=(TextView)findViewById(R.id.text3);

        clickButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                azimuthdata.clear();
                pitchdata.clear();
                rolldata.clear();
                accelzdata.clear();
                accelydata.clear();
                accelxdata.clear();
                active = true;
                data = new Data();
                data.timestamp = System.currentTimeMillis();

                new CountDownTimer(10000, 1000) { // adjust the milli seconds here

                    public void onTick(long millisUntilFinished) {

                        text4.setText("" + String.format(FORMAT,
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                    }

                    public void onFinish() {
                        //text4.setText("done!");
                    }
                }.start();

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (data.classifications.size() >= 3) {
                            // text.setText("Thank you");
                            //active = false;
                            //mSensorManager.unregisterListener(MainActivity.this);
                            try {
                                saveCSVX();

                            } catch (IOException e) {
                                e.printStackTrace();
                                //text.setText("Something went wrong");
                            }
                            text4.setText("Data saved\n Count="+count);
                        } else {
                            // active = true;
                            Classification c = new Classification();
                            c.label = "fusion";
                            data.classifications.add(c);
                            // text.setText(c.label);
                            mHandler.postDelayed(this, 2000);
                        }
                    }
                }, 3000);

            }
        });

    }


    private void saveCSVX() throws IOException {


        File topDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/fusion_data/dtw_csv/up");
        if (!topDir.exists()) {
            topDir.mkdirs();
        }

        for (Classification c : data.classifications) {
            //File f = new File(dir, c.label + "_" + Math.random() + ".csv");
            File f = new File(topDir, "x.csv");
            File f2 = new File(topDir, "y.csv");
            File f3 = new File(topDir, "z.csv");
            // File f4 = new File(topDir, "accx.csv");
            //File f5 = new File(topDir, "accy.csv");
            //File f6 = new File(topDir, "accz.csv");
            FileWriter w1 = new FileWriter(f);
            FileWriter w2 = new FileWriter(f2);
            FileWriter w3 = new FileWriter(f3);
            //FileWriter w4 = new FileWriter(f4);
            //FileWriter w5 = new FileWriter(f5);
            //FileWriter w6 = new FileWriter(f6);
            //w.write("azimuth\n");
            int minSize = Math.min(pitchdata.size(),accelxdata.size());
            for (int i = 0; i < minSize; i++) {
                float a = azimuthdata.get(i)-azimuthdata.get(0);
                float p = pitchdata.get(i)-pitchdata.get(0);
                float r = rolldata.get(i)-rolldata.get(0);
                //  float x = accelxdata.get(i)-accelxdata.get(0);
                // float y = accelydata.get(i)-accelydata.get(0);
                // float z = accelzdata.get(i)-accelzdata.get(0);
                w1.write(String.format(Locale.US,"%f\n",
                        p));
                w2.write(String.format(Locale.US,"%f\n",
                        r));
                w3.write(String.format(Locale.US,"%f\n",
                        a));
                //w4.write(String.format(Locale.US,"%.3f\n", x));
                //w5.write(String.format(Locale.US,"%.3f\n", y));
                //w6.write(String.format(Locale.US,"%.3f\n", z));
            }
            w1.close();
            w2.close();
            w3.close();
            //w4.close();
            //w5.close();
            //w6.close();
        }
    }

    protected void onResume() {
        super.onResume();
        registerForSensorUpdates();
    }

    private void registerForSensorUpdates(){
        mSensorManager.registerListener(mSensorEventListener, mRotationVectorSensor, SensorManager.SENSOR_DELAY_UI);
        //mSensorManager.registerListener(mSensorEventListener, mAccelerometer, SensorManager.SENSOR_DELAY_UI);

        if(mMagneticSensor != null) {
            mSensorManager.registerListener(mSensorEventListener, mMagneticSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    protected void onPause() {
        mSensorManager.unregisterListener(mSensorEventListener);
        super.onPause();
    }


    private SensorEventListener mSensorEventListener = new SensorEventListener() {

        float[] orientation = new float[2];
        float[] rMat = new float[9];
        float[] qua=new float[16];
        float[] gravSensorVals=new float[3];

        public void onAccuracyChanged( Sensor sensor, int accuracy ) {}



        @Override
        public void onSensorChanged( SensorEvent event ) {
            if( event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR ){
                // calculate th rotation matrix
                //SensorManager.getRotationMatrixFromVector( rMat, event.values );
                float[] values = event.values;
                SensorManager.getQuaternionFromVector( qua,values);

                if(active==true) {


                    azimuthdata.add(qua[0]);
                    pitchdata.add(qua[1]);
                    rolldata.add(qua[2]);
                    count++;
                }



            }

           /* if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

                gravSensorVals = lowPass(event.values.clone(), gravSensorVals);

                //float x = gravSensorVals[0];
                //float y = gravSensorVals[1];
                //float z = gravSensorVals[2];

                float x=event.values[0];
                float y=event.values[1];
                float z=event.values[2];
                mLowPassX=lowPass(x,mLowPassX);
                mLowPassY=lowPass(y,mLowPassY);
                mLowPassZ=lowPass(z,mLowPassZ);

                // mAccelXText.setText("Accel X:" + String.valueOf(mLowPassX));
                //mAccelYText.setText("Accel Y:"+String.valueOf(mLowPassY));
                //mAccelZText.setText("Accel Z:"+String.valueOf(mLowPassZ));

                if(active==true) {


                    double acc_x = mLowPassX;
                    double acc_y = mLowPassY;
                    double acc_z = mLowPassZ;

                    float f4 =  (float) acc_x;
                    float f5 = (float) acc_y;
                    float f6 =  (float) acc_z;
                    accelxdata.add(f4);
                    accelydata.add(f5);
                    accelzdata.add(f6);
                }

            }*/


        }
    };

 }

