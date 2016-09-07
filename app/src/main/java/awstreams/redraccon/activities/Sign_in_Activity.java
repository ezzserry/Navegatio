package awstreams.redraccon.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import awstreams.redraccon.R;
import awstreams.redraccon.helpers.Constants;
import awstreams.redraccon.helpers.Utils;


public class Sign_in_Activity extends Activity implements View.OnClickListener {

    private EditText etEmail, etPassword;
    private TextView tvSign_in;
    private Button btn_signin;

    private String sEmail, sPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        initViews();
    }

    private void initViews() {
        etEmail = (EditText) findViewById(R.id.email_et);
        etPassword = (EditText) findViewById(R.id.password_et);
        etEmail.setTypeface(Constants.getTypeface_Light(this));
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
                }
                break;
        }

    }

    private boolean validateParams() {
        boolean cancel = true;
        etEmail.setError(null);
        etPassword.setError(null);

        sEmail = etEmail.getText().toString();
        sPassword = etPassword.getText().toString();

        if (sEmail.equals("") || !Utils.isEmailValid(sEmail)) {
            etEmail.setError(getString(R.string.invalid_email));
            etEmail.requestFocus();
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
