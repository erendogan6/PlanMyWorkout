package com.erendogan6.planmyworkout.presentation.home;

import android.os.Bundle;

import com.erendogan6.planmyworkout.presentation.common.BaseActivity;
import com.erendogan6.planmyworkout.presentation.home.fragment.HomeFragment;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Activity for the home screen.
 * This activity uses a fragment-based approach for better modularity.
 */
@AndroidEntryPoint
public class HomeActivity extends BaseActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Add the HomeFragment if this is the first creation
        if (savedInstanceState == null) {
            addFragment(new HomeFragment(), false);
        }
    }
}
