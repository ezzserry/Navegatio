package awstreams.redracc.notifications;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import awstreams.redracc.helpers.Constants;


public class RegistrationServices extends IntentService {
    private SharedPreferences preferences;
    private SharedPreferences.Editor prefEditor;
    private int mStatusCode;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public RegistrationServices(String name) {
        super(name);
    }

    public RegistrationServices() {
        super("RegistrationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        prefEditor = preferences.edit();

        InstanceID instanceID = InstanceID.getInstance(this);
        try {
            String token = instanceID.getToken(Constants.Notifications_SenderID, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            if (!preferences.getBoolean("token_sent", false))
                sendTokenToServer(token);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Registration Service", "Error :get Token Failed !");


        }
    }

    private void sendTokenToServer(final String token) {
        String ADD_TOKEN_URL = Constants.URL_NOTIFICATIONS;
        StringRequest request = new StringRequest(Request.Method.POST, ADD_TOKEN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if ( mStatusCode == 200) {
                    prefEditor.putBoolean("token_sent", true).apply();
                    Log.e("Registration Service", "Response : Send Token Success");

                } else {
                    prefEditor.putBoolean("token_sent", false).apply();
                    Log.e("Registration Service", "Response : Send Token Failed");


                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                prefEditor.putBoolean("token_sent", false).apply();
                Log.e("Registration Service", "Error :Send Token Failed");

            }
        }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                 mStatusCode = response.statusCode;
                return super.parseNetworkResponse(response);
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                params.put("os", "Android");
                return params;

            }
        };

        Volley.newRequestQueue(this).add(request);

    }
}
