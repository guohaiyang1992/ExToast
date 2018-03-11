package com.simon.toastdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.simon.toastlib.core.ExToast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.normal_toast_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "noraml", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.unnormal_toast_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExToast.makeText(MainActivity.this, "unnormal", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.unnormal2_toast_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ExToast.makeText(, "unnormal2", Toast.LENGTH_SHORT).show();
                ExToast.makeText(MainActivity.this, "unnormal2", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
