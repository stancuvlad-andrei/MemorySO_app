package com.example.memoryso;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView gifProgressBar = findViewById(R.id.spinning_globe);
        Glide.with(this)
                .asGif()
                .load(R.drawable.robo)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .into(gifProgressBar);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, Login.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}
