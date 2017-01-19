package awstreams.redraccon.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.widget.Toast;

import awstreams.redraccon.R;
import awstreams.redraccon.helpers.ConnectionDetector;
import awstreams.redraccon.helpers.Constants;
import awstreams.redraccon.notifications.RegistrationServices;

//import awstreams.redraccon.helpers.ServicesHelper;
public class Splash_Activity extends Activity {

    private static int SPLASH_TIME_OUT = 2500;

    private boolean isLogged_in;
    private Boolean isInternetPresent = false;
    private ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        startService(new Intent(this, RegistrationServices.class));
        float test = getResources().getDimension(R.dimen.nSubTitle);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        isLogged_in = prefs.getBoolean(Constants.isLoggedin, false);
        cd = new ConnectionDetector(this);
        isInternetPresent = cd.isConnectingToInternet();
        if (!isInternetPresent) {
            Toast.makeText(this, "no internet connection ", Toast.LENGTH_LONG).show();
        }
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
