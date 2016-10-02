package awstreams.redraccon.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import awstreams.redraccon.R;
import awstreams.redraccon.helpers.Constants;
import awstreams.redraccon.helpers.ServicesHelper;

public class Splash_Activity extends Activity {

    private static int SPLASH_TIME_OUT = 2000;

    private boolean isLogged_in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        isLogged_in = prefs.getBoolean(Constants.isLoggedin, false);

        if (isLogged_in) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {

                    Intent intent = new Intent(Splash_Activity.this, Base_Activity.class);
                    startActivity(intent);
                    finish();
                }
            }, SPLASH_TIME_OUT);
        } else {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {

                    Intent intent = new Intent(Splash_Activity.this, Sign_up_Activity.class);
                    startActivity(intent);
                    finish();
                }
            }, SPLASH_TIME_OUT);

        }
    }
}
