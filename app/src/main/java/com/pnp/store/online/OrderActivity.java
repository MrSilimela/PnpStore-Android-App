package com.pnp.store.online;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class OrderActivity extends AppCompatActivity {
    final String LOG = "OrderActivity";
    public EditText contact,adress;
    Button home, track;
    Spinner location;
    public static final String PREFS = "prefFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        contact = (EditText)findViewById(R.id.Contactno);
        adress = (EditText)findViewById(R.id.locationAddress);

        home = (Button)findViewById(R.id.btnHome);
        track = (Button)findViewById(R.id.btnTrack);
        location = (Spinner) findViewById(R.id.spinnerLocation);


        SharedPreferences prefs = getSharedPreferences(PREFS,MODE_PRIVATE);
        final String restoredText = prefs.getString("username", null);

        ArrayAdapter<String> myAdaptorlocation = new ArrayAdapter<String>(OrderActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.location));

        myAdaptorlocation.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        location.setAdapter(myAdaptorlocation);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy");
        final String formattedDate2 = df2.format(c.getTime());

        SimpleDateFormat df3 = new SimpleDateFormat("HH:mm:ss a");
        final String formattedDate3 = df3.format(c.getTime());


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(OrderActivity.this,
                        MainActivity.class);
                startActivity(intent);

            }
        });

        track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap postData = new HashMap();

                postData.put("location", location.getSelectedItem().toString());
                postData.put("contact", contact.getText().toString());
                postData.put("address", adress.getText().toString());
                postData.put("time", formattedDate3);
                postData.put("date", formattedDate2);
                postData.put("customer", restoredText);
                PostResponseAsyncTask cardTask = new
                        PostResponseAsyncTask(OrderActivity.this, postData, new
                        AsyncResponse() {
                            @Override
                            public void processFinish(String s) {
                                if((contact.getText().toString().isEmpty() ||adress.getText().toString().isEmpty()) || formattedDate3.isEmpty() || formattedDate2.isEmpty() || restoredText.isEmpty())
                                {
                                    AlertDialog.Builder dialogBox = new
                                            AlertDialog.Builder(OrderActivity.this);
                                    dialogBox.setMessage("Not successful ")
                                            .setCancelable(false)
                                            .setNegativeButton("Try again!",
                                                    new DialogInterface.OnClickListener() {
                                                        public void
                                                        onClick(DialogInterface dialog, int which) {
                                                            dialog.cancel();//closes the dialog box
                                                        }
                                                    });
                                    AlertDialog dialog = dialogBox.create();
                                    dialog.setTitle("Error");
                                    dialog.show();


                                }
                                else
                                {
                                    Log.d(LOG, s);
                                    Toast.makeText(OrderActivity.this, "Thank you order will be delivered", Toast.LENGTH_LONG).show();
                                            Intent intent = new
                                                    Intent(OrderActivity.this, TrackOrderActivity.class);
                                    startActivity(intent);

                                }
                            }
                        });

                cardTask.execute("https://pnponlineshopping1.000webhostapp.com/AddOrder.php");

            }
        });
    }

}