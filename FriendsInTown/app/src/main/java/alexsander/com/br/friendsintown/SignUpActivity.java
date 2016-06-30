package alexsander.com.br.friendsintown;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SignUpActivity extends AppCompatActivity {
    private static String TAG = "SignUpActivity";

    private EditText etName;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initViews();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp(etName.getText().toString(), etEmail.getText().toString(), etPassword.getText().toString());
            }
        });
    }

    private void initViews() {
        etName = (EditText) findViewById(R.id.input_name);
        etEmail = (EditText) findViewById(R.id.input_email);
        etPassword = (EditText) findViewById(R.id.input_password);
        etConfirmPassword = (EditText) findViewById(R.id.input_confirm_password);
        btnSignUp = (Button) findViewById(R.id.btn_signup);
    }

    private boolean validate() {
        boolean valid = true;

        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();

        Log.d(TAG, email);
        Log.d(TAG, password);

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Insert a valid email!");
            valid = false;
        } else {
            etEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 20) {
            etPassword.setError("Password must have at least 6 characters and maximum of 20!");
            valid = false;
        } else {
            etPassword.setError(null);
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords don't match!");
        }

        return valid;
    }

    private void signUp(String name, String email, String password) {
        if (!validate()) {
            return;
        }

        btnSignUp.setEnabled(false);

        ProgressDialog progress = new ProgressDialog(this, R.style.AppTheme);
        progress.setMessage("Creating your account...");
        progress.show();

        // cadastrar com backend
        finish();
    }
}
