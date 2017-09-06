package com.priyo.go.view.activity.signup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.priyo.go.Fragments.OnFragmentInteractionListener;
import com.priyo.go.R;
import com.priyo.go.Utilities.Utilities;
import com.priyo.go.view.fragment.signup.SignupFragment;
import com.priyo.go.view.fragment.signup.SignupInitFragment;


public class SignupActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    public static final int ID_SIGN_UP_INIT_FRAGMENT = 1;
    public static final int ID_SIGN_UP_FRAGMENT = 2;
    private static final String TAG = SignupActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate(Bundle savedInstanceState)");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, new SignupInitFragment()).commit();

    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed()");
        Utilities.hideKeyboard(this);
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }

    @Override
    public void replaceWith(int changeToFragmentId) {
        switch (changeToFragmentId) {
            case ID_SIGN_UP_INIT_FRAGMENT:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new SignupInitFragment()).commit();
                break;
            case ID_SIGN_UP_FRAGMENT:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new SignupFragment()).commit();
                break;
        }

    }

    @Override
    public void replaceWith(int changeToFragmentId, Bundle bundle) {
        switch (changeToFragmentId) {
            case ID_SIGN_UP_INIT_FRAGMENT:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, SignupInitFragment.newInstance(bundle)).commit();
                break;
            case ID_SIGN_UP_FRAGMENT:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, SignupFragment.newInstance(bundle)).commit();
                break;
        }
    }

    @Override
    public void addToBackStack(int addFragmentId) {
        switch (addFragmentId) {
            case ID_SIGN_UP_INIT_FRAGMENT:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new SignupInitFragment()).addToBackStack(null).commit();
                break;
            case ID_SIGN_UP_FRAGMENT:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new SignupFragment()).addToBackStack(null).commit();
                break;
        }
    }

    @Override
    public void addToBackStack(int addFragmentId, Bundle bundle) {
        switch (addFragmentId) {
            case ID_SIGN_UP_INIT_FRAGMENT:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, SignupInitFragment.newInstance(bundle)).addToBackStack(null).commit();
                break;
            case ID_SIGN_UP_FRAGMENT:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, SignupFragment.newInstance(bundle)).addToBackStack(null).commit();
                break;
        }
    }
}

