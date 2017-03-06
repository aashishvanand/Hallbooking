package com.aashish.hallbooking;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.calendardatepicker.MonthAdapter;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import fr.ganfra.materialspinner.MaterialSpinner;


public class Request extends AppCompatActivity implements View.OnClickListener {

    public static final String PREF = "Hallbooking";
    Calendar calendar;
    int year, month, day,mHour,mMinute;
    SimpleDateFormat formatter;
    String code, dept_pref;
    EditText date1,fromtime,totime;
    MaterialSpinner programme_spinner, hall_spinner;
    String[] programme_array = {"Guest Lecture","Workshop","FDP","Dept_Meeting","Others"};
    String[] hall_array = {"Seminar Hall - Workshop Block","Mini Seminar Hall - Workshop Block","Conference Hall - Main Block","Seminar Hall - Tifac Core","Class Room - Dept", "Auditorium","AV Hall - Academic Block","Conference Hall - Tifaac Core","Purple Hall - Main Block"};

    TimePicker timePicker;

    Button submit;
    Snackbar SnackbarRequest;
    CoordinatorLayout coordinatorLayoutRequest;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        coordinatorLayoutRequest = (CoordinatorLayout) findViewById(R.id.coordinatorLayoutRequest);
        calendar = Calendar.getInstance();
        SharedPreferences prefs = getSharedPreferences(PREF, MODE_PRIVATE);
        code = prefs.getString("code", null);
        dept_pref = prefs.getString("dept", null);

        final ArrayAdapter<String> programme_adapter = new ArrayAdapter<String>(getApplication(), android.R.layout.simple_spinner_item, programme_array);
        programme_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final ArrayAdapter<String> hall_adapter = new ArrayAdapter<String>(getApplication(), android.R.layout.simple_spinner_item, hall_array);
        hall_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        programme_spinner = (MaterialSpinner) findViewById(R.id.spinner_programme);
        programme_spinner.setHint(getResources().getString(R.string.select_programme));
        programme_spinner.setAdapter(programme_adapter);

        hall_spinner = (MaterialSpinner) findViewById(R.id.spinner_hall);
        hall_spinner.setHint(getResources().getString(R.string.select_hall));
        hall_spinner.setAdapter(hall_adapter);

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        date1 = (EditText) findViewById(R.id.date);
        fromtime = (EditText) findViewById(R.id.fromtime);
        totime = (EditText) findViewById(R.id.totime);
        submit = (Button) findViewById(R.id.submit);

        fromtime.setOnClickListener(this);
        totime.setOnClickListener(this);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String programme = programme_spinner.getSelectedItem().toString();
                String hall = hall_spinner.getSelectedItem().toString();

                if (programme.equalsIgnoreCase(getResources().getString(R.string.select_programme)) || date1.equals("")) {

                    if (programme.equalsIgnoreCase(getResources().getString(R.string.select_programme))) {
                        programme_spinner.setError(getResources().getString(R.string.select_proper_value));
                    }

                    SnackbarRequest = Snackbar
                            .make(coordinatorLayoutRequest, getResources().getString(R.string.check_selection), Snackbar.LENGTH_SHORT);
                    SnackbarRequest.show();
                } else {
                    requestData(programme, "a", "b", "d", "c");
                }
            }
        });

        date1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTime now = DateTime.now();
                MonthAdapter.CalendarDay minDate = new MonthAdapter.CalendarDay(now.getYear(), now.getMonthOfYear() - 1, now.getDayOfMonth());
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment();
                cdp.show(Request.this.getSupportFragmentManager(), "Calender");
                cdp.setDateRange(minDate, null);
                cdp.setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
                    @Override
                    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
                        try {
                            formatter = new SimpleDateFormat("yyyy-MM-dd");
                            String dateInString = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                            Date date = formatter.parse(dateInString);

                            date1.setText(formatter.format(date));
                        } catch (Exception ex) {
                            date1.setText(ex.getMessage());
                        }
                    }
                });
            }
        });




    }

    private void requestData(final String period, final String projector, final String dept, final String YEar, final String sec) {
        // Tag used to cancel the request
        String tag_string_req = "req_proj";
        pDialog.setMessage("Requesting Projector");
        showDialog();

        final StringRequest strReq = new StringRequest(com.android.volley.Request.Method.POST,
                BuildConfig.URL_request, new com.android.volley.Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    // Check for error node in json
                    if (!error) {
                        SnackbarRequest = Snackbar
                                .make(coordinatorLayoutRequest, getResources().getString(R.string.booked_successfully), Snackbar.LENGTH_SHORT);
                        SnackbarRequest.show();
                        MainActivity.dept_projector.clear();
                        Intent i = new Intent(Request.this, MainActivity.class);
                        MainActivity.dept_projector.clear();
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);

                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        SnackbarRequest = Snackbar
                                .make(coordinatorLayoutRequest, errorMsg, Snackbar.LENGTH_SHORT);
                        SnackbarRequest.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    SnackbarRequest = Snackbar
                            .make(coordinatorLayoutRequest, getString(R.string.unknown_error), Snackbar.LENGTH_SHORT);
                    SnackbarRequest.show();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                SnackbarRequest = Snackbar
                        .make(coordinatorLayoutRequest, error.getMessage(), Snackbar.LENGTH_SHORT);
                SnackbarRequest.show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("date", date1.getText().toString());

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
        super.onBackPressed();
        this.finish();
    }


    @Override
    public void onClick(View view) {
        if (view == fromtime) {

            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            fromtime.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
        if (view == totime) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            totime.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }


}
