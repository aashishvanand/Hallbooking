package com.aashish.hallbooking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.android.volley.*;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Login extends AppCompatActivity {

    public static final String PREF = "Projectrequest";
    CustomEditText code, password;
    CoordinatorLayout coordinatorLayoutLogin;
    Snackbar SnackbarLogin;
    Button submit;
    Typeface tf_regular;
    private ProgressDialog pDialog;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        coordinatorLayoutLogin = (CoordinatorLayout) findViewById(R.id.coordinatorLayoutLogin);


        pDialog = new ProgressDialog(Login.this);
        pDialog.setTitle(getString(R.string.login));

        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        code = (CustomEditText) findViewById(R.id.code);
        password = (CustomEditText) findViewById(R.id.password);
        submit = (Button) findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(code.getText().toString().equals("") || password.getText().toString().equals(""))
            {
                SnackbarLogin = Snackbar
                        .make(coordinatorLayoutLogin, getString(R.string.wrong_user_or_password_combination), Snackbar.LENGTH_SHORT);
                SnackbarLogin.show();
            }
            else
            {
                checkLogin(code.getText().toString(), password.getText().toString());
            }

            }
        });
    }


    private void checkLogin(final String code, final String password) {

        // Tag used to cancel the request
        String tag_string_req = "req_login";

        showDialog();

        StringRequest strReq = new StringRequest(com.android.volley.Request.Method.POST,
                BuildConfig.URL_login, new com.android.volley.Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    // Check for error node in json
                    if (!error) {
                        String dept = jObj.getString("department");
                        SharedPreferences.Editor editor = getSharedPreferences(PREF, MODE_PRIVATE).edit();
                        editor.putString("code", code);
                        editor.putString("dept", dept);
                        editor.apply();
                        session.setLogin(true);
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        SnackbarLogin = Snackbar
                                .make(coordinatorLayoutLogin, errorMsg, Snackbar.LENGTH_SHORT);
                        SnackbarLogin.show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    SnackbarLogin = Snackbar
                            .make(coordinatorLayoutLogin, getString(R.string.wrong_user_or_password_combination), Snackbar.LENGTH_SHORT);
                    SnackbarLogin.show();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                SnackbarLogin = Snackbar
                        .make(coordinatorLayoutLogin, error.getMessage(), Snackbar.LENGTH_SHORT);
                SnackbarLogin.show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("staffcode", code);
                params.put("password", password);

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
