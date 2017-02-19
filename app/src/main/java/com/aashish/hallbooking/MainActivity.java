package com.aashish.hallbooking;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    ImageView day1_imageView, day2_imageView, day3_imageView, day4_imageView, day5_imageView, day6_imageView;
    TextView day1_day, day2_day, day3_day, day4_day, day5_day, day6_day;
    TextView day1_month, day2_month, day3_month, day4_month, day5_month, day6_month;
    TextView day1_year, day2_year, day3_year, day4_year, day5_year, day6_year;

    CoordinatorLayout coordinatorLayoutMainActivity;
    Snackbar SnackbarMain;
    private ProgressDialog pDialog;
    public static final String PREF = "Hallbooking";
    String dept;
    static ArrayList<String> dept_projector = new ArrayList<String>();
    SessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isOnline()) {
            setContentView(R.layout.check_connection);
        } else {
            setContentView(R.layout.activity_main);
            coordinatorLayoutMainActivity = (CoordinatorLayout) findViewById(R.id.coordinatorLayoutMainActivity);
            SharedPreferences prefs = getSharedPreferences(PREF, MODE_PRIVATE);
            dept = prefs.getString("dept", null);
            session = new SessionManager(getApplicationContext());

            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setCancelable(false);

            getProjectorData();

            day1_imageView = (ImageView) findViewById(R.id.day1_imageView);
            day2_imageView = (ImageView) findViewById(R.id.day2_imageView);
            day3_imageView = (ImageView) findViewById(R.id.day3_imageView);
            day4_imageView = (ImageView) findViewById(R.id.day4_imageView);
            day5_imageView = (ImageView) findViewById(R.id.day5_imageView);
            day6_imageView = (ImageView) findViewById(R.id.day6_imageView);

            day1_day = (TextView) findViewById(R.id.day1_day);
            day2_day = (TextView) findViewById(R.id.day2_day);
            day3_day = (TextView) findViewById(R.id.day3_day);
            day4_day = (TextView) findViewById(R.id.day4_day);
            day5_day = (TextView) findViewById(R.id.day5_day);
            day6_day = (TextView) findViewById(R.id.day6_day);

            day1_month = (TextView) findViewById(R.id.day1_month);
            day2_month = (TextView) findViewById(R.id.day2_month);
            day3_month = (TextView) findViewById(R.id.day3_month);
            day4_month = (TextView) findViewById(R.id.day4_month);
            day5_month = (TextView) findViewById(R.id.day5_month);
            day6_month = (TextView) findViewById(R.id.day6_month);

            day1_year = (TextView) findViewById(R.id.day1_year);
            day2_year = (TextView) findViewById(R.id.day2_year);
            day3_year = (TextView) findViewById(R.id.day3_year);
            day4_year = (TextView) findViewById(R.id.day4_year);
            day5_year = (TextView) findViewById(R.id.day5_year);
            day6_year = (TextView) findViewById(R.id.day6_year);

            Calendar calendar = Calendar.getInstance();
            Date day1_date = calendar.getTime();
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            Date day2_date = calendar.getTime();
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            Date day3_date = calendar.getTime();
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            Date day4_date = calendar.getTime();
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            Date day5_date = calendar.getTime();
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            Date day6_date = calendar.getTime();


            DateFormat intent = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat year = new SimpleDateFormat("yyyy");
            DateFormat month = new SimpleDateFormat("MMM");
            DateFormat date = new SimpleDateFormat("dd");

            final String day1 = intent.format(day1_date);
            final String day2 = intent.format(day2_date);
            final String day3 = intent.format(day3_date);
            final String day4 = intent.format(day4_date);
            final String day5 = intent.format(day5_date);
            final String day6 = intent.format(day6_date);

            day1_day.setText(date.format(day1_date));
            day1_month.setText(month.format(day1_date));
            day1_year.setText(year.format(day1_date));

            day2_day.setText(date.format(day2_date));
            day2_month.setText(month.format(day2_date));
            day2_year.setText(year.format(day2_date));

            day3_day.setText(date.format(day3_date));
            day3_month.setText(month.format(day3_date));
            day3_year.setText(year.format(day3_date));

            day4_day.setText(date.format(day4_date));
            day4_month.setText(month.format(day4_date));
            day4_year.setText(year.format(day4_date));

            day5_day.setText(date.format(day5_date));
            day5_month.setText(month.format(day5_date));
            day5_year.setText(year.format(day5_date));

            day6_day.setText(date.format(day6_date));
            day6_month.setText(month.format(day6_date));
            day6_year.setText(year.format(day6_date));

            day1_imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), DetailedView.class);
                    intent.putExtra("date", day1);
                    startActivity(intent);
                }
            });

            day2_imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), DetailedView.class);
                    intent.putExtra("date", day2);
                    startActivity(intent);
                }
            });

            day3_imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), DetailedView.class);
                    intent.putExtra("date", day3);
                    startActivity(intent);

                }
            });

            day4_imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), DetailedView.class);
                    intent.putExtra("date", day4);
                    startActivity(intent);
                }
            });

            day5_imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), DetailedView.class);
                    intent.putExtra("date", day5);
                    startActivity(intent);
                }
            });

            day6_imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), DetailedView.class);
                    intent.putExtra("date", day6);
                    startActivity(intent);
                }
            });

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.date, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.logout) {
            session.setLogin(false);
            getApplicationContext().getSharedPreferences(PREF, 0).edit().clear().apply();
            Intent i = new Intent(this, Login.class);
            MainActivity.dept_projector.clear();
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }



    public boolean isOnline() {

        Runtime runtime = Runtime.getRuntime();
        try {

            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }

    private void getProjectorData() {
        // Tag used to cancel the request
        String tag_string_req = "update_proj_list";

        pDialog.setMessage("Updating Projector");
        showDialog();

        final StringRequest strReq = new StringRequest(com.android.volley.Request.Method.POST,
                BuildConfig.URL_update, new com.android.volley.Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    // Check for error node in json
                    if (!error) {
                        JSONArray jsonArray = jObj.getJSONArray("projector");

                        for(int i=0;i<jsonArray.length();i++)
                        {
                            dept_projector.add(jsonArray.getString(i));
                        }


                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        SnackbarMain = Snackbar
                                .make(coordinatorLayoutMainActivity, errorMsg, Snackbar.LENGTH_SHORT);
                        SnackbarMain.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    SnackbarMain = Snackbar
                            .make(coordinatorLayoutMainActivity, getString(R.string.unknown), Snackbar.LENGTH_SHORT);
                    SnackbarMain.show();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                SnackbarMain = Snackbar
                        .make(coordinatorLayoutMainActivity, error.getMessage(), Snackbar.LENGTH_SHORT);
                SnackbarMain.show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("dept", dept);

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

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_exit)
                .setTitle(getResources().getString(R.string.exit))
                .setMessage(getResources().getString(R.string.close))
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton(getResources().getString(R.string.no), null)
                .show();
    }

}
