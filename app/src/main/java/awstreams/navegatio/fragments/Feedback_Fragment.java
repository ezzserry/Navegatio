package awstreams.navegatio.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import awstreams.navegatio.activities.Base_Activity;
import awstreams.navegatio.helpers.Constants;
import awstreams.navegatio.helpers.ServicesHelper;
import awstreams.navegatio.R;
import awstreams.navegatio.helpers.Utils;

/**
 * Created by LENOVO on 10/10/2016.
 */
public class Feedback_Fragment extends Fragment implements View.OnClickListener {

    private View view;
    private Button btnSendFeedback;
    private EditText etFeedback;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragement_feedback, container, false);
        initViews();
        return view;
    }

    private void initViews() {
        etFeedback = (EditText) view.findViewById(R.id.feedback_et);
        btnSendFeedback = (Button) view.findViewById(R.id.feedback_btn);
        btnSendFeedback.setOnClickListener(this);
//        etFeedback.setTextSize(TypedValue.COMPLEX_UNIT_SP, Constants.getTextAppSize(getActivity(), false, true, false));
//        btnSendFeedback.setTextSize(TypedValue.COMPLEX_UNIT_SP, Constants.getTextAppSize(getActivity(), false, false, true));

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.feedback_btn:
                String theFeedback = etFeedback.getText().toString();
                if (!theFeedback.equals("")) {
                    sendFeedback(theFeedback);
                    Utils.showloading(getActivity());
                } else
                    Toast.makeText(getActivity(), "write your feedback , please", Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void sendFeedback(String theFeedback) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String username = preferences.getString(Constants.User_NAME, "");

        ServicesHelper.getInstance().sendFeedback(getActivity(), theFeedback, username, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("status").equals("SUCCESS")) {
                        Utils.dismissloading();
                        Toast.makeText(getActivity(), "your message has been sent to Redraccon team, Thank you !!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getActivity(), Base_Activity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Utils.dismissloading();
                    Toast.makeText(getActivity(), "failed !!", Toast.LENGTH_LONG).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.dismissloading();
                Toast.makeText(getActivity(), "failed , check your connection!!", Toast.LENGTH_LONG).show();

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
