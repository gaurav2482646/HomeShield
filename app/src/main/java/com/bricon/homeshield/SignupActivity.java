package com.bricon.homeshield;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.ButterKnife;
import butterknife.Bind;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    String name,email,mobile,password,reEnterPassword;
    @Bind(R.id.input_name) EditText _nameText;
    @Bind(R.id.input_address) EditText _addressText;
    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_mobile) EditText _mobileText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.input_reEnterPassword) EditText _reEnterPasswordText;
    @Bind(R.id.btn_signup) Button _signupButton;
    @Bind(R.id.link_login) TextView _loginLink;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        preferences= PreferenceManager.getDefaultSharedPreferences(SignupActivity.this);
        editor=preferences.edit();
        ButterKnife.bind(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    signup();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    public void signup() throws JSONException, IOException {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        //_signupButton.setEnabled(false);

        if(new Utils().checkConnection(SignupActivity.this))
        {
            new ProcessRegister().execute();




        }
        else
        {
            Toast.makeText(SignupActivity.this, "Please connect to the internet.", Toast.LENGTH_SHORT).show();
        }
       /* final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();*/

        name = _nameText.getText().toString();
        // String address = _addressText.getText().toString();
        email = _emailText.getText().toString();
        mobile = _mobileText.getText().toString();
        password = _passwordText.getText().toString();
        reEnterPassword = _reEnterPasswordText.getText().toString();

        // TODO: Implement your own signup logic here.

       /* new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);*/
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        // String address = _addressText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

       /* if (address.isEmpty()) {
            _addressText.setError("Enter Valid Address");
            valid = false;
        } else {
            _addressText.setError(null);
        }*/


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (mobile.isEmpty() || mobile.length()!=10) {
            _mobileText.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            _mobileText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

        return valid;
    }
    ProgressDialog pDialog1;
    class ProcessRegister extends AsyncTask<String, String, String>
    {
        @Override
        protected void onPreExecute() {
            pDialog1=new ProgressDialog(SignupActivity.this);
            pDialog1 = new ProgressDialog(SignupActivity.this,
                    R.style.AppTheme_Dark_Dialog);
            pDialog1.setIndeterminate(true);
            pDialog1.setCancelable(false);
            pDialog1.setMessage("Please wait while we are creating your account.");
            pDialog1.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try{Thread.sleep(2000);}catch (Exception e){}
            return post2("http://www.androidcinema.com/brighten_controls/register.php");


        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pDialog1.dismiss();
            // <br>Toast.makeText(getApplicationContext(),"result"+result,Toast.LENGTH_LONG).show();

            Log.d("jsonaman", result);

            if(result.contains("1")) {
                Log.d("jsonaman", result);

                Toast.makeText(getApplicationContext(),"Account Successfully created.",Toast.LENGTH_LONG).show();

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
                Toast.makeText(getApplicationContext(),"Error! Somthing is wrong.",Toast.LENGTH_LONG).show();
        }
    }


    String post2(String url)  {
        try {
            OkHttpClient client = new OkHttpClient();
            Log.e(TAG, "post2: ready for sending: " + name + mobile + password + email);
       /* RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Log.e(TAG, "post: request : "+body.toString() );*/
            FormBody.Builder formBuilder = new FormBody.Builder()
                    .add("name", name);

// dynamically add more parameter like this:
            formBuilder.add("email", email);
            formBuilder.add("password", password);
            formBuilder.add("mobile", mobile);

            Log.e(TAG, "post2: ready for sending: " + name + mobile + password + email);
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
}