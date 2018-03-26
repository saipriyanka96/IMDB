package com.example.layout.imdb.mainwork;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.layout.imdb.R;

/**
 * Created by Pri on 10/21/2017.
 */

public class SplashScreen extends Activity {
//Android splash screen are normally used to show user some kind of progress before the app loads completely
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        int SPLASH_TIME_OUT = 3000;
        new Handler().postDelayed(new Runnable() {
            /*Causes the Runnable r to be added to the message queue, to be run after the specified amount of time elapses

             * Showing splash screen with a timer. This will be useful when you want to show case your app logo / company
*/

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);//activity get started
                finish();//finish the activity
            }
        }, SPLASH_TIME_OUT);
    }

}
