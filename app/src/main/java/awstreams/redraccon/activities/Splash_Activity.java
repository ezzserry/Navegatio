package awstreams.redraccon.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import awstreams.redraccon.R;

public class Splash_Activity extends Activity {

    private static int SPLASH_TIME_OUT = 2000;

    private boolean is_Logged_in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

//        if (is_Logged_in) {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                Intent intent = new Intent(Splash_Activity.this, Base_Activity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);


//        } else {
//            new Handler().postDelayed(new Runnable() {
//
//                @Override
//                public void run() {
//
//                    Intent intent = new Intent(Splash_Activity.this, Login_Activity.class);
//                    startActivity(intent);
//                    finish();
//                }
//            }, SPLASH_TIME_OUT);
//
//        }
    }
}
