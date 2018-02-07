package com.pnp.store.online;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class IndexActivity extends AppCompatActivity{

    String LOG = "IndexActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        OnButtonClick();

        final ImageView animImageView = (ImageView) findViewById(R.id.iv_animation);
        animImageView.setBackgroundResource(R.drawable.anim);
        animImageView.post(new Runnable() {
            @Override
            public void run() {
                AnimationDrawable frameAnimation =
                        (AnimationDrawable) animImageView.getBackground();
                frameAnimation.start();
            }
        });
    }

    public void OnButtonClick() {
        Button Return = (Button) findViewById(R.id.loginReturn);
        Button ButtonSignUp = (Button) findViewById(R.id.btnSignUp);

        //Upon being pressed, user is taken back to the login page
        Return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });


        //By pressing the Sign-Up button, the Sign-up page appears
        ButtonSignUp.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(i);
            }
        });







}



}
