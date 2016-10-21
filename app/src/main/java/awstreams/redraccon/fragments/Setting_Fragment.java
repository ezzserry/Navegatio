package awstreams.redraccon.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import awstreams.redraccon.R;
import awstreams.redraccon.activities.Sign_up_Activity;
import awstreams.redraccon.helpers.ConnectionDetector;
import awstreams.redraccon.helpers.Constants;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by LENOVO on 08/10/2016.
 */
public class Setting_Fragment extends Fragment implements View.OnClickListener {
    private View view;
    private SeekBar seekBar;
    private TextView tvNotificationsTitle, tvLargeText, tvSmallText, tvTextSize, tvSignOut;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Switch aSwitchNotifactions;
    private int progressValue;
    private boolean ifSwitch;
    private Boolean isInternetPresent = false;
    private ConnectionDetector cd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting, container, false);
        initViews();
        cd = new ConnectionDetector(getActivity());
        isInternetPresent = cd.isConnectingToInternet();

        return view;
    }

    private void initViews() {
        tvNotificationsTitle = (TextView) view.findViewById(R.id.notifications_tv);
        tvNotificationsTitle.setTypeface(Constants.getTypeface_Medium(getActivity()));
        tvNotificationsTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, Constants.getTextAppSize(getActivity(), true, false, false));

        tvTextSize = (TextView) view.findViewById(R.id.textsize_tv);
        tvTextSize.setTypeface(Constants.getTypeface_Medium(getActivity()));
        tvTextSize.setTextSize(TypedValue.COMPLEX_UNIT_SP, Constants.getTextAppSize(getActivity(), true, false, false));

        tvSmallText = (TextView) view.findViewById(R.id.textsmall_tv);
        tvSmallText.setTypeface(Constants.getTypeface_Light(getActivity()));
        tvSmallText.setTextSize(TypedValue.COMPLEX_UNIT_SP, Constants.getTextAppSize(getActivity(), false, true, false));

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
                    editor.apply();
                    Intent intent = new Intent(getActivity(), Sign_up_Activity.class);
                    startActivity(intent);
                    getActivity().finish();

                } else
                    Toast.makeText(getApplicationContext(), "no internet connection", Toast.LENGTH_LONG).show();
        }
    }

}
