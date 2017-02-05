package com.bricon.homeshield;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.Bind;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.btn_login) Button _loginButton;
    @Bind(R.id.link_signup) TextView _signupLink;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String email,password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        preferences= PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        editor=preferences.edit();

        ButterKnife.bind(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }
        onLoginSuccess();



        email = _emailText.getText().toString();
        password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.
        //   startActivity(new Intent(LoginActivity.this,NavigationActivity.class));

       /* new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                    }
                }, 3000);*/
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {

        if(new Utils().checkConnection(LoginActivity.this))
        {
            new ProcessRegister().execute();




        }
        else
        {
            Toast.makeText(LoginActivity.this, "Please connect to the internet.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        email = _emailText.getText().toString();
        password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
    String post2(String url)  {
        try {
            OkHttpClient client = new OkHttpClient();
            Log.e(TAG, "post2: ready for sending: " + password + email);
       /* RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Log.e(TAG, "post: request : "+body.toString() );*/
            FormBody.Builder formBuilder = new FormBody.Builder();



            formBuilder.add("email", email);
            formBuilder.add("password", password);


            Log.e(TAG, "post2: ready for sending: " + email + password);
            RequestBody formBody = formBuilder.build();

            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();
            Response response = client.newCall(request).execute();

            return response.body().string();
        }
        catch (IOException e)
        {
            Log.e(TAG, "post2: exception: "+e.toString() );
            return "0";
        }
    }

    ProgressDialog pDialog1;
    class ProcessRegister extends AsyncTask<String, String, String>
    {
        @Override
        protected void onPreExecute() {
            pDialog1=new ProgressDialog(LoginActivity.this);
            pDialog1 = new ProgressDialog(LoginActivity.this,
                    R.style.AppTheme_Dark_Dialog);
            pDialog1.setIndeterminate(true);
            pDialog1.setCancelable(false);
            pDialog1.setMessage("Authenticating...");
            pDialog1.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try{Thread.sleep(2000);}catch (Exception e){}
            return post2("http://www.androidcinema.com/brighten_controls/login.php");
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pDialog1.dismiss();
            // <br>Toast.makeText(getApplicationContext(),"result"+result,Toast.LENGTH_LONG).show();

            Log.d("jsonaman", result);

            if(result.contains("&&")) {
                Log.d("jsonaman", result);

                String splitted[]=result.split("&&");
                String name=splitted[0];
                String mobile=splitted[1];
                Toast.makeText(getApplicationContext(),"Login successful.",Toast.LENGTH_LONG).show();
                editor.putString(AppConstants.name,name);
                editor.putString(AppConstants.email,email);
                editor.putString(AppConstants.password,password);
                editor.putString(AppConstants.mobile,mobile);
                editor.putBoolean(AppConstants.registered,true);
                editor.commit();
                startActivity(new Intent(getApplicationContext(),NavigationActivity.class));
                finish();

            }
            else
                Toast.makeText(getApplicationContext(),"Please check your credentials.",Toast.LENGTH_LONG).show();
        }
    }

}