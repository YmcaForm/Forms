package com.ymcaforms.ymcaforms;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {
    TextView ymca,forms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_splash);

        ymca = (TextView) findViewById(R.id.ymca);
        forms = (TextView) findViewById(R.id.forms);

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Floorlight.ttf");
        Typeface tf2 = Typeface.createFromAsset(getAssets(), "fonts/pencil_box.ttf");
        ymca.setTypeface(tf);
        forms.setTypeface(tf2);

        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        };
        thread.start();
    }
}
