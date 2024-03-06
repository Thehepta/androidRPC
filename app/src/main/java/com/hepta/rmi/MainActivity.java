package com.hepta.rmi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hepta.rmi.databinding.ActivityMainBinding;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'rmi' library on application startup.
    static {
//        System.loadLibrary("rmi");
    }

    private RmiServer rmiServer;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Example of a call to a native method
        Button btn = binding.sampleText;
        btn.setText("android rmi");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RmiServer.entry(getApplicationContext(),getApplicationInfo().sourceDir,"19999,1");
            }
        });
    }

    /**
     * A native method that is implemented by the 'rmi' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}