package com.erendogan6.planmyworkout.presentation.auth;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.erendogan6.planmyworkout.R;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Activity that hosts all authentication-related fragments.
 * This includes login, registration, and forgot password.
 */
@AndroidEntryPoint
public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
    }

}
