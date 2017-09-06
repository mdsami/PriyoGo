package com.priyo.go.view.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.priyo.go.R;

public class AboutActivity extends AppCompatActivity {

    private static final String TAG = AboutActivity.class.getSimpleName();
    private TextView mBuildNumberView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate(Bundle savedInstanceState)");
        super.onCreate(savedInstanceState);

        Log.d(TAG, "Setting up Content View");
        setContentView(R.layout.activity_about_us);

        Log.d(TAG, "Initializing ToolBar/ActionBar");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Log.d(TAG, "Initializing View objects");
        mBuildNumberView = (TextView) findViewById(R.id.textview_build_number);
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            String version = pInfo.versionName;
            mBuildNumberView.setText(getString(R.string.app_version, version));
        } catch (PackageManager.NameNotFoundException e) {
            Log.wtf(TAG, e.getClass().getSimpleName());
            if (e.getMessage() != null)
                Log.wtf(TAG, e.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed()");
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected(MenuItem item)");
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.d(TAG, "Home As Up Button Pressed.");
                onBackPressed();
                break;
        }
        return true;
    }

}
