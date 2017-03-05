package awstreams.redracc.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import awstreams.redracc.R;
import awstreams.redracc.helpers.Constants;
import awstreams.redracc.helpers.ServicesHelper;

public class ForgetPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etUsername;
    private TextView tvSign_in;
    private Button btnSubmit;

    private String sUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        initViews();
    }

    private void initViews() {
        etUsername = (EditText) findViewById(R.id.email_et);

        etUsername.setTypeface(Constants.getTypeface_Light(this));

        tvSign_in = (TextView) findViewById(R.id.signin_tv);
        tvSign_in.setText(getResources().getText(R.string.reset_password));
        tvSign_in.setTypeface(Constants.getTypeface_Medium(this));
        btnSubmit = (Button) findViewById(R.id.signin_btn);
//        tvSign_in.setTextSize(TypedValue.COMPLEX_UNIT_SP, Constants.getTextAppSize(this, false, false, true));

        btnSubmit.setOnClickListener(this);
        btnSubmit.setText("Reset");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signin_btn:
                sUsername = etUsername.getText().toString();
                resetPassword(sUsername);
                break;
        }
    }

    private void resetPassword(String username) {
        ServicesHelper.getInstance().resetPassword(this, username, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String status = response.getString("status");
                    if (status.equals("ok")) {
                        Toast.makeText(getApplicationContext(), response.getString("msg"), Toast.LENGTH_LONG).show();
                    } else
                        Toast.makeText(getApplicationContext(), response.getString("msg"), Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "connection error", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "connection error", Toast.LENGTH_LONG).show();

            }
        });
    }
}
