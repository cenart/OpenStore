package com.ecolemultimedia.openstore.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ecolemultimedia.openstore.R;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //TODO : Lancer la HomeActivity
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
                // finish();
            }
        }, 2000);
    }
}
