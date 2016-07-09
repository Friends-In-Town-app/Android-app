package alexsander.com.br.friendsintown;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "MainActivity";

    private CallbackManager fbCallbackManager;
    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnSignUp;
    private LoginButton fbLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkUserLoggedWithEmailAndPassword();
        checkUserLoggedWithFacebook();
        initViews();

        fbCallbackManager = CallbackManager.Factory.create();

        fbLoginButton.setReadPermissions("public_profile", "email", "user_friends");

        fbLoginButton.registerCallback(fbCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, loginResult.getAccessToken().getToken());
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            Log.d(TAG, object.toString());
                            String name = object.getString("name");
                            String email = object.getString("email");
                            Log.d(TAG, name);
                            Log.d(TAG, email);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                new GraphRequest(loginResult.getAccessToken(), "/me/friends", null, HttpMethod.GET, new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        Log.d(TAG, response.getJSONObject().toString());
                        startActivity(new Intent(MainActivity.this, ChooseLocationActivity.class));
                    }
                }).executeAsync();



                Bundle parameters = new Bundle();
                parameters.putString("fields", "name, email");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {}

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(MainActivity.this, "Tente novamente", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(etEmail.getText().toString(), etPassword.getText().toString());
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignUpActivity.class));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fbCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void initViews() {
        etEmail = (EditText) findViewById(R.id.input_email);
        etPassword = (EditText) findViewById(R.id.input_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnSignUp = (Button) findViewById(R.id.btn_signup);
        fbLoginButton = (LoginButton) findViewById(R.id.login_button);
    }
    private void checkUserLoggedWithFacebook() {
        if (AccessToken.getCurrentAccessToken() != null) {
            Log.d(TAG, AccessToken.getCurrentAccessToken().getToken());
            // start another activity
        }
    }

    private void login(String email, String password) {
        if (!validate()) {
            return;
        }

        //btnLogin.setEnabled(false);

        //ProgressDialog progress = new ProgressDialog(this, R.style.AppTheme);
        //progress.setMessage("Logging...");
        //progress.show();

        LoginAsyncTask task = new LoginAsyncTask();
        task.execute(email, password);
        //finish();
        //startActivity(new Intent(this, NextActivity.class));
    }

    private boolean validate() {
        boolean valid = true;

        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        Log.d(TAG, email);
        Log.d(TAG, password);

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Entre com um email válido");
            valid = false;
        } else {
            etEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 20) {
            etPassword.setError("A senha deve ter entre 6 e 20 caracteres");
            valid = false;
        } else {
            etPassword.setError(null);
        }

        return valid;
    }

    private void checkUserLoggedWithEmailAndPassword() {
        SharedPreferences pref = this.getPreferences(MODE_PRIVATE);
        String token = pref.getString("token", "");
        if (!token.isEmpty()) {
            Log.d("token", token);
            startActivity(new Intent(this, ChooseLocationActivity.class));
        }
    }

    private class LoginAsyncTask extends AsyncTask<String, Void, Boolean> {
        //ProgressDialog progress = new ProgressDialog(MainActivity.this, R.style.AppTheme);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("AsyncTaskLogin", "onPreExecute()");
            //progress.setMessage("Logging...");
            //progress.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            Log.d("AsyncTaskLogin", "doInBackground()");
            return UserService.userLogin(MainActivity.this, params[0], params[1]);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            Log.d("AsyncTaskLogin", "onPostExecute()");
            //progress.dismiss();
            if (success) {
                Toast.makeText(MainActivity.this, "Logged successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, ChooseLocationActivity.class));
            } else {
                Toast.makeText(MainActivity.this, "Error logging!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
