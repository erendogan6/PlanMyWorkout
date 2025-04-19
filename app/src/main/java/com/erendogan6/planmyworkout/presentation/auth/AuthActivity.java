package com.erendogan6.planmyworkout.presentation.auth;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.erendogan6.planmyworkout.R;
import com.erendogan6.planmyworkout.databinding.ActivityAuthBinding;
import com.erendogan6.planmyworkout.presentation.auth.fragment.LoginFragment;
import com.erendogan6.planmyworkout.presentation.common.BaseActivity;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Activity that hosts all authentication-related fragments.
 * This includes login, registration, and forgot password.
 */
@AndroidEntryPoint
public class AuthActivity extends BaseActivity {

    private ActivityAuthBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Initialize ViewBinding
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        // Add the LoginFragment as the default fragment if this is the first creation
        if (savedInstanceState == null) {
            addFragment(new LoginFragment(), false);
        }
    }
    
    /**
     * Navigate to a specific authentication fragment.
     * @param fragment The fragment to navigate to
     * @param addToBackStack Whether to add the transaction to the back stack
     */
    public void navigateToAuthFragment(Fragment fragment, boolean addToBackStack) {
        replaceFragment(fragment, addToBackStack);
    }
}
