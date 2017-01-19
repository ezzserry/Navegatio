package awstreams.redracc.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import awstreams.redracc.R;
import awstreams.redracc.activities.Base_Activity;
import awstreams.redracc.activities.Sign_up_Activity;
import awstreams.redracc.helpers.ConnectionDetector;
import awstreams.redracc.helpers.Constants;
import awstreams.redracc.helpers.ServicesHelper;
import awstreams.redracc.helpers.Utils;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by LENOVO on 08/10/2016.
 */
public class Setting_Fragment extends Fragment implements View.OnClickListener {
    private View view;
    private SeekBar seekBar;
    private TextView tvNotificationsTitle, tvLargeText, tvSmallText, tvTextSize, tvSignOut, tvUsername;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Switch aSwitchNotifactions;
    private int progressValue;
    private boolean ifSwitch;
    private Boolean isInternetPresent = false;
    private ConnectionDetector cd;
    private ImageButton ibEditProfile;
    private String sUsername;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting, container, false);
        initViews();
        sUsername = prefs.getString(Constants.User_NAME, "");
        cd = new ConnectionDetector(getActivity());
        isInternetPresent = cd.isConnectingToInternet();

        if (!sUsername.equals(""))
            tvUsername.setText(sUsername);
        else {
            if (isInternetPresent) {
                Utils.showloading(getActivity());
                getUserInfo();
            } else
                Toast.makeText(getActivity(), getResources().getString(R.string.connection_error), Toast.LENGTH_LONG).show();
        }
        return view;
    }

    private void initViews() {
        ibEditProfile = (ImageButton) view.findViewById(R.id.edit_profile_ib);
        ibEditProfile.setOnClickListener(this);

        tvUsername = (TextView) view.findViewById(R.id.profile_username_tv);
        tvUsername.setTypeface(Constants.getTypeface_Medium(getActivity()));
        tvUsername.setTextSize(TypedValue.COMPLEX_UNIT_SP, Constants.getTextAppSize(getActivity(), true, false, false));

        tvNotificationsTitle = (TextView) view.findViewById(R.id.notifications_tv);
        tvNotificationsTitle.setTypeface(Constants.getTypeface_Medium(getActivity()));
        tvNotificationsTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, Constants.getTextAppSize(getActivity(), true, false, false));

        tvTextSize = (TextView) view.findViewById(R.id.textsize_tv);
        tvTextSize.setTypeface(Constants.getTypeface_Medium(getActivity()));
        tvTextSize.setTextSize(TypedValue.COMPLEX_UNIT_SP, Constants.getTextAppSize(getActivity(), true, false, false));

        tvSmallText = (TextView) view.findViewById(R.id.textsmall_tv);
        tvSmallText.setTypeface(Constants.getTypeface_Light(getActivity()));
        tvSmallText.setTextSize(TypedValue.COMPLEX_UNIT_SP, Constants.getTextAppSize(getActivity(), false, false, true));

        tvLargeText = (TextView) view.findViewById(R.id.textlarge_tv);
        tvLargeText.setTypeface(Constants.getTypeface_Light(getActivity()));
        tvLargeText.setTextSize(TypedValue.COMPLEX_UNIT_SP, Constants.getTextAppSize(getActivity(), true, false, false));

        tvSignOut = (TextView) view.findViewById(R.id.signout_tv);
        tvSignOut.setTypeface(Constants.getTypeface_Medium(getActivity()));
        tvSignOut.setTextSize(TypedValue.COMPLEX_UNIT_SP, Constants.getTextAppSize(getActivity(), false, true, false));
        tvSignOut.setOnClickListener(this);

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = prefs.edit();
        aSwitchNotifactions = (Switch) view.findViewById(R.id.notications_switch);
        ifSwitch = prefs.getBoolean(Constants.ifNotifications, true);
        aSwitchNotifactions.setChecked(ifSwitch);
        aSwitchNotifactions.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    editor.putBoolean(Constants.ifNotifications, true);
                else
                    editor.putBoolean(Constants.ifNotifications, false);
                editor.apply();
            }
        });
        seekBar = (SeekBar) view.findViewById(R.id.seekbar);
        progressValue = prefs.getInt(Constants.defaultTextSize, 50);
        seekBar.setProgress(progressValue);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                seekBar.setProgress(progressValue);
                editor.putInt(Constants.defaultTextSize, progressValue);
                editor.apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub
                progressValue = progress;

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signout_tv:
                if (isInternetPresent) {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean(Constants.isLoggedin, false);
                    editor.putString(Constants.User_ID, "");
                    editor.putString(Constants.User_NAME, "");
                    editor.putString(Constants.User_EMAIL, "");
                    editor.apply();
                    Intent intent = new Intent(getActivity(), Sign_up_Activity.class);
                    ActivityCompat.finishAffinity(getActivity());
                    startActivity(intent);
                    getActivity().finish();
                } else
                    Toast.makeText(getApplicationContext(), "no internet connection", Toast.LENGTH_LONG).show();
                break;
            case R.id.edit_profile_ib:
                break;

        }
    }

    private void getUserInfo() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final SharedPreferences.Editor editor = prefs.edit();
        String id = prefs.getString(Constants.User_ID, "");
        ServicesHelper.getInstance().getUserInfo(getApplicationContext(), id, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String status = response.getString("status");
                    if (status.equals("ok")) {
                        Utils.dismissloading();
                        tvUsername.setText(response.getString("displayname"));
                        editor.putString(Constants.User_NAME, response.getString("displayname"));
                        editor.apply();
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
                Toast.makeText(getApplicationContext(), "couldn't load your profile information", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getView() == null) {
            return;
        }
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    // handle back button's click listener
                    Intent intent = new Intent(getActivity(), Base_Activity.class);
                    startActivity(intent);
                    getActivity().finish();
                    return true;
                }
                return false;
            }
        });
    }
}
