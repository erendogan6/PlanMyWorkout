package com.erendogan6.planmyworkout.presentation.home;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.erendogan6.planmyworkout.R;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Activity for the home screen.
 * This activity uses a fragment-based approach for better modularity.
 */
@AndroidEntryPoint
public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }
}