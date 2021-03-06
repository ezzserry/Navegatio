package awstreams.navegatio.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import awstreams.navegatio.helpers.Constants;
import awstreams.navegatio.helpers.ServicesHelper;
import awstreams.navegatio.helpers.Utils;
import awstreams.navegatio.R;


public class Sign_in_Activity extends Activity implements View.OnClickListener {

    private EditText etUsername, etPassword;
    private TextView tvSign_in, tvForgetPassword;
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
        etPassword.setVisibility(View.VISIBLE);
        etUsername.setTypeface(Constants.getTypeface_Light(this));
        etPassword.setTypeface(Constants.getTypeface_Light(this));

        tvSign_in = (TextView) findViewById(R.id.signin_tv);
        tvSign_in.setText(getResources().getText(R.string.sign_in));
        tvSign_in.setTypeface(Constants.getTypeface_Medium(this));
        tvForgetPassword = (TextView) findViewById(R.id.forget_pw_tv);
        tvForgetPassword.setTypeface(Constants.getTypeface_Medium(this));
        tvForgetPassword.setVisibility(View.VISIBLE);
//        tvSign_in.setTextSize(TypedValue.COMPLEX_UNIT_SP, Constants.getTextAppSize(this, true, false, false));

        btn_signin = (Button) findViewById(R.id.signin_btn);
        btn_signin.setText("Sign in");
//        tvSign_in.setTextSize(TypedValue.COMPLEX_UNIT_SP, Constants.getTextAppSize(this, false, false, true));

        btn_signin.setOnClickListener(this);
        tvForgetPassword.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signin_btn:
                if (validateParams()) {
                    Utils.showloading(this);
                    signIn();
                }
                break;
            case R.id.forget_pw_tv:
                Intent intent = new Intent(Sign_in_Activity.this, ForgetPasswordActivity.class);
                startActivity(intent);
                break;
        }

    }

    private void signIn() {
        ServicesHelper.getInstance().signIn(this, sUsername, sPassword, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String status = response.getString("status");
                    Utils.dismissloading();
                    if (status.equals("ok")) {
                        String id = response.getJSONObject("user").getString("id");
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean(Constants.isLoggedin, true);
                        editor.putString(Constants.User_ID, id);
                        editor.putString(Constants.User_NAME, sUsername);
                        editor.apply();
                        Toast.makeText(getApplicationContext(), "Login succeeded", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Sign_in_Activity.this, Base_Activity.class);
                        ActivityCompat.finishAffinity(Sign_in_Activity.this);
                        startActivity(intent);
                        finish();

                    } else {
                        Utils.dismissloading();
                        Toast.makeText(getApplicationContext(), response.getString("error"), Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    Utils.dismissloading();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.dismissloading();

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
            etUsername.setError(getString(R.string.invalid_username));
            etUsername.requestFocus();
            cancel = false;

        }
        if (sPassword.equals("")) {
            etPassword.setError(getString(R.string.no_password));
            etPassword.requestFocus();
            cancel = false;
        }
        if (!Utils.isPasswordValid(sPassword)) {
            etPassword.setError(getString(R.string.short_password));
            etPassword.requestFocus();
            cancel = false;
        }
        return cancel;
    }
}
