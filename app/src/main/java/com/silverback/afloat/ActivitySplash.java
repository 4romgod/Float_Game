package com.silverback.afloat;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;


public class ActivitySplash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread splashThread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(2000);
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
                finally {
                    Intent mainIntent = new Intent(ActivitySplash.this, com.silverback.afloat.MainActivity.class);
                    startActivity(mainIntent);
                }
            }       //end run()
        };

        splashThread.start();

    }       //end onCreate()


    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }       //end onPause()


}       //end class
