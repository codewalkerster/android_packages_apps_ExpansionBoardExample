package com.hardkernel.odroid.expansionboardexample;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

public class MainActivity extends Activity {
    
    private ProgressBar mPB_ADC;
    private CheckBox mCB_Light;
    private TextView mTV_Lux;
    private CheckBox mCB_Pressure;
    private TextView mTV_pa;
    private TextView mTV_celsius;
    private RadioButton mRB_Key1;
    private RadioButton mRB_Key2;
    private RadioButton mRB_Key3;
    private RadioButton mRB_Key4;
    private CheckBox mCB_LED1;
    private CheckBox mCB_LED2;
    private CheckBox mCB_LED3;
    private CheckBox mCB_LED4;
    private CheckBox mCB_redLED;
    
    private EditText mET_Data;
    private TextView mTV_Data;
    private Button mBTN_Write;

    private static final String ADC_NODE = "/sys/bus/iio/devices/iio:device0/in_voltage0_raw";
    private static final String LIGHT_SENSOR_NODE = "/sys/devices/i2c_gpio.11/i2c-3/3-0029/";
    private static final String PRESSURE_SENSOR_NODE = "/sys/devices/i2c_gpio.11/i2c-3/3-0077/";
    private static final String KEYLED_NODE = "/sys/devices/ioboard_key_led.10/";

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);

            // TODO Auto-generated method stub
            byte buffer[] = new byte[16];
            int count = 0;
            try {
                FileInputStream fis = new FileInputStream(KEYLED_NODE + "sw1");
                fis.read(buffer);
                fis.close();
                if (buffer[0] == '1')
                    mRB_Key1.setChecked(false);
                else
                    mRB_Key1.setChecked(true);
                
                fis = new FileInputStream(KEYLED_NODE + "sw2");
                fis.read(buffer);
                fis.close();
                if (buffer[0] == '1')
                    mRB_Key2.setChecked(false);
                else
                    mRB_Key2.setChecked(true);

                fis = new FileInputStream(KEYLED_NODE + "sw3");
                fis.read(buffer);
                fis.close();
                if (buffer[0] == '1')
                    mRB_Key3.setChecked(false);
                else
                    mRB_Key3.setChecked(true);
                
                fis = new FileInputStream(KEYLED_NODE + "sw4");
                fis.read(buffer);
                fis.close();
                if (buffer[0] == '1')
                    mRB_Key4.setChecked(false);
                else
                    mRB_Key4.setChecked(true);
                
                if (mCB_Light.isChecked()) {
                    fis = new FileInputStream(LIGHT_SENSOR_NODE + "lux");
                    count = fis.read(buffer);
                    fis.close();
                    mTV_Lux.setText(new String(buffer, 0, count - 1));
                }

                if (mCB_Pressure.isChecked()) {
                    fis = new FileInputStream(PRESSURE_SENSOR_NODE + "pressure");
                    count = fis.read(buffer);
                    fis.close();
                    mTV_pa.setText(new String(buffer, 0, count - 1));
                    
                    fis = new FileInputStream(PRESSURE_SENSOR_NODE + "temperature");
                    count = fis.read(buffer);
                    fis.close();
                    String temperature = new String(buffer, 0, count - 1);
                    float celsius = Float.parseFloat(temperature) / 10;
                    mTV_celsius.setText(Float.toString(celsius));
                }
                
             
                fis = new FileInputStream(ADC_NODE);
                count = fis.read(buffer);
                fis.close();
                mPB_ADC.setProgress(Integer.parseInt(new String(buffer, 0, count - 1)));
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (msg.what != 1)
                mHandler.sendEmptyMessageDelayed(0, 1000);
        }
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mPB_ADC = (ProgressBar)findViewById(R.id.pb_adc);
       
        mCB_Light = (CheckBox)findViewById(R.id.cb_light);
        mCB_Light.setOnCheckedChangeListener(new OnCheckedChangeListener () {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked) {
                // TODO Auto-generated method stub
                
                try {
                    byte[] buffer = new byte[1];
                    FileOutputStream fos = new FileOutputStream(LIGHT_SENSOR_NODE + "enable");
                    if (isChecked)
                        buffer[0] = '1';
                    else
                        buffer[0] = '0';
                    fos.write(buffer);
                    fos.close();
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                updateControls();
            }
            
        });
        mTV_Lux = (TextView)findViewById(R.id.tv_lux);
        mTV_Lux.setEnabled(false);
        
        mCB_Pressure = (CheckBox)findViewById(R.id.cb_pressure);
        mCB_Pressure.setOnCheckedChangeListener(new OnCheckedChangeListener () {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked) {
                // TODO Auto-generated method stub
                
                try {
                    byte[] bytes = new byte[1];
                    FileOutputStream fos = new FileOutputStream(PRESSURE_SENSOR_NODE + "enable");
                    if (isChecked)
                        bytes[0] = '1';
                    else
                        bytes[0] = '0';
                    fos.write(bytes);
                    fos.close();
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                updateControls();
            }
            
        });
        mTV_pa = (TextView)findViewById(R.id.tv_pressure);
        mTV_pa.setEnabled(false);
        mTV_celsius = (TextView)findViewById(R.id.tv_temperature);
        mTV_celsius.setEnabled(false);
        
        mRB_Key1 = (RadioButton)findViewById(R.id.rb_key1);
        mRB_Key1.setEnabled(false);
        mRB_Key2 = (RadioButton)findViewById(R.id.rb_key2);
        mRB_Key2.setEnabled(false);
        mRB_Key3 = (RadioButton)findViewById(R.id.rb_key3);
        mRB_Key3.setEnabled(false);
        mRB_Key4 = (RadioButton)findViewById(R.id.rb_key4);
        mRB_Key4.setEnabled(false);

        mCB_LED1 = (CheckBox)findViewById(R.id.cb_LED1);
        mCB_LED1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked) {
                // TODO Auto-generated method stub
                
                byte[] bytes = new byte[1];
                try {
                    FileOutputStream fos = new FileOutputStream(KEYLED_NODE + "led1");
                    if (isChecked)
                        bytes[0] = '1';
                    else
                        bytes[0] = '0';
                    fos.write(bytes);
                    fos.close();  
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            
        });
        mCB_LED2 = (CheckBox)findViewById(R.id.cb_LED2);
        mCB_LED2.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked) {
                // TODO Auto-generated method stub
                
                byte[] bytes = new byte[1];
                try {
                    FileOutputStream fos = new FileOutputStream(KEYLED_NODE + "led2");
                    if (isChecked)
                        bytes[0] = '1';
                    else
                        bytes[0] = '0';
                    fos.write(bytes);
                    fos.close();  
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            
        });
        mCB_LED3 = (CheckBox)findViewById(R.id.cb_LED3);
        mCB_LED3.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked) {
                // TODO Auto-generated method stub
                
                byte[] bytes = new byte[1];
                try {
                    FileOutputStream fos = new FileOutputStream(KEYLED_NODE + "led3");
                    if (isChecked)
                        bytes[0] = '1';
                    else
                        bytes[0] = '0';
                    fos.write(bytes);
                    fos.close();  
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            
        });
        mCB_LED4 = (CheckBox)findViewById(R.id.cb_LED4);
        mCB_LED4.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked) {
                // TODO Auto-generated method stub
                
                byte[] bytes = new byte[1];
                try {
                    FileOutputStream fos = new FileOutputStream(KEYLED_NODE + "led4");
                    if (isChecked)
                        bytes[0] = '1';
                    else
                        bytes[0] = '0';
                    fos.write(bytes);
                    fos.close();  
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            
        });
        
        mCB_redLED = (CheckBox)findViewById(R.id.cb_redLED);
        mCB_redLED.setOnCheckedChangeListener(new OnCheckedChangeListener () {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked) {
                // TODO Auto-generated method stub       
                
                byte[] bytes = new byte[1];
                try {
                    FileOutputStream fos = new FileOutputStream(KEYLED_NODE + "board_test");
                    if (isChecked)
                        bytes[0] = '1';
                    else
                        bytes[0] = '0';
                    fos.write(bytes);
                    fos.close();  
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
         
                if (isChecked) {
                   mCB_LED1.setEnabled(false); 
                   mCB_LED2.setEnabled(false); 
                   mCB_LED3.setEnabled(false); 
                   mCB_LED4.setEnabled(false); 
                   mCB_LED1.setChecked(false); 
                   mCB_LED2.setChecked(false); 
                   mCB_LED3.setChecked(false); 
                   mCB_LED4.setChecked(false); 
                } else {
                   mCB_LED1.setEnabled(true); 
                   mCB_LED2.setEnabled(true); 
                   mCB_LED3.setEnabled(true); 
                   mCB_LED4.setEnabled(true); 
                }
            }
            
        });
        
        mET_Data = (EditText)findViewById(R.id.et_data);
       
        mBTN_Write = (Button)findViewById(R.id.btn_write); 
        mBTN_Write.setEnabled(false);
        mBTN_Write.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            String data = mET_Data.getText().toString();
            byte buf[] = new byte[16];
            //buf = data.getBytes();
            for(int i = 0; i < 16; i++) {
                if (i < data.length())
                    buf[i] = (byte) data.charAt(i);
            }
            SPIWrite(buf);
        }
           
       });
       
       mET_Data.addTextChangedListener(new TextWatcher() {

           @Override
           public void afterTextChanged(Editable s) {
               // TODO Auto-generated method stub
               if (mET_Data.getText().toString().length() > 0)
                   mBTN_Write.setEnabled(true);
               else
                   mBTN_Write.setEnabled(false);
               
           }

           @Override
           public void beforeTextChanged(CharSequence s, int start, int count,
                   int after) {
               // TODO Auto-generated method stub
               
           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before,
                   int count) {
               // TODO Auto-generated method stub
               
           }
           
       });
       
       mTV_Data = (TextView)findViewById(R.id.tv_data);
       
       Button btn = (Button)findViewById(R.id.btn_read);
       btn.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            byte buf[] = new byte[16];
            buf = SPIRead();
            
            mTV_Data.setText(new String(buf));

        }
           
       });
    }
    
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();    
        
        SPIOpen();
        
       updateControls(); 
       mHandler.sendEmptyMessageDelayed(0, 1000); 
       
       mCB_LED1.setChecked(false);
       mCB_LED2.setChecked(false);
       mCB_LED3.setChecked(false);
       mCB_LED4.setChecked(false);

       byte[] buffer = new byte[1];
       try {
           FileOutputStream fos = new FileOutputStream(KEYLED_NODE + "led1");
           buffer[0] = '0';
           fos.write(buffer);
           fos.close(); 
           fos = new FileOutputStream(KEYLED_NODE + "led2");
           buffer[0] = '0';
           fos.write(buffer);
           fos.close();   
           fos = new FileOutputStream(KEYLED_NODE + "led3");
           buffer[0] = '0';
           fos.write(buffer);
           fos.close(); 
           fos = new FileOutputStream(KEYLED_NODE + "led4");
           buffer[0] = '0';
           fos.write(buffer);
           fos.close();     
       } catch (FileNotFoundException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
       } catch (IOException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
           }        
       
       FileInputStream fis;
       try {
            fis = new FileInputStream(LIGHT_SENSOR_NODE + "enable");            
            fis.read(buffer);
            fis.close();
            if (buffer[0] == '1')
                mCB_Light.setChecked(true);
            else 
                mCB_Light.setChecked(false);

            fis = new FileInputStream(PRESSURE_SENSOR_NODE + "enable");            
            fis.read(buffer);
            fis.close();
            if (buffer[0] == '1')
                mCB_Pressure.setChecked(true);
            else 
                mCB_Pressure.setChecked(false);

            fis = new FileInputStream(KEYLED_NODE + "board_test");            
            fis.read(buffer);
            fis.close();
            if (buffer[0] == '1')
                mCB_redLED.setChecked(true);
            else 
                mCB_redLED.setChecked(false);
 
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        
        SPIClose();
        
        mHandler.sendEmptyMessage(1);
    }

    private void updateControls() {
        FileInputStream fis;
        byte[] buffer = new byte[1];
        try {
            fis = new FileInputStream(ADC_NODE + "enable");
            fis.read(buffer);
            fis.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        
        try {
            fis = new FileInputStream(LIGHT_SENSOR_NODE + "enable");
            fis.read(buffer);
            fis.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        if (buffer[0] == '1') {
            mTV_Lux.setEnabled(true);
        } else {
            mTV_Lux.setEnabled(false);
        }

        try {
            fis = new FileInputStream(PRESSURE_SENSOR_NODE + "enable");
            fis.read(buffer);
            fis.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        if (buffer[0] == '1') {
            mTV_pa.setEnabled(true);
            mTV_celsius.setEnabled(true);
        } else {
            mTV_pa.setEnabled(false);
            mTV_celsius.setEnabled(false);
        }
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public native static int SPIOpen();
    public native static void SPIClose();
    public native static void SPIWrite(byte[] arr);
    public native static byte[] SPIRead();

    static {
        System.loadLibrary("SPIUtil");
    }
}
