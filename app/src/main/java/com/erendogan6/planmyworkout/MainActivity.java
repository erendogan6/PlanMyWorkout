package com.erendogan6.planmyworkout;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.erendogan6.planmyworkout.domain.usecase.auth.IsUserLoggedInUseCase;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Main activity for the PlanMyWorkout app.
 * This activity hosts the navigation components and bottom navigation.
 */
@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Inject
    IsUserLoggedInUseCase isUserLoggedInUseCase;

    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the NavController
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();

            // For now, we'll just use the default navigation graph
            // which starts with auth_navigation
            Log.d(TAG, "Setting up navigation");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    // For testing purposes, you can call this method to sign out
    private void signOut() {
        // Get the AuthRepository and sign out
        com.google.firebase.auth.FirebaseAuth.getInstance().signOut();

        // Restart the activity to show the login screen
        recreate();
    }
}
