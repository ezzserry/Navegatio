package awstreams.redraccon.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.JsonObject;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import awstreams.redraccon.R;
import awstreams.redraccon.helpers.Constants;
import awstreams.redraccon.helpers.Utils;
import awstreams.redraccon.helpers.ServicesHelper;
import okhttp3.internal.Util;

public class Sign_up_Activity extends FragmentActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private EditText etEmail, etPassword, etUSername, etAge;
    private Button btnSign_up;
    private ImageButton ibFacebook, ibTwitter, ibGmail;
    private TextView tvAlready_have_account, tvSingwith_social;
    private String sEmail, sPassword, sAge, sUsername;
    private SignInButton signInButton;

    //    private TwitterLoginButton twitterLoginButton;
//    TwitterAuthClient mTwitterAuthClient;

    //Signin button
    //Signing Options
    private GoogleSignInOptions gso;

    //google api client
    private GoogleApiClient mGoogleApiClient;

    //Signin constant to check the activity result
    private int Gmail_SIGN_IN_CODE = 100;
    private CallbackManager callbackManager;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        TwitterAuthConfig authConfig = new TwitterAuthConfig(Constants.TWITTER_KEY, Constants.TWITTER_SECRET);
//        Fabric.with(this, new Twitter(authConfig));
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("Success", "Login");
                        String fbToken = loginResult.getAccessToken().getToken();
                        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        editor = prefs.edit();
                        editor.putString(Constants.fbAccessToken, fbToken);
                        editor.apply();
                        fbLogin(fbToken);

                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(Sign_up_Activity.this, "Login Cancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(Sign_up_Activity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        setContentView(R.layout.activity_login);
        initViews();
        //Initializing google api client
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(Sign_up_Activity.this, this /* OnConnectionFailedListener */)
//                .addApi(Auth.GOOGLE_SIGN_IN_API)
//                .build();

    }


    private void initViews() {

        etEmail = (EditText) findViewById(R.id.email_et);
        etPassword = (EditText) findViewById(R.id.password_et);
        etUSername = (EditText) findViewById(R.id.username_et);
        etAge = (EditText) findViewById(R.id.age_et);

        etEmail.setTypeface(Constants.getTypeface_Light(this));
        etPassword.setTypeface(Constants.getTypeface_Light(this));
        etUSername.setTypeface(Constants.getTypeface_Light(this));
        etAge.setTypeface(Constants.getTypeface_Light(this));
        etEmail.setTextSize(TypedValue.COMPLEX_UNIT_SP, Constants.getTextAppSize(this, false, true, false));
        etPassword.setTextSize(TypedValue.COMPLEX_UNIT_SP, Constants.getTextAppSize(this, false, true, false));
        etUSername.setTextSize(TypedValue.COMPLEX_UNIT_SP, Constants.getTextAppSize(this, false, true, false));
        etAge.setTextSize(TypedValue.COMPLEX_UNIT_SP, Constants.getTextAppSize(this, false, true, false));

        tvAlready_have_account = (TextView) findViewById(R.id.already_have_acc_tv);
        tvAlready_have_account.setTypeface(Constants.getTypeface_Medium(this));
        tvAlready_have_account.setTextSize(TypedValue.COMPLEX_UNIT_SP, Constants.getTextAppSize(this, false, true, false));
        tvAlready_have_account.setOnClickListener(this);

        tvSingwith_social = (TextView) findViewById(R.id.signin_tv);
        tvSingwith_social.setTypeface(Constants.getTypeface_Medium(this));
        tvAlready_have_account.setTextSize(TypedValue.COMPLEX_UNIT_SP, Constants.getTextAppSize(this, false, false, true));


        btnSign_up = (Button) findViewById(R.id.signup_btn);
        btnSign_up.setOnClickListener(this);

        ibFacebook = (ImageButton) findViewById(R.id.fb_btn);
        ibGmail = (ImageButton) findViewById(R.id.gmail_btn);
//        ibTwitter = (ImageButton) findViewById(R.id.twitter_btn);

        ibGmail.setOnClickListener(this);
        ibFacebook.setOnClickListener(this);
//        ibTwitter.setOnClickListener(this);

//        twitterLoginButton = (TwitterLoginButton) findViewById(R.id.twitter_Login);

    }

    private boolean validateParams() {
        boolean cancel = true;
        etEmail.setError(null);
        etPassword.setError(null);
        etAge.setError(null);
        etUSername.setError(null);

        sEmail = etEmail.getText().toString();
        sPassword = etPassword.getText().toString();
        sUsername = etUSername.getText().toString();
        sAge = etAge.getText().toString();


        if (sUsername.equals("")) {
            etUSername.setError(getString(R.string.invalid_username));
            etUSername.requestFocus();
            cancel = false;
        }
        if (sEmail.equals("") || !Utils.isEmailValid(sEmail)) {
            etEmail.setError(getString(R.string.invalid_email));
            etEmail.requestFocus();
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
        if (sAge.equals("")) {
            etAge.setError(getString(R.string.invalid_age));
            etAge.requestFocus();
            cancel = false;
        }
        return cancel;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signup_btn:
                if (validateParams()) {
                    Utils.showloading(this);
                    signUp();
                }
                break;
            case R.id.fb_btn:
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends"));

                break;
//            case R.id.twitter_btn:
//                executeTwitter_Login();
//                break;
            case R.id.gmail_btn:
                executeGmail_Login();
                break;
            case R.id.already_have_acc_tv:
                Intent intent = new Intent(Sign_up_Activity.this, Sign_in_Activity.class);
                startActivity(intent);
                break;
        }

    }

    private void signUp() {
        ServicesHelper.getInstance().get_Nonce(this, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    final String stat = response.getString("status");
                    if (stat.equals("ok")) {
                        String nonce = response.getString("nonce");
                        ServicesHelper.getInstance().signUp(getApplicationContext(), nonce, sUsername, sEmail, sPassword, sAge, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String status = response.getString("status");
                                    Utils.dismissloading();
                                    String id = response.getString("user_id");
                                    if (status.equals("ok")) {
                                        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                        editor = prefs.edit();
                                        editor.putBoolean(Constants.isLoggedin, true);
                                        editor.putString(Constants.User_ID, id);
                                        editor.apply();
                                        Toast.makeText(getApplicationContext(), "Registration succeeded", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(Sign_up_Activity.this, Base_Activity.class);
                                        startActivity(intent);
                                        finish();
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
                                int networkResponse = error.networkResponse.statusCode;
                                String json = null;
                                NetworkResponse response = error.networkResponse;
                                if (response != null && response.data != null) {
                                    switch (response.statusCode) {
                                        case 404:
                                            json = new String(response.data);
                                            json = trimMessage(json, "error");
                                            if (json != null) displayMessage(json);
                                            break;
                                    }
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "Try again please", Toast.LENGTH_LONG).show();
                        Utils.dismissloading();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Utils.dismissloading();
                    Toast.makeText(getApplicationContext(), "Try again please", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Utils.dismissloading();
                Toast.makeText(getApplicationContext(), "Try again please", Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Adding the login result back to the button
//        twitterLoginButton.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == Facebook_SIGN_IN_CODE)
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Gmail_SIGN_IN_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            //Calling a new function to handle signin
            handleSignInResult(result);
        }
    }

//    private void executeTwitter_Login() {
//        mTwitterAuthClient = new TwitterAuthClient();
//
//        mTwitterAuthClient.authorize(this, new com.twitter.sdk.android.core.Callback<TwitterSession>() {
//
//            @Override
//            public void success(Result<TwitterSession> twitterSessionResult) {
//                String Username = twitterSessionResult.data.getUserName();
//                String Token = twitterSessionResult.data.getAuthToken().toString();
//                String UserID = String.valueOf(twitterSessionResult.data.getUserId());
//            }
//
//            @Override
//            public void failure(TwitterException e) {
//                e.printStackTrace();
//                Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_LONG).show();
//            }
//        });
//
//    }

    private void executeGmail_Login() {
        //Initializing google signin option
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        //Initializing signinbutton
        signInButton = (SignInButton) findViewById(R.id.gmail_login);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setScopes(gso.getScopeArray());

        stopAutoManage();
        //Initializing google api client
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(Sign_up_Activity.this, 0, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        //Creating an intent
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);

        //Starting intent for result
        startActivityForResult(signInIntent, Gmail_SIGN_IN_CODE);


    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    //After the signing we are calling this function
    private void handleSignInResult(GoogleSignInResult result) {
        //If the login succeed
        if (result.isSuccess()) {
            Utils.showloading(this);
            //Getting google account
            GoogleSignInAccount acct = result.getSignInAccount();

            sUsername = acct.getDisplayName();
            sEmail = acct.getEmail();
            sPassword = acct.getId();
            sAge = "";
            ServicesHelper.getInstance().get_Nonce(this, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        final String stat = response.getString("status");
                        if (stat.equals("ok")) {
                            String nonce = response.getString("nonce");
                            ServicesHelper.getInstance().signUp(getApplicationContext(), nonce, sUsername, sEmail, sPassword, sAge, new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            Utils.dismissloading();
                                            try {
                                                String status = response.getString("status");
                                                if (status.equals("ok")) {
                                                    String id = response.getString("user_id");
                                                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                                    SharedPreferences.Editor editor = prefs.edit();
                                                    editor.putBoolean(Constants.isLoggedin, true);
                                                    editor.putString(Constants.User_ID, id);
                                                    editor.apply();
                                                    Toast.makeText(getApplicationContext(), "Registration succeeded", Toast.LENGTH_LONG).show();
                                                    Intent intent = new Intent(Sign_up_Activity.this, Base_Activity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }

                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            int networkResponse = error.networkResponse.statusCode;
                                            if (networkResponse == 404) {
                                                Utils.dismissloading();
                                                Toast.makeText(getApplicationContext(), "Username already exists", Toast.LENGTH_LONG).show();
                                                mGoogleApiClient.clearDefaultAccountAndReconnect();

                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(getApplicationContext(), "Try again please", Toast.LENGTH_LONG).show();
                            Utils.dismissloading();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Utils.dismissloading();
                        Toast.makeText(getApplicationContext(), "Try again please", Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Utils.dismissloading();
                    Toast.makeText(getApplicationContext(), "Try again please", Toast.LENGTH_LONG).show();
                }
            });

//            String sProfileimg= acct.getPhotoUrl().toString();


//            //Initializing image loader
//            imageLoader = CustomVolleyRequest.getInstance(this.getApplicationContext())
//                    .getImageLoader();
//
//            imageLoader.get(acct.getPhotoUrl().toString(),
//                    ImageLoader.getImageListener(profilePhoto,
//                            R.mipmap.ic_launcher,
//                            R.mipmap.ic_launcher));
//
//            //Loading image
//            profilePhoto.setImageUrl(acct.getPhotoUrl().toString(), imageLoader);

        } else {
            //If login fails
            Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
        }
    }


    private void fbLogin(String fbToken) {
        ServicesHelper.getInstance().fbLogin(this, fbToken, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("status").equals("ok")) {
                        Toast.makeText(getApplicationContext(), response.getString("msg"), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Sign_up_Activity.this, Base_Activity.class);
                        startActivity(intent);
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Try again", Toast.LENGTH_LONG).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Try again", Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();

    }

    @Override
    protected void onStop() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.stopAutoManage(this);
        mGoogleApiClient.disconnect();
    }

    private void stopAutoManage() {
        if (mGoogleApiClient != null)
            mGoogleApiClient.stopAutoManage(this);
    }

    public String trimMessage(String json, String key) {
        String trimmedString = null;

        try {
            JSONObject obj = new JSONObject(json);
            trimmedString = obj.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return trimmedString;
    }

    //Somewhere that has access to a context
    public void displayMessage(String toastString) {
        Toast.makeText(Sign_up_Activity.this, toastString, Toast.LENGTH_LONG).show();
    }
}


