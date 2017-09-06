package com.priyo.go.view.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.priyo.go.R;
import com.priyo.go.Utilities.Constants;
import com.priyo.go.view.activity.signup.SignupActivity;

public class TourActivity extends AppCompatActivity {

    private Button buttonGo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour);

        buttonGo = (Button) findViewById(R.id.lets_go_button);
        buttonGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TourActivity.this, SignupActivity.class);
                intent.putExtra(Constants.TARGET_FRAGMENT, Constants.SIGN_IN);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onResume() {
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
        super.onResume();
    }
}
