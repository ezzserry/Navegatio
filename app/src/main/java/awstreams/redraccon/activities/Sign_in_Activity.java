package awstreams.redraccon.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import awstreams.redraccon.R;
import awstreams.redraccon.helpers.Constants;
import awstreams.redraccon.helpers.ServicesHelper;
import awstreams.redraccon.helpers.Utils;


public class Sign_in_Activity extends Activity implements View.OnClickListener {

    private EditText etUsername, etPassword;
    private TextView tvSign_in;
    private Button btn_signin;

    private String sUsername, sPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        initViews();
    }

    private void initViews() {
        etUsername = (EditText) findViewById(R.id.email_et);
        etPassword = (EditText) findViewById(R.id.password_et);
        etUsername.setTypeface(Constants.getTypeface_Light(this));
        etPassword.setTypeface(Constants.getTypeface_Light(this));


        tvSign_in = (TextView) findViewById(R.id.signin_tv);
        tvSign_in.setTypeface(Constants.getTypeface_Medium(this));

        btn_signin = (Button) findViewById(R.id.signin_btn);
        btn_signin.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signin_btn:
                if (validateParams()) {
                    Utils.showloading(this);
                    signin();
                }
                break;
        }

    }

    private void signin() {
        ServicesHelper.getInstance().signIn(this, sUsername, sPassword, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String status = response.getString("status");
                    if (status.equals("ok")) {
                        Utils.dismissloading();
                        String id = response.getJSONObject("user").getString("id");
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean(Constants.isLoggedin, true);
                        editor.putString(Constants.User_ID, id);
                        editor.apply();
                        Toast.makeText(getApplicationContext(), "Login succeeded", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Sign_in_Activity.this, Base_Activity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), response.getString("error"), Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    private boolean validateParams() {
        boolean cancel = true;
        etUsername.setError(null);
        etPassword.setError(null);

        sUsername = etUsername.getText().toString();
        sPassword = etPassword.getText().toString();

        if (etUsername.equals("")) {
            etUsername.setError(getString(R.string.invalid_email));
            etUsername.requestFocus();
            cancel = false;

        }
        if (sPassword.equals("") || !Utils.isPasswordValid(sPassword)) {
            etPassword.setError(getString(R.string.invalid_password));
            etPassword.requestFocus();
            cancel = false;
        }
        return cancel;
    }
}
